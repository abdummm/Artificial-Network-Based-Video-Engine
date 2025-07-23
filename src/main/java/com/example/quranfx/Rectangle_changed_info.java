package com.example.quranfx;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Rectangle_changed_info {
    private double original_x;
    private MovementType type_of_movement;
    private double original_start_rectangle;
    private double original_end_rectangle;
    private double relative_x;
    private double[][] sorted_array;
    private Rectangle fake_rectangle;
    private String image_id;
    private boolean did_we_ever_change_the_photo;

    public Rectangle_changed_info(double original_x, MovementType type_of_movement,double original_start_rectangle,double original_end_rectangle,double relative_x,double[][] sorted_array,String image_id) {
        this.original_x = original_x;
        this.type_of_movement = type_of_movement;
        this.original_start_rectangle = original_start_rectangle;
        this.original_end_rectangle = original_end_rectangle;
        this.relative_x = relative_x;
        this.sorted_array = sorted_array;
        this.fake_rectangle = null;
        this.image_id = image_id;
        this.did_we_ever_change_the_photo = false;
    }


    public Rectangle_changed_info(double original_x, MovementType type_of_movement,double original_start_rectangle,double original_end_rectangle,double relative_x,double[][] sorted_array,Rectangle fake_rectangle,String image_id) {
        this.original_x = original_x;
        this.type_of_movement = type_of_movement;
        this.original_start_rectangle = original_start_rectangle;
        this.original_end_rectangle = original_end_rectangle;
        this.relative_x = relative_x;
        this.sorted_array = sorted_array;
        this.fake_rectangle = fake_rectangle;
        this.did_we_ever_change_the_photo = false;
        this.image_id = image_id;
    }

    public double getOriginal_x() {
        return original_x;
    }

    public MovementType getType_of_movement() {
        return type_of_movement;
    }

    public double getOriginal_start_rectangle() {
        return original_start_rectangle;
    }

    public double getOriginal_end_rectangle() {
        return original_end_rectangle;
    }

    public double getRelative_x() {
        return relative_x;
    }

    public double[][] getSorted_array() {
        return sorted_array;
    }

    public Rectangle getFake_rectangle() {
        return fake_rectangle;
    }

    public String getImage_id() {
        return image_id;
    }

    public boolean isDid_we_ever_change_the_photo() {
        return did_we_ever_change_the_photo;
    }

    public void setDid_we_ever_change_the_photo(boolean did_we_ever_change_the_photo) {
        this.did_we_ever_change_the_photo = did_we_ever_change_the_photo;
    }
}
