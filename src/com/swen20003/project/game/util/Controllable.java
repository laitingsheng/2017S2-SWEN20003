package com.swen20003.project.game.util;

import com.swen20003.project.game.sprite.block.AbstractBlock;
import com.swen20003.project.game.sprite.background.Floor;

public interface Controllable extends Movable {
    /**
     * Determine the ability of pushing a block, perform the pushing if possible
     * @param block the block to be pushed
     * @param direction the direction to be pushed
     * @param backgrounds As in Movable
     * @return true if the action is successful
     */
    default boolean push(AbstractBlock block, KeyAction direction, Floor[][] backgrounds) {
        return false;
    }
}
