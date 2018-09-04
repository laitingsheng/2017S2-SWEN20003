package com.swen20003.project.util;

import org.newdawn.slick.Image;

import java.util.HashMap;

/**
 * A factory contains all image to be rendered
 */
public final class ImageFactory {
    private final static HashMap<String, Image> images;

    // initialise and insert the images
    static {
        images = new HashMap<>();
    }

    /**
     * Return the original copy of the image, or null if the input name does not exists
     * @param name the name of the image to be loaded
     * @return the image stored
     */
    public static Image getImage(String name) {
        Image tmp = images.get(name);
        if(tmp == null)
            return insert(name);
        return tmp;
    }

    // insert and return the image
    private static Image insert(String name) {
        Image tmp = load_image("res/" + name + ".png");
        images.put(name, tmp);
        return tmp;
    }

    // load the image from the file system
    private static Image load_image(String filename) {
        Image tmp = null;
        try {
            tmp = new Image(filename);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return tmp;
    }
}
