package com.example.quranfx;

import javafx.geometry.Point2D;

public class Text_box_info {
    Point2D center_position;
    private double text_box_width;
    private double text_box_height;
    private boolean visible;
    private double min_x_point;
    private double max_x_point;
    private double min_y_point;
    private double max_y_point;
    private boolean set = false;
    private double min_width;
    private double min_height;

    /*public Text_box_info(double text_box_width,double text_box_height) {
        this.center_position = new Point2D(0, 0);
        this.text_box_width = text_box_width;
        this.text_box_height = text_box_height;
        this.visible = false;
        this.min_x_point = 0;
        this.max_x_point = 0;
        this.min_y_point = 0;
        this.max_y_point = 0;
    }*/

    public Text_box_info(Point2D center_position, double text_box_width, double text_box_height, boolean visible) {
        this.center_position = new Point2D(center_position.getX(), center_position.getY());
        this.text_box_width = text_box_width;
        this.text_box_height = text_box_height;
        this.min_width = text_box_width;
        this.min_height = text_box_height;
        this.visible = visible;
        this.min_x_point = center_position.getX() - this.text_box_width / 2;
        this.max_x_point = center_position.getX() + this.text_box_width / 2;
        this.min_y_point = center_position.getY() - this.text_box_height / 2;
        this.max_y_point = center_position.getY() + this.text_box_height / 2;
        set = true;
    }

    public double getText_box_width() {
        return text_box_width;
    }

    public void setText_box_width(double text_box_width) {
        this.text_box_width = text_box_width;
        update_the_x_points();
    }

    public double getText_box_height() {
        return text_box_height;
    }

    public void setText_box_height(double text_box_height) {
        this.text_box_height = text_box_height;
        update_the_y_points();
    }

    public Point2D getCenter_position() {
        return center_position;
    }

    public void setCenter_position(Point2D center_position) {
        this.center_position = center_position;
        update_the_x_points();
        update_the_y_points();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private void update_the_x_points() {
        min_x_point = center_position.getX() - text_box_width / 2;
        max_x_point = center_position.getX() + text_box_width / 2;
    }

    private void update_the_y_points() {
        min_y_point = center_position.getY() - text_box_height / 2;
        max_y_point = center_position.getY() + text_box_height / 2;
    }

    public double getMin_x_point() {
        return min_x_point;
    }

    public double getMax_x_point() {
        return max_x_point;
    }

    public double getMin_y_point() {
        return min_y_point;
    }

    public double getMax_y_point() {
        return max_y_point;
    }

    public double get_area() {
        return (max_y_point - min_y_point) * (max_x_point - min_x_point);
    }

    public boolean isSet() {
        return set;
    }

    public void set_x_position(double x_position){
        center_position = new Point2D(x_position,center_position.getY());
    }

    public void set_y_position(double y_position){
        center_position = new Point2D(center_position.getX(),y_position);
    }

    public double getMin_width() {
        return min_width;
    }

    public double getMin_height() {
        return min_height;
    }
}
