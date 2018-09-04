package com.swen20003.project.game.sprite.background;

import com.swen20003.project.game.exception.BlockUnsuitableException;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.AbstractSprite;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

/**
 * A wall can block the movement of a <code>MovableObject</code>
 * @author Tingsheng Lai
 */
public class Wall extends Floor {
    private final static Image IMAGE_SRC = ImageFactory.getImage("wall");

    /**
     * Construct a Wall object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Wall(float x, float y) {
        super(x, y, IMAGE_SRC);
    }

    protected Wall(float x, float y, Image image_src) {
        super(x, y, image_src);
    }

    /**
     * All objects cannot be placed on a wall
     * @param block unused parameter
     * @return always <code>false</code>
     */
    @Override
    public boolean canMoveOn(Movable block) {
        // Wall cannot be moved on
        return false;
    }

    /**
     * A runtime exception will be raised if call this method directly
     * @param block the block to be put on the current tile
     */
    @Override
    public void putBlock(Movable block) {
        throw new BlockUnsuitableException();
    }
}
