package com.example.quranfx;

import javafx.scene.paint.Color;

public class Text_accessory_info {
    private boolean is_the_accessory_on;
    private Color accessory_color;
    private double accessory_weight;

    public Text_accessory_info() {
        this.is_the_accessory_on = false;
        this.accessory_color = Color.BLACK;
        this.accessory_weight = Global_default_values.stroke_weight;
    }

    public boolean isIs_the_accessory_on() {
        return is_the_accessory_on;
    }

    public void setIs_the_accessory_on(boolean is_the_accessory_on) {
        this.is_the_accessory_on = is_the_accessory_on;
    }

    public Color getAccessory_color() {
        return accessory_color;
    }

    public void setAccessory_color(Color accessory_color) {
        this.accessory_color = accessory_color;
    }

    public double getAccessory_weight() {
        return accessory_weight;
    }

    public void setAccessory_weight(double accessory_weight) {
        this.accessory_weight = accessory_weight;
    }
}
