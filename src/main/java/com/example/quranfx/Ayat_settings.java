package com.example.quranfx;

public class Ayat_settings {

    private String english_font_name = "Arial";

    private double english_font_size = 10.0D;

    private String english_color_hex = "#FFFFFF";

    private String alignment = "Center";

    private double english_top_margin = 0.0D;

    private int brightness_of_image = 100;

    private String arabic_font_name = "Arial";

    private double arabic_font_size = 10.0D;

    private String arabic_color_hex = "#FFFFFF";

    private String arabic_alignment = "Center";

    private double arabic_top_margin = 0.0D;

    private String surat_font_name = "Arial";

    private double surat_font_size = 10.0D;

    private String surat_color_hex = "#FFFFFF";

    private String surat_alignment = "Center";

    private double surat_top_margin = 0.0D;

    public Ayat_settings() {

    }

    public void set_ayat_settings(Ayat_settings ayatSettings) {
        this.english_font_name = ayatSettings.english_font_name;
        this.english_font_size = ayatSettings.english_font_size;
        this.english_color_hex = ayatSettings.english_color_hex;
        this.alignment = ayatSettings.alignment;
        this.english_top_margin = ayatSettings.english_top_margin;
        this.brightness_of_image = ayatSettings.brightness_of_image;
        this.arabic_font_name = ayatSettings.arabic_font_name;
        this.arabic_font_size = ayatSettings.arabic_font_size;
        this.arabic_color_hex = ayatSettings.arabic_color_hex;
        this.arabic_alignment = ayatSettings.arabic_alignment;
        this.arabic_top_margin = ayatSettings.arabic_top_margin;
        this.surat_font_name = ayatSettings.surat_font_name;
        this.surat_font_size = ayatSettings.surat_font_size;
        this.surat_color_hex = ayatSettings.surat_color_hex;
        this.surat_alignment = ayatSettings.surat_alignment;
        this.surat_top_margin = ayatSettings.surat_top_margin;
    }

    public String getEnglish_font_name() {
        return english_font_name;
    }

    public void setEnglish_font_name(String english_font_name) {
        this.english_font_name = english_font_name;
    }

    public double getEnglish_font_size() {
        return english_font_size;
    }

    public void setEnglish_font_size(double english_font_size) {
        this.english_font_size = english_font_size;
    }

    public String getEnglish_color_hex() {
        return english_color_hex;
    }

    public void setEnglish_color_hex(String english_color_hex) {
        this.english_color_hex = english_color_hex;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public double getEnglish_top_margin() {
        return english_top_margin;
    }

    public void setEnglish_top_margin(double english_top_margin) {
        this.english_top_margin = english_top_margin;
    }

    public int getBrightness_of_image() {
        return brightness_of_image;
    }

    public void setBrightness_of_image(int brightness_of_image) {
        this.brightness_of_image = brightness_of_image;
    }

    public String getArabic_font_name() {
        return arabic_font_name;
    }

    public void setArabic_font_name(String arabic_font_name) {
        this.arabic_font_name = arabic_font_name;
    }

    public double getArabic_font_size() {
        return arabic_font_size;
    }

    public void setArabic_font_size(double arabic_font_size) {
        this.arabic_font_size = arabic_font_size;
    }

    public String getArabic_color_hex() {
        return arabic_color_hex;
    }

    public void setArabic_color_hex(String arabic_color_hex) {
        this.arabic_color_hex = arabic_color_hex;
    }

    public String getArabic_alignment() {
        return arabic_alignment;
    }

    public void setArabic_alignment(String arabic_alignment) {
        this.arabic_alignment = arabic_alignment;
    }

    public double getArabic_top_margin() {
        return arabic_top_margin;
    }

    public void setArabic_top_margin(double arabic_top_margin) {
        this.arabic_top_margin = arabic_top_margin;
    }

    public String getSurat_font_name() {
        return surat_font_name;
    }

    public void setSurat_font_name(String surat_font_name) {
        this.surat_font_name = surat_font_name;
    }

    public double getSurat_font_size() {
        return surat_font_size;
    }

    public void setSurat_font_size(double surat_font_size) {
        this.surat_font_size = surat_font_size;
    }

    public String getSurat_color_hex() {
        return surat_color_hex;
    }

    public void setSurat_color_hex(String surat_color_hex) {
        this.surat_color_hex = surat_color_hex;
    }

    public String getSurat_alignment() {
        return surat_alignment;
    }

    public void setSurat_alignment(String surat_alignment) {
        this.surat_alignment = surat_alignment;
    }

    public double getSurat_top_margin() {
        return surat_top_margin;
    }

    public void setSurat_top_margin(double surat_top_margin) {
        this.surat_top_margin = surat_top_margin;
    }
}
