package com.example.quranfx;

public class Verse_class_final {

    private String verse;
    private String arabic_verse;
    private Integer verse_number;
    private Ayat_settings ayatSettings;
    private double duration;
    private double start_millisecond;

    public Verse_class_final(String verse, Integer verse_number, String arabic_verse,double start_millisecond,double duration) {
        this.verse = verse;
        this.verse_number = verse_number;
        this.ayatSettings = new Ayat_settings();
        this.arabic_verse = arabic_verse;
        this.start_millisecond = start_millisecond;
        this.duration = duration;
    }

    public String getVerse() {
        return verse;
    }

    public String getArabic_verse() {
        return arabic_verse;
    }

    public Integer getVerse_number() {
        return verse_number;
    }

    public Ayat_settings getAyatSettings() {
        return ayatSettings;
    }

    public double getDuration() {
        return duration;
    }

    public double getStart_millisecond() {
        return start_millisecond;
    }
}
