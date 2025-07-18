package com.example.quranfx;

import javafx.scene.shape.Shape;

public class Shape_object_time_line {
    private double start;
    private double end;
    private Shape shape;

    public Shape_object_time_line(double start, double end, Shape shape) {
        this.start = start;
        this.end = end;
        this.shape = shape;
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    public Shape getShape() {
        return shape;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public void setEnd(double end) {
        this.end = end;
    }
}
