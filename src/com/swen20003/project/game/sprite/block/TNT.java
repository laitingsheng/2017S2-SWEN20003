package com.swen20003.project.game.sprite.block;

import com.swen20003.project.game.sprite.background.Cracked;
import com.swen20003.project.game.World;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

public final class TNT extends AbstractBlock {
    private final static int DISPLAY = 400;
    private final static Image IMAGE_SRC, EXPLOSION;
    private final static Sound EXPLOSION_SOUND;
    private boolean exploded = false;
    private int elapsed = 0;

    static {
        IMAGE_SRC = ImageFactory.getImage("tnt");
        EXPLOSION = ImageFactory.getImage("explosion");

        Sound tmp = null;
        try {
            tmp = new Sound("res/explosion.ogg");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        EXPLOSION_SOUND = tmp;
    }

    /**
     * Construct a TNT object
     * @param x   x-coordinate
     * @param y   y-coordinate
     * @param map the World class (map) to put the player
     */
    public TNT(float x, float y, World map) {
        super(x, y, map, IMAGE_SRC);
    }

    /**
     * If the TNT is exploded, then render the explosion effect
     * @param g
     */
    @Override
    public void render(Graphics g) {
        if(usable())
            super.render(g);
        else
            // the explosion is a 92*92 tile, adjust the position of display
            g.drawImage(EXPLOSION, x - 30, y - 30);
    }

    /**
     * Record the time elapsed for the explosion effect
     * @param delta As in World.update()
     */
    @Override
    public void update(int delta) {
        if(exploded)
            elapsed += delta;
    }

    /**
     * A TNT is usable if it is not exploded
     * @return <code>true</code> if not exploded
     */
    public boolean usable() {
        return !exploded;
    }

    /**
     * Determine the time elapsed for the explosion effect
     * @return <code>true</code> if the duration is longer than the threshold
     */
    public boolean timeOver() { return elapsed >= DISPLAY; }

    /**
     * Crack a given cracked wall
     * @param cw a Cracked wall to be exploded
     */
    public void crack(Cracked cw) {
        cw.exploded(this, map);
        exploded = true;
        EXPLOSION_SOUND.play();
    }
}
