package com.example.quranfx;

public class Reciters_info {

    private int id;
    private String name;
    private String displayed_name;
    private String link_for_32_bits;
    private String link_for_64_bits;
    private String link_for_128_bits;

    public Reciters_info(int id, String name, String link_for_32_bits, String link_for_64_bits, String link_for_128_bits) {
        this.id = id;
        this.name = name;
        this.displayed_name = name;
        this.link_for_32_bits = link_for_32_bits;
        this.link_for_64_bits = link_for_64_bits;
        this.link_for_128_bits = link_for_128_bits;
    }

    public Reciters_info(int id, String name,String displayed_name, String link_for_32_bits, String link_for_64_bits, String link_for_128_bits) {
        this.id = id;
        this.name = name;
        this.displayed_name = displayed_name;
        this.link_for_32_bits = link_for_32_bits;
        this.link_for_64_bits = link_for_64_bits;
        this.link_for_128_bits = link_for_128_bits;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink_for_32_bits() {
        return link_for_32_bits;
    }

    public String getLink_for_64_bits() {
        return link_for_64_bits;
    }

    public String getLink_for_128_bits() {
        return link_for_128_bits;
    }

    public String getDisplayed_name() {
        return displayed_name;
    }
}
