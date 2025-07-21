package com.example.quranfx;

import com.sun.source.tree.Tree;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.TreeMap;

public class Time_line_pane_data {
    private double pixels_in_between_each_line;
    private long time_between_every_line;
    private Polygon polygon;
    private double mouse_drag_y_area = 30D;
    private double polygon_width;
    private double time_line_base_line;
    private double time_line_end_base_line;
    private HashMap<String, Shape_object_time_line> hashMap_containing_all_of_the_items;

    public Time_line_pane_data() {
        hashMap_containing_all_of_the_items = new HashMap<>();
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

    public double getTime_line_end_base_line() {
        return time_line_end_base_line;
    }

    public void setTime_line_end_base_line(double time_line_end_base_line) {
        this.time_line_end_base_line = time_line_end_base_line;
    }

    public HashMap<String, Shape_object_time_line> getHashMap_containing_all_of_the_items() {
        return hashMap_containing_all_of_the_items;
    }
}
