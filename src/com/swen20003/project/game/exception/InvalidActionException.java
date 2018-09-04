package com.swen20003.project.game.exception;

/**
 * Unchecked runtime exception, used to identify an unknown keyboard action is passed to the
 * Movable object
 * @author Tingsheng Lai
 */
public class InvalidActionException extends RuntimeException {
    public InvalidActionException() {
        super("undefined key board action");
    }
}
