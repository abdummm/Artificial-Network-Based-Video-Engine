package com.example.quranfx;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Text_item {
    private String verse_text;
    private Point2D point2D;
    private Font font;
    private Color color;
    private double text_size;
    private long start_time;
    private long end_time;
    private double width;
    private double height;

    public Text_item(String verse_text) {
        this.verse_text = verse_text;
    }

    public String getVerse_text() {
        return verse_text;
    }
}
