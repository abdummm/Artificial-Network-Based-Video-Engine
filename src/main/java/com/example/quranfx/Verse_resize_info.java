package com.example.quranfx;

public class Verse_resize_info {
    private Resizing_mode resizing_mode;
    private double initial_mouse_x_position;
    private boolean set;

    public Verse_resize_info(Resizing_mode resizing_mode, double initial_mouse_x_position,boolean set) {
        this.resizing_mode = resizing_mode;
        this.initial_mouse_x_position = initial_mouse_x_position;
        this.set = set;
    }

    public Verse_resize_info(boolean set) {
        this.set = set;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public Resizing_mode getResizing_mode() {
        return resizing_mode;
    }
}
