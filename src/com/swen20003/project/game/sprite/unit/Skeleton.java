package com.swen20003.project.game.sprite.unit;

import com.swen20003.project.App;
import com.swen20003.project.game.util.KeyAction;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.World;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

/**
 * The skeleton is a type of enemy which is mindless
 *
 * @author Tingsheng Lai
 */
public final class Skeleton extends AbstractEnemy {
    private static final Image IMAGE_SRC = ImageFactory.getImage("skull");
    private final static int INTERVAL = 1000;
    private int sign = -1, elapsed = 0;         // move up by default

    /**
     * Construct a Skeleton object
     * @param x x-coordinate
     * @param y y-coordinate
     * @param map the World class (map) which includes the current enemy
     */
    public Skeleton(float x, float y, World map) {
        super(x, y, map, IMAGE_SRC);
    }

    /**
     * The skeleton will not respond to the key press, so its movement is always locked
     * @return <code>true</code>
     */
    @Override
    public boolean isLocked() {
        return true;
    }

    // not respond to the key pressed
    @Override
    public Movable moveInDirection(KeyAction action, Floor[][] backgrounds) {
        return null;
    }

    /**
     * Automove according to the specification
     * @param delta As in World.update()
     */
    @Override
    public void update(int delta) {
        elapsed += delta;
        if(elapsed >= INTERVAL) {
            map.applyToBackground(backgrounds -> {
                if(automove(backgrounds))
                    return true;

                // change direction and retry the movement
                sign = -sign;
                return automove(backgrounds);
            });
            elapsed = 0;
        }
    }

    private boolean automove(Floor[][] backgrounds) {
        int w = map.xToIndex(x), h = map.yToIndex(y);
        Floor curr = backgrounds[h][w], next;
        if((next = backgrounds[h + sign][w]).canMoveOn(this)) {
            curr.removeBlock(this);
            next.putBlock(this);
            y += sign * App.TILE_SIZE;
            return true;
        }

        if(next.getBlock() instanceof Player)
            contact();

        return false;
    }
}
