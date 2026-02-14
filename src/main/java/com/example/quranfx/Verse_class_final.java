package com.example.quranfx;

import javafx.scene.layout.StackPane;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Verse_class_final {
    private Integer verse_number;
    private long duration;
    private long start_millisecond;
    private StackPane stack_pane_hosting_rectangle;

    public Verse_class_final() {
    }

    public Verse_class_final(long duration) {
        this.duration = duration;
    }

    public Verse_class_final(Integer verse_number, long duration, long start_millisecond) {
        this.verse_number = verse_number;
        this.duration = duration;
        this.start_millisecond = start_millisecond;
    }

    public Integer getVerse_number() {
        return verse_number;
    }

    public void setVerse_number(Integer verse_number) {
        this.verse_number = verse_number;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getStart_millisecond() {
        return start_millisecond;
    }

    public void setStart_millisecond(long start_millisecond) {
        this.start_millisecond = start_millisecond;
    }

    public StackPane getStack_pane_hosting_rectangle() {
        return stack_pane_hosting_rectangle;
    }

    public void setStack_pane_hosting_rectangle(StackPane stack_pane_hosting_rectangle) {
        this.stack_pane_hosting_rectangle = stack_pane_hosting_rectangle;
    }
}
