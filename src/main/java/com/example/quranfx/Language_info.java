package com.example.quranfx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.util.ArrayList;

public class Language_info {
    private String language_name;
    private String displayed_language_name;
    private boolean visible_check_mark_checked;
    private ArrayList<Text_item> arrayList_of_all_of_the_translations;
    private boolean item_extended;
    private Canvas language_canvas;

    public Language_info(String language_name, ArrayList<Text_item> arrayList_of_all_of_the_translations) {
        this.language_name = language_name;
        this.visible_check_mark_checked = false;
        this.arrayList_of_all_of_the_translations = arrayList_of_all_of_the_translations;
        this.displayed_language_name = edit_displayed_language_name(language_name);
        this.item_extended = false;
        this.language_canvas = null;
    }

    private String edit_displayed_language_name(String language_name) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] split_on_a_comma = language_name.split(",");
        for (int i = 0; i < split_on_a_comma.length; i++) {
            split_on_a_comma[i] = split_on_a_comma[i].trim();
            stringBuilder.append(return_the_first_letter_capital(split_on_a_comma[i]));
            stringBuilder.append(", ");
        }
        if (!stringBuilder.isEmpty()) {
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        return stringBuilder.toString();
    }

    private String return_the_first_letter_capital(String string) {
        if (string.isEmpty()) {
            return string;
        }
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    /*private void edit_the_verses_before_adding_them(ArrayList<String> arrayList_of_all_of_the_translations) {
        for (int i = 0; i < arrayList_of_all_of_the_translations.size(); i++) {
            Document doc = Jsoup.parse(arrayList_of_all_of_the_translations.get(i));
            doc.select("script, style, noscript, sup, sub").remove(); // drop entire elements incl. text
            String cleaned_html = doc.text().trim();
            cleaned_html = cleaned_html.replaceAll("::\\{\\d+}", "");
            cleaned_html = cleaned_html.replace("\"", "");
            arrayList_of_all_of_the_translations.set(i, cleaned_html);
        }
    }*/

    public String getLanguage_name() {
        return language_name;
    }

    public void setArrayList_of_all_of_the_translations(ArrayList<Text_item> arrayList_of_all_of_the_translations) {
        this.arrayList_of_all_of_the_translations = arrayList_of_all_of_the_translations;
    }

    public String getDisplayed_language_name() {
        return displayed_language_name;
    }

    public boolean isItem_extended() {
        return item_extended;
    }

    public void setItem_extended(boolean item_extended) {
        this.item_extended = item_extended;
    }

    public boolean isVisible_check_mark_checked() {
        return visible_check_mark_checked;
    }

    public void setVisible_check_mark_checked(boolean visible_check_mark_checked) {
        this.visible_check_mark_checked = visible_check_mark_checked;
    }

    public ArrayList<Text_item> getArrayList_of_all_of_the_translations() {
        return arrayList_of_all_of_the_translations;
    }

    public Canvas getLanguage_canvas() {
        return language_canvas;
    }

    public void setLanguage_canvas(Canvas language_canvas) {
        this.language_canvas = language_canvas;
    }


}
