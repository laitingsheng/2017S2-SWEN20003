package com.swen20003.project.util;

/**
 * The key combination of actions of Player
 *
 * @author Tingsheng Lai
 */
public final class KeyComb {
    public final int up, down, left, right;

    /**
     * Construct a new KeyComb
     * @param up the key corresponds to Up action
     * @param down the key corresponds to Down action
     * @param left the key corresponds to Left action
     * @param right the key corresponds to Right action
     */
    public KeyComb(int up, int down, int left, int right) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }
}
