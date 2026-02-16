package com.example.quranfx;

import javafx.scene.paint.Color;

public class Text_accessory_info {
    private Accessory_type accessory_type;
    private boolean is_the_accessory_on;
    private Color accessory_color;
    private double accessory_weight;
    private double max_accessory_weight;

    public Text_accessory_info(Accessory_type accessory_type, double accessory_weight,double max_accessory_weight) {
        this.accessory_type = accessory_type;
        this.is_the_accessory_on = false;
        this.accessory_color = Color.BLACK;
        this.accessory_weight = accessory_weight;
        this.max_accessory_weight = max_accessory_weight;
    }

    public Text_accessory_info(Accessory_type accessory_type, boolean is_the_accessory_on, Color accessory_color, double accessory_weight, double max_accessory_weight) {
        this.accessory_type = accessory_type;
        this.is_the_accessory_on = is_the_accessory_on;
        this.accessory_color = accessory_color;
        this.accessory_weight = accessory_weight;
        this.max_accessory_weight = max_accessory_weight;
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

    public double getMax_accessory_weight() {
        return max_accessory_weight;
    }

    public void setMax_accessory_weight(double max_accessory_weight) {
        this.max_accessory_weight = max_accessory_weight;
    }

    public Accessory_type getAccessory_type() {
        return accessory_type;
    }
}
