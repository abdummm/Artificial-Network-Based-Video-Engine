package com.example.quranfx;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class HelloController {

    @FXML
    public ListView<String> choose_the_surat = new ListView<>();

    @FXML
    public BorderPane choose_surat_screen;

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
    public JFXButton previous_page_second_screen;

    @FXML
    public JFXButton next_page_second_screen;

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
    public Label what_verse_is_this;

    /*@FXML
    public JFXButton play_sound;*/

    @FXML
    public CheckBox generate_chat_gpt_images;

    @FXML
    public JFXButton choose_sound_third_screen;

    @FXML
    public Pane stack_pane_of_image_view_and_text;

    @FXML
    public ListView<Reciters_info> list_view_with_the_recitors;

    @FXML
    public JFXComboBox<Integer> combobox_to_choose_starting_ayat;

    @FXML
    public JFXComboBox<Integer> combobox_to_choose_last_ayat;

    @FXML
    public TilePane tile_pane_media_pool;

    @FXML
    public JFXButton add_media_button;

    @FXML
    public StackPane right_stack_pane_in_grid_pane;

    @FXML
    public StackPane left_stack_pane_in_grid_pane;

    @FXML
    public StackPane center_group_grid_pane;

    @FXML
    public StackPane bottom_stack_pane_fourth_screen;

    @FXML
    public Label upload_media_text;

    @FXML
    public MFXProgressSpinner progress_indicator_media_pool;

    @FXML
    public ScrollPane scroll_pane_hosting_tile_pane_media_pool;

    @FXML
    public StackPane stack_pane_hosting_the_scroll_pane_and_the_tile_pane;

    @FXML
    public Pane time_line_pane;

    @FXML
    public ScrollPane scroll_pane_hosting_the_time_line;

    @FXML
    public JFXButton give_feedback_button;

    @FXML
    public Pane pane_holding_the_fourth_screen;

    @FXML
    public StackPane parent_stack_pane;

    @FXML
    public StackPane show_the_result_screen_stack_pane;

    /*@FXML
    public ImageView blurry_chatgpt_image_view;*/

    @FXML
    public ImageView logo_at_the_start_of_the_app;

    @FXML
    public StackPane show_logo_loading_screen;

    @FXML
    public MFXProgressSpinner progress_indicator_first_loading_screen;

    @FXML
    public Canvas canvas_on_top_of_time_line_pane;

    @FXML
    public Pane pane_overlying_the_time_line_pane_for_polygon_indicator;

    @FXML
    public ListView<Language_info> list_view_with_all_of_the_languages;

    @FXML
    public JFXButton fast_rewind_button;

    @FXML
    public JFXButton rewind_button;

    @FXML
    public JFXButton play_pause_button;

    @FXML
    public JFXButton forward_button;

    @FXML
    public JFXButton fast_forward_button;

    @FXML
    public CheckBox check_box_saying_help_spread_the_app;

    @FXML
    public JFXButton question_mark_beside_help_spread_the_app;

    @FXML
    public Rectangle rectangle_on_top_of_chat_gpt_image_view_for_opacity_tint;

    @FXML
    public Slider slider_to_control_the_opacity_of_an_image;

    @FXML
    public Slider slider_to_control_fade_in_of_image;

    @FXML
    public Slider slider_to_control_fade_out_of_image;

    @FXML
    public StackPane image_controls_stack_pane;

    @FXML
    public Label label_holding_the_opacity_percentage;

    @FXML
    public Label label_holding_the_fade_in;

    @FXML
    public Label label_holding_the_fade_out;

    @FXML
    public Label fake_label_holding_the_opacity_percentage;

    @FXML
    public Label fake_label_holding_the_fade_in;

    @FXML
    public Label fake_label_holding_the_fade_out;

    @FXML
    public JFXButton render_video;


   /* @FXML
    public Rectangle black_rectangle_behind_image_view;*/


}