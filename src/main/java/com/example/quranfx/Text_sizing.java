package com.example.quranfx;

import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.TextLine;

import java.util.Arrays;

public class Text_sizing {
    private static Text_sizing text_sizing = null;
    private static final String text_splitter = "[ \\n]+";

    private Text_sizing() {
    }

    public static Text_sizing getInstance() {
        if (text_sizing == null) {
            text_sizing = new Text_sizing();
        }
        return text_sizing;
    }

    public String do_i_need_to_resize_the_verse_text(String verse_text, io.github.humbleui.skija.Font font, double allowed_width, double left_margin, double right_margin) {
        final double EPS = 1e-6; // floating point inconsistent.
        allowed_width = allowed_width - (2 * left_margin) - (2 * right_margin);
        String[] split_verse_into_words = verse_text.split(" ");
        StringBuilder string_builder_for_final_string = new StringBuilder();
        StringBuilder current_line = new StringBuilder();
        for (String current_word : split_verse_into_words) {
            if (current_word.isEmpty()) {
                continue;
            }
            double current_line_width = TextLine.make(current_line.toString(), font).getWidth();
            double current_word_width = TextLine.make(current_word, font).getWidth();
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
            string_builder_for_final_string.append(current_line.deleteCharAt(current_line.length() - 1));
        }
        if (!string_builder_for_final_string.isEmpty() && string_builder_for_final_string.charAt(string_builder_for_final_string.length() - 1) == '\n') {
            string_builder_for_final_string.deleteCharAt(string_builder_for_final_string.length() - 1);
        }
        return string_builder_for_final_string.toString();
    }

    public double[] get_width_and_height_of_string(String adjusted_verse_text, io.github.humbleui.skija.Font font) {
        String[] lines = adjusted_verse_text.split("\n");
        float max_width = 0;
        float total_height = 0;
        float last_height = 0;
        for (String line : lines) {
            TextLine textLine = TextLine.make(line, font);
            max_width = Math.max(max_width, textLine.getWidth());
            total_height += Math.abs(textLine.getAscent()) + textLine.getDescent() + textLine.getLeading();
            last_height = textLine.getLeading();
        }
        total_height -= last_height;
        return new double[]{max_width, total_height + Global_default_values.height_text_margin};
    }

    public double return_the_min_width(String adjusted_verse_text, Font font) {
        String[] words = adjusted_verse_text.split(text_splitter);
        double min_width = 0;
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            TextLine textLine = TextLine.make(word, font);
            float width = textLine.getWidth();
            min_width = Math.max(min_width, width);
        }
        return min_width;
    }
}
