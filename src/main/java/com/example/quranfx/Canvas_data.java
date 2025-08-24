package com.example.quranfx;

import java.util.Objects;

public class Canvas_data {
    private String language_name_from_canvas_data;

    public Canvas_data(String language_name_from_canvas_data) {
        this.language_name_from_canvas_data = language_name_from_canvas_data;
    }

    public String getLanguage_name_from_canvas_data() {
        return language_name_from_canvas_data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Canvas_data that = (Canvas_data) o;
        return Objects.equals(language_name_from_canvas_data, that.language_name_from_canvas_data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(language_name_from_canvas_data);
    }
}
