package com.example.quranfx;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Text_item {
    private String verse_text;
    private String adjusted_verse_text;
    //private Point2D point2D;
    private double font_size;
    private Font font;
    private Color color;
    private long start_time;
    private long end_time;
    private Text_on_canvas_mode text_on_canvas_mode;
    private Stroke_info stroke_info;
    private double left_margin;
    private double right_margin;
    private Text_box_info text_box_info;
    private static final double video_height = 1920;
    private static final double video_width = 1080;

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

    public Text_item(String verse_text, double text_box_width, long start_time, long end_time) {
        this.verse_text = verse_text;
        this.font_size = 36;
        this.font = return_default_font(font_size);
        this.adjusted_verse_text = do_i_need_to_resize_the_verse_text(verse_text,font,text_box_width,left_margin,right_margin);
        this.color = Color.WHITE;
        this.start_time = start_time;
        this.end_time = end_time;
        text_on_canvas_mode = Text_on_canvas_mode.CENTER;
        this.stroke_info = new Stroke_info();
        this.left_margin = 0;
        this.right_margin = 0;
        this.text_box_info = new Text_box_info(new Point2D(video_width / 2D, video_height / 2D), adjusted_verse_text, font, true);
    }

    private Font return_first_font(double font_size) {
        String first_family_in_fonts = Font.getFamilies().getFirst();
        Sub_fonts sub_fonts = new Sub_fonts(Font.getFontNames(first_family_in_fonts), first_family_in_fonts);
        ArrayList<Font_name_and_displayed_name> sorted_fonts = sub_fonts.getFont_names();
        int font_position = sub_fonts.getRegular_position();
        return new Font(sorted_fonts.get(font_position).getFont_name(), font_size);
    }

    private Font return_default_font(double font_size) {
        String font_name = "System";
        if (Font.getFamilies().contains(font_name)) {
            Sub_fonts sub_fonts = new Sub_fonts(Font.getFontNames(font_name), font_name);
            ArrayList<Font_name_and_displayed_name> sorted_fonts = sub_fonts.getFont_names();
            int font_position = sub_fonts.getRegular_position();
            return new Font(sorted_fonts.get(font_position).getFont_name(), font_size);
        } else {
            return return_first_font(font_size);
        }
    }

    private String do_i_need_to_resize_the_verse_text(String verse_text, Font font, double allowed_width, double left_margin, double right_margin) {
        allowed_width = allowed_width - 2 * left_margin - 2 * right_margin;
        String[] split_verse_into_words = verse_text.split(" ");
        StringBuilder string_builder_for_final_string = new StringBuilder();
        StringBuilder current_line = new StringBuilder();
        for (String current_word : split_verse_into_words) {
            Text current_line_text_item = new Text(current_line.toString());
            Text current_word_text_item = new Text(current_word.concat(" "));
            current_line_text_item.setFont(font);
            current_word_text_item.setFont(font);
            double current_line_width = current_line_text_item.getLayoutBounds().getWidth();
            double current_word_width = current_word_text_item.getLayoutBounds().getWidth();
            if (current_line.isEmpty()) {
                if (current_word_width >= allowed_width) {
                    string_builder_for_final_string.append(current_word);
                    string_builder_for_final_string.append("\n");
                } else {
                    current_line.append(current_word).append(" ");
                }
            } else {
                if (current_line_width + current_word_width >= allowed_width) {
                    current_line.deleteCharAt(current_line.length() - 1);
                    string_builder_for_final_string.append(current_line.toString());
                    string_builder_for_final_string.append("\n");
                    current_line = new StringBuilder(current_word).append(" ");
                } else {
                    current_line.append(current_word).append(" ");
                }
            }
        }
        if (!current_line.isEmpty()) {
            string_builder_for_final_string.append(current_line.deleteCharAt(current_line.length() - 1));
        }
        if (string_builder_for_final_string.charAt(string_builder_for_final_string.length() - 1) == '\n') {
            string_builder_for_final_string.deleteCharAt(string_builder_for_final_string.length() - 1);
        }
        return string_builder_for_final_string.toString();
    }

    public String getVerse_text() {
        return verse_text;
    }

    public double getFont_size() {
        return font_size;
    }

    public void setFont_size(double font_size) {
        this.font = new Font(font.getName(), font_size);
        this.font_size = font_size;
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

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Stroke_info getStroke_info() {
        return stroke_info;
    }

    public void setStroke_info(Stroke_info stroke_info) {
        this.stroke_info = stroke_info;
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
}
