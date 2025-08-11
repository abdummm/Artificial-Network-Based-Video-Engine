package com.example.quranfx;

public class Language_text {
    private String language_name;
    private String verse;

    public Language_text(String language_name, String verse) {
        this.language_name = language_name;
        this.verse = verse;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public String getVerse() {
        return verse;
    }
}
