package com.example.quranfx;

import javafx.geometry.Point2D;

public class Text_box_info {
    Point2D center_position;
    private double text_box_width;
    private double text_box_height;

    public Text_box_info(Point2D center_position, double text_box_width, double text_box_height) {
        this.center_position = center_position;
        this.text_box_width = text_box_width;
        this.text_box_height = text_box_height;
    }

    public double getText_box_width() {
        return text_box_width;
    }

    public void setText_box_width(double text_box_width) {
        this.text_box_width = text_box_width;
    }

    public double getText_box_height() {
        return text_box_height;
    }

    public void setText_box_height(double text_box_height) {
        this.text_box_height = text_box_height;
    }

    public Point2D getCenter_position() {
        return center_position;
    }

    public void setCenter_position(Point2D center_position) {
        this.center_position = center_position;
    }
}
