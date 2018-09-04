/**
 * Project skeleton for SWEN20003: Object Oriented Software Development 2017
 *
 * @author Eleanor McMurtry
 */

package com.swen20003.project;

import com.swen20003.project.game.World;
import com.swen20003.project.util.KeyComb;
import org.newdawn.slick.*;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Main class for the game.
 * Handles initialisation, input and rendering
 * @author Tingsheng Lai
 */
public class App extends BasicGame {
    public static final Properties p;

    /**
     * screen width, in pixels
     */
    public static final int SCREEN_WIDTH;
    /**
     * screen height, in pixels
     */
    public static final int SCREEN_HEIGHT;
    /**
     * size of the tiles, in pixels
     */
    public static final int TILE_SIZE;
    /**
     * Default key combination for movement
     */
    public static final KeyComb DEFAULT_KEYS;

    // static initialisation
    static {
        p = new Properties();
        try(FileInputStream f = new FileInputStream("res/Settings.properties")) {
            p.load(f);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        SCREEN_WIDTH = Integer.parseInt(p.getProperty("Screen_Width"));
        SCREEN_HEIGHT = Integer.parseInt(p.getProperty("Screen_Height"));
        TILE_SIZE = Integer.parseInt(p.getProperty("Tile_Size"));
        DEFAULT_KEYS = new KeyComb(Input.KEY_UP, Input.KEY_DOWN, Input.KEY_LEFT, Input.KEY_RIGHT);
    }

    private World world;

    /**
     * Construct the App class
     */
    public App() {
        super("Shadow Blocks");
    }

    /**
     * Start-up method. Creates the game and runs it.
     * @param args Command-line arguments (ignored).
     */
    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new App(), SCREEN_WIDTH, SCREEN_HEIGHT, false);
        app.setShowFPS(true);
        app.start();
    }

    /**
     * Initialise the Slick graphics
     * @param gc a GameContainer object to control the setting
     */
    @Override
    public void init(GameContainer gc) {
        world = new World();
        gc.getInput().addKeyListener(world);
    }

    /**
     * Update the game state for a frame.
     * @param gc    The Slick game container object.
     * @param delta Time passed since last frame (milliseconds).
     */
    @Override
    public void update(GameContainer gc, int delta) {
        world.update(delta);
    }

    /**
     * Render the entire screen, so it reflects the current game state.
     * @param gc The Slick game container object.
     * @param g  The Slick graphics object, used for drawing.
     */
    @Override
    public void render(GameContainer gc, Graphics g) {
        world.render(g);
    }
}