package com.example.quranfx;

public class Rectangle_changed_info {
    private double original_x;
    private MovementType type_of_movement;
    private double original_start_rectangle;
    private double original_end_rectangle;
    private double relative_x;
    private double[][] sorted_array;

    public Rectangle_changed_info(double original_x, MovementType type_of_movement,double original_start_rectangle,double original_end_rectangle,double relative_x,double[][] sorted_array) {
        this.original_x = original_x;
        this.type_of_movement = type_of_movement;
        this.original_start_rectangle = original_start_rectangle;
        this.original_end_rectangle = original_end_rectangle;
        this.relative_x = relative_x;
        this.sorted_array = sorted_array;
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
}
