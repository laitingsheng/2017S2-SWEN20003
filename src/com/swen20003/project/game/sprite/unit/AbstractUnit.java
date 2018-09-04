package com.swen20003.project.game.sprite.unit;

import com.swen20003.project.game.sprite.MovableObject;
import com.swen20003.project.game.sprite.block.AbstractBlock;
import com.swen20003.project.game.util.Controllable;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.World;
import com.swen20003.project.game.util.KeyAction;
import org.newdawn.slick.Image;

import java.util.ConcurrentModificationException;

/**
 * An abstract class of unit, predefined some possible common action
 *
 * @author Tingsheng Lai
 */
public abstract class AbstractUnit extends MovableObject implements Controllable {
    protected AbstractUnit(float x, float y, World map, Image image_src) {
        super(x, y, map, image_src);
    }

    protected Movable moveByKey(KeyAction action, Floor[][] backgrounds, Image image_src_left,
                                Image image_src_right) {
        // change the image according to the direction
        if(action == KeyAction.Left)
            image_src = image_src_left;
        else if(action == KeyAction.Right)
            image_src = image_src_right;

        return super.moveInDirection(action, backgrounds);
    }

    protected boolean push(AbstractBlock block, KeyAction action, Floor[][] backgrounds,
                           Image image_src_left, Image image_src_right) {
        // try to move the block first
        if(block.moveInDirection(action, backgrounds) != block)
            return false;

        // move the player
        if(moveByKey(action, backgrounds, image_src_left, image_src_right) != this)
            throw new ConcurrentModificationException();
        return true;
    }
}
