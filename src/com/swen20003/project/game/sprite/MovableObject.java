package com.swen20003.project.game.sprite;

import com.swen20003.project.game.util.KeyAction;
import com.swen20003.project.game.World;
import com.swen20003.project.game.exception.InvalidActionException;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.util.Movable;
import org.newdawn.slick.Image;

import static com.swen20003.project.App.TILE_SIZE;

/**
 * An abstract class represents all movable sprites
 * @author Tingsheng Lai
 */
public abstract class MovableObject extends AbstractSprite implements Movable {
    protected World map;

    protected MovableObject(float x, float y, World map, Image image_src) {
        super(x, y, image_src);
        this.map = map;
    }

    /**
     * Set the position of the current sprite
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param backgrounds pass from <code>World.applyToBackground</code>
     * @return As in <code>Movable</code>
     */
    public final Movable setPosition(float x, float y, Floor[][] backgrounds) {
        Floor next = backgrounds[map.yToIndex(y)][map.xToIndex(x)];
        if(!next.canMoveOn(this))
            return next.getBlock();

        backgrounds[map.yToIndex(this.y)][map.xToIndex(this.x)].removeBlock(this);
        next.putBlock(this);
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Get the position of the current sprite
     * @return A Coordinate object with current x-coordinate and y-coordinate
     */
    public final synchronized Coordinate getPosition() {
        return new Coordinate(x, y);
    }

    /**
     * Default movement method
     * @param direction   the direction to move
     * @param backgrounds the backgrounds passed from <code>World.applyToBackground</code>
     * @return As in <code>Movable</code>
     */
    @Override
    public Movable moveInDirection(KeyAction direction, Floor[][] backgrounds) {
        float tmpX = x, tmpY = y;
        World.Boundary boundary = map.getBoundary();

        switch(direction) {
            case Up:
                tmpY -= TILE_SIZE;
                if(tmpY < boundary.origY)
                    return null;
                break;
            case Down:
                tmpY += TILE_SIZE;
                if(tmpY > boundary.endY - TILE_SIZE)
                    return null;
                break;
            case Left:
                tmpX -= TILE_SIZE;
                if(tmpX < boundary.origX)
                    return null;
                break;
            case Right:
                tmpX += TILE_SIZE;
                if(tmpX > boundary.endX - TILE_SIZE)
                    return null;
                break;
            default:
                throw new InvalidActionException();
        }

        return setPosition(tmpX, tmpY, backgrounds);
    }

    /**
     * Wrap the coordinate of the current sprite
     */
    public final static class Coordinate {
        public final float x, y;

        // can only created by MovableObject
        private Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
