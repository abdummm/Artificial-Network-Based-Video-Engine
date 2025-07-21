package com.example.quranfx;

import javafx.scene.shape.Shape;

public class Shape_object_time_line {
    private double start;
    private double end;
    private Shape shape;
    private String image_id;

    public Shape_object_time_line(double start, double end, Shape shape,String image_id) {
        this.start = start;
        this.end = end;
        this.shape = shape;
        this.image_id = image_id;
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

    public String getImage_id() {
        return image_id;
    }
}
