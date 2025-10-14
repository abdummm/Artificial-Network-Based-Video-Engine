package com.example.quranfx;

import javafx.geometry.Point2D;

public class Text_on_canvas_dragged {
    Point2D original_point2D_of_mouse_event;
    Point2D original_point2D_of_text;
    Language_info language_info;
    private boolean data_set = false;

    public Point2D getOriginal_point2D_of_mouse_event() {
        return original_point2D_of_mouse_event;
    }

    public void setOriginal_point2D_of_mouse_event(Point2D original_point2D_of_mouse_event) {
        this.original_point2D_of_mouse_event = original_point2D_of_mouse_event;
    }

    public Language_info getLanguage_info() {
        return language_info;
    }

    public void setLanguage_info(Language_info language_info) {
        this.language_info = language_info;
    }

    public boolean isData_set() {
        return data_set;
    }

    public void setData_set(boolean data_set) {
        this.data_set = data_set;
    }

    public Point2D getOriginal_point2D_of_text() {
        return original_point2D_of_text;
    }

    public void setOriginal_point2D_of_text(Point2D original_point2D_of_text) {
        this.original_point2D_of_text = original_point2D_of_text;
    }
}
