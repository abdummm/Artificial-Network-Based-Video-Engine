package com.example.quranfx;

import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Typeface;

public class Sub_font_name_and_style {
    private String font_name;
    private FontStyle font_style;

    public Sub_font_name_and_style(String font_name, FontStyle font_style) {
        this.font_name = font_name;
        this.font_style = font_style;
    }

    public String getFont_name() {
        return font_name;
    }

    public FontStyle getFont_style() {
        return font_style;
    }
}
