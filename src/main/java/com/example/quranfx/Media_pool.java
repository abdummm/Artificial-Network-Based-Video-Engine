package com.example.quranfx;

import javafx.scene.image.Image;

public class Media_pool {
    private String id;
    private Image thumbnail;
    private String original_image_name;

    public Media_pool(String id, Image thumbnail, String original_image_name) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.original_image_name = original_image_name;
    }

    public String getId() {
        return id;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public String getOriginal_image_name() {
        return original_image_name;
    }
}
