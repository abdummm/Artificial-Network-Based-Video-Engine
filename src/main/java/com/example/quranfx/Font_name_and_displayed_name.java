package com.example.quranfx;

public class Font_name_and_displayed_name {
    private String font_name;
    private String displayed_name;

    public Font_name_and_displayed_name(String font_name, String displayed_name) {
        this.font_name = font_name;
        this.displayed_name = displayed_name;
    }

    public String getFont_name() {
        return font_name;
    }

    public String getDisplayed_name() {
        return displayed_name;
    }

    @Override
    public String toString() {
        return "Font_name_and_displayed_name{" +
                "font_name='" + font_name + '\'' +
                ", displayed_name='" + displayed_name + '\'' +
                '}';
    }
}
