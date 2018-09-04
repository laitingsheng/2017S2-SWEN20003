package com.swen20003.project.game.sprite.block;

import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.World;
import com.swen20003.project.game.util.KeyAction;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Image;

public final class Ice extends Stone {
    private final static Image IMAGE_SRC = ImageFactory.getImage("ice");
    private final static int INTERVAL = 250;
    private int elapsed = 0;
    private boolean stop = true;
    private KeyAction direction;

    /**
     * Construct an Ice object
     * @param x   x-coordinate
     * @param y   y-coordinate
     * @param map the World class (map) to put the player
     */
    public Ice(float x, float y, World map) {
        super(x, y, map, IMAGE_SRC);
    }

    /**
     * The ice starts sliding when a unit push it
     * @param direction As in Movable
     * @param backgrounds As in Movable
     * @return As in Movable
     */
    @Override
    public Movable moveInDirection(KeyAction direction, Floor[][] backgrounds) {
        Movable block = super.moveInDirection(direction, backgrounds);

        // the ice starts sliding if the action performs successfully
        if(block == this) {
            stop = false;
            elapsed = 0;
            this.direction = direction;
        }
        return block;
    }

    /**
     * Update the position of ice if it is in sliding condition
     * @param delta As in World.update()
     */
    @Override
    public void update(int delta) {
        if(!stop) {
            elapsed += delta;
            if(elapsed >= INTERVAL) {
                // determine whether the ice is blocked by any objects or walls
                stop = map.applyToBackground(backgrounds ->
                        moveInDirection(direction, backgrounds) != this);
                elapsed = 0;
            }
        }
    }

    /**
     * Force to stop the sliding
     */
    public void stop() {
        stop = true;
        elapsed = 0;
    }
}
