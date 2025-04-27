package com.example.quranfx;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Base64_image {
    private static final Base64_image instance = new Base64_image();
    private Base64_image(){}
    public static Base64_image getInstance() {
        return instance;
    }

    public BufferedImage vertical_place_holder = image_to_buffered_image(new Image(new File("default_images/9_16_no_image.bmp").toURI().toString()));
    public BufferedImage horizontal_place_holder = image_to_buffered_image(new Image(new File("default_images/16_9_no_image.bmp").toURI().toString()));
    public BufferedImage square_place_holder = image_to_buffered_image(new Image(new File("default_images/1_1_no_image.bmp").toURI().toString()));

    private BufferedImage image_to_buffered_image(Image image) {
        return SwingFXUtils.fromFXImage(image, null);
    }
}
