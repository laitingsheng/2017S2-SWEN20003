package com.swen20003.project.game.sprite.unit;

import com.swen20003.project.game.World;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

/**
 * An abstract class of enemies, predefined some common actions and properties
 *
 * @author Tingsheng Lai
 */
public abstract class AbstractEnemy extends AbstractUnit {
    private static final Sound CONTACT;             // the sound effect when contact with player
    protected boolean lock = false;                 // lock the current position

    static {
        Sound tmp = null;
        try {
            tmp = new Sound("res/contact.ogg");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        CONTACT = tmp;
    }

    protected AbstractEnemy(float x, float y, World map, Image image_src) {
        super(x, y, map, image_src);
    }

    /**
     * Determine whether the current object is locked in the position or not
     * @return true if it is locked
     */
    public boolean isLocked() {
        return lock;
    }

    /**
     * Lock the movement of the enemy
     */
    public void setLock() {
        lock = true;
    }

    /**
     * Unlock the movement of the enemy
     */
    public void releaseLock() {
        lock = false;
    }

    /**
     * Call when the player contact with the current enemy
     */
    public void contact() {
        CONTACT.play();

        // the game restarts if player contact with the enemies
        map.reinit();
    }
}
