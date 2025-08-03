package com.example.quranfx;

import javafx.scene.shape.Shape;

public class Shape_object_time_line {
    private double start;
    private double end;
    private Shape shape;
    private String image_id;
    private long start_time;
    private long end_time;

    public Shape_object_time_line(double start, double end, Shape shape,String image_id,long start_time,long end_time) {
        this.start = start;
        this.end = end;
        this.shape = shape;
        this.image_id = image_id;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Shape_object_time_line(double start) {
        this.start = start;
        this.end = 0;
        this.shape = null;
        this.image_id = "";
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

    public long getStart_time() {
        return start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }
}
