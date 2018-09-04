package com.swen20003.project.game.exception;

import java.io.IOException;

/**
 * An IOException which will be raised if the input level file is corrupted
 * @author Tingsheng Lai
 */
public class FileCorruptedException extends IOException {
    public FileCorruptedException() {
        super("data file corrupted");
    }
}
