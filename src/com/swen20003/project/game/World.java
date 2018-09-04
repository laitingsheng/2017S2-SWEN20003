package com.swen20003.project.game;

import com.swen20003.project.App;
import com.swen20003.project.game.exception.FileCorruptedException;
import com.swen20003.project.game.sprite.MovableObject;
import com.swen20003.project.game.sprite.block.AbstractBlock;
import com.swen20003.project.game.sprite.unit.AbstractEnemy;
import com.swen20003.project.game.sprite.background.Door;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.sprite.background.Switch;
import com.swen20003.project.game.sprite.background.Target;
import com.swen20003.project.game.sprite.block.Stone;
import com.swen20003.project.game.sprite.block.TNT;
import com.swen20003.project.game.sprite.unit.Player;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.util.Refreshable;
import com.swen20003.project.game.util.KeyAction;
import com.swen20003.project.util.KeyComb;
import org.newdawn.slick.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static com.swen20003.project.App.*;

/**
 * The World class for the whole map creation/coordination.
 * @author Tingsheng Lai
 */
public final class World implements KeyListener {
    // Constants defined in the properties file.
    private final static int LEVEL_COUNT;
    /**
     * The maximum number of undo, -1 to enable infinite record, or any other negative number or
     * 0 will disable the record.
     */
    public static final int MAXIMUM_UNDO;
    /**
     * <code>true</code> if the undo is enabled
     */
    public static final boolean UNDO_ENABLED;

    private static KeyComb keys;                    // the keys combination to be monitored
    private final static Music BGM;                 // the background music of the game

    // static initialisation
    static {
        LEVEL_COUNT = Integer.parseInt(App.p.getProperty("Level_Count"));
        keys = DEFAULT_KEYS;
        MAXIMUM_UNDO = Integer.parseInt(p.getProperty("Maximum_Undo"));
        UNDO_ENABLED = MAXIMUM_UNDO == -1 || MAXIMUM_UNDO > 0;

        Music tmp = null;
        try {
            tmp = new Music("res/bgm.ogg");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        BGM = tmp;
    }

    // variable relates to different level
    private Boundary boundary;
    private Floor[][] backgrounds;                  // array for faster access
    private List<Stone> stones;
    private List<AbstractEnemy> enemies;
    private List<TNT> tnts;
    private Player player;
    private volatile int score = 0;                 // volatile keyword for thread-safety
    private int level, targets;

    private volatile boolean reiniting = false;     // flag of reinitialisation, normally false

    /**
     * Default constructor, use to create a new game (load the default level file)
     */
    public World() {
        level = 0;
        load("res/levels/" + level + ".lvl");
        BGM.loop();                                 // start the background music
    }

    /**
     * Rendering all objects loaded from the level file, order described in reflection
     * @param g a Graphics object to manipulate the frame on the screen
     */
    public void render(Graphics g) {
        for(Refreshable[] ss : backgrounds)
            for(Refreshable s : ss)
                if(s != null)
                    s.render(g);

        if(level == LEVEL_COUNT)
            return;

        for(Refreshable s : stones)
            s.render(g);
        for(TNT tnt : tnts)
            tnt.render(g);

        for(Refreshable s : enemies)
            s.render(g);

        player.render(g);
        g.drawString("Level: " + level, 10, 24);
    }

    /**
     * Update the sprites in the following order
     * @param delta time elapsed since the last call of this method
     */
    public void update(int delta) {
        if(reiniting || level == LEVEL_COUNT)
            return;

        Iterator<TNT> it = tnts.iterator();
        while(it.hasNext()) {
            TNT tnt = it.next();
            if(!tnt.usable() && tnt.timeOver())
                it.remove();
            else
                tnt.update(delta);
        }

        // update the movement of ice stone first
        for(Refreshable i : stones)
            i.update(delta);

        // then update the enemies
        for(Refreshable i : enemies)
            i.update(delta);

        if(score == targets)
            mission_complete();
    }

    /**
     * Convert the given x-coordinate to index of backgrounds
     * @param x x-coordinate
     * @return index
     */
    public int xToIndex(float x) {
        return (int)((x - boundary.origX) / TILE_SIZE);
    }

    /**
     * Convert the given y-coordinate to index of backgrounds
     * @param y y-coordinate
     * @return index
     */
    public int yToIndex(float y) {
        return (int)((y - boundary.origY) / TILE_SIZE);
    }

    /**
     * Provides the location of player
     * @return the coordinate of the player
     */
    public MovableObject.Coordinate getPlayerPosition() {
        return player.getPosition();
    }

    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * A synchronised method to mutate the backgrounds, thread-safety
     *
     * Note: it is possible to return a Floor object from this function, but it is not recommended
     * to do so. All amendment should be done within the function. As described in the reflection.
     *
     * @param function a function to apply certain actions to background and return a value,
     *                 thread-safety
     */
    public synchronized <R> R applyToBackground(Function<Floor[][], R> function) {
        return function.apply(backgrounds);
    }

    /**
     * reinitialise the world with the current level
     */
    public synchronized void reinit() {
        reiniting = true;
        load("res/levels/" + level + ".lvl");
        reiniting = false;
    }

    /**
     * increment score if one of the target has a stone on it
     */
    public synchronized void incrementScore() {
        ++score;
    }

    /**
     * decrement score if the stone on the target was removed
     */
    public synchronized void decrementScore() {
        --score;
    }

    /**
     * Respond to key press
     * @param key the value in the predefined constants in Input
     * @param c the corresponding character of the key press
     */
    @Override
    public void keyPressed(int key, char c) {
        if(level == LEVEL_COUNT)
            return;

        // force reinitialisation
        if(key == Input.KEY_R) {
            reinit();
            return;
        }

        // undo action
        if(key == Input.KEY_Z) {
            applyToBackground(backgrounds ->  {
                player.undo(backgrounds);
                return true;
            });
            return;
        }

        KeyAction action;
        // only respond to the key represents movement
        if(key == keys.up)
            action = KeyAction.Up;
        else if(key == keys.down)
            action = KeyAction.Down;
        else if(key == keys.left)
            action = KeyAction.Left;
        else if(key == keys.right)
            action = KeyAction.Right;
        else
            return;

        // player have the priority to move first
        applyToBackground(backgrounds -> {
            // if the player encounter a block, try to push it
            Movable block = player.moveInDirection(action, backgrounds);
            if(block instanceof AbstractBlock)
                player.push((AbstractBlock)block, action, backgrounds);
            // if the player encounter an enemy, try to move the enemy first, if fails, it is
            // determined to be in contact
            else if(block instanceof AbstractEnemy) {
                AbstractEnemy enemy = (AbstractEnemy)block;
                if(enemy.isLocked()) {
                    enemy.contact();
                    return false;
                }

                block = enemy.moveInDirection(action, backgrounds);
                if(block instanceof Player) {
                    enemy.contact();
                    return false;
                }

                player.moveInDirection(action, backgrounds);
                // lock the movement of the enemy to ensure that it will not move again
                enemy.setLock();
            }

            // move the enemies
            for(AbstractEnemy e : enemies) {
                block = e.moveInDirection(action, backgrounds);
                if(block instanceof Player) {
                    e.contact();
                    return false;
                }
                // unlock the movement of all enemies
                e.releaseLock();
            }

            return true;
        });
    }

    @Override
    public void keyReleased(int key, char c) {}

    @Override
    public void setInput(Input input) {}

    /**
     * enable this key listener
     * @return true
     */
    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void inputEnded() {}

    @Override
    public void inputStarted() {}

    private void mission_complete() {
        level++;
        reinit();
    }

    // load the level file
    private synchronized void load(String filename) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // reinitialise all variables
            stones = new LinkedList<>();
            enemies = new LinkedList<>();
            tnts = new LinkedList<>();
            player = null;
            score = targets = 0;

            // create the backgrounds and set the boundary of the current level
            String curr = reader.readLine();
            String[] buff = curr.split(",");
            int width = Integer.parseInt(buff[0]), height = Integer.parseInt(buff[1]);
            backgrounds = new Floor[height][width];
            boundary = new Boundary((SCREEN_WIDTH - width * TILE_SIZE) / 2.0f,
                                    (SCREEN_HEIGHT - height * TILE_SIZE) / 2.0f,
                                    (SCREEN_WIDTH + width * TILE_SIZE) / 2.0f,
                                    (SCREEN_HEIGHT + height * TILE_SIZE) / 2.0f);

            String classname;
            Constructor con;
            Switch _switch = null;
            Door door = null;
            while((curr = reader.readLine()) != null) {
                buff = curr.split(",");
                int x = Integer.parseInt(buff[1]), y = Integer.parseInt(buff[2]);
                float posX = boundary.origX + x * TILE_SIZE, posY = boundary.origY + y * TILE_SIZE;
                switch(buff[0]) {
                    // background tiles
                    case "wall":
                    case "cracked":
                    case "floor":
                        classname = "com.swen20003.project.game.sprite.background." +
                                    buff[0].substring(0, 1).toUpperCase() + buff[0].substring(1);
                        con = Class.forName(classname).getConstructors()[0];
                        backgrounds[y][x] = (Floor)con.newInstance(posX, posY);
                        break;
                    // for the door-switch pair, I was told that they actually overlap the original
                    // floor (where I initially thought there is no background floor behind them),
                    // I still decided to replace the original background to reduce extra space for
                    // the background floors
                    case "door":
                        backgrounds[y][x] = door = new Door(posX, posY);
                        break;
                    case "switch":
                        backgrounds[y][x] = _switch = new Switch(posX, posY);
                        break;
                    case "target":
                        backgrounds[y][x] = new Target(posX, posY, this);
                        ++targets;
                        break;

                    // blocks
                    case "stone":
                    case "ice":
                        classname = "com.swen20003.project.game.sprite.block." +
                                    buff[0].substring(0, 1).toUpperCase() + buff[0].substring(1);
                        con = Class.forName(classname).getConstructors()[0];
                        Stone stone = (Stone)con.newInstance(posX, posY, this);
                        backgrounds[y][x].putBlock(stone);
                        stones.add(stone);
                        break;
                    case "tnt":
                        TNT tnt = new TNT(posX, posY, this);
                        backgrounds[y][x].putBlock(tnt);
                        tnts.add(tnt);
                        break;

                    // units
                    case "player":
                        if(player != null)
                            throw new Exception("too many players supplied");
                        player = new Player(posX, posY, this);
                        backgrounds[y][x].putBlock(player);
                        break;
                    case "mage":
                    case "rogue":
                    case "skeleton":
                        classname = "com.swen20003.project.game.sprite.unit." +
                                    buff[0].substring(0, 1).toUpperCase() + buff[0].substring(1);
                        con = Class.forName(classname).getConstructors()[0];
                        AbstractEnemy enemy = (AbstractEnemy)con.newInstance(posX, posY, this);
                        backgrounds[y][x].putBlock(enemy);
                        enemies.add(enemy);
                        break;
                    default:
                        System.err.println("invalid type " + buff[0]);
                        throw new FileCorruptedException();
                }
            }

            if(door == null && _switch != null || door != null && _switch == null)
                throw new FileCorruptedException();

            if(_switch != null)
                _switch.setDoor(door);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Static inner immutable class which represents the boundary of the map, cannot be created
     * outside
     */
    public final static class Boundary {
        // all boundaries are constants
        public final float origX, origY, endX, endY;

        // only the World class can create this
        private Boundary(float origX, float origY, float endX, float endY) {
            this.origX = origX;
            this.origY = origY;
            this.endX = endX;
            this.endY = endY;
        }
    }
}
