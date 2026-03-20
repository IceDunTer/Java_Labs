package dev.boev.lab3;

import javafx.scene.image.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageLoader {

    public static Image loadFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            return new Image(inputStream, 800, 600, true, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image loadFromResource(String path) {
        InputStream inputStream = ImageLoader.class.getResourceAsStream(path);
        if (inputStream == null) return null;

        try {
            return new Image(inputStream, 800, 600, true, true);
        } finally {
            try { inputStream.close(); } catch (IOException ignored) {}
        }
    }
}