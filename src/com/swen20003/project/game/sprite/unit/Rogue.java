package com.swen20003.project.game.sprite.unit;

import com.swen20003.project.game.exception.InvalidActionException;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.block.AbstractBlock;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.World;
import com.swen20003.project.game.util.KeyAction;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

/**
 * Rogue is a type of enemy which will move horizontally when the user mtry to ove the player
 */
public final class Rogue extends AbstractEnemy {
    private static final Image IMAGE_SRC_LEFT = ImageFactory.getImage("rogue"),
            IMAGE_SRC_RIGHT = IMAGE_SRC_LEFT .getFlippedCopy(true, false);
    private KeyAction direction = KeyAction.Left;

    /**
     * Construct a Rogue object
     * @param x x-coordinate
     * @param y y-coordinate
     * @param map the World class (map) which includes the current enemy
     */
    public Rogue(float x, float y, World map) {
        super(x, y, map, IMAGE_SRC_LEFT);
    }

    /**
     * Rogues will only move horizontally, regardless of the direction the player moved
     * @param _direction unused parameter, only for conforming to the interface
     * @param backgrounds As in <code>Movable</code>
     * @return As in <code>Movable</code>
     */
    @Override
    public Movable moveInDirection(KeyAction _direction, Floor[][] backgrounds) {
        if(lock)
            return null;

        Movable block = moveByKey(direction, backgrounds,  IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);
        if(block == this)
            return this;

        if(block instanceof Player)
            return block;

        // push a block if possible
        if(block instanceof AbstractBlock)
            if(push((AbstractBlock)block, direction, backgrounds))
                return this;

        // if the movement is not valid, change direction and repeat the process before
        if(direction == KeyAction.Right)
            direction = KeyAction.Left;
        else if(direction == KeyAction.Left)
            direction = KeyAction.Right;
        else
            throw new InvalidActionException();

        block = moveByKey(direction, backgrounds,  IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);

        if(block == this)
            return this;

        if(block instanceof AbstractBlock)
            if(push((AbstractBlock)block, direction, backgrounds))
                return this;

        return block;
    }

    /**
     * Rogue has the ability to push a block
     * @param block the block to be pushed
     * @param direction the direction to be pushed
     * @param backgrounds As in Movable
     * @return True if the push action performed
     */
    @Override
    public boolean push(AbstractBlock block, KeyAction direction, Floor[][] backgrounds) {
        return push(block, direction, backgrounds, IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);
    }
}
