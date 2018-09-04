package com.swen20003.project.game.util;

import com.swen20003.project.game.sprite.background.Floor;

/**
 * An interface contains action that can be perform by a <code>MovableObject</code>
 *
 * @author Tingsheng Lai
 */
public interface Movable {
    /**
     * Move the object in a specific direction with one tile
     *
     * @param direction   the direction to move
     * @param backgrounds the backgrounds passed from <code>World.applyToBackground</code>
     * @return a Movable object, <code>this</code> represents a successful move, <code>null</code>
     * represents an invalid move or other object means there is a object on the current
     * floor
     */
    Movable moveInDirection(KeyAction direction, Floor[][] backgrounds);
}
