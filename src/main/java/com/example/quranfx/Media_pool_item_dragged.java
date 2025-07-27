package com.example.quranfx;

import javafx.scene.image.ImageView;

import java.util.TreeSet;

public class Media_pool_item_dragged {
    private ImageView imageView;
    private double x_pos;
    private double y_pos;
    private boolean has_this_been_dragged;
    private String image_key_uuid;
    private Shape_object_time_line shapeObjectTimeLine;
    private TreeSet<Shape_object_time_line> tree_set_containing_all_of_the_items;
    private boolean did_this_change_the_image;

    public Media_pool_item_dragged(ImageView imageView, double x_pos, double y_pos,String image_key_uuid, Shape_object_time_line shapeObjectTimeLine, TreeSet<Shape_object_time_line> tree_set_containing_all_of_the_items) {
        this.imageView = imageView;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.has_this_been_dragged = false;
        this.image_key_uuid = image_key_uuid;
        this.shapeObjectTimeLine = shapeObjectTimeLine;
        this.did_this_change_the_image = false;
        this.tree_set_containing_all_of_the_items = tree_set_containing_all_of_the_items;
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

    public String getImage_key_uuid() {
        return image_key_uuid;
    }

    public Shape_object_time_line getShapeObjectTimeLine() {
        return shapeObjectTimeLine;
    }

    public boolean isDid_this_change_the_image() {
        return did_this_change_the_image;
    }

    public void setDid_this_change_the_image(boolean did_this_change_the_image) {
        this.did_this_change_the_image = did_this_change_the_image;
    }

    public TreeSet<Shape_object_time_line> getTree_set_containing_all_of_the_items() {
        return tree_set_containing_all_of_the_items;
    }
}
