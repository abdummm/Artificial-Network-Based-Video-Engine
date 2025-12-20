package com.example.quranfx;

public class Verse_resize_info {
    private Resizing_mode resizing_mode;
    private double initial_mouse_x_position;
    private boolean set;
    private double verse_start_x;
    private double verse_end_x;
    private double verse_width;

    public Verse_resize_info(Resizing_mode resizing_mode, double initial_mouse_x_position,boolean set, double verse_start_x, double verse_end_x, double verse_width) {
        this.resizing_mode = resizing_mode;
        this.initial_mouse_x_position = initial_mouse_x_position;
        this.set = set;
        this.verse_start_x = verse_start_x;
        this.verse_end_x = verse_end_x;
        this.verse_width = verse_width;
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

    public double getInitial_mouse_x_position() {
        return initial_mouse_x_position;
    }

    public double getVerse_start_x() {
        return verse_start_x;
    }

    public double getVerse_end_x() {
        return verse_end_x;
    }

    public double getVerse_width() {
        return verse_width;
    }
}
