package com.example.quranfx;

import javafx.geometry.Point2D;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Text_box_info {
    Point2D center_position;
    private double text_box_width;
    private double text_box_height;
    private boolean visible;
    private double min_x_point;
    private double max_x_point;
    private double min_y_point;
    private double max_y_point;
    private boolean set = false;
    private double min_width;
    private double min_height;
    private Text_item text_item;

    public Text_box_info(Text_item text_item, Point2D center_position, String adjusted_verse, Font font, boolean visible) {
        this.center_position = new Point2D(center_position.getX(), center_position.getY());
        double[] width_and_height = Text_sizing.getInstance().get_width_and_height_of_string(adjusted_verse, font);
        //this.text_box_width = Math.min(width_and_height[0] + extra_width_padding, 1080);
        this.text_box_width = 1080;
        this.text_box_height = width_and_height[1] + text_item.getExtra_height_padding();
        this.min_width = Text_sizing.getInstance().return_the_min_width(adjusted_verse, font) + text_item.getExtra_width_padding();
        this.min_height = width_and_height[1] + text_item.getExtra_height_padding();
        this.visible = visible;
        this.min_x_point = center_position.getX() - this.text_box_width / 2;
        this.max_x_point = center_position.getX() + this.text_box_width / 2;
        this.min_y_point = center_position.getY() - this.text_box_height / 2;
        this.max_y_point = center_position.getY() + this.text_box_height / 2;
        this.set = true;
        this.text_item = text_item;
    }

    public double getText_box_width() {
        return text_box_width;
    }

    public void setText_box_width(double text_box_width) {
        this.text_box_width = text_box_width;
        update_the_x_points();
    }

    public double getText_box_height() {
        return text_box_height;
    }

    public void setText_box_height(double text_box_height) {
        this.text_box_height = text_box_height;
        update_the_y_points();
    }

    public Point2D getCenter_position() {
        return center_position;
    }

    public void setCenter_position(Point2D center_position) {
        this.center_position = center_position;
        update_the_x_points();
        update_the_y_points();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private void update_the_x_points() {
        min_x_point = center_position.getX() - text_box_width / 2;
        max_x_point = center_position.getX() + text_box_width / 2;
    }

    private void update_the_y_points() {
        min_y_point = center_position.getY() - text_box_height / 2;
        max_y_point = center_position.getY() + text_box_height / 2;
    }

    public double getMin_x_point() {
        return min_x_point;
    }

    public double getMax_x_point() {
        return max_x_point;
    }

    public double getMin_y_point() {
        return min_y_point;
    }

    public double getMax_y_point() {
        return max_y_point;
    }

    public double get_area() {
        return (max_y_point - min_y_point) * (max_x_point - min_x_point);
    }

    public boolean isSet() {
        return set;
    }

    public void set_x_position(double x_position) {
        center_position = new Point2D(x_position, center_position.getY());
    }

    public void set_y_position(double y_position) {
        center_position = new Point2D(center_position.getX(), y_position);
    }

    public double getMin_width() {
        return min_width;
    }

    public double getMin_height() {
        return min_height;
    }

    public void setMin_width(double min_width) {
        this.min_width = min_width;
    }

    public void setMin_height(double min_height) {
        this.min_height = min_height;
    }

    public void update_the_min_height_and_width() {
        min_width = Text_sizing.getInstance().return_the_min_width(text_item.getAdjusted_verse_text(), text_item.getFont()) + text_item.getExtra_width_padding();
        text_box_width = Math.max(min_width, text_box_width);
        text_box_width = Math.min(text_box_width,text_item.getVideo_width());
        String new_adjusted_verse = Text_sizing.getInstance().do_i_need_to_resize_the_verse_text(text_item.getVerse_text(), text_item.getFont(), text_box_width - text_item.getExtra_width_padding(), text_item.getLeft_margin(), text_item.getRight_margin());
        text_item.setAdjusted_verse_text(new_adjusted_verse);
        min_height = Text_sizing.getInstance().get_width_and_height_of_string(text_item.getAdjusted_verse_text(), text_item.getFont())[1] + text_item.getExtra_height_padding();
        text_box_height = /*Math.max(min_height, text_box_height);*/ min_height;
        update_the_x_points();
        update_the_y_points();
    }
}
