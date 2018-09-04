package com.swen20003.project.game.sprite.unit;

import com.swen20003.project.game.sprite.block.Ice;
import com.swen20003.project.game.sprite.block.TNT;
import com.swen20003.project.game.util.Movable;
import com.swen20003.project.game.sprite.block.AbstractBlock;
import com.swen20003.project.game.sprite.background.Floor;
import com.swen20003.project.game.World;
import com.swen20003.project.game.util.KeyAction;
import com.swen20003.project.util.ImageFactory;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * Player is a unit which can push block can control by the keyboard
 *
 * @author Tingsheng Lai
 */
public final class Player extends AbstractUnit {
    private final static Image IMAGE_SRC_LEFT = ImageFactory.getImage("player_left"),
            IMAGE_SRC_RIGHT = IMAGE_SRC_LEFT .getFlippedCopy(true, false);
    private PlayerState ps;         // record the state
    private int moves = 0;          // record the number of moves

    /**
     * Constructor to create a player on map with an initial coordinate
     * @param x   x-coordinate
     * @param y   y-coordinate
     * @param map the World class (map) to put the player
     */
    public Player(float x, float y, World map) {
        // default direction is to the left
        super(x, y, map, IMAGE_SRC_LEFT);
        this.map = map;
        if(World.UNDO_ENABLED)
            ps = new PlayerState();
    }

    /**
     * Normal rendering process, plus the render of the counter of movement
     * @param g As in World.render()
     */
    @Override
    public void render(Graphics g) {
        super.render(g);

        // render the move, the position is tested and aligned to the FPS
        g.drawString("Moves: " + moves, 10, 36);
    }

    /**
     * Record the state and increase the counter, then move in the direction
     * @param direction As in Movable
     * @param backgrounds As in Movable
     * @return As in Movable
     */
    @Override
    public Movable moveInDirection(KeyAction direction, Floor[][] backgrounds) {
        ps.add_state();
        moves++;            // increment the counter regardless of the validity of movement
        Movable block = moveByKey(direction, backgrounds, IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);

        // record the block it encounter
        if(block instanceof AbstractBlock)
            ps.set_block((AbstractBlock)block);
        return block;
    }

    @Override
    public boolean push(AbstractBlock block, KeyAction direction, Floor[][] backgrounds) {
        boolean re = push(block, direction, backgrounds, IMAGE_SRC_LEFT, IMAGE_SRC_RIGHT);
        // if the push action fail, remove the record of block
        if(!re)
            ps.set_block(null);
        return re;
    }

    public boolean undo(Floor[][] backgrounds) {
        return World.UNDO_ENABLED && ps.undo(backgrounds);
    }

    // a linked list implementation of record
    private class PlayerState {
        private int size;
        private State start, end;

        private PlayerState() {
            start = end = null;
            size = 0;
        }

        private boolean add_state() {
            if(size == 0) {
                start = end = new State(null, null, null);
                size = 1;
                return true;
            }

            // if the position remains unchanged, not to repeat the record
            if(same_pos())
                return false;

            end.next = new State(end, null, null);
            end = end.next;

            // trim to the given maximum size
            if(size == World.MAXIMUM_UNDO) {
                start = start.next;
                start.prev = null;
            } else
                ++size;

            return true;
        }

        // set the block with respect to the current movement
        private void set_block(AbstractBlock block) {
            end.block = block;
            if(block != null)
                end.block_pos = block.getPosition();
            else
                end.block_pos = null;
        }

        private boolean undo(Floor[][] backgrounds) {
            if(size == 0)
                return false;

            // if the last record is the current position, step back 1
            if(same_pos()) {
                end = end.prev;
                --size;
            }

            int w = map.xToIndex(x), h = map.yToIndex(y);
            Floor prev = backgrounds[h][w], next;
            w = map.xToIndex(end.pos.x);
            h = map.yToIndex(end.pos.y);
            next = backgrounds[h][w];

            // if the floor has other block, the undo fails
            if(!next.canMoveOn(Player.this)) {
                if(same_pos()) {
                    end = end.next;
                    ++size;
                }
                return false;
            }

            // move back to the previous position
            prev.removeBlock(Player.this);
            next.putBlock(Player.this);
            x = end.pos.x;
            y = end.pos.y;
            moves = end.moves;

            // if contact with any block, undo the block as well (except an exploded TNT)
            if(end.block != null && (!(end.block instanceof TNT) || ((TNT)end.block).usable()))
                end.block.setPosition(end.block_pos.x, end.block_pos.y, backgrounds);

            // undo the ice and stop the ice by default
            if(end.block instanceof Ice) {
                Ice tmp = (Ice)end.block;
                tmp.stop();
            }

            // point to the previous position
            end = end.prev;
            end.next = null;
            --size;
            return true;
        }

        // determine if the record is the same as the current position
        private boolean same_pos() {
            return x == end.pos.x && y == end.pos.y;
        }

        private class State {
            private State prev, next;
            private final Coordinate pos;           // position of player
            private Coordinate block_pos;           // position of block (if any)
            private AbstractBlock block;            // the block contact with (if any)
            private final int moves;                // the movement counter

            private State(State prev, State next, AbstractBlock block) {
                this.prev = prev;
                this.next = next;
                moves = Player.this.moves;
                pos = getPosition();

                this.block = block;
                if(block != null)
                    block_pos = block.getPosition();
                else
                    block_pos = null;
            }
        }
    }
}
