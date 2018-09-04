package com.swen20003.project.game.sprite.block;

import com.swen20003.project.game.sprite.MovableObject;
import com.swen20003.project.game.World;
import org.newdawn.slick.Image;

/**
 * An abstract class as a base class of all blocks
 * @author Tingsheng Lai
 */
public abstract class AbstractBlock extends MovableObject {
    protected AbstractBlock(float x, float y, World map, Image image_src) {
        super(x, y, map, image_src);
    }
}
