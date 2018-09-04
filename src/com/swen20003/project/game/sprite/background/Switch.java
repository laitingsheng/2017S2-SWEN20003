package com.swen20003.project.game.sprite.background;

import com.swen20003.project.game.util.Movable;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * A switch tile to control a Door object
 * @author Tingsheng Lai
 */
public final class Switch extends Floor {
    private final static Image IMAGE_SRC = ImageFactory.getImage("switch"),
            IMAGE_SRC_FLOOR = ImageFactory.getImage("floor");
    private Door door;

    /**
     * Construct a Switch object
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Switch(float x, float y) {
        super(x, y, IMAGE_SRC);
    }

    /**
     * Set the door relates to the current switch
     * @param door
     */
    public void setDoor(Door door) {
        this.door = door;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(IMAGE_SRC_FLOOR, x, y);
        super.render(g);
    }

    /**
     * Any <code>MovableObject</code>s on the switch will open the door
     * @param block the block to be put on the current tile
     */
    @Override
    public void putBlock(Movable block) {
        super.putBlock(block);
        door.open = true;
    }

    /**
     * Removal of block will close the door
     * @param block the block to be removed from the current tile
     */
    @Override
    public void removeBlock(Movable block) {
        super.removeBlock(block);
        door.open = false;
    }
}
