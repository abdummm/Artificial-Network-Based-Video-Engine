package com.example.quranfx;

public class Verse_class {
    private String surat;
    private String verse;

    private String arabic_verse;
    private Integer verse_number;


    public Verse_class(String surat, String verse, Integer verse_number,String arabic_verse) {
        this.verse = verse;
        this.verse_number = verse_number;
        this.arabic_verse = arabic_verse;
    }

    public String getVerse() {
        return verse;
    }

    public Integer getVerse_number() {
        return verse_number;
    }


    public String getArabic_verse() {
        return arabic_verse;
    }
}
