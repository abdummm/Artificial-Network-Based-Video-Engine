package com.example.quranfx;

public class Polygon_data {
    private double real_polygon_position;
    private double polygon_width;
    private boolean should_the_polygon_be_fixed_in_the_middle;

    public Polygon_data(double real_polygon_position, double polygon_width) {
        this.real_polygon_position = real_polygon_position;
        this.polygon_width = polygon_width;
        this.should_the_polygon_be_fixed_in_the_middle = false;
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

    public boolean isShould_the_polygon_be_fixed_in_the_middle() {
        return should_the_polygon_be_fixed_in_the_middle;
    }

    public void setShould_the_polygon_be_fixed_in_the_middle(boolean should_the_polygon_be_fixed_in_the_middle) {
        this.should_the_polygon_be_fixed_in_the_middle = should_the_polygon_be_fixed_in_the_middle;
    }
}
