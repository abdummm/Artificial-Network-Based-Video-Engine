package com.example.quranfx;

import atlantafx.base.controls.RingProgressIndicator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

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
    public JFXButton next_photo_chat_gpt_result;

    @FXML
    public JFXButton previous_photo_chat_gpt_result;

    @FXML
    public Label what_verse_is_this;

    @FXML
    public Button play_sound;

    @FXML
    public JFXButton full_screen_button_fourth_screen;

    @FXML
    public CheckBox generate_chat_gpt_images;

    @FXML
    public JFXButton choose_sound_third_screen;

    @FXML
    public Button create_video_final;

    @FXML
    public StackPane stack_pane_of_image_view_and_text;

    @FXML
    public JFXButton cancel_video;

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
    public StackPane top_pane_fourth_screen;

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


}