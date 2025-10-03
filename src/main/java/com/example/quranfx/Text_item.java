package com.example.quranfx;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Text_item {
    private String verse_text;
    private String adjusted_verse_text;
    private Point2D point2D;
    private double font_size;
    private Font font;
    private Color color;
    private long start_time;
    private long end_time;
    private double width;
    private double height;
    private Text_on_canvas_mode text_on_canvas_mode;
    private Stroke_text strokeText;
    private double left_margin;
    private double right_margin;
    private static final double video_height = 1920;
    private static final double video_width = 1080;

    public Text_item(String verse_text,String adjusted_verse_text, Point2D point2D, double font_size, Font font, Color color, long start_time, long end_time, double width, double height, Text_on_canvas_mode text_on_canvas_mode, Stroke_text strokeText, double left_margin, double right_margin) {
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
    }

    public Text_item(String verse_text, long start_time, long end_time) {
        this.verse_text = verse_text;
        this.adjusted_verse_text = verse_text;
        this.point2D = new Point2D(video_width / 2D, video_height / 2D);
        this.font_size = 36;
        this.font = return_default_font(font_size);
        this.color = Color.WHITE;
        this.start_time = start_time;
        this.end_time = end_time;
        double[] width_and_height_of_text = get_width_and_height_of_string(verse_text, this.font);
        this.width = width_and_height_of_text[0];
        this.height = width_and_height_of_text[1];
        text_on_canvas_mode = Text_on_canvas_mode.CENTER;
        this.strokeText = new Stroke_text();
        this.left_margin = 0;
        this.right_margin = 0;
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
        if(Font.getFamilies().contains(font_name)){
            Sub_fonts sub_fonts = new Sub_fonts(Font.getFontNames(font_name), font_name);
            ArrayList<Font_name_and_displayed_name> sorted_fonts = sub_fonts.getFont_names();
            int font_position = sub_fonts.getRegular_position();
            return new Font(sorted_fonts.get(font_position).getFont_name(), font_size);
        } else {
            return return_first_font(font_size);
        }
    }

    public String getVerse_text() {
        return verse_text;
    }

    public Point2D getPoint2D() {
        return point2D;
    }

    public void setPoint2D(Point2D point2D) {
        this.point2D = point2D;
    }

    private double[] get_width_and_height_of_string(String verse_text, Font font) {
        Text textNode = new Text(verse_text);
        textNode.setFont(font);
        return new double[]{textNode.getLayoutBounds().getWidth(), textNode.getLayoutBounds().getHeight()};
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

    public double get_x_position() {
        return point2D.getX();
    }

    public void set_x_position(double x_position) {
        this.point2D = new Point2D(x_position, this.point2D.getY());
    }

    public double get_y_position() {
        return point2D.getY();
    }

    public void set_y_position(double y_position) {
        this.point2D = new Point2D(this.point2D.getX(), y_position);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Stroke_text getStrokeText() {
        return strokeText;
    }

    public void setStrokeText(Stroke_text strokeText) {
        this.strokeText = strokeText;
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
}
