package com.example.quranfx;

import javafx.scene.image.Image;

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

    public Image vertical_place_holder = new Image(new File("default_images/9_16_no_image.bmp").toURI().toString());
    public Image horizontal_place_holder = new Image(new File("default_images/16_9_no_image.bmp").toURI().toString());
    public Image square_place_holder = new Image(new File("default_images/1_1_no_image.bmp").toURI().toString());
    public String file_path_vertical_place_holder = "default_images/9_16_no_image.bmp";
    public String file_path_horizontal_place_holder = "default_images/16_9_no_image.bmp";
    public String file_path_square_place_holder = "default_images/1_1_no_image.bmp";


}
