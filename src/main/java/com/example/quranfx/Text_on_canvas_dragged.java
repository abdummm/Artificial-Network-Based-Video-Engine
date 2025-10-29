package com.example.quranfx;

import javafx.geometry.Point2D;

public class Text_on_canvas_dragged {
    private Point2D original_point2D_of_mouse_event;
    private Point2D original_point2D_of_text;
    private Language_info language_info;
    private Type_of_cursor type_of_cursor;
    private Text_item text_item;
    private double original_height;
    private double original_width;
    private double canvas_height;
    private double canvas_width;
    private double height_difference;
    private double width_difference;

    public Text_on_canvas_dragged(Point2D original_point2D_of_mouse_event, Point2D original_point2D_of_text, Language_info language_info, Type_of_cursor type_of_cursor, Text_item text_item, double original_height, double original_width,double canvas_height,double canvas_width) {
        this.original_point2D_of_mouse_event = original_point2D_of_mouse_event;
        this.original_point2D_of_text = original_point2D_of_text;
        this.language_info = language_info;
        this.type_of_cursor = type_of_cursor;
        this.text_item = text_item;
        this.original_height = original_height;
        this.original_width = original_width;
        this.canvas_height = canvas_height;
        this.canvas_width = canvas_width;
    }

    public Point2D getOriginal_point2D_of_mouse_event() {
        return original_point2D_of_mouse_event;
    }

    public Language_info getLanguage_info() {
        return language_info;
    }

    public Point2D getOriginal_point2D_of_text() {
        return original_point2D_of_text;
    }

    public Type_of_cursor getType_of_cursor() {
        return type_of_cursor;
    }

    public Text_item getText_item() {
        return text_item;
    }

    public double getOriginal_height() {
        return original_height;
    }

    public double getOriginal_width() {
        return original_width;
    }

    public double getCanvas_height() {
        return canvas_height;
    }

    public double getCanvas_width() {
        return canvas_width;
    }

    public double getHeight_difference() {
        return height_difference;
    }

    public void setHeight_difference(double height_difference) {
        this.height_difference = height_difference;
    }

    public double getWidth_difference() {
        return width_difference;
    }

    public void setWidth_difference(double width_difference) {
        this.width_difference = width_difference;
    }
}
