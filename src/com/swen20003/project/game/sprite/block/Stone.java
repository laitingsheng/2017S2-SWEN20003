package com.swen20003.project.game.sprite.block;

import com.swen20003.project.game.World;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

/**
 * Stone can move in the same way as Player, but it can move only if a Player push it and the
 * direction it moves is not blocked.
 */
public class Stone extends AbstractBlock {
    private final static Image IMAGE_SRC = ImageFactory.getImage("stone");

    /**
     * Construct a Stone object
     * @param x   x-coordinate
     * @param y   y-coordinate
     * @param map the World class (map) to put the player
     */
    public Stone(float x, float y, World map) {
        super(x, y, map, IMAGE_SRC);
    }

    protected Stone(float x, float y, World map, Image image_src) {
        super(x, y, map, image_src);
    }
}
