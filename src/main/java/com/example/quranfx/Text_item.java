package com.example.quranfx;

import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Typeface;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Text_item {
    private static final float default_font_size = 36;
    private String original_verse_text;
    private String verse_text;
    private String adjusted_verse_text;
    private io.github.humbleui.skija.Font font;
    private double font_size = default_font_size;
    private Color color;
    private long start_time;
    private long end_time;
    private Text_on_canvas_mode text_on_canvas_mode;
    private Text_accessory_info stroke_info;
    private Text_accessory_info shadow_info;
    private double left_margin;
    private double right_margin;
    private Text_box_info text_box_info;
    private double video_height = 1920;
    private double video_width = 1080;
    private double extra_width_padding = 25;
    private double extra_height_padding = 20;
    private double fade_in;
    private double fade_out;

    /*public Text_item(String verse_text,String adjusted_verse_text, Point2D point2D, double font_size, Font font, Color color, long start_time, long end_time, double width, double height, Text_on_canvas_mode text_on_canvas_mode, Stroke_text strokeText, double left_margin, double right_margin) {
        this.verse_text = verse_text;
        this.adjusted_verse_text = adjusted_verse_text;
        this.point2D = point2D;
        this.font_size = font_size;
        this.font = font;
        this.color = color;
        this.start_time = start_time;
        this.end_time = end_time;
        this.width = width;
        this.height = height;
        this.text_on_canvas_mode = text_on_canvas_mode;
        this.strokeText = strokeText;
        this.left_margin = left_margin;
        this.right_margin = right_margin;
        this.text_box_info = new Text_box_info(new Point2D(0,0),0,0);
    }*/

    public Text_item(String verse_text, long start_time, long end_time) {
        this.original_verse_text = verse_text;
        this.verse_text = verse_text;
        this.font = return_default_font(font_size);
        this.adjusted_verse_text = Text_sizing.getInstance().do_i_need_to_resize_the_verse_text(verse_text, font, video_width - extra_width_padding, left_margin, right_margin);
        this.color = Color.WHITE;
        this.start_time = start_time;
        this.end_time = end_time;
        text_on_canvas_mode = Text_on_canvas_mode.CENTER;
        this.stroke_info = new Text_accessory_info(Accessory_type.STROKE,Global_default_values.stroke_weight,Global_default_values.max_stroke_weight);
        this.shadow_info = new Text_accessory_info(Accessory_type.SHADOW,Global_default_values.shadow_weight,Global_default_values.max_shadow_weight);
        this.left_margin = 0;
        this.right_margin = 0;
        this.fade_in = 0;
        this.fade_out = 0;
        this.text_box_info = new Text_box_info(this, new Point2D(video_width / 2D, video_height / 2D), adjusted_verse_text, font, true);
    }

    private io.github.humbleui.skija.Font return_default_font(double font_size) {
        Typeface type_face = FontMgr.getDefault().matchFamilyStyle("Arial", FontStyle.NORMAL);
        return new Font(type_face, (float) font_size);
    }

    public String getVerse_text() {
        return verse_text;
    }

    public double getFont_size() {
        return font_size;
    }

    public void setFont_size(double font_size) {
        this.font_size = font_size;
        font.setSize((float) font_size);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Text_on_canvas_mode getText_on_canvas_mode() {
        return text_on_canvas_mode;
    }

    public void setText_on_canvas_mode(Text_on_canvas_mode text_on_canvas_mode) { // will not be used now
        this.text_on_canvas_mode = text_on_canvas_mode;
    }

    public io.github.humbleui.skija.Font getFont() {
        return font;
    }

    public void setFont(String font,Sub_font_name_and_style sub_font_name_and_style) {
        Typeface type_face = FontMgr.getDefault().matchFamilyStyle(font, sub_font_name_and_style.getFont_style());
        this.font = new Font(type_face,(float) font_size);
    }

    public Text_accessory_info getStroke_info() {
        return stroke_info;
    }

    public void setStroke_info(Text_accessory_info textaccessory_info) {
        this.stroke_info = textaccessory_info;
    }

    public String getAdjusted_verse_text() {
        return adjusted_verse_text;
    }

    public void setAdjusted_verse_text(String adjusted_verse_text) {
        this.adjusted_verse_text = adjusted_verse_text;
    }

    public double getRight_margin() {
        return right_margin;
    }

    public void setRight_margin(double right_margin) {
        this.right_margin = right_margin;
    }

    public double getLeft_margin() {
        return left_margin;
    }

    public void setLeft_margin(double left_margin) {
        this.left_margin = left_margin;
    }

    public Text_box_info getText_box_info() {
        return text_box_info;
    }

    public void setText_box_info(Text_box_info text_box_info) {
        this.text_box_info = text_box_info;
    }

    public double getExtra_width_padding() {
        return extra_width_padding;
    }

    public void setExtra_width_padding(double extra_width_padding) {
        this.extra_width_padding = extra_width_padding;
    }

    public double getExtra_height_padding() {
        return extra_height_padding;
    }

    public void setExtra_height_padding(double extra_height_padding) {
        this.extra_height_padding = extra_height_padding;
    }

    public Text_accessory_info getShadow_info() {
        return shadow_info;
    }

    public void setShadow_info(Text_accessory_info shadow_info) {
        this.shadow_info = shadow_info;
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

    public void setVerse_text(String verse_text) {
        this.verse_text = verse_text;
    }

    public double getVideo_height() {
        return video_height;
    }

    public double getVideo_width() {
        return video_width;
    }

    public String getOriginal_verse_text() {
        return original_verse_text;
    }


}
