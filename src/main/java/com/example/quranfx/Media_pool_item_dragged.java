package com.example.quranfx;

import javafx.scene.image.ImageView;

public class Media_pool_item_dragged {
    public ImageView imageView;
    public double x_pos;
    public double y_pos;
    public boolean has_this_been_dragged;

    public Media_pool_item_dragged(ImageView imageView, double x_pos, double y_pos) {
        this.imageView = imageView;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.has_this_been_dragged = false;
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
}
