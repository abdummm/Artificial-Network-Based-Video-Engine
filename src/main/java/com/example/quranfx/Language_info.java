package com.example.quranfx;

import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;


import java.util.ArrayList;

public class Language_info {
    private String language_name;
    private String displayed_language_name;
    private boolean visible_check_mark_checked;
    private ArrayList<Text_item> arrayList_of_all_of_the_translations;
    private boolean item_extended;
    private Canvas language_canvas;
    private ChangeListener<String> x_position_change_listener;
    private ChangeListener<String> y_position_change_listener;
    private ChangeListener<? super Color> color_change_listener;
    private ChangeListener<String> font_size_change_listener;
    private ChangeListener<String> font_change_listener;
    private ChangeListener<Sub_font_name_and_style> sub_font_change_listener;
    private ChangeListener<? super Color> stroke_color_change_listener;
    private ChangeListener<? super Number> stroke_weight_change_listener;
    private ChangeListener<? super Boolean> advanced_options_change_listener;
    /*private ChangeListener<String> left_margin_text_change_listener;
    private ChangeListener<String> right_margin_text_change_listener;*/
    private ChangeListener<? super Number> shadow_weight_change_listener;
    private ChangeListener<? super Color> shadow_color_change_listener;
    private ChangeListener<? super Number> verse_fade_in_listener;
    private ChangeListener<? super Number> verse_fade_out_listener;
    private ChangeListener<? super String> verse_text_area_text_change_listener;
    private boolean advanced_options_selected;
    private boolean text_box_showing;
    private Translation_UI_fields translation_ui_fields;


    public Language_info(String language_name, ArrayList<Text_item> arrayList_of_all_of_the_translations) {
        this.language_name = language_name;
        this.visible_check_mark_checked = false;
        this.arrayList_of_all_of_the_translations = arrayList_of_all_of_the_translations;
        this.displayed_language_name = edit_displayed_language_name(language_name);
        this.item_extended = false;
        this.language_canvas = null;
        this.advanced_options_selected = false;
        this.text_box_showing = false;
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

    public ChangeListener<String> getX_position_change_listener() {
        return x_position_change_listener;
    }

    public void setX_position_change_listener(ChangeListener<String> x_position_change_listener) {
        this.x_position_change_listener = x_position_change_listener;
    }

    public ChangeListener<String> getFont_size_change_listener() {
        return font_size_change_listener;
    }

    public void setFont_size_change_listener(ChangeListener<String> font_size_change_listener) {
        this.font_size_change_listener = font_size_change_listener;
    }

    public ChangeListener<? super Color> getColor_change_listener() {
        return color_change_listener;
    }

    public void setColor_change_listener(ChangeListener<? super Color> color_change_listener) {
        this.color_change_listener = color_change_listener;
    }

    public ChangeListener<String> getY_position_change_listener() {
        return y_position_change_listener;
    }

    public void setY_position_change_listener(ChangeListener<String> y_position_change_listener) {
        this.y_position_change_listener = y_position_change_listener;
    }

    @Override
    public String toString() {
        return "Language_info{" +
                "language_name='" + language_name + '\'' +
                ", displayed_language_name='" + displayed_language_name + '\'' +
                ", visible_check_mark_checked=" + visible_check_mark_checked +
                ", arrayList_of_all_of_the_translations=" + arrayList_of_all_of_the_translations +
                ", item_extended=" + item_extended +
                ", language_canvas=" + language_canvas +
                ", x_position_change_listener=" + x_position_change_listener +
                ", y_position_change_listener=" + y_position_change_listener +
                ", color_change_listener=" + color_change_listener +
                ", font_size_change_listener=" + font_size_change_listener +
                '}';
    }

    public ChangeListener<String> getFont_change_listener() {
        return font_change_listener;
    }

    public void setFont_change_listener(ChangeListener<String> font_change_listener) {
        this.font_change_listener = font_change_listener;
    }

    public ChangeListener<Sub_font_name_and_style> getSub_font_change_listener() {
        return sub_font_change_listener;
    }

    public void setSub_font_change_listener(ChangeListener<Sub_font_name_and_style> sub_font_change_listener) {
        this.sub_font_change_listener = sub_font_change_listener;
    }

    public ChangeListener<? super Color> getStroke_color_change_listener() {
        return stroke_color_change_listener;
    }

    public void setStroke_color_change_listener(ChangeListener<? super Color> stroke_color_change_listener) {
        this.stroke_color_change_listener = stroke_color_change_listener;
    }

    public ChangeListener<? super Number> getStroke_weight_change_listener() {
        return stroke_weight_change_listener;
    }

    public void setStroke_weight_change_listener(ChangeListener<? super Number> stroke_weight_change_listener) {
        this.stroke_weight_change_listener = stroke_weight_change_listener;
    }

    public ChangeListener<? super Boolean> getAdvanced_options_change_listener() {
        return advanced_options_change_listener;
    }

    public void setAdvanced_options_change_listener(ChangeListener<? super Boolean> advanced_options_change_listener) {
        this.advanced_options_change_listener = advanced_options_change_listener;
    }

    /*public ChangeListener<String> getLeft_margin_text_change_listener() {
        return left_margin_text_change_listener;
    }

    public void setLeft_margin_text_change_listener(ChangeListener<String> left_margin_text_change_listener) {
        this.left_margin_text_change_listener = left_margin_text_change_listener;
    }

    public ChangeListener<String> getRight_margin_text_change_listener() {
        return right_margin_text_change_listener;
    }

    public void setRight_margin_text_change_listener(ChangeListener<String> right_margin_text_change_listener) {
        this.right_margin_text_change_listener = right_margin_text_change_listener;
    }*/

    public boolean isAdvanced_options_selected() {
        return advanced_options_selected;
    }

    public void setAdvanced_options_selected(boolean advanced_options_selected) {
        this.advanced_options_selected = advanced_options_selected;
    }

    public ChangeListener<? super Number> getShadow_weight_change_listener() {
        return shadow_weight_change_listener;
    }

    public void setShadow_weight_change_listener(ChangeListener<? super Number> shadow_weight_change_listener) {
        this.shadow_weight_change_listener = shadow_weight_change_listener;
    }

    public ChangeListener<? super Color> getShadow_color_change_listener() {
        return shadow_color_change_listener;
    }

    public void setShadow_color_change_listener(ChangeListener<? super Color> shadow_color_change_listener) {
        this.shadow_color_change_listener = shadow_color_change_listener;
    }

    public ChangeListener<? super Number> getVerse_fade_in_listener() {
        return verse_fade_in_listener;
    }

    public void setVerse_fade_in_listener(ChangeListener<? super Number> verse_fade_in_listener) {
        this.verse_fade_in_listener = verse_fade_in_listener;
    }

    public ChangeListener<? super Number> getVerse_fade_out_listener() {
        return verse_fade_out_listener;
    }

    public void setVerse_fade_out_listener(ChangeListener<? super Number> verse_fade_out_listener) {
        this.verse_fade_out_listener = verse_fade_out_listener;
    }

    public ChangeListener<? super String> getVerse_text_area_text_change_listener() {
        return verse_text_area_text_change_listener;
    }

    public void setVerse_text_area_text_change_listener(ChangeListener<? super String> verse_text_area_text_change_listener) {
        this.verse_text_area_text_change_listener = verse_text_area_text_change_listener;
    }

    public boolean isText_box_showing() {
        return text_box_showing;
    }

    public void setText_box_showing(boolean text_box_showing) {
        this.text_box_showing = text_box_showing;
    }

    public void setTranslation_ui_fields(Translation_UI_fields translation_ui_fields) {
        this.translation_ui_fields = translation_ui_fields;
    }

    public Translation_UI_fields getTranslation_ui_fields() {
        return translation_ui_fields;
    }
}
