package com.example.quranfx;

import javafx.scene.image.Image;

public class Media_pool {
    private String id;
    private Image thumbnail;

    public Media_pool(String id, Image thumbnail) {
        this.id = id;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public Image getThumbnail() {
        return thumbnail;
    }
}
