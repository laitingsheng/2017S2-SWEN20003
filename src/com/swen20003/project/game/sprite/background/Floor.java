package com.swen20003.project.game.sprite.background;

import com.swen20003.project.game.exception.BlockUnsuitableException;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.AbstractSprite;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

import java.util.ConcurrentModificationException;

/**
 * Every thing can move freely on a floor
 * @author Tingsheng Lai
 */
public class Floor extends AbstractSprite {
    private final static Image IMAGE_SRC = ImageFactory.getImage("floor");
    protected volatile Movable block = null;

    /**
     * Construct a Floor object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Floor(float x, float y) {
        super(x, y, IMAGE_SRC);
    }

    protected Floor(float x, float y, Image image_src) {
        super(x, y, image_src);
    }

    /**
     * Determine whether an object can move on to the current tile
     * @param block a block to be moved onto the current tile
     * @return <code>true</code> if the block can be put on the current tile
     */
    public boolean canMoveOn(Movable block) {
        // floor with nothing on it can be moved to
        return this.block == null;
    }

    /**
     * Get the block occupied the current tile
     * @return the block on the floor
     */
    public final Movable getBlock() {
        return block;
    }

    /**
     * Remove the block from the current tile
     * @param block the block to be removed from the current tile
     */
    public void removeBlock(Movable block) {
        if(this.block == null || this.block != block)
            throw new ConcurrentModificationException();

        this.block = null;
    }

    /**
     * Put a block on the current tile, should call <code>canMoveOn</code> before calling this
     * method
     * @param block the block to be put on the current tile
     */
    public void putBlock(Movable block) {
        if(this.block != null)
            throw new BlockUnsuitableException();
        this.block = block;
    }
}
