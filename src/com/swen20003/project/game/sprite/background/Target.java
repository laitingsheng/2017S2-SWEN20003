package com.swen20003.project.game.sprite.background;

import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.block.Stone;
import com.swen20003.project.game.World;
import com.swen20003.project.game.sprite.AbstractSprite;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

/**
 * The sprite represents a Target, which is actually a special type of Floor
 * @author Tingsheng Lai
 */
public final class Target extends Floor {
    private final static Image IMAGE_SRC = ImageFactory.getImage("target");
    private World map;

    /**
     * Construct a Target object
     * @param x x-coordinate
     * @param y y-coordinate
     * @param map the World class (map) to put the player
     */
    public Target(float x, float y, World map) {
        super(x, y, IMAGE_SRC);
        this.map = map;
    }

    /**
     * Removing a stone from the target incur a decrement of score in <code>map</code>
     * @param block the block to be removed from the current tile
     */
    @Override
    public void removeBlock(Movable block) {
        super.removeBlock(block);
        if(block instanceof Stone)
            map.decrementScore();
    }

    /**
     * Putting a stone on the target will increment the score in <code>map</code>
     * @param block the block to be put on the current tile
     */
    @Override
    public void putBlock(Movable block) {
        super.putBlock(block);
        if(block instanceof Stone)
            map.incrementScore();
    }
}
