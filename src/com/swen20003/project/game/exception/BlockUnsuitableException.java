package com.swen20003.project.game.exception;

/**
 * Unchecked runtime exception, represents a block has been mistakenly placed on a floor
 * @author Tingsheng Lai
 */
public class BlockUnsuitableException extends RuntimeException {
    public BlockUnsuitableException() {
        super("block cannot be put on the current location");
    }
}
