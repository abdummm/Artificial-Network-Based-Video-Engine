package com.example.quranfx;

import javafx.scene.shape.Shape;

public class Shape_object_time_line {
    private double start;
    private double end;
    private Shape shape;
    private String image_id;
    private long start_time;
    private long end_time;
    private double opacity;
    private double fade_in;
    private double fade_out;

    public Shape_object_time_line(double start, double end, Shape shape, String image_id, long start_time, long end_time, double opacity, double fade_in, double fade_out) {
        this.start = start;
        this.end = end;
        this.shape = shape;
        this.image_id = image_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.opacity = opacity;
        this.fade_in = fade_in;
        this.fade_out = fade_out;
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

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public double getFade_in() {
        return fade_in;
    }

    public void setFade_in(double fade_in) {
        this.fade_in = fade_in;
    }

    public double getFade_out() {
        return fade_out;
    }

    public void setFade_out(double fade_out) {
        this.fade_out = fade_out;
    }

    @Override
    public String toString() {
        return "Shape_object_time_line{" +
                "start=" + start +
                ", end=" + end +
                ", shape=" + shape +
                ", image_id='" + image_id + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                '}';
    }
}
