package com.example.quranfx;

public class Polygon_data {
    private double real_polygon_position;
    private double polygon_width;

    public Polygon_data(double real_polygon_position, double polygon_width) {
        this.real_polygon_position = real_polygon_position;
        this.polygon_width = polygon_width;
    }

    public double getReal_polygon_position() {
        return real_polygon_position;
    }

    public double getPolygon_width() {
        return polygon_width;
    }

    public void setReal_polygon_position(double real_polygon_position) {
        this.real_polygon_position = real_polygon_position;
    }
}
