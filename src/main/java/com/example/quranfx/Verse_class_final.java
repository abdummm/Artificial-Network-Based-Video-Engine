package com.example.quranfx;

import javafx.scene.image.Image;

public class Verse_class_final {

    private String verse;
    private String arabic_verse;
    private Integer verse_number;

    private Image base_64_image;

    private long time_in_milliseconds = 0;
    private Image editied_base_64_image;

    private Ayat_settings ayatSettings;

    public Verse_class_final(String verse, Integer verse_number, Image base_64_image, long time_in_milliseconds, Ayat_settings ayatSettings,String arabic_verse) {
        this.verse = verse;
        this.verse_number = verse_number;
        this.base_64_image = base_64_image;
        this.time_in_milliseconds = time_in_milliseconds;
        this.editied_base_64_image = base_64_image;
        this.ayatSettings = ayatSettings;
        this.arabic_verse = arabic_verse;
    }

    public String getVerse() {
        return verse;
    }

    public Integer getVerse_number() {
        return verse_number;
    }

    public Image getBase_64_image() {
        return base_64_image;
    }

    public long getTime_in_milliseconds() {
        return time_in_milliseconds;
    }

    public void setTime_in_milliseconds(long time_in_milliseconds) {
        this.time_in_milliseconds = time_in_milliseconds;
    }

    public void setBase_64_image(Image base_64_image) {
        this.base_64_image = base_64_image;
    }


    public Image getEditied_base_64_image() {
        return editied_base_64_image;
    }

    public void setEditied_base_64_image(Image editied_base_64_image) {
        this.editied_base_64_image = editied_base_64_image;
    }

    public Ayat_settings getAyatSettings() {
        return ayatSettings;
    }

    public String getArabic_verse() {
        return arabic_verse;
    }
}
