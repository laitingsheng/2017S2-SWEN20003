package com.swen20003.project.game.sprite.background;

import com.swen20003.project.game.exception.BlockUnsuitableException;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.block.TNT;
import com.swen20003.project.game.World;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

/**
 * A cracked wall is a special type of wall that can be cracked by TNT
 * @author Tingsheng Lai
 */
public final class Cracked extends Wall {
    private final static Image IMAGE_SRC = ImageFactory.getImage("cracked_wall");

    /**
     * Construct a Cracked object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Cracked(float x, float y) {
        super(x, y, IMAGE_SRC);
    }

    /**
     * The only object can be moved onto the cracked wall is TNT
     * @param block unused parameter
     * @return <code>true</code> if the block is TNT
     */
    @Override
    public boolean canMoveOn(Movable block) {
        return block instanceof TNT;
    }

    /**
     * A TNT put on the cracked wall will have the effect of explosion, if the block is not TNT
     * then a runtime exception will be thrown
     * @param block the block to be put on the current tile
     */
    @Override
    public void putBlock(Movable block) {
        if(block instanceof TNT) {
            ((TNT)block).crack(this);
        } else
            throw new BlockUnsuitableException();
    }

    /**
     * An exploded cracked wall will be replace by a normal floor tile
     * @param tnt the TNT to be used
     * @param map the map to be updated
     */
    public void exploded(TNT tnt, World map) {
        if(tnt == null)
            throw new NullPointerException();

        int w = map.xToIndex(x), h = map.yToIndex(y);
        map.applyToBackground((backgrounds) -> {
            backgrounds[h][w] = new Floor(x, y);
            return true;
        });
    }
}
