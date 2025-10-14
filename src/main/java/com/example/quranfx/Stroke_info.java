package com.example.quranfx;

import javafx.scene.paint.Color;

public class Stroke_info {
    private boolean is_the_stroke_on;
    private Color stroke_color;
    private double stroke_weight;

    public Stroke_info() {
        this.is_the_stroke_on = false;
        this.stroke_color = Color.BLACK;
        this.stroke_weight = Global_default_values.stroke_weight;
    }

    public boolean isIs_the_stroke_on() {
        return is_the_stroke_on;
    }

    public void setIs_the_stroke_on(boolean is_the_stroke_on) {
        this.is_the_stroke_on = is_the_stroke_on;
    }

    public Color getStroke_color() {
        return stroke_color;
    }

    public void setStroke_color(Color stroke_color) {
        this.stroke_color = stroke_color;
    }

    public double getStroke_weight() {
        return stroke_weight;
    }

    public void setStroke_weight(double stroke_weight) {
        this.stroke_weight = stroke_weight;
    }
}
