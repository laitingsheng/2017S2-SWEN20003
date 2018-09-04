package com.swen20003.project.game.sprite.background;

import com.swen20003.project.game.exception.BlockUnsuitableException;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * A Door object controlled by a switch
 * @author Tingsheng Lai
 */
public final class Door extends Floor {
    private final static Image IMAGE_SRC = ImageFactory.getImage("door"),
            IMAGE_SRC_FLOOR = ImageFactory.getImage("floor");
    boolean open = false;                       // represents the state of the door, package-private

    /**
     * Construct a Door object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Door(float x, float y) {
        super(x, y, IMAGE_SRC);
    }

    /**
     * Determine which image to be rendered, the door will be hidden if it is open and render the
     * floor image
     * @param g As in World.render()
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(IMAGE_SRC_FLOOR, x, y);
        if(!open)
            super.render(g);
    }

    /**
     * Object cannot move onto a closed door
     * @param block As in Floor
     * @return As in Floor
     */
    @Override
    public boolean canMoveOn(Movable block) {
        return open && super.canMoveOn(block);
    }

    /**
     * Cannot put a block on a closed door
     * method
     * @param block As in Floor
     */
    @Override
    public void putBlock(Movable block) {
        if(!open)
            throw new BlockUnsuitableException();
        super.putBlock(block);
    }
}
