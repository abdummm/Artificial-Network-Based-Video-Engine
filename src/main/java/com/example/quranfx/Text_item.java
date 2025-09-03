package com.example.quranfx;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Text_item {
    private String verse_text;
    private Point2D point2D;
    private double font_size;
    private Font font;
    private Color color;
    private long start_time;
    private long end_time;
    private double width;
    private double height;
    private Text_on_canvas_mode text_on_canvas_mode;
    private static final double video_height = 1920;
    private static final double video_width = 1080;

    public Text_item(String verse_text, Point2D point2D, double font_size, Font font, Color color, long start_time, long end_time, double width, double height, Text_on_canvas_mode text_on_canvas_mode) {
        this.verse_text = verse_text;
        this.point2D = point2D;
        this.font_size = font_size;
        this.font = font;
        this.color = color;
        this.start_time = start_time;
        this.end_time = end_time;
        this.width = width;
        this.height = height;
        this.text_on_canvas_mode = text_on_canvas_mode;
    }

    public Text_item(String verse_text, long start_time, long end_time) {
        this.verse_text = verse_text;
        this.point2D = new Point2D(video_width/2D, video_height/2D);
        this.font_size = 36;
        this.font = new Font(this.font_size);
        this.color = Color.WHITE;
        this.start_time = start_time;
        this.end_time = end_time;
        double[] width_and_height_of_text = get_width_and_height_of_string(verse_text,this.font);
        this.width = width_and_height_of_text[0];
        this.height = width_and_height_of_text[1];
        text_on_canvas_mode = Text_on_canvas_mode.CENTER;
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

    public double get_x_position(){
        return point2D.getX();
    }

    public void set_x_position(double x_position){
        this.point2D = new Point2D(x_position,this.point2D.getY());
    }

    public double get_y_position(){
        return point2D.getY();
    }

    public void set_y_position(double y_position){
        this.point2D = new Point2D(this.point2D.getX(),y_position);
    }
}
