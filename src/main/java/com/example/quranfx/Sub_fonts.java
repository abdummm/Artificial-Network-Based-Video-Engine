package com.example.quranfx;

import java.util.*;

public class Sub_fonts {
    private ArrayList<Font_name_and_displayed_name> font_names = new ArrayList<>();
    private int regular_position = 0;

    public Sub_fonts(List<String> font_names_string, String family_name) {
        make_the_array_list_of_strings_fonts_and_names(font_names_string, font_names, family_name);
        sort_the_font_names(font_names);
    }

    private void sort_the_font_names(ArrayList<Font_name_and_displayed_name> font_names) {
        font_names.sort(new Comparator<Font_name_and_displayed_name>() {
            @Override
            public int compare(Font_name_and_displayed_name o1, Font_name_and_displayed_name o2) {
                return o1.getDisplayed_name().compareTo(o2.getDisplayed_name());
            }
        });
    }

    private String make_the_first_letter_capital(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private void make_the_array_list_of_strings_fonts_and_names(List<String> font_names_string, ArrayList<Font_name_and_displayed_name> font_name_and_displayed_names, String family_name) {
        for (int i = 0; i < font_names_string.size(); i++) {
            String displayed_name = font_names_string.get(i).replace(family_name, "").trim();
            if (displayed_name.isEmpty()) {
                displayed_name = "Regular";
            }
            displayed_name = make_the_first_letter_capital(displayed_name);
            if (displayed_name.equals("Regular")) {
                regular_position = i;
            }
            Font_name_and_displayed_name font_name_and_displayed_name = new Font_name_and_displayed_name(font_names_string.get(i), displayed_name);
            font_name_and_displayed_names.add(font_name_and_displayed_name);
        }
    }

    public int getRegular_position() {
        return regular_position;
    }

    public ArrayList<Font_name_and_displayed_name> getFont_names() {
        return font_names;
    }
}
