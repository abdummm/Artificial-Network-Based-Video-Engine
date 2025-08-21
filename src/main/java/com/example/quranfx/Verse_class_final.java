package com.example.quranfx;

import java.util.ArrayList;
import java.util.HashMap;

public class Verse_class_final {
    private Integer verse_number;
    private long duration;
    private long start_millisecond;

    public Verse_class_final(long duration) {
        this.duration = duration;
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
}
