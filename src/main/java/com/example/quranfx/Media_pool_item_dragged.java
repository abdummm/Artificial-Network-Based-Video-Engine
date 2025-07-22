package com.example.quranfx;

import javafx.scene.image.ImageView;

public class Media_pool_item_dragged {
    private ImageView imageView;
    private double x_pos;
    private double y_pos;
    private boolean has_this_been_dragged;
    private long image_key_uuid;
    private Shape_object_time_line shapeObjectTimeLine;
    private double[][] sorted_array;

    public Media_pool_item_dragged(ImageView imageView, double x_pos, double y_pos,long image_key_uuid, Shape_object_time_line shapeObjectTimeLine,double[][] sorted_array) {
        this.imageView = imageView;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.has_this_been_dragged = false;
        this.image_key_uuid = image_key_uuid;
        this.shapeObjectTimeLine = shapeObjectTimeLine;
        this.sorted_array = sorted_array;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public double getX_pos() {
        return x_pos;
    }

    public void setX_pos(double x_pos) {
        this.x_pos = x_pos;
    }

    public double getY_pos() {
        return y_pos;
    }

    public void setY_pos(double y_pos) {
        this.y_pos = y_pos;
    }

    public boolean isHas_this_been_dragged() {
        return has_this_been_dragged;
    }

    public void setHas_this_been_dragged(boolean has_this_been_dragged) {
        this.has_this_been_dragged = has_this_been_dragged;
    }

    public long getImage_key_uuid() {
        return image_key_uuid;
    }

    public Shape_object_time_line getShapeObjectTimeLine() {
        return shapeObjectTimeLine;
    }

    public double[][] getSorted_array() {
        return sorted_array;
    }
}
