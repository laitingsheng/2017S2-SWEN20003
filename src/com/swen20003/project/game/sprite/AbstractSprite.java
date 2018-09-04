package com.swen20003.project.game.sprite;

import com.swen20003.project.game.util.Refreshable;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class AbstractSprite implements Refreshable {
    protected float x, y;                   // position of the sprite
    protected Image image_src;              // the image source to be rendered

    protected AbstractSprite(float x, float y, Image image_src) {
        this.x = x;
        this.y = y;
        this.image_src = image_src;
    }

    /**
     * Default rendering method
     * @param g As in World.render()
     */
    @Override
    public void render(Graphics g) {
        // Default rendering method
        g.drawImage(image_src, x, y);
    }

}
