package com.example.quranfx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HelloController {

    @FXML
    public ListView<String> choose_the_surat = new ListView<>();

    @FXML
    public StackPane choose_surat_screen;

    @FXML
    public StackPane choose_ayat_screen;

    @FXML
    public Label show_surat_name;

    @FXML
    public Label show_surat_name_arabic;

    @FXML
    public Label number_of_ayats;

    @FXML
    public Label show_information_about_surat;

    @FXML
    public Button previous_page_second_screen;

    @FXML
    public Button next_page_second_screen;

    @FXML
    public TextField enter_the_ayats_wanted;

    @FXML
    public StackPane generating_screen;

    @FXML
    public Label generating_text;

    @FXML
    public Label how_many_verses_are_left_text;

    @FXML
    public CheckBox show_advanced_settings_second_screen;

    @FXML
    public VBox advanced_setting_v_box;

    @FXML
    public Spinner which_chatgpt_to_use;

    @FXML
    public Spinner size_of_image;

    @FXML
    public Spinner style_of_image;

    @FXML
    public Spinner quality_of_image;

    @FXML
    public TextArea enter_the_prompt;

    @FXML
    public BorderPane show_the_result_screen;

    @FXML
    public ImageView chatgpt_image_view;

    @FXML
    public Button next_photo_chat_gpt_result;

    @FXML
    public Button previous_photo_chat_gpt_result;

    @FXML
    public Label what_verse_is_this;

    @FXML
    public ListView<Verse_class_final> list_view_with_the_verses_preview;

    @FXML
    public Label showing_the_engligh_translation_at_the_right;

    @FXML
    public Button play_sound;

    @FXML
    public Slider sound_slider_fourth_screen;

    @FXML
    public Label duration_of_media;

    @FXML
    public Button full_screen_button_fourth_screen;

    @FXML
    public Button settings_of_video_button_fourth_screen;

    @FXML
    public TextField end_time_of_each_image;

    @FXML
    public Button copy_duration_fourth_screen;

    @FXML
    public CheckBox generate_chat_gpt_images;

    @FXML
    public CheckBox add_arabic_text_fourth_screen;

    @FXML
    public CheckBox add_surat_name_in_video;

    @FXML
    public Button upload_image_button_for_each_ayat;

    @FXML
    public Button choose_sound_third_screen;

    @FXML
    public Button create_video_final;

    @FXML
    public CheckBox enable_english_text;

    @FXML
    public Label verse_input_field;

    @FXML
    public ToggleButton position_of_english_text_button_top_center;

    @FXML
    public ToggleButton position_of_english_text_button_center;

    @FXML
    public ToggleButton position_of_english_text_button_bottom_center;

    @FXML
    public VBox english_translation_settings;

    @FXML
    public Spinner font_size_text_field;

    @FXML
    public TextField english_text_color_in_ayat;

    @FXML
    public Spinner top_margin_english_text;

    @FXML
    public StackPane stack_pane_of_image_view_and_text;

    @FXML
    public ComboBox spinner_to_choose_font;

    @FXML
    public Button apply_to_all_english_translation;

    @FXML
    public Button apply_changes_to_current_ayat;

    @FXML
    public Spinner choose_brightness_of_an_image;

    @FXML
    public ComboBox spinner_to_choose_font_arabic;

    @FXML
    public Spinner font_size_text_field_arabic;

    @FXML
    public TextField text_color_in_ayat_arabic;

    @FXML
    public ToggleButton position_of_english_text_button_top_center_arabic;

    @FXML
    public ToggleButton position_of_english_text_button_center_arabic;

    @FXML
    public ToggleButton position_of_english_text_button_bottom_center_arabic;

    @FXML
    public Spinner top_margin_text_arabic;

    @FXML
    public VBox arabic_translation_settings;

    @FXML
    public VBox surat_name_settings;

    @FXML
    public ComboBox spinner_to_choose_font_arabic_surat;

    @FXML
    public Spinner font_size_text_field_arabic_surat;

    @FXML
    public TextField text_color_in_ayat_arabic_surat;

    @FXML
    public ToggleButton position_of_english_text_button_top_center_arabic_surat;

    @FXML
    public ToggleButton position_of_english_text_button_center_arabic_surat;

    @FXML
    public ToggleButton position_of_english_text_button_bottom_center_arabic_surat;

    @FXML
    public Spinner top_margin_text_arabic_surat;

    @FXML
    public Button cancel_video;

    @FXML
    public ListView<Reciters_info> list_view_with_the_recitors;
}