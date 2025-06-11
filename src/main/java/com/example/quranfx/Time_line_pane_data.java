package com.example.quranfx;

import javafx.scene.shape.Polygon;

public class Time_line_pane_data {
    private double pixels_in_between_each_line;
    private long time_between_every_line;
    private Polygon polygon;
    private double mouse_drag_y_area = 30D;
    private double polygon_x_position;
    private double polygon_width;
    private double time_line_base_line;

    public Time_line_pane_data() {

    }

    public double getPixels_in_between_each_line() {
        return pixels_in_between_each_line;
    }

    public void setPixels_in_between_each_line(double pixels_in_between_each_line) {
        this.pixels_in_between_each_line = pixels_in_between_each_line;
    }

    public long getTime_between_every_line() {
        return time_between_every_line;
    }

    public void setTime_between_every_line(long time_between_every_line) {
        this.time_between_every_line = time_between_every_line;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public double getMouse_drag_y_area() {
        return mouse_drag_y_area;
    }

    public void setMouse_drag_y_area(double mouse_drag_y_area) {
        this.mouse_drag_y_area = mouse_drag_y_area;
    }

    public double getPolygon_x_position() {
        return polygon_x_position;
    }

    public void setPolygon_x_position(double polygon_x_position) {
        this.polygon_x_position = polygon_x_position;
    }

    public double getPolygon_width() {
        return polygon_width;
    }

    public void setPolygon_width(double polygon_width) {
        this.polygon_width = polygon_width;
    }

    public double getTime_line_base_line() {
        return time_line_base_line;
    }

    public void setTime_line_base_line(double time_line_base_line) {
        this.time_line_base_line = time_line_base_line;
    }
}
