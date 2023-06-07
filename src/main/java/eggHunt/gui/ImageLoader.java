package eggHunt.gui;

import javafx.scene.image.Image;

import java.io.File;

// Class to handle loading of Images
public class ImageLoader {
    public static Image loadImage(String filePath) {
        return new Image(new File(filePath).toURI().toString());
    }
}
