package com.example.quranfx;

public class Rectangle_changed_info {
    private double original_x;
    private MovementType type_of_movement;

    public Rectangle_changed_info(double original_x, MovementType type_of_movement) {
        this.original_x = original_x;
        this.type_of_movement = type_of_movement;
    }

    public double getOriginal_x() {
        return original_x;
    }

    public MovementType getType_of_movement() {
        return type_of_movement;
    }
}
