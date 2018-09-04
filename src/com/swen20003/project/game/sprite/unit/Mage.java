package com.swen20003.project.game.sprite.unit;

import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.World;
import com.swen20003.project.game.util.KeyAction;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

/**
 * Mage is a clever enemy which can track down the player
 */
public final class Mage extends AbstractEnemy {
    private static final Image IMAGE_SRC_LEFT = ImageFactory.getImage("mage"),
            IMAGE_SRC_RIGHT = IMAGE_SRC_LEFT .getFlippedCopy(true, false);

    /**
     * Construct a Mage object
     * @param x x-coordinate
     * @param y y-coordinate
     * @param map the World class (map) which includes the current enemy
     */
    public Mage(float x, float y, World map) {
        super(x, y, map, IMAGE_SRC_LEFT);
    }

    /**
     * Mage will determine its movement according to the current position of the player, using
     * the algorithm described in the specification
     * @param _direction unused parameter, only for conforming to the interface
     * @param backgrounds As in <code>Movable</code>
     * @return As in <code>Movable</code>
     */
    @Override
    public Movable moveInDirection(KeyAction _direction, Floor[][] backgrounds) {
        if(lock)
            return null;

        Coordinate player = map.getPlayerPosition();
        Movable block;
        float xDiff = player.x - x, yDiff = player.y - y;

        if(Math.abs(xDiff) > Math.abs(yDiff)) {
            if(xDiff < 0)
                block = moveByKey(KeyAction.Left, backgrounds, IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);
            else
                block = moveByKey(KeyAction.Right, backgrounds, IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);

            if(block instanceof Player)
                return block;
        }

        if(yDiff < 0)
            block = moveByKey(KeyAction.Up, backgrounds, IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);
        else
            block = moveByKey(KeyAction.Down, backgrounds, IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);

        return block;
    }
}
