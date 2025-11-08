package com.example.quranfx;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Text_sizing {
    private static Text_sizing text_sizing = null;

    private Text_sizing(){}

    public static Text_sizing getInstance(){
        if(text_sizing == null){
            text_sizing = new Text_sizing();
        }
        return text_sizing;
    }

    public String do_i_need_to_resize_the_verse_text(String verse_text, Font font, double allowed_width, double left_margin, double right_margin) {
        final double EPS = 1e-6; // floating point inconsistent.
        allowed_width = allowed_width - 2 * left_margin - 2 * right_margin;
        String[] split_verse_into_words = verse_text.split(" ");
        StringBuilder string_builder_for_final_string = new StringBuilder();
        StringBuilder current_line = new StringBuilder();
        for (String current_word : split_verse_into_words) {
            Text current_line_text_item = new Text(current_line.toString());
            Text current_word_text_item = new Text(current_word);
            current_line_text_item.setFont(font);
            current_word_text_item.setFont(font);
            double current_line_width = current_line_text_item.getLayoutBounds().getWidth();
            double current_word_width = current_word_text_item.getLayoutBounds().getWidth();
            if (current_line.isEmpty()) {
                if (current_word_width >= allowed_width + EPS) {
                    string_builder_for_final_string.append(current_word);
                    string_builder_for_final_string.append("\n");
                } else {
                    current_line.append(current_word).append(" ");
                }
            } else {
                if (current_line_width + current_word_width > allowed_width + EPS) {
                    current_line.deleteCharAt(current_line.length() - 1);
                    string_builder_for_final_string.append(current_line);
                    string_builder_for_final_string.append("\n");
                    current_line = new StringBuilder(current_word).append(" ");
                } else {
                    current_line.append(current_word).append(" ");
                }
            }
        }
        if (!current_line.isEmpty()) {
            string_builder_for_final_string.append(current_line.deleteCharAt(current_line.length()-1));
        }
        if(!string_builder_for_final_string.isEmpty() && string_builder_for_final_string.charAt(string_builder_for_final_string.length()-1) == '\n'){
            string_builder_for_final_string.deleteCharAt(string_builder_for_final_string.length()-1);
        }
        return string_builder_for_final_string.toString();
    }

    public double[] get_width_and_height_of_string(String adjusted_verse_text, Font font) {
        Text text = new Text(adjusted_verse_text);
        text.setFont(font);
        double width = text.getLayoutBounds().getWidth();
        double height = text.getLayoutBounds().getHeight();
        return new double[]{width, height};
    }
}
