package com.example.quranfx;

import javafx.scene.image.Image;

public class Media_pool {
    private String id;
    private Image thumbnail;
    private String original_image_name;
    private boolean did_the_image_get_down_scaled;

    public Media_pool(String id, Image thumbnail, String original_image_name, boolean did_the_image_get_down_scaled) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.original_image_name = original_image_name;
        this.did_the_image_get_down_scaled = did_the_image_get_down_scaled;
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

    public boolean isDid_the_image_get_down_scaled() {
        return did_the_image_get_down_scaled;
    }
}
