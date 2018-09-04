package com.swen20003.project.game.util;

import org.newdawn.slick.Graphics;

/**
 * An interface represents all objects which can be rendered
 *
 * @author Tingsheng Lai
 */
@FunctionalInterface
public interface Refreshable {
    /**
     * Rendering method called by World.render()
     *
     * @param g As in World.render()
     */
    void render(Graphics g);

    /**
     * Default updating method, which does nothing
     *
     * @param delta As in World.update()
     */
    default void update(int delta) {}
}
