package com.example.quranfx;

public class Smallest_text_box_info {
    private Text_box_info text_box_info;
    private Language_info language_info;
    private boolean set = false;

    public Smallest_text_box_info() {
        this.set = false;
    }

    public Smallest_text_box_info(Text_box_info text_box_info, Language_info language_info) {
        this.text_box_info = text_box_info;
        this.language_info = language_info;
        this.set = true;
    }

    public Text_box_info getText_box_info() {
        return text_box_info;
    }

    public Language_info getLanguage_info() {
        return language_info;
    }

    public boolean isSet() {
        return set;
    }
}
