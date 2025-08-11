package com.example.quranfx;

import java.util.ArrayList;

public class Verse_class_final {
    private ArrayList<Language_text> arrayList_of_all_of_the_languages;
    private Integer verse_number;
    private Ayat_settings ayatSettings;
    private long duration;
    private long start_millisecond;

    public Verse_class_final(long duration) {
        this.duration = duration;
        this.ayatSettings = new Ayat_settings();
    }

    public ArrayList<Language_text> getArrayList_of_all_of_the_languages() {
        return arrayList_of_all_of_the_languages;
    }

    public void setArrayList_of_all_of_the_languages(ArrayList<Language_text> arrayList_of_all_of_the_languages) {
        this.arrayList_of_all_of_the_languages = arrayList_of_all_of_the_languages;
    }

    public Integer getVerse_number() {
        return verse_number;
    }

    public void setVerse_number(Integer verse_number) {
        this.verse_number = verse_number;
    }

    public Ayat_settings getAyatSettings() {
        return ayatSettings;
    }

    public void setAyatSettings(Ayat_settings ayatSettings) {
        this.ayatSettings = ayatSettings;
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
