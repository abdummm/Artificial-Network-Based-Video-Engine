package com.example.quranfx;

import javafx.scene.layout.StackPane;

public class Verse_resize_info {
    private Resizing_mode resizing_mode;
    private double initial_mouse_x_position;
    private double initial_scene_mouse_x_position;
    private boolean set;
    private double verse_start_x;
    private double verse_end_x;
    private double verse_width;
    private double previous_verse_start_x;
    private double previous_verse_end_x;
    private double previous_verse_width;
    private double next_verse_start_x;
    private double next_verse_end_x;
    private double next_verse_width;
    private Polygon_position polygon_position;
    private double initial_polygon_x_position;
    private long last_out_of_scene_update = 0;

    public Verse_resize_info(Resizing_mode resizing_mode, double initial_mouse_x_position,double initial_scene_mouse_x_position, boolean set, StackPane main_stack_pane, StackPane previous_stack_pane, StackPane next_stack_pane,Polygon_position polygon_position,double initial_polygon_x_position) {
        this.resizing_mode = resizing_mode;
        this.initial_mouse_x_position = initial_mouse_x_position;
        this.initial_scene_mouse_x_position = initial_scene_mouse_x_position;
        this.set = set;
        this.verse_start_x = main_stack_pane.getLayoutX();
        this.verse_end_x = main_stack_pane.getLayoutX() + main_stack_pane.getWidth();
        this.verse_width = main_stack_pane.getWidth();
        this.previous_verse_start_x = previous_stack_pane==null ? 0 : previous_stack_pane.getLayoutX();
        this.previous_verse_end_x = previous_stack_pane==null ? 0 : previous_stack_pane.getLayoutX() + previous_stack_pane.getWidth();
        this.previous_verse_width = previous_stack_pane==null ? 0 :previous_stack_pane.getWidth();
        this.next_verse_start_x = next_stack_pane==null ? 0 : next_stack_pane.getLayoutX();
        this.next_verse_end_x = next_stack_pane==null ? 0 : next_stack_pane.getLayoutX() + next_stack_pane.getWidth();
        this.next_verse_width = next_stack_pane==null ? 0 :next_stack_pane.getWidth();
        this.polygon_position = polygon_position;
        this.initial_polygon_x_position = initial_polygon_x_position;
        this.last_out_of_scene_update = 0;
    }

    public Verse_resize_info(boolean set) {
        this.set = set;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }

    public Resizing_mode getResizing_mode() {
        return resizing_mode;
    }

    public double getInitial_mouse_x_position() {
        return initial_mouse_x_position;
    }

    public double getVerse_start_x() {
        return verse_start_x;
    }

    public double getVerse_end_x() {
        return verse_end_x;
    }

    public double getVerse_width() {
        return verse_width;
    }

    public double getPrevious_verse_start_x() {
        return previous_verse_start_x;
    }

    public double getPrevious_verse_end_x() {
        return previous_verse_end_x;
    }

    public double getPrevious_verse_width() {
        return previous_verse_width;
    }

    public double getNext_verse_start_x() {
        return next_verse_start_x;
    }

    public double getNext_verse_end_x() {
        return next_verse_end_x;
    }

    public double getNext_verse_width() {
        return next_verse_width;
    }

    public double getInitial_scene_mouse_x_position() {
        return initial_scene_mouse_x_position;
    }

    public Polygon_position getPolygon_position() {
        return polygon_position;
    }

    public void setPolygon_position(Polygon_position polygon_position) {
        this.polygon_position = polygon_position;
    }

    public double getInitial_polygon_x_position() {
        return initial_polygon_x_position;
    }

    public long getLast_out_of_scene_update() {
        return last_out_of_scene_update;
    }

    public void setLast_out_of_scene_update(long last_out_of_scene_update) {
        this.last_out_of_scene_update = last_out_of_scene_update;
    }
}
