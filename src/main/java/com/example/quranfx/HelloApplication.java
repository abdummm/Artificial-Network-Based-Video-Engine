package com.example.quranfx;

import atlantafx.base.theme.PrimerLight;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import okhttp3.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.imgscalr.Scalr;
import org.jsoup.*;

import javax.imageio.ImageIO;
import java.util.concurrent.CountDownLatch;


public class HelloApplication extends Application {
    private String chapters_string;

    private String basic_prompt = "Create a 9:16 image based on this. Do not portray god or any human or add any text.";

    private int number_of_prompts_per_minute = 5;
    private ArrayList<Long> array_list_with_times = new ArrayList<>();
    private ArrayList<Verse_class> array_list_with_verses = new ArrayList<>();
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ArrayList<Verse_class_final> chatgpt_responses = new ArrayList<>();

    private int initial_number_of_ayats = 0;
    private int chat_gpt_processed_ayats = 0;
    private int selected_verse = 0;
    private String surat_name_selected = "Al-Fatiha";
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean is_the_slider_being_held_right_now = false;
    private String sound_path = "";
    private Stage main_stage;

    private Long[] durations;
    private Long[] end_of_the_picture_durations;
    private int number_of_audio_channels = 2;
    private boolean did_this_play_already = false;
    private int audio_frequncy_of_the_sound = 44100;
    private double max_hight_of_top_pane_fourth_screen = 0;
    private double max_hight_of_bottom_pane_fourth_screen = 0;
    private ContextMenu empty_tile_pane_context_menu = null;

    private final static double play_pause_button_size = 20D;
    private final static int image_view_in_tile_pane_width = 90;
    private final static int image_view_in_tile_pane_height = 160;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth() / 2, Screen.getPrimary().getBounds().getHeight() / 2);
        //scene.getStylesheets().add(PrimerLight.class.getResource("primer-light.css").toExternalForm());
        stage.setTitle("السلام عليكم ورحمة الله وبركاته");
        stage.setScene(scene);
        stage.show();
        stage.toFront();
        stage.requestFocus();
        main_stage = stage;
        HelloController helloController = fxmlLoader.getController();
        call_chapters_api(helloController);
        listen_to_surat_choose(helloController);
        dalle_spinner_listener(helloController);
        ratio_spinner_listen(helloController);
        listen_to_previous_button_clicked(helloController);
        listen_to_next_button_clicked(helloController);
        watch_the_advanced_settings_box(helloController);
        set_up_the_which_chat_gpt_to_use(helloController);
        set_up_the_which_chat_gpt_to_use(helloController);
        set_the_width_of_which_gpt_spinner(helloController);
        set_up_the_which_image_spinner(helloController);
        set_the_width_of_image_size(helloController);
        set_up_the_style_spinner(helloController);
        set_the_width_of_the_style_spinner(helloController);
        set_up_the_quality_spinner(helloController);
        set_the_width_of_the_quality_spinner(helloController);
        set_the_height_of_text_prompt_text_area(helloController);
        set_the_height_of_text_prompt_text_area(helloController);
        next_photo_click_listen(helloController);
        previous_photo_click_listen(helloController);
        listen_to_list_view_item_select(helloController);
        listen_to_list_view_keyboard_select(helloController);
        listen_to_play(helloController);
        //set_the_width_of_play_pause_button(helloController);
        listen_to_slide_clicked(helloController);
        listen_to_full_screen_button(helloController);
        set_the_text_field_formatter_of_milliseconds_end(helloController);
        listen_to_copy_duration(helloController);
        listen_to_millisecond_for_each_ayat_focus_change(helloController);
        listen_to_time_update_of_each_ayat_and_update_it_in_list(helloController);
        listen_to_enter_pressed_time_for_each_ayat(helloController);
        listen_to_genereate_chat_gpt_checkbox(helloController);
        uplaod_image_button_for_each_ayat_listen(helloController);
        upload_sound_listen(helloController);
        listen_to_create_video_button(helloController);
        listen_to_enable_english_text(helloController);
        listen_to_all_of_the_buttons_for_english_text_position(helloController);
        set_the_text_formatter_for_font_size(helloController);
        watch_the_font_size_change(helloController);
        set_the_formatter_for_the_english_text_color(helloController);
        watch_the_font_color_change(helloController);
        set_the_text_formatter_for_the_margin_text_filed_english(helloController);
        watch_top_margin_text_field_change(helloController);
        Put_all_of_the_fonts_in_a_spinner(helloController);
        listen_to_update_in_font_choose(helloController);
        set_the_cursor_at_the_end_of_the_margin_spinner(helloController);
        set_the_cursor_at_the_end_of_the_dont_size(helloController);
        select_font_first_time_english(helloController);
        listen_to_apply_to_all_button(helloController);
        apply_changes_button_listen(helloController);
        set_up_the_brightness_spinner(helloController);
        listen_to_arabic_translation_enabled(helloController);
        add_the_fonts_to_the_arabic_combox(helloController);
        set_the_font_of_the_arabic_font(helloController);
        set_the_arabic_font_size(helloController);
        watch_the_font_size_change_arabic(helloController);
        set_the_cursor_at_the_end_of_the_dont_size_arabic(helloController);
        set_the_formatter_for_the_english_text_color_arabic(helloController);
        listen_to_all_of_the_buttons_for_english_text_position_arabic_text(helloController);
        set_up_the_top_margin_for_arabic(helloController);
        watch_top_margin_text_field_change_arabic(helloController);
        set_the_cursor_at_the_end_of_the_margin_spinner_arabic(helloController);
        add_surat_name_checkbox_listen(helloController);
        set_up_combobox_arabic_surat(helloController);
        set_first_item_of_the_combobox_surat_font(helloController);
        set_up_surant_name_font_size_spinner(helloController);
        set_up_the_top_margin_for_arabic_surat(helloController);
        watch_top_margin_text_field_change_arabic_surat(helloController);
        set_the_cursor_at_the_end_of_the_margin_spinner_arabic_surat(helloController);
        watch_the_font_size_change_arabic_surat(helloController);
        set_the_cursor_at_the_end_of_the_dont_size_arabic_surat(helloController);
        set_the_formatter_for_the_english_text_color_arabic_surat(helloController);
        listen_to_all_of_the_buttons_for_english_text_position_arabic_text_surat(helloController);
        listen_to_cancel_button_third_screen(helloController);
        get_all_of_the_recitors(helloController);
        listen_to_the_recitor_list_view_click(helloController);
        listen_to_set_image_to_all(helloController);
        make_temp_dir();
        clear_temp_directory();
        listen_to_slider_value_updated(helloController);
        set_the_icons(helloController);
        listen_to_enter_click_on_select_surat_listview(helloController);
        listen_to_list_click_list_view(helloController);
        listen_to_upload_media_button(helloController);
        set_the_width_of_the_left_and_right(helloController);
        listen_to_top_and_bottom_pane_size_change_fourth_screen(helloController, scene);
        listen_to_whole_screen_resize(scene, helloController);
        //listen_to_tile_pane_resized(helloController);
        listen_to_tile_pane_size_change(helloController);
    }

    public static void main(String[] args) {
        launch();
    }


    private void call_chapters_api(HelloController helloController) {
        HttpUrl httpurl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.quran.com")
                .addPathSegment("api")
                .addPathSegment("v4")
                .addPathSegment("chapters")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(httpurl)
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            chapters_string = response.body().string();
            add_surats_to_the_list_view(helloController, chapters_string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void call_verses_api(HelloController helloController, int id, int page) {
        HttpUrl httpurl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.quran.com")
                .addPathSegment("api")
                .addPathSegment("v4")
                .addPathSegment("verses")
                .addPathSegment("by_chapter")
                .addPathSegment(String.valueOf(id + 1))
                .addQueryParameter("language", "en")
                .addQueryParameter("translations", "85")
                .addQueryParameter("translation_fields", "text")
                .addQueryParameter("per_page", "50")
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("fields", "text_uthmani,audio")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(httpurl)
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String verses_string = response.body().string();
            add_all_of_the_verses_to_the_list_after(helloController, verses_string, page);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void call_the_chatgpt_api(HelloController helloController, String ayat_text, int ayat_number) throws JsonProcessingException {
        HttpUrl httpurl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.openai.com")
                .addPathSegment("v1")
                .addPathSegment("images")
                .addPathSegment("generations")
                .build();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(null, "{\"model\":\"dall-e-2\",\"prompt\":\"" + basic_prompt.replace("\"", "") + ayat_text.replace("\"", "") + "\",\"n\":1,\"size\":\"256x256\",\"style\":\"vivid\",\"response_format\":\"b64_json\"}");
        Request request = new Request.Builder()
                .url(httpurl)
                .addHeader("Content-Type", "application/json")
                .post(body) // Note that it's a POST request
                .build();
        try {
            Response response = client.newCall(request).execute();
            String response_string = response.body().string();
            process_chat_gpt_response_and_add_it_to_the_list(ayat_text, ayat_number, response_string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process_chat_gpt_response_and_add_it_to_the_list(String ayat_text, int ayat_number, String response_string) {
        JsonNode Node = return_name_node(response_string);
        ArrayNode arrayNode = (ArrayNode) Node.get("data");
        //Verse_class_final verseClassFinal = new Verse_class_final(ayat_text, ayat_number, String.valueOf(arrayNode.get(0).get("b64_json")), ayat_number * 1000L, new Ayat_settings());
        //chatgpt_responses.add(verseClassFinal);
    }

    private void add_surats_to_the_list_view(HelloController helloController, String response_string) {
        JsonNode nameNode = return_name_node(response_string);
        if (nameNode != null) {
            for (int i = 0; i < nameNode.get("chapters").size(); i++) {
                helloController.choose_the_surat.getItems().add(String.valueOf(nameNode.get("chapters").get(i).get("id")).concat(" - ").concat(String.valueOf(nameNode.get("chapters").get(i).get("name_simple"))).replace("\"", ""));
            }
        }
    }

    private void listen_to_surat_choose(HelloController helloController) {
        helloController.choose_the_surat.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (helloController.choose_the_surat.getSelectionModel().getSelectedIndex() != -1) {
                    helloController.choose_surat_screen.setVisible(false);
                    helloController.choose_ayat_screen.setVisible(true);
                    set_up_second_screen(helloController, helloController.choose_the_surat.getSelectionModel().getSelectedIndex());
                }
            }
        });
    }

    private void set_up_second_screen(HelloController helloController, int id) {
        JsonNode nameNode = return_name_node(chapters_string);
        String surat_name = String.valueOf(nameNode.get("chapters").get(id).get("name_simple"));
        String arabic_surat_name = String.valueOf(nameNode.get("chapters").get(id).get("name_arabic"));
        int verse_count = Integer.parseInt(String.valueOf(nameNode.get("chapters").get(id).get("verses_count")));
        int revelation_order = Integer.parseInt(String.valueOf(nameNode.get("chapters").get(id).get("revelation_order")));
        String revelation_place = String.valueOf(nameNode.get("chapters").get(id).get("revelation_place"));
        ArrayNode arrayNode = (ArrayNode) nameNode.get("chapters").get(id).get("pages");
        int number_of_pages = Integer.parseInt(String.valueOf(arrayNode.get(1))) - Integer.parseInt(String.valueOf(arrayNode.get(0))) + 1;
        helloController.show_surat_name.setText(surat_name);
        helloController.show_surat_name_arabic.setText(arabic_surat_name);
        set_the_number_of_verses(helloController, verse_count);
        set_the_extra_information(helloController, surat_name, revelation_order, revelation_place, verse_count, number_of_pages);
        surat_name_selected = surat_name;
        set_up_first_and_last_ayat_combobox(helloController, verse_count);
        initialize_the_combo_box_start_ayat(helloController);
    }

    private void set_the_number_of_verses(HelloController helloController, int number_of_verses) {
        if (number_of_verses == 1) {
            helloController.number_of_ayats.setText("1 verse");
        } else {
            helloController.number_of_ayats.setText(String.valueOf(number_of_verses).concat(" verses"));
        }
    }

    private void set_the_extra_information(HelloController helloController, String surat_name, int revelation_order, String revelation_place, int verses_count, int number_of_pages) {
        String revelation_order_string;
        if (revelation_order == 11 || revelation_order == 12 || revelation_order == 13) {
            revelation_order_string = String.valueOf(revelation_order).concat("th");
        } else {
            if (revelation_order % 10 == 1) {
                revelation_order_string = String.valueOf(revelation_order).concat("st");
            } else if (revelation_order % 10 == 2) {
                revelation_order_string = String.valueOf(revelation_order).concat("nd");
            } else if (revelation_order % 10 == 3) {
                revelation_order_string = String.valueOf(revelation_order).concat("rd");
            } else {
                revelation_order_string = String.valueOf(revelation_order).concat("th");
            }
        }
        revelation_place = revelation_place.replace("\"", "");
        revelation_place = revelation_place.substring(0, 1).toUpperCase() + revelation_place.substring(1);
        String pages;
        if (number_of_pages == 1) {
            pages = "1 page.";
        } else {
            pages = String.valueOf(number_of_pages).concat(" pages.");
        }
        helloController.show_information_about_surat.setText("Surat ".concat(surat_name).concat(" is the ").concat(revelation_order_string).concat(" revelation of the Quran and was revealed in ").concat(revelation_place).concat(". It contains ").concat(String.valueOf(verses_count)).concat(" verses and consists of ").concat(pages));
    }


    private JsonNode return_name_node(String response_string) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nameNode = null;
        try {
            nameNode = mapper.readTree(response_string);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return nameNode;
    }

    private boolean is_the_ayat_format_correct(HelloController helloController) {
        int start_ayat = helloController.combobox_to_choose_starting_ayat.getSelectionModel().getSelectedItem();
        int end_ayat = helloController.combobox_to_choose_last_ayat.getSelectionModel().getSelectedItem();
        if (end_ayat >= start_ayat) {
            return true;
        } else {
            return false;
        }
    }

    private void show_alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.initOwner(main_stage);
        alert.setTitle("Error!");
        alert.setHeaderText("An error has occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void set_up_third_screen(HelloController helloController, int id) {
        JsonNode nameNode = return_name_node(chapters_string);
        String surat_name = String.valueOf(nameNode.get("chapters").get(id).get("name_simple"));
        String generating_message;
        int start_ayat = return_start_ayat(helloController);
        int end_ayat = return_end_ayat(helloController);
        if (is_it_only_one_ayat_selected(helloController)) {
            generating_message = "Generating ayat ".concat(String.valueOf(return_start_ayat(helloController))).concat(" for surat ".concat(surat_name).concat("."));
        } else {
            generating_message = "Generating ayats ".concat(String.valueOf(return_start_ayat(helloController))).concat(" - ").concat(String.valueOf(return_end_ayat(helloController))).concat(" for surat ".concat(surat_name).concat("."));
        }
        make_the_generating_text(helloController, generating_message);
        initial_number_of_ayats = end_ayat - start_ayat + 1;
        int start_ayat_section = (int) Math.ceil(start_ayat / 50D);
        int end_ayat_section = (int) Math.ceil(end_ayat / 50D);
        for (int i = start_ayat_section; i <= end_ayat_section; i++) {
            call_verses_api(helloController, id, i);
        }
        set_number_of_verses_left(helloController, initial_number_of_ayats);
        if (helloController.generate_chat_gpt_images.isSelected()) {

        }
    }

    private void add_all_of_the_verses_to_the_list_after(HelloController helloController, String verses_string, int page) {
        JsonNode nameNode = return_name_node(verses_string);
        ArrayNode arrayNode = (ArrayNode) nameNode.get("verses");
        int start_ayat = return_start_ayat(helloController);
        int end_ayat = return_end_ayat(helloController);
        if (!is_it_only_one_ayat_selected(helloController)) {
            for (int i = 0; i < arrayNode.size(); i++) {
                int ayat_number = Integer.parseInt(String.valueOf(arrayNode.get(i).get("verse_number")));
                if (ayat_number < start_ayat) {
                    continue;
                } else if (ayat_number > end_ayat) {
                    break;
                }
                String arabic_ayat = String.valueOf(arrayNode.get(i).get("text_uthmani"));
                ArrayNode translations_array_node = (ArrayNode) arrayNode.get(i).get("translations");
                add_to_array_list_with_verses(String.valueOf(Jsoup.parse(String.valueOf(translations_array_node.get(0).get("text"))).text()), ayat_number, arabic_ayat);
                if (!helloController.generate_chat_gpt_images.isSelected() && array_list_with_verses.size() == initial_number_of_ayats) {
                    for (int j = 0; j < array_list_with_verses.size(); j++) {
                        if (durations == null || durations.length == 0) {
                            chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), Long.MAX_VALUE, new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), return_the_aspect_ratio_as_an_object(helloController)));
                        } else {
                            if (j == 0) {
                                chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), durations[j], new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), return_the_aspect_ratio_as_an_object(helloController)));
                            } else {
                                chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), end_of_the_picture_durations[j], new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), return_the_aspect_ratio_as_an_object(helloController)));
                            }
                        }
                    }
                    set_up_the_fourth_screen(helloController);
                }
            }
        } else {
            for (int i = 0; i < arrayNode.size(); i++) {
                int ayat_number = Integer.parseInt(String.valueOf(arrayNode.get(i).get("verse_number")));
                if (ayat_number < start_ayat) {
                    continue;
                }
                String arabic_ayat = String.valueOf(arrayNode.get(i).get("text_uthmani"));
                ArrayNode translations_array_node = (ArrayNode) arrayNode.get(i).get("translations");
                add_to_array_list_with_verses(String.valueOf(Jsoup.parse(String.valueOf(translations_array_node.get(0).get("text"))).text()), ayat_number, arabic_ayat);
                if (!helloController.generate_chat_gpt_images.isSelected() && array_list_with_verses.size() == initial_number_of_ayats) {
                    for (int j = 0; j < array_list_with_verses.size(); j++) {
                        if (durations == null || durations.length == 0) {
                            chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), Long.MAX_VALUE, new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), return_the_aspect_ratio_as_an_object(helloController)));
                        } else {
                            if (j == 0) {
                                chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), durations[j], new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), return_the_aspect_ratio_as_an_object(helloController)));
                            } else {
                                chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), end_of_the_picture_durations[j], new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), return_the_aspect_ratio_as_an_object(helloController)));
                            }
                        }
                    }
                    set_up_the_fourth_screen(helloController);
                }
                break;
            }
        }
    }


    private void add_to_array_list_with_verses(String verse, int ayat_number, String arabic_verse) {
        Verse_class verseClass = new Verse_class(surat_name_selected, verse, ayat_number, arabic_verse);
        array_list_with_verses.add(verseClass);
    }

    private Verse_class get_verse(HelloController helloController) {
        if (!array_list_with_verses.isEmpty()) {
            Verse_class verseClass = array_list_with_verses.get(0);
            array_list_with_verses.remove(0);
            return verseClass;
        } else {
            return null;
        }
    }

    private void set_number_of_verses_left(HelloController helloController, int number_of_verses) {
        if (number_of_verses == 1) {
            update_the_number_of_verses_left_text(helloController, String.valueOf(number_of_verses).concat(" verse left"));
        } else {
            update_the_number_of_verses_left_text(helloController, String.valueOf(number_of_verses).concat(" verses left"));
        }
    }

    private double return_width_of_the_spinner(ObservableList<String> items, Font font) {
        double max_width = 0;
        for (int i = 0; i < items.size(); i++) {
            Text text = new Text(items.get(i));
            text.setFont(font);
            if (text.getLayoutBounds().getWidth() > max_width) {
                max_width = text.getLayoutBounds().getWidth();
            }
        }
        return max_width;
    }

    private void dalle_spinner_listener(HelloController helloController) {
        helloController.which_chatgpt_to_use.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                set_up_the_quality_spinner(helloController);
            }
        });
    }

    private void ratio_spinner_listen(HelloController helloController) {
        helloController.size_of_image.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (helloController.which_chatgpt_to_use.getValue().equals("Dalle 3")) {
                    if (helloController.size_of_image.getValue().equals("9:16")) {
                        helloController.quality_of_image.getValueFactory().setValue("1024x1792");
                    } else if (helloController.size_of_image.getValue().equals("16:9")) {
                        helloController.quality_of_image.getValueFactory().setValue("1792x1024");
                    } else if (helloController.size_of_image.getValue().equals("1:1")) {
                        helloController.quality_of_image.getValueFactory().setValue("1024x1024");
                    }
                }
            }
        });
    }

    private double set_height_of_prompt_text_area(Font font) {
        Text text = new Text("\n\n\n");
        text.setFont(font);
        return text.getLayoutBounds().getHeight();
    }

    private void reset_all_of_the_advanced_settings(HelloController helloController) {
        helloController.choose_the_surat.scrollTo(0);
        helloController.choose_the_surat.getSelectionModel().clearSelection();
        helloController.which_chatgpt_to_use.getValueFactory().setValue("Dalle 3");
        helloController.size_of_image.getValueFactory().setValue("9:16");
        helloController.style_of_image.getValueFactory().setValue("Natural");
        helloController.quality_of_image.getValueFactory().setValue("1024x1792");
        helloController.generate_chat_gpt_images.setSelected(false);
        helloController.show_advanced_settings_second_screen.setSelected(false);
        helloController.enter_the_prompt.setText("Create a 9:16 image based on this. Do not portray god or any human or add any text.");
        helloController.advanced_setting_v_box.setManaged(false);
        helloController.advanced_setting_v_box.setVisible(false);
        helloController.enter_the_prompt.setManaged(false);
        helloController.enter_the_prompt.setVisible(false);
        helloController.quality_of_image.setManaged(false);
        helloController.quality_of_image.setVisible(false);
        helloController.which_chatgpt_to_use.setManaged(false);
        helloController.which_chatgpt_to_use.setVisible(false);
        helloController.style_of_image.setManaged(false);
        helloController.style_of_image.setVisible(false);
        scroll_to_top_of_combo_box(helloController.combobox_to_choose_starting_ayat);
        scroll_to_top_of_combo_box(helloController.combobox_to_choose_last_ayat);

        helloController.choose_sound_third_screen.setText("Upload Sound");
        helloController.list_view_with_the_recitors.scrollTo(0);
        helloController.list_view_with_the_recitors.getSelectionModel().clearSelection();

        helloController.enable_english_text.setSelected(false);
        helloController.spinner_to_choose_font.setValue("Arial");
        helloController.font_size_text_field.getValueFactory().setValue(10D);
        helloController.english_text_color_in_ayat.setText("#FFFFFF");
        set_all_of_the_english_text_position_buttons_not_selected(helloController);
        helloController.position_of_english_text_button_center.setSelected(true);
        helloController.top_margin_english_text.getValueFactory().setValue(0D);
        helloController.english_translation_settings.setManaged(false);
        helloController.english_translation_settings.setVisible(false);

        helloController.add_arabic_text_fourth_screen.setSelected(false);
        helloController.spinner_to_choose_font_arabic.setValue("Arial");
        helloController.font_size_text_field_arabic.getValueFactory().setValue(10D);
        helloController.text_color_in_ayat_arabic.setText("#FFFFFF");
        set_all_of_the_english_text_position_buttons_not_selected_arabic(helloController);
        helloController.position_of_english_text_button_center_arabic.setSelected(true);
        helloController.top_margin_text_arabic.getValueFactory().setValue(0D);
        helloController.arabic_translation_settings.setManaged(false);
        helloController.arabic_translation_settings.setVisible(false);

        helloController.add_surat_name_in_video.setSelected(false);
        helloController.spinner_to_choose_font_arabic_surat.setValue("Arial");
        helloController.font_size_text_field_arabic_surat.getValueFactory().setValue(10D);
        helloController.text_color_in_ayat_arabic_surat.setText("#FFFFFF");
        set_all_of_the_english_text_position_buttons_not_selected_arabic_surat(helloController);
        helloController.position_of_english_text_button_center_arabic_surat.setSelected(true);
        helloController.top_margin_text_arabic_surat.getValueFactory().setValue(0D);
        helloController.surat_name_settings.setManaged(false);
        helloController.surat_name_settings.setVisible(false);
        helloController.choose_brightness_of_an_image.getValueFactory().setValue(100);

        set_the_play_pause_button(helloController, "play");

        helloController.sound_slider_fourth_screen.setValue(0);
        helloController.list_view_with_the_verses_preview.getSelectionModel().select(0);
        helloController.list_view_with_the_verses_preview.getFocusModel().focus(0);
        helloController.list_view_with_the_verses_preview.scrollTo(0);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (durations != null) {
            Arrays.fill(durations, null);
        }
        if (end_of_the_picture_durations != null) {
            Arrays.fill(end_of_the_picture_durations, null);
        }
        helloController.duration_of_media.setText("00:00");
        helloController.total_duration_of_media.setText("00:00");
        helloController.upload_image_button_for_each_ayat.setText("Upload Image");
        selected_verse = 0;
        sound_path = "";
        array_list_with_times.clear();
        array_list_with_verses.clear();
        chatgpt_responses.clear();

        clear_temp_directory();
    }

    private void listen_to_previous_button_clicked(HelloController helloController) {
        helloController.previous_page_second_screen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                helloController.choose_surat_screen.setVisible(true);
                helloController.choose_ayat_screen.setVisible(false);
                reset_all_of_the_advanced_settings(helloController);
            }
        });
    }

    private void listen_to_next_button_clicked(HelloController helloController) {
        helloController.next_page_second_screen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean result = is_the_ayat_format_correct(helloController);
                if (!result) {
                    show_alert("The starting verse must be less than or equal to the ending verse.");
                    return;
                }
                if (sound_path.isEmpty() && helloController.list_view_with_the_recitors.getSelectionModel().isEmpty()) {
                    show_alert("Please select a sound before proceeding. You can do so by uploading a sound or by simply selecting a reciter.");
                    return;
                }
                helloController.generating_screen.setVisible(true);
                helloController.choose_ayat_screen.setVisible(false);
                copy_the_images(helloController, get_the_right_basic_image_aspect_ratio(return_the_aspect_ratio_as_an_object(helloController)));
                get_the_sound_and_concat_them_into_one(helloController);
                set_up_third_screen(helloController, helloController.choose_the_surat.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void make_the_generating_text(HelloController helloController, String text) {
        helloController.generating_text.setText(text);
    }

    private void update_the_number_of_verses_left_text(HelloController helloController, String text) {
        helloController.how_many_verses_are_left_text.setText(text);
        helloController.how_many_verses_are_left_text.setVisible(true);
    }

    private void watch_the_advanced_settings_box(HelloController helloController) {
        helloController.show_advanced_settings_second_screen.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                helloController.advanced_setting_v_box.setManaged(newValue);
                helloController.advanced_setting_v_box.setVisible(newValue);
                if (!newValue) {
                    reset_all_of_the_advanced_settings(helloController);
                }
            }
        });
    }

    private void set_up_the_which_chat_gpt_to_use(HelloController helloController) {
        SpinnerValueFactory<String> valueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Dalle 3", "Dalle 2"));
        valueFactory.setWrapAround(true);
        helloController.which_chatgpt_to_use.setValueFactory(valueFactory);
    }

    private void set_the_width_of_which_gpt_spinner(HelloController helloController) {
        double max_width = return_width_of_the_spinner(((SpinnerValueFactory.ListSpinnerValueFactory<String>) helloController.which_chatgpt_to_use.getValueFactory()).getItems(), helloController.which_chatgpt_to_use.getEditor().getFont());
        helloController.which_chatgpt_to_use.setPrefWidth(max_width + helloController.which_chatgpt_to_use.getEditor().getPadding().getLeft() + helloController.which_chatgpt_to_use.getEditor().getPadding().getRight() + 30);
    }

    private void set_up_the_which_image_spinner(HelloController helloController) {
        SpinnerValueFactory<String> valueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("9:16", "16:9", "1:1"));
        valueFactory.setWrapAround(true);
        helloController.size_of_image.setValueFactory(valueFactory);
    }

    private void set_the_width_of_image_size(HelloController helloController) {
        double max_width = return_width_of_the_spinner(((SpinnerValueFactory.ListSpinnerValueFactory<String>) helloController.size_of_image.getValueFactory()).getItems(), helloController.size_of_image.getEditor().getFont());
        helloController.size_of_image.setPrefWidth(max_width + helloController.size_of_image.getEditor().getPadding().getLeft() + helloController.size_of_image.getEditor().getPadding().getRight() + 30);
    }

    private void set_up_the_style_spinner(HelloController helloController) {
        SpinnerValueFactory<String> valueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Natural", "Vivid"));
        valueFactory.setWrapAround(true);
        helloController.style_of_image.setValueFactory(valueFactory);
    }

    private void set_the_width_of_the_style_spinner(HelloController helloController) {
        double max_width = return_width_of_the_spinner(((SpinnerValueFactory.ListSpinnerValueFactory<String>) helloController.style_of_image.getValueFactory()).getItems(), helloController.style_of_image.getEditor().getFont());
        helloController.style_of_image.setPrefWidth(max_width + helloController.style_of_image.getEditor().getPadding().getLeft() + helloController.style_of_image.getEditor().getPadding().getRight() + 30);
    }

    private void set_up_the_quality_spinner(HelloController helloController) {
        if ((helloController.which_chatgpt_to_use.getValue().equals("Dalle 2"))) {
            SpinnerValueFactory<String> valueFactory =
                    new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("256x256", "512x512", "1024x1024"));
            valueFactory.setWrapAround(true);
            helloController.quality_of_image.setValueFactory(valueFactory);
        } else if ((helloController.which_chatgpt_to_use.getValue().equals("Dalle 3"))) {
            SpinnerValueFactory<String> valueFactory =
                    new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("1024x1792", "1792x1024", "1024x1024"));
            valueFactory.setWrapAround(true);
            helloController.quality_of_image.setValueFactory(valueFactory);
        }
    }

    private void set_the_width_of_the_quality_spinner(HelloController helloController) {
        double max_width = return_width_of_the_spinner(((SpinnerValueFactory.ListSpinnerValueFactory<String>) helloController.quality_of_image.getValueFactory()).getItems(), helloController.quality_of_image.getEditor().getFont());
        helloController.quality_of_image.setPrefWidth(max_width + helloController.quality_of_image.getEditor().getPadding().getLeft() + helloController.quality_of_image.getEditor().getPadding().getRight() + 30);
    }

    private void set_the_height_of_text_prompt_text_area(HelloController helloController) {
        helloController.enter_the_prompt.setPrefHeight(set_height_of_prompt_text_area(helloController.enter_the_prompt.getFont()));
    }

    private void set_up_the_fourth_screen(HelloController helloController) {
        helloController.generating_screen.setVisible(false);
        helloController.show_the_result_screen.setVisible(true);
        set_the_image_fourth_screen(helloController, 0);
        set_the_visibility_of_the_buttons(helloController, 0);
        set_selected_verse_text(helloController, 0);
        set_up_the_chatgpt_verses_list_view(helloController);
        helloController.list_view_with_the_verses_preview.getSelectionModel().select(0);
        helloController.list_view_with_the_verses_preview.requestFocus();
        //set_the_english_text_of_the_ayat_in_the_image_view(helloController, 0);
        set_up_the_media(helloController);
        set_the_max_of_the_slider_and_set_time_of_last_ayat(helloController);
        set_the_media_player_listener(helloController);
        set_the_first_text_field_of_first_ayat(helloController);
        listen_to_end_of_audio_fourth_screen(helloController);
        listen_to_slider_audio(helloController);
        set_up_the_width_and_height_of_the_image_in_fourth_screen(helloController);
        check_if_scroll_bar_is_visible(helloController);
    }

    private void set_the_visibility_of_the_buttons(HelloController helloController, int position) {
        if (position <= 0) {
            helloController.previous_photo_chat_gpt_result.setDisable(true);
        } else {
            helloController.previous_photo_chat_gpt_result.setDisable(false);
        }
        if (position >= chatgpt_responses.size() - 1) {
            helloController.next_photo_chat_gpt_result.setDisable(true);
        } else {
            helloController.next_photo_chat_gpt_result.setDisable(false);
        }
    }

    private void set_the_image_fourth_screen(HelloController helloController, int position) {
        helloController.chatgpt_image_view.setImage(buffer_image_to_image(chatgpt_responses.get(position).getEditied_base_64_image()));
    }

    private void next_photo_click_listen(HelloController helloController) {
        helloController.next_photo_chat_gpt_result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selected_verse++;
                the_verse_changed(helloController, selected_verse);
                scroll_to_specific_verse_time();
            }
        });
    }

    private void previous_photo_click_listen(HelloController helloController) {
        helloController.previous_photo_chat_gpt_result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selected_verse--;
                the_verse_changed(helloController, selected_verse);
                scroll_to_specific_verse_time();
            }
        });
    }

    private void set_selected_verse_text(HelloController helloController, int verse_position) {
        set_the_verse_text_fourth_screen(helloController, chatgpt_responses.get(verse_position).getVerse_number(), surat_name_selected);
    }

    private void set_the_verse_text_fourth_screen(HelloController helloController, int verse, String surat) {
        String set_me = "Surat ".concat(surat).concat(" verse ").concat(String.valueOf(verse));
        helloController.what_verse_is_this.setText(set_me);
    }

    private void set_up_the_chatgpt_verses_list_view(HelloController helloController) {
        ObservableList<Verse_class_final> items = FXCollections.observableArrayList();
        items.addAll(chatgpt_responses);
        helloController.list_view_with_the_verses_preview.setItems(items);
        helloController.list_view_with_the_verses_preview.setCellFactory(param -> new ListCell<Verse_class_final>() {
            private ImageView imageView = new ImageView();
            private VBox vBox = new VBox(5); // 5 is the spacing between elements
            private Text text = new Text();

            @Override
            public void updateItem(Verse_class_final item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Pic_aspect_ratio pic_aspect_ratio = return_the_aspect_ratio_as_an_object(helloController);
                    if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_vertical_9_16)) {
                        imageView.setFitHeight(80);
                        imageView.setFitWidth(45);
                        imageView.setImage(item.getThumbnail_vertical());
                    } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_horizontal_16_9)) {
                        imageView.setFitWidth(80);
                        imageView.setFitHeight(45);
                        imageView.setImage(item.getThumbnail_horizontal());
                    } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_square_1_1)) {
                        imageView.setFitWidth(45);
                        imageView.setFitHeight(45);
                        imageView.setImage(item.getThumbnail_square());
                    }
                    imageView.setPreserveRatio(true);
                    imageView.setSmooth(true);
                    text.setText(String.valueOf(item.getVerse_number()));
                    vBox.getChildren().setAll(imageView, text); // Stack the image and text vertically
                    vBox.setAlignment(Pos.CENTER);
                    setGraphic(vBox);
                }
            }
        });
    }

    private void select_the_correct_verse_in_the_list_view(HelloController helloController, int position) {
        helloController.list_view_with_the_verses_preview.getSelectionModel().select(position);
        //helloController.list_view_with_the_verses_preview.scrollTo(position);
        helloController.list_view_with_the_verses_preview.requestFocus();
    }

    private void listen_to_list_view_item_select(HelloController helloController) {
        helloController.list_view_with_the_verses_preview.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selected_verse = helloController.list_view_with_the_verses_preview.getSelectionModel().getSelectedIndex();
                the_verse_changed(helloController, selected_verse);
                scroll_to_specific_verse_time();
            }
        });
    }

    private void listen_to_list_view_keyboard_select(HelloController helloController) {
        helloController.list_view_with_the_verses_preview.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    break;
                case DOWN:
                    break;
                case LEFT:
                    if (selected_verse > 0) {
                        selected_verse--;
                        the_verse_changed(helloController, selected_verse);
                        scroll_to_specific_verse_time();
                    }
                    break;
                case RIGHT:
                    if (selected_verse < chatgpt_responses.size() - 1) {
                        selected_verse++;
                        the_verse_changed(helloController, selected_verse);
                        scroll_to_specific_verse_time();
                    }
                    break;
                default:
                    // Handle other keys or ignore
                    break;
            }
        });
    }

    private void the_verse_changed(HelloController helloController, int selected_verse) {
        set_the_visibility_of_the_buttons(helloController, selected_verse);
        //add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
        set_the_image_fourth_screen(helloController, selected_verse);
        set_selected_verse_text(helloController, selected_verse);
        select_the_correct_verse_in_the_list_view(helloController, selected_verse);
        update_the_text_field_based_on_previous_values(helloController, selected_verse);
        update_the_name_of_the_image_button_fourth_screen(helloController, selected_verse);
        set_the_english_text_of_the_ayat_in_the_image_view(helloController, selected_verse);
        set_the_ayat_settings(helloController, selected_verse);
        hide_and_show_the_time_text_field_editor(helloController, selected_verse);
    }

    private void set_up_the_media(HelloController helloController) {
        media = new Media(Paths.get(sound_path).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    private void listen_to_play(HelloController helloController) {
        helloController.play_sound.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                play_or_pause_the_video_after_click(helloController);
            }
        });
    }

    private void play_or_pause_the_video_after_click(HelloController helloController) {
        if (helloController.play_sound.getProperties().get("type").equals("play")) {
            if (mediaPlayer.getCurrentTime().toMillis() >= get_duration()) {
                mediaPlayer.seek(Duration.ZERO);
            }
            set_the_play_pause_button(helloController, "pause");
            mediaPlayer.play();
        } else if (helloController.play_sound.getProperties().get("type").equals("pause")) {
            set_the_play_pause_button(helloController, "play");
            mediaPlayer.pause();
        }
    }

    /*private void set_the_width_of_play_pause_button(HelloController helloController) {
        Text text = new Text("Pause");
        text.setFont(helloController.play_sound.getFont());
        helloController.play_sound.setPrefWidth(text.getLayoutBounds().getWidth() + 20);
    }*/

    private void set_the_media_player_listener(HelloController helloController) {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if (!is_the_slider_being_held_right_now) {
                    helloController.sound_slider_fourth_screen.setValue(newValue.toMillis());
                }
                update_the_duration_time(helloController, newValue.toMillis());
                change_the_image_based_on_audio_fourth_screen(helloController, newValue.toMillis() + 10);
            }
        });
    }

    private void set_the_max_of_the_slider_and_set_time_of_last_ayat(HelloController helloController) {
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                helloController.sound_slider_fourth_screen.setMax(get_duration());
                chatgpt_responses.get(chatgpt_responses.size() - 1).setTime_in_milliseconds((long) get_duration());
                set_the_time_total_time(helloController, get_duration());
                if (!did_this_play_already) {
                    start_and_unstart_the_media_player(0);
                }
                did_this_play_already = true;
                create_the_time_line(helloController);
            }
        });
    }

    private void listen_to_end_of_audio_fourth_screen(HelloController helloController) {
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                selected_verse = chatgpt_responses.size() - 1;
                set_the_play_pause_button(helloController, "play");
                select_the_correct_verse_in_the_list_view(helloController, selected_verse);
                set_the_duration_to_reflect_end_of_media(helloController);
                helloController.sound_slider_fourth_screen.setValue(helloController.sound_slider_fourth_screen.getMax());
                stop_and_start_the_media_again();
            }
        });
    }

    private void listen_to_slider_audio(HelloController helloController) {
        helloController.sound_slider_fourth_screen.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.millis(helloController.sound_slider_fourth_screen.getValue()));
                is_the_slider_being_held_right_now = false;
            }
        });
    }

    private void listen_to_slide_clicked(HelloController helloController) {
        helloController.sound_slider_fourth_screen.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                is_the_slider_being_held_right_now = true;
            }
        });
    }

    private void update_the_duration_time(HelloController helloController, double total_millis) {
        helloController.duration_of_media.setText(formatTime((long) total_millis));
    }

    private void listen_to_full_screen_button(HelloController helloController) {
        helloController.full_screen_button_fourth_screen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
    }

    private void set_the_text_field_formatter_of_milliseconds_end(HelloController helloController) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {  // Allow only numbers
                return change;
            }
            return null;  // Reject the change
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        helloController.end_time_of_each_image.setTextFormatter(formatter);
    }

    private void listen_to_copy_duration(HelloController helloController) {
        helloController.copy_duration_fourth_screen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(Math.round(mediaPlayer.getCurrentTime().toMillis())));
                clipboard.setContent(content);
            }
        });
    }

    private void listen_to_millisecond_for_each_ayat_focus_change(HelloController helloController) {
        helloController.end_time_of_each_image.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    javafx.application.Platform.runLater(() -> {
                        helloController.end_time_of_each_image.selectAll();
                    });
                }
            }
        });
    }

    private void listen_to_time_update_of_each_ayat_and_update_it_in_list(HelloController helloController) {
        helloController.end_time_of_each_image.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int selected_index = helloController.list_view_with_the_verses_preview.getSelectionModel().getSelectedIndex();
                long time_in_milli = Long.parseLong(newValue);
                chatgpt_responses.get(selected_index).setTime_in_milliseconds(time_in_milli);
            }
        });
    }

    private void update_the_text_field_based_on_previous_values(HelloController helloController, int selected_verse) {
        long time_in_milli = chatgpt_responses.get(selected_verse).getTime_in_milliseconds();
        helloController.end_time_of_each_image.setText(String.valueOf(time_in_milli));
    }

    private void set_the_first_text_field_of_first_ayat(HelloController helloController) {
        long time_in_milli = chatgpt_responses.get(0).getTime_in_milliseconds();
        helloController.end_time_of_each_image.setText(String.valueOf(time_in_milli));
    }

    private void listen_to_enter_pressed_time_for_each_ayat(HelloController helloController) {
        helloController.end_time_of_each_image.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    helloController.list_view_with_the_verses_preview.requestFocus();
                }
            }
        });
    }

    private void change_the_image_based_on_audio_fourth_screen(HelloController helloController, Double time_of_audio_millis) {
        if (chatgpt_responses.isEmpty()) {// check that array_is_not_empty
            return;
        }
        if (time_of_audio_millis > chatgpt_responses.get(selected_verse).getTime_in_milliseconds() && selected_verse < chatgpt_responses.size() - 1) {
            selected_verse++;
            while (time_of_audio_millis > chatgpt_responses.get(selected_verse).getTime_in_milliseconds() && selected_verse < chatgpt_responses.size() - 1) {
                selected_verse++;
            }
            the_verse_changed(helloController, selected_verse);
        } else if (selected_verse > 0 && time_of_audio_millis < chatgpt_responses.get(selected_verse - 1).getTime_in_milliseconds()) {
            selected_verse--;
            while (selected_verse > 0 && time_of_audio_millis < chatgpt_responses.get(selected_verse - 1).getTime_in_milliseconds()) {
                selected_verse--;
            }
            the_verse_changed(helloController, selected_verse);
        }
    }

    private void listen_to_genereate_chat_gpt_checkbox(HelloController helloController) {
        helloController.generate_chat_gpt_images.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                helloController.enter_the_prompt.setManaged(newValue);
                helloController.enter_the_prompt.setVisible(newValue);
                helloController.quality_of_image.setManaged(newValue);
                helloController.quality_of_image.setVisible(newValue);
                helloController.which_chatgpt_to_use.setManaged(newValue);
                helloController.which_chatgpt_to_use.setVisible(newValue);
                helloController.style_of_image.setManaged(newValue);
                helloController.style_of_image.setVisible(newValue);
            }
        });
    }

    /*private void only_allow_digits_and_dash_for_ayat_input(HelloController helloController) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0123456789-]*")) {  // Regex to allow only 1-9, space, and dash
                return change;
            }
            return null; // Reject the change
        };

        TextFormatter<String> formatter = new TextFormatter<>(filter);
        helloController.enter_the_ayats_wanted.setTextFormatter(formatter);
    }*/

    private void set_up_the_width_and_height_of_the_image_in_fourth_screen(HelloController helloController) {
        /*if (helloController.size_of_image.getValue().equals("9:16")) {
            helloController.chatgpt_image_view.setFitWidth(360);
            helloController.chatgpt_image_view.setFitHeight(640);
        } else if (helloController.size_of_image.getValue().equals("16:9")) {
            helloController.chatgpt_image_view.setFitWidth(640);
            helloController.chatgpt_image_view.setFitHeight(360);
        } else if (helloController.size_of_image.getValue().equals("1:1")) {
            helloController.chatgpt_image_view.setFitWidth(360);
            helloController.chatgpt_image_view.setFitHeight(360);
        }*/
    }

    private void uplaod_image_button_for_each_ayat_listen(HelloController helloController) {
        helloController.upload_image_button_for_each_ayat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean too_many_images_selected = false;
                FileChooser fileChooser = new FileChooser();
                //Set extension filter
                FileChooser.ExtensionFilter image_filter;
                if (is_this_a_mac_device()) {
                    image_filter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg, *.jpeg, *.heic)", "*.png", "*.PNG", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.heic", "*.HEIC");
                } else {
                    image_filter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "*.png", "*.PNG", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG");

                }
                fileChooser.getExtensionFilters().addAll(image_filter);
                //Show open file dialog
                List<File> files = fileChooser.showOpenMultipleDialog(null);
                if (files != null && !files.isEmpty()) {
                    for (int i = 0; i < files.size(); i++) {
                        if (selected_verse + i >= chatgpt_responses.size()) {
                            too_many_images_selected = true;
                            break;
                        }
                        try {
                            File image_file = files.get(i);
                            String fileName_lower_case = image_file.getName().toLowerCase();
                            Image image;
                            if (fileName_lower_case.endsWith("heic")) {
                                File new_jpg_file = new File("temp/converted images/".concat(UUID.randomUUID().toString()).concat(".png"));
                                new_jpg_file.deleteOnExit();
                                convertHeicToJpg(image_file, new_jpg_file);
                                image = new Image(new FileInputStream(new_jpg_file));
                            } else {
                                image = new Image(new FileInputStream(image_file));
                            }
                            BufferedImage bufferedImage = image_to_buffered_image(image);
                            int orientation = getExifOrientation(files.get(i));
                            if (orientation == 3 || orientation == 6 || orientation == 8) {
                                bufferedImage = return_the_rotated_image(bufferedImage, orientation);
                            }
                            Pic_aspect_ratio picAspectRatio = return_the_aspect_ratio_as_an_object(helloController);
                            if ((picAspectRatio.equals(Pic_aspect_ratio.aspect_square_1_1) && image.getWidth() == image.getHeight()) || (picAspectRatio.equals(Pic_aspect_ratio.aspect_vertical_9_16) && image.getWidth() * 16D == image.getHeight() * 9D) || (picAspectRatio.equals(Pic_aspect_ratio.aspect_horizontal_16_9) && image.getWidth() * 9D == image.getHeight() * 16D)) {
                                chatgpt_responses.get(selected_verse + i).setBase_64_image(bufferedImage);
                            } else {
                                if (picAspectRatio.equals(Pic_aspect_ratio.aspect_vertical_9_16)) {
                                    BufferedImage formattedImage = null;
                                    int targetWidth = bufferedImage.getWidth();
                                    int targetHeight = targetWidth * 16 / 9;  // Calculate the new height for a 9:16 ratio
                                    // Create a new black image with a 9:16 ratio
                                    formattedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
                                    Graphics g = formattedImage.createGraphics();
                                    g.setColor(java.awt.Color.BLACK);
                                    g.fillRect(0, 0, targetWidth, targetHeight);  // Fill the background with black
                                    int buffer_at_the_top = (targetHeight - bufferedImage.getHeight()) / 2;
                                    g.drawImage(bufferedImage, 0, buffer_at_the_top, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
                                    g.dispose();
                                    chatgpt_responses.get(selected_verse + i).setBase_64_image(formattedImage);
                                    bufferedImage.flush();
                                    formattedImage.flush();
                                } else if (picAspectRatio.equals(Pic_aspect_ratio.aspect_horizontal_16_9)) {

                                } else if (picAspectRatio.equals(Pic_aspect_ratio.aspect_square_1_1)) {

                                }
                            }
                            add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse + i).getAyatSettings(), selected_verse + i);
                            helloController.upload_image_button_for_each_ayat.setText("Change Image");
                            set_the_image_fourth_screen(helloController, selected_verse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        chatgpt_responses.get(selected_verse + i).setImage_edited(true);
                    }
                    helloController.list_view_with_the_verses_preview.refresh();
                    if (too_many_images_selected) {
                        show_alert("Too many images selected. Only the number of images matching the remaining verses will be used.");
                    }
                }
            }
        });
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        else return "";
    }

    private void update_the_name_of_the_image_button_fourth_screen(HelloController helloController, int selected_verse) {
        if (chatgpt_responses.get(selected_verse).isImage_edited()) {
            helloController.upload_image_button_for_each_ayat.setText("Change Image");
        } else {
            helloController.upload_image_button_for_each_ayat.setText("Upload Image");
        }
        /*if (helloController.size_of_image.getValue().equals("9:16")) {
            if (chatgpt_responses.get(selected_verse).getBase_64_image().equals(Base64_image.getInstance().vertical_place_holder)) {
                helloController.upload_image_button_for_each_ayat.setText("Upload Image");
            } else {
                helloController.upload_image_button_for_each_ayat.setText("Change Image");
            }
        } else if (helloController.size_of_image.getValue().equals("16:9")) {
            if (chatgpt_responses.get(selected_verse).getBase_64_image().equals(Base64_image.getInstance().horizontal_place_holder)) {
                helloController.upload_image_button_for_each_ayat.setText("Upload Image");
            } else {
                helloController.upload_image_button_for_each_ayat.setText("Change Image");
            }
        } else if (helloController.size_of_image.getValue().equals("1:1")) {
            if (chatgpt_responses.get(selected_verse).getBase_64_image().equals(Base64_image.getInstance().square_place_holder)) {
                helloController.upload_image_button_for_each_ayat.setText("Upload Image");
            } else {
                helloController.upload_image_button_for_each_ayat.setText("Change Image");
            }
        }*/
    }

    private void upload_sound_listen(HelloController helloController) {
        helloController.choose_sound_third_screen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilterAudio = new FileChooser.ExtensionFilter("Audio files (*.mp3, *.wav)", "*.mp3", "*.wav");
                fileChooser.getExtensionFilters().add(extFilterAudio);
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    String check_if_mp3 = file.getAbsolutePath().toLowerCase();
                    if (check_if_mp3.endsWith(".mp3")) {
                        sound_path = convert_mp3_to_wav(file.getAbsolutePath());
                    } else {
                        sound_path = file.getAbsolutePath();
                    }
                    helloController.choose_sound_third_screen.setText("Change Sound");
                    helloController.list_view_with_the_recitors.getSelectionModel().clearSelection();
                }
            }
        });
    }

    private void listen_to_create_video_button(HelloController helloController) {
        helloController.create_video_final.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                create_video(helloController);
            }
        });
    }

    private void create_video(HelloController helloController) {
        String videoFileName = "/Users/abdelrahmanabdelkader/Downloads/output.mp4";
        File file = new File(videoFileName);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
        }
        int captureWidth = 0;
        int captureHeight = 0;
        if (return_the_aspect_ratio_as_an_object(helloController).equals(Pic_aspect_ratio.aspect_vertical_9_16)) {
            captureWidth = 1080;
            captureHeight = 1920;
        } else if (return_the_aspect_ratio_as_an_object(helloController).equals(Pic_aspect_ratio.aspect_horizontal_16_9)) {
            captureWidth = 1920;
            captureHeight = 1080;
        } else if (return_the_aspect_ratio_as_an_object(helloController).equals(Pic_aspect_ratio.aspect_square_1_1)) {
            captureWidth = 1080;
            captureHeight = 1080;
        }
        int frameRate = 30;

        int audioBitrate = 1024000; // Standard quality

        // Configure the FFmpegFrameRecorder
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoFileName, captureWidth, captureHeight, number_of_audio_channels);
        recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
        recorder.setAudioCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_AAC);
        recorder.setFormat("mp4");
        recorder.setFrameRate(frameRate);
        recorder.setSampleRate(audio_frequncy_of_the_sound);
        recorder.setVideoOption("preset", "veryslow");
        recorder.setVideoOption("crf", "10");
        recorder.setVideoOption("tune", "stillimage");
        recorder.setVideoBitrate(20000000);
        //recorder.setVideoOption("profile", "high");
        recorder.setVideoOption("level", "5.1");
        recorder.setAudioBitrate(audioBitrate);
        recorder.setAudioChannels(number_of_audio_channels);
        recorder.setVideoQuality(0); // Maximum quality
        recorder.setTimestamp(System.currentTimeMillis());
        Java2DFrameConverter converter = new Java2DFrameConverter();
        try {
            recorder.start();
            for (int i = 0; i < chatgpt_responses.size(); i++) {
                BufferedImage image_fake = chatgpt_responses.get(i).getEditied_base_64_image();
                BufferedImage image = new BufferedImage(image_fake.getWidth(), image_fake.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                image.getGraphics().drawImage(image_fake, 0, 0, null);
                long time_on_screen;
                if (i == 0) {
                    time_on_screen = chatgpt_responses.get(i).getTime_in_milliseconds();
                } else {
                    time_on_screen = chatgpt_responses.get(i).getTime_in_milliseconds() - chatgpt_responses.get(i - 1).getTime_in_milliseconds();
                }
                time_on_screen = (time_on_screen * frameRate) / 1000L;
                for (int j = 0; j < time_on_screen; j++) {
                    recorder.record(converter.convert(image));
                }
            }
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(sound_path);
            grabber.start();
            Frame frame;
            while ((frame = grabber.grabSamples()) != null) {
                recorder.record(frame);
            }
            grabber.stop();

            recorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                recorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void listen_to_enable_english_text(HelloController helloController) {
        helloController.enable_english_text.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                helloController.english_translation_settings.setManaged(newValue);
                helloController.english_translation_settings.setVisible(newValue);
                add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
                helloController.chatgpt_image_view.setImage(buffer_image_to_image(chatgpt_responses.get(selected_verse).getEditied_base_64_image()));
            }
        });
    }

    private void listen_to_all_of_the_buttons_for_english_text_position(HelloController helloController) {
        helloController.position_of_english_text_button_top_center.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected(helloController);
                helloController.position_of_english_text_button_top_center.setSelected(true);
            }
        });
        helloController.position_of_english_text_button_center.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected(helloController);
                helloController.position_of_english_text_button_center.setSelected(true);
            }
        });
        helloController.position_of_english_text_button_bottom_center.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected(helloController);
                helloController.position_of_english_text_button_bottom_center.setSelected(true);
            }
        });
    }

    private void set_all_of_the_english_text_position_buttons_not_selected(HelloController helloController) {
        helloController.position_of_english_text_button_top_center.setSelected(false);
        helloController.position_of_english_text_button_center.setSelected(false);
        helloController.position_of_english_text_button_bottom_center.setSelected(false);
    }

    private void set_the_text_formatter_for_font_size(HelloController helloController) {
        helloController.font_size_text_field.setEditable(true);
        helloController.font_size_text_field.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 10.0, 0.1));
        helloController.font_size_text_field.getValueFactory().setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Format the double to avoid scientific notation
                return String.format("%.1f", value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    // Try to parse the user input to a double
                    return Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    // If the input is not valid, return the current value
                    return Double.parseDouble(String.valueOf(helloController.font_size_text_field.getValue()));
                }
            }
        });
    }

    private void watch_the_font_size_change(HelloController helloController) {
        helloController.font_size_text_field.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d{0,1})?")) {  // Regex to allow only digits or digits followed by one decimal
                    helloController.font_size_text_field.getEditor().setText(oldValue);
                }
            }
        });
    }

    private void set_the_formatter_for_the_english_text_color(HelloController helloController) {
        String pattern = "^#[A-Za-z0-9]{0,6}$";
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(pattern)) {
                return change;
            } else {
                return null;
            }
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        helloController.english_text_color_in_ayat.setTextFormatter(formatter);
    }

    private void watch_the_font_color_change(HelloController helloController) {
        helloController.english_text_color_in_ayat.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                /*chatgpt_responses.get(selected_verse).getAyatSettings().setEnglish_color_hex(newValue);
                add_the_text_to_the_photo(helloController,chatgpt_responses.get(selected_verse).getAyatSettings());*/
            }
        });
    }

    private void set_the_text_formatter_for_the_margin_text_filed_english(HelloController helloController) {
        helloController.top_margin_english_text.setEditable(true);

        // Set up the StringConverter to handle user input
        helloController.top_margin_english_text.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 1));
        helloController.top_margin_english_text.getValueFactory().setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Format the double to avoid scientific notation
                return String.format("%.1f", value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    // Try to parse the user input to a double
                    return Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    // If the input is not valid, return the current value
                    return Double.parseDouble(String.valueOf(helloController.top_margin_english_text.getValue()));
                }
            }
        });
    }

    private void watch_top_margin_text_field_change(HelloController helloController) {
        helloController.top_margin_english_text.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d{0,1})?")) {  // Regex to allow only digits or digits followed by one decimal
                    helloController.top_margin_english_text.getEditor().setText(oldValue);
                }
            }
        });
    }

    private void set_the_english_text_of_the_ayat_in_the_image_view(HelloController helloController, int selected_verse) {
        if (helloController.enable_english_text.isSelected() && chatgpt_responses.get(selected_verse).getBase_64_image().equals(chatgpt_responses.get(selected_verse).getEditied_base_64_image())) {
            add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
            helloController.chatgpt_image_view.setImage(buffer_image_to_image(chatgpt_responses.get(selected_verse).getEditied_base_64_image()));
        }
    }

    private void Put_all_of_the_fonts_in_a_spinner(HelloController helloController) {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(Font.getFamilies());
        helloController.spinner_to_choose_font.setItems(items);
        helloController.spinner_to_choose_font.setVisibleRowCount(20);
        helloController.spinner_to_choose_font.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font(item, 14)); // Set the font to the name of the item
                }
            }
        });
        helloController.spinner_to_choose_font.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font(item, 14)); // This ensures the selection also shows the font
                }
            }
        });
    }

    private void listen_to_update_in_font_choose(HelloController helloController) {
        helloController.spinner_to_choose_font.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                /*chatgpt_responses.get(selected_verse).getAyatSettings().setEnglish_font_name(String.valueOf(newValue));
                add_the_text_to_the_photo(helloController,chatgpt_responses.get(selected_verse).getAyatSettings());*/
            }
        });
    }

    private void set_the_cursor_at_the_end_of_the_margin_spinner(HelloController helloController) {
        helloController.top_margin_english_text.valueProperty().addListener((obs, oldValue, newValue) -> {
            javafx.application.Platform.runLater(() -> {
                helloController.top_margin_english_text.getEditor().end(); // Move the cursor to the end
            });
        });
    }

    private void set_the_cursor_at_the_end_of_the_dont_size(HelloController helloController) {
        helloController.font_size_text_field.valueProperty().addListener((obs, oldValue, newValue) -> {
            javafx.application.Platform.runLater(() -> {
                helloController.font_size_text_field.getEditor().end(); // Move the cursor to the end
            });
        });
    }

    private void add_the_text_to_the_photo(HelloController helloController, Ayat_settings ayatSettings, int selected_verse) {
        BufferedImage bufferedImage = chatgpt_responses.get(selected_verse).getBase_64_image();
        float brightnessFactor = (float) (ayatSettings.getBrightness_of_image() / 100.0);
        RescaleOp rescaleOp = new RescaleOp(brightnessFactor, 0, null);
        rescaleOp.filter(bufferedImage, bufferedImage);
        Graphics2D g = bufferedImage.createGraphics();
        /*{
            double difference = 100 - ayatSettings.getBrightness_of_image();
            double real_difference = (difference / 100) * 255;
            Color dimColor = new Color(0, 0, 0, (int) real_difference);
            g.setColor(dimColor);
            g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        }*/
        {
            double font_size = (ayatSettings.getEnglish_font_size() / 500D) * Math.sqrt(Math.pow(bufferedImage.getHeight(), 2) + Math.pow(bufferedImage.getWidth(), 2));
            java.awt.Font font = new java.awt.Font(ayatSettings.getEnglish_font_name(), java.awt.Font.PLAIN, (int) font_size);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            FontMetrics metrics = g.getFontMetrics(font);
            g.setFont(font);
            g.setColor(Color.decode(ayatSettings.getEnglish_color_hex()));
            ArrayList<String> strings_to_be_drawn;
            if (helloController.enable_english_text.isSelected()) {
                strings_to_be_drawn = wrapText(chatgpt_responses.get(selected_verse).getVerse(), metrics, bufferedImage.getWidth(), 10);
            } else {
                strings_to_be_drawn = new ArrayList<>();
                strings_to_be_drawn.add("");
            }
            double first_y;
            if (ayatSettings.getAlignment().equals("Top")) {
                first_y = metrics.getHeight() * strings_to_be_drawn.size();
            } else if (ayatSettings.getAlignment().equals("Center")) {
                first_y = ((bufferedImage.getHeight() - metrics.getHeight() * strings_to_be_drawn.size()) / 2D) + metrics.getAscent() * strings_to_be_drawn.size();
            } else {
                first_y = bufferedImage.getHeight() + metrics.getAscent() * strings_to_be_drawn.size();
            }
            first_y = first_y + ayatSettings.getEnglish_top_margin() * 0.005D * (double) bufferedImage.getHeight();
            for (int i = strings_to_be_drawn.size() - 1; i >= 0; i--) {
                double x = (bufferedImage.getWidth() - metrics.stringWidth(strings_to_be_drawn.get(i))) / 2D;
                int counter_variable = strings_to_be_drawn.size() - 1 - i;
                double y = first_y - counter_variable * metrics.getHeight();
                g.drawString(strings_to_be_drawn.get(i), (int) x, (int) y);
            }
        }
        {
            double font_size = (ayatSettings.getArabic_font_size() / 500D) * Math.sqrt(Math.pow(bufferedImage.getHeight(), 2) + Math.pow(bufferedImage.getWidth(), 2));
            java.awt.Font font = new java.awt.Font(ayatSettings.getArabic_font_name(), java.awt.Font.PLAIN, (int) font_size);
            FontMetrics metrics = g.getFontMetrics(font);
            g.setFont(font);
            g.setColor(Color.decode(ayatSettings.getArabic_color_hex()));
            ArrayList<String> strings_to_be_drawn;
            if (helloController.add_arabic_text_fourth_screen.isSelected()) {
                strings_to_be_drawn = wrapText(remove_qoutes_from_arabic_text(chatgpt_responses.get(selected_verse).getArabic_verse()), metrics, bufferedImage.getWidth(), 10);
            } else {
                strings_to_be_drawn = new ArrayList<>();
                strings_to_be_drawn.add("");
            }
            double first_y;
            if (ayatSettings.getArabic_alignment().equals("Top")) {
                first_y = metrics.getHeight() * strings_to_be_drawn.size();
            } else if (ayatSettings.getArabic_alignment().equals("Center")) {
                first_y = ((bufferedImage.getHeight() - metrics.getHeight() * strings_to_be_drawn.size()) / 2D) + metrics.getAscent() * strings_to_be_drawn.size();
            } else {
                first_y = bufferedImage.getHeight() + metrics.getAscent() * strings_to_be_drawn.size();
            }
            first_y = first_y + ayatSettings.getArabic_top_margin() * 0.005D * (double) bufferedImage.getHeight();
            for (int i = strings_to_be_drawn.size() - 1; i >= 0; i--) {
                double x = (bufferedImage.getWidth() - metrics.stringWidth(strings_to_be_drawn.get(i))) / 2D;
                int counter_variable = strings_to_be_drawn.size() - 1 - i;
                double y = first_y - counter_variable * metrics.getHeight();
                g.drawString(strings_to_be_drawn.get(i), (int) x, (int) y);
            }
        }
        {
            double font_size = (ayatSettings.getSurat_font_size() / 500D) * Math.sqrt(Math.pow(bufferedImage.getHeight(), 2) + Math.pow(bufferedImage.getWidth(), 2));
            java.awt.Font font = new java.awt.Font(ayatSettings.getSurat_font_name(), java.awt.Font.PLAIN, (int) font_size);
            FontMetrics metrics = g.getFontMetrics(font);
            g.setFont(font);
            g.setColor(Color.decode(ayatSettings.getSurat_color_hex()));
            ArrayList<String> strings_to_be_drawn;
            if (helloController.add_surat_name_in_video.isSelected()) {
                strings_to_be_drawn = wrapText(remove_qoutes_from_arabic_text(surat_name_selected), metrics, bufferedImage.getWidth(), 10);
            } else {
                strings_to_be_drawn = new ArrayList<>();
                strings_to_be_drawn.add("");
            }
            double first_y;
            if (ayatSettings.getSurat_alignment().equals("Top")) {
                first_y = metrics.getHeight() * strings_to_be_drawn.size();
            } else if (ayatSettings.getSurat_alignment().equals("Center")) {
                first_y = ((bufferedImage.getHeight() - metrics.getHeight() * strings_to_be_drawn.size()) / 2D) + metrics.getAscent() * strings_to_be_drawn.size();
            } else {
                first_y = bufferedImage.getHeight() + metrics.getAscent() * strings_to_be_drawn.size();
            }
            first_y = first_y + ayatSettings.getSurat_top_margin() * 0.005D * (double) bufferedImage.getHeight();
            for (int i = strings_to_be_drawn.size() - 1; i >= 0; i--) {
                double x = (bufferedImage.getWidth() - metrics.stringWidth(strings_to_be_drawn.get(i))) / 2D;
                int counter_variable = strings_to_be_drawn.size() - 1 - i;
                double y = first_y - counter_variable * metrics.getHeight();
                g.drawString(strings_to_be_drawn.get(i), (int) x, (int) y);
            }
        }
        g.dispose();
        chatgpt_responses.get(selected_verse).setEditied_base_64_image(bufferedImage);
        bufferedImage.flush();
    }

    public ArrayList<String> wrapText(String text, FontMetrics metrics, int maxWidth, int margin_left_and_right) {
        double max_width_double = maxWidth;
        max_width_double /= 100;
        max_width_double *= margin_left_and_right;
        String[] text_split = text.split(" ");
        String currentLine = "";
        ArrayList<String> new_strings = new ArrayList<>();
        for (int i = 0; i < text_split.length; i++) {
            int width = metrics.stringWidth(currentLine.concat(text_split[i]).concat(" ")) + (int) max_width_double;
            if (width > maxWidth) {
                new_strings.add(currentLine);
                currentLine = text_split[i].concat(" ");
            } else {
                currentLine = currentLine.concat(text_split[i]).concat(" ");
            }
        }
        new_strings.add(currentLine);
        return new_strings;
    }

    private String update_the_verse_text(String verse_text) {
        verse_text = verse_text.replaceAll("\\d", "");
        verse_text = verse_text.replaceAll("—", "-");
        verse_text = verse_text.replaceAll("˹", "[");
        verse_text = verse_text.replaceAll("˺", "]");
        verse_text = verse_text.replaceAll("\"", "");
        return verse_text;
    }

    private void select_font_first_time_english(HelloController helloController) {
        helloController.spinner_to_choose_font.setValue("Arial");
    }

    private void set_the_ayat_settings(HelloController helloController, int selected_verse) {
        Ayat_settings ayatSettings = chatgpt_responses.get(selected_verse).getAyatSettings();
        if (helloController.enable_english_text.isSelected()) {
            helloController.spinner_to_choose_font.setValue(ayatSettings.getEnglish_font_name());
            helloController.font_size_text_field.getValueFactory().setValue(ayatSettings.getEnglish_font_size());
            helloController.english_text_color_in_ayat.setText(ayatSettings.getEnglish_color_hex());
            set_all_of_the_english_text_position_buttons_not_selected(helloController);
            if (ayatSettings.getAlignment().equals("Top")) {
                helloController.position_of_english_text_button_top_center.setSelected(true);
            } else if (ayatSettings.getAlignment().equals("Center")) {
                helloController.position_of_english_text_button_center.setSelected(true);
            } else if (ayatSettings.getAlignment().equals("Bottom")) {
                helloController.position_of_english_text_button_bottom_center.setSelected(true);
            }
            helloController.top_margin_english_text.getValueFactory().setValue(ayatSettings.getEnglish_top_margin());
        }
        if (helloController.add_arabic_text_fourth_screen.isSelected()) {
            helloController.spinner_to_choose_font_arabic.setValue(ayatSettings.getArabic_font_name());
            helloController.font_size_text_field_arabic.getValueFactory().setValue(ayatSettings.getArabic_font_size());
            helloController.text_color_in_ayat_arabic.setText(ayatSettings.getArabic_color_hex());
            set_all_of_the_english_text_position_buttons_not_selected_arabic(helloController);
            if (ayatSettings.getArabic_alignment().equals("Top")) {
                helloController.position_of_english_text_button_top_center_arabic.setSelected(true);
            } else if (ayatSettings.getArabic_alignment().equals("Center")) {
                helloController.position_of_english_text_button_center_arabic.setSelected(true);
            } else if (ayatSettings.getArabic_alignment().equals("Bottom")) {
                helloController.position_of_english_text_button_bottom_center_arabic.setSelected(true);
            }
            helloController.top_margin_text_arabic.getValueFactory().setValue(ayatSettings.getArabic_top_margin());
        }
        if (helloController.add_surat_name_in_video.isSelected()) {
            helloController.spinner_to_choose_font_arabic_surat.setValue(ayatSettings.getSurat_font_name());
            helloController.font_size_text_field_arabic_surat.getValueFactory().setValue(ayatSettings.getSurat_font_size());
            helloController.text_color_in_ayat_arabic_surat.setText(ayatSettings.getSurat_color_hex());
            set_all_of_the_english_text_position_buttons_not_selected_arabic_surat(helloController);
            if (ayatSettings.getSurat_alignment().equals("Top")) {
                helloController.position_of_english_text_button_top_center_arabic_surat.setSelected(true);
            } else if (ayatSettings.getSurat_alignment().equals("Center")) {
                helloController.position_of_english_text_button_center_arabic_surat.setSelected(true);
            } else if (ayatSettings.getSurat_alignment().equals("Bottom")) {
                helloController.position_of_english_text_button_bottom_center_arabic_surat.setSelected(true);
            }
            helloController.top_margin_text_arabic_surat.getValueFactory().setValue(ayatSettings.getSurat_top_margin());
        }
        helloController.choose_brightness_of_an_image.getValueFactory().setValue(ayatSettings.getBrightness_of_image());
    }

    private void listen_to_apply_to_all_button(HelloController helloController) {
        helloController.apply_to_all_english_translation.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                update_current_ayat_settings(helloController);
                add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
                helloController.chatgpt_image_view.setImage(buffer_image_to_image(chatgpt_responses.get(selected_verse).getEditied_base_64_image()));
                for (int i = 0; i < chatgpt_responses.size(); i++) {
                    if (i != selected_verse) {
                        chatgpt_responses.get(i).getAyatSettings().set_ayat_settings(chatgpt_responses.get(selected_verse).getAyatSettings());
                        add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), i);
                    }
                }
                helloController.list_view_with_the_verses_preview.refresh();
            }
        });
    }

    private void apply_changes_button_listen(HelloController helloController) {
        helloController.apply_changes_to_current_ayat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                update_current_ayat_settings(helloController);
                add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
                helloController.chatgpt_image_view.setImage(buffer_image_to_image(chatgpt_responses.get(selected_verse).getEditied_base_64_image()));
                helloController.list_view_with_the_verses_preview.refresh();
            }
        });
    }

    private void update_current_ayat_settings(HelloController helloController) {
        Ayat_settings ayatSettings = new Ayat_settings();

        ayatSettings.setBrightness_of_image((int) helloController.choose_brightness_of_an_image.getValue());

        //english settings
        ayatSettings.setEnglish_font_name((String) helloController.spinner_to_choose_font.getValue());
        ayatSettings.setEnglish_font_size((Double) helloController.font_size_text_field.getValue());
        ayatSettings.setEnglish_color_hex(helloController.english_text_color_in_ayat.getText());
        if (helloController.position_of_english_text_button_top_center.isSelected()) {
            ayatSettings.setAlignment("Top");
        } else if (helloController.position_of_english_text_button_center.isSelected()) {
            ayatSettings.setAlignment("Center");
        } else {
            ayatSettings.setAlignment("Bottom");
        }
        ayatSettings.setEnglish_top_margin((Double) helloController.top_margin_english_text.getValue());

        //arabic settings
        ayatSettings.setArabic_font_name((String) helloController.spinner_to_choose_font_arabic.getValue());
        ayatSettings.setArabic_font_size((Double) helloController.font_size_text_field_arabic.getValue());
        ayatSettings.setArabic_color_hex(helloController.text_color_in_ayat_arabic.getText());
        if (helloController.position_of_english_text_button_top_center_arabic.isSelected()) {
            ayatSettings.setArabic_alignment("Top");
        } else if (helloController.position_of_english_text_button_center_arabic.isSelected()) {
            ayatSettings.setArabic_alignment("Center");
        } else {
            ayatSettings.setArabic_alignment("Bottom");
        }
        ayatSettings.setArabic_top_margin((Double) helloController.top_margin_text_arabic.getValue());

        //surat setting
        ayatSettings.setSurat_font_name((String) helloController.spinner_to_choose_font_arabic_surat.getValue());
        ayatSettings.setSurat_font_size((Double) helloController.font_size_text_field_arabic_surat.getValue());
        ayatSettings.setSurat_color_hex(helloController.text_color_in_ayat_arabic_surat.getText());
        if (helloController.position_of_english_text_button_top_center_arabic_surat.isSelected()) {
            ayatSettings.setSurat_alignment("Top");
        } else if (helloController.position_of_english_text_button_center_arabic_surat.isSelected()) {
            ayatSettings.setSurat_alignment("Center");
        } else {
            ayatSettings.setSurat_alignment("Bottom");
        }
        ayatSettings.setSurat_top_margin((Double) helloController.top_margin_text_arabic_surat.getValue());

        chatgpt_responses.get(selected_verse).getAyatSettings().set_ayat_settings(ayatSettings);
    }

    private void set_up_the_brightness_spinner(HelloController helloController) {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 100, 1);
        helloController.choose_brightness_of_an_image.setValueFactory(valueFactory);
        helloController.choose_brightness_of_an_image.setEditable(true);
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 100, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9][0-9]{0,1}|100)?")) {
                return change;
            }
            return null;
        });
        helloController.choose_brightness_of_an_image.getEditor().setTextFormatter(formatter);
    }

    private BufferedImage convertToBufferedImage(Image image) {
        if (image == null) {
            return null;
        }

        // Read the Image into a BufferedImage
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        if (bufferedImage == null) {
            // In case the Image is not loaded properly, create an empty BufferedImage
            bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
            SwingFXUtils.fromFXImage(image, bufferedImage);
        }
        return bufferedImage;
    }

    private void listen_to_arabic_translation_enabled(HelloController helloController) {
        helloController.add_arabic_text_fourth_screen.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                helloController.arabic_translation_settings.setManaged(newValue);
                helloController.arabic_translation_settings.setVisible(newValue);
                update_current_ayat_settings(helloController);
                add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
                helloController.chatgpt_image_view.setImage(buffer_image_to_image(chatgpt_responses.get(selected_verse).getEditied_base_64_image()));
                for (int i = 0; i < chatgpt_responses.size(); i++) {
                    if (i != selected_verse) {
                        chatgpt_responses.get(i).getAyatSettings().set_ayat_settings(chatgpt_responses.get(selected_verse).getAyatSettings());
                        add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), i);
                    }
                }
                helloController.list_view_with_the_verses_preview.refresh();
            }
        });
    }

    private void add_the_fonts_to_the_arabic_combox(HelloController helloController) {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(Font.getFamilies());
        helloController.spinner_to_choose_font_arabic.setItems(items);
        helloController.spinner_to_choose_font_arabic.setVisibleRowCount(20);
        helloController.spinner_to_choose_font_arabic.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font(item, 14)); // Set the font to the name of the item
                }
            }
        });
        helloController.spinner_to_choose_font_arabic.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font(item, 14)); // This ensures the selection also shows the font
                }
            }
        });
    }

    private void set_the_font_of_the_arabic_font(HelloController helloController) {
        helloController.spinner_to_choose_font_arabic.setValue("Arial");
    }

    private void set_the_arabic_font_size(HelloController helloController) {
        helloController.font_size_text_field_arabic.setEditable(true);
        helloController.font_size_text_field_arabic.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 10.0, 0.1));
        helloController.font_size_text_field_arabic.getValueFactory().setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Format the double to avoid scientific notation
                return String.format("%.1f", value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    // Try to parse the user input to a double
                    return Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    // If the input is not valid, return the current value
                    return Double.parseDouble(String.valueOf(helloController.font_size_text_field_arabic.getValue()));
                }
            }
        });
    }

    private void watch_the_font_size_change_arabic(HelloController helloController) {
        helloController.font_size_text_field_arabic.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d{0,1})?")) {  // Regex to allow only digits or digits followed by one decimal
                    helloController.font_size_text_field_arabic.getEditor().setText(oldValue);
                }
            }
        });
    }

    private void set_the_cursor_at_the_end_of_the_dont_size_arabic(HelloController helloController) {
        helloController.font_size_text_field_arabic.valueProperty().addListener((obs, oldValue, newValue) -> {
            javafx.application.Platform.runLater(() -> {
                helloController.font_size_text_field_arabic.getEditor().end(); // Move the cursor to the end
            });
        });
    }

    private void set_the_formatter_for_the_english_text_color_arabic(HelloController helloController) {
        String pattern = "^#[A-Za-z0-9]{0,6}$";
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(pattern)) {
                return change;
            } else {
                return null;
            }
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        helloController.text_color_in_ayat_arabic.setTextFormatter(formatter);
    }

    private void listen_to_all_of_the_buttons_for_english_text_position_arabic_text(HelloController helloController) {
        helloController.position_of_english_text_button_top_center_arabic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected_arabic(helloController);
                helloController.position_of_english_text_button_top_center_arabic.setSelected(true);
            }
        });
        helloController.position_of_english_text_button_center_arabic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected_arabic(helloController);
                helloController.position_of_english_text_button_center_arabic.setSelected(true);
            }
        });
        helloController.position_of_english_text_button_bottom_center_arabic.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected_arabic(helloController);
                helloController.position_of_english_text_button_bottom_center_arabic.setSelected(true);
            }
        });
    }

    private void set_all_of_the_english_text_position_buttons_not_selected_arabic(HelloController helloController) {
        helloController.position_of_english_text_button_top_center_arabic.setSelected(false);
        helloController.position_of_english_text_button_center_arabic.setSelected(false);
        helloController.position_of_english_text_button_bottom_center_arabic.setSelected(false);
    }

    private void set_up_the_top_margin_for_arabic(HelloController helloController) {
        helloController.top_margin_text_arabic.setEditable(true);

        // Set up the StringConverter to handle user input
        helloController.top_margin_text_arabic.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 1));
        helloController.top_margin_text_arabic.getValueFactory().setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Format the double to avoid scientific notation
                return String.format("%.1f", value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    // Try to parse the user input to a double
                    return Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    // If the input is not valid, return the current value
                    return Double.parseDouble(String.valueOf(helloController.top_margin_text_arabic.getValue()));
                }
            }
        });
    }

    private void watch_top_margin_text_field_change_arabic(HelloController helloController) {
        helloController.top_margin_text_arabic.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d{0,1})?")) {  // Regex to allow only digits or digits followed by one decimal
                    helloController.top_margin_text_arabic.getEditor().setText(oldValue);
                }
            }
        });
    }

    private void set_the_cursor_at_the_end_of_the_margin_spinner_arabic(HelloController helloController) {
        helloController.top_margin_text_arabic.valueProperty().addListener((obs, oldValue, newValue) -> {
            javafx.application.Platform.runLater(() -> {
                helloController.top_margin_text_arabic.getEditor().end(); // Move the cursor to the end
            });
        });
    }

    private void hide_and_show_the_time_text_field_editor(HelloController helloController, int selected_verse) {
        if (selected_verse == chatgpt_responses.size() - 1) {
            helloController.end_time_of_each_image.setVisible(false);
        } else {
            helloController.end_time_of_each_image.setVisible(true);
        }
    }

    private String remove_qoutes_from_arabic_text(String arabic_verse) {
        arabic_verse = arabic_verse.replaceAll("\"", "");
        return arabic_verse;
    }

    private void add_surat_name_checkbox_listen(HelloController helloController) {
        helloController.add_surat_name_in_video.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                helloController.surat_name_settings.setManaged(newValue);
                helloController.surat_name_settings.setVisible(newValue);
                add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
                helloController.chatgpt_image_view.setImage(buffer_image_to_image(chatgpt_responses.get(selected_verse).getEditied_base_64_image()));
            }
        });
    }

    private void set_up_combobox_arabic_surat(HelloController helloController) {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.addAll(Font.getFamilies());
        helloController.spinner_to_choose_font_arabic_surat.setItems(items);
        helloController.spinner_to_choose_font_arabic_surat.setVisibleRowCount(20);
        helloController.spinner_to_choose_font_arabic_surat.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font(item, 14)); // Set the font to the name of the item
                }
            }
        });
        helloController.spinner_to_choose_font_arabic_surat.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);
                    setFont(new Font(item, 14)); // This ensures the selection also shows the font
                }
            }
        });
    }

    private void set_first_item_of_the_combobox_surat_font(HelloController helloController) {
        helloController.spinner_to_choose_font_arabic_surat.setValue("Arial");
    }

    private void set_up_surant_name_font_size_spinner(HelloController helloController) {
        helloController.font_size_text_field_arabic_surat.setEditable(true);
        helloController.font_size_text_field_arabic_surat.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0, 10.0, 0.1));
        helloController.font_size_text_field_arabic_surat.getValueFactory().setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Format the double to avoid scientific notation
                return String.format("%.1f", value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    // Try to parse the user input to a double
                    return Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    // If the input is not valid, return the current value
                    return Double.parseDouble(String.valueOf(helloController.font_size_text_field_arabic_surat.getValue()));
                }
            }
        });
    }

    private void watch_the_font_size_change_arabic_surat(HelloController helloController) {
        helloController.font_size_text_field_arabic_surat.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d{0,1})?")) {  // Regex to allow only digits or digits followed by one decimal
                    helloController.font_size_text_field_arabic_surat.getEditor().setText(oldValue);
                }
            }
        });
    }

    private void set_the_cursor_at_the_end_of_the_dont_size_arabic_surat(HelloController helloController) {
        helloController.font_size_text_field_arabic_surat.valueProperty().addListener((obs, oldValue, newValue) -> {
            javafx.application.Platform.runLater(() -> {
                helloController.font_size_text_field_arabic_surat.getEditor().end(); // Move the cursor to the end
            });
        });
    }

    private void set_the_formatter_for_the_english_text_color_arabic_surat(HelloController helloController) {
        String pattern = "^#[A-Za-z0-9]{0,6}$";
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(pattern)) {
                return change;
            } else {
                return null;
            }
        };
        TextFormatter<String> formatter = new TextFormatter<>(filter);
        helloController.text_color_in_ayat_arabic_surat.setTextFormatter(formatter);
    }

    private void listen_to_all_of_the_buttons_for_english_text_position_arabic_text_surat(HelloController helloController) {
        helloController.position_of_english_text_button_top_center_arabic_surat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected_arabic_surat(helloController);
                helloController.position_of_english_text_button_top_center_arabic_surat.setSelected(true);
            }
        });
        helloController.position_of_english_text_button_center_arabic_surat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected_arabic_surat(helloController);
                helloController.position_of_english_text_button_center_arabic_surat.setSelected(true);
            }
        });
        helloController.position_of_english_text_button_bottom_center_arabic_surat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected_arabic_surat(helloController);
                helloController.position_of_english_text_button_bottom_center_arabic_surat.setSelected(true);
            }
        });
    }

    private void set_all_of_the_english_text_position_buttons_not_selected_arabic_surat(HelloController helloController) {
        helloController.position_of_english_text_button_top_center_arabic_surat.setSelected(false);
        helloController.position_of_english_text_button_center_arabic_surat.setSelected(false);
        helloController.position_of_english_text_button_bottom_center_arabic_surat.setSelected(false);
    }

    private void set_up_the_top_margin_for_arabic_surat(HelloController helloController) {
        helloController.top_margin_text_arabic_surat.setEditable(true);

        // Set up the StringConverter to handle user input
        helloController.top_margin_text_arabic_surat.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, Double.MAX_VALUE, 0.0, 1));
        helloController.top_margin_text_arabic_surat.getValueFactory().setConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double value) {
                // Format the double to avoid scientific notation
                return String.format("%.1f", value);
            }

            @Override
            public Double fromString(String text) {
                try {
                    // Try to parse the user input to a double
                    return Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    // If the input is not valid, return the current value
                    return Double.parseDouble(String.valueOf(helloController.top_margin_text_arabic_surat.getValue()));
                }
            }
        });
    }

    private void watch_top_margin_text_field_change_arabic_surat(HelloController helloController) {
        helloController.top_margin_text_arabic_surat.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d{0,1})?")) {  // Regex to allow only digits or digits followed by one decimal
                    helloController.top_margin_text_arabic_surat.getEditor().setText(oldValue);
                }
            }
        });
    }

    private void set_the_cursor_at_the_end_of_the_margin_spinner_arabic_surat(HelloController helloController) {
        helloController.top_margin_text_arabic_surat.valueProperty().addListener((obs, oldValue, newValue) -> {
            javafx.application.Platform.runLater(() -> {
                helloController.top_margin_text_arabic_surat.getEditor().end(); // Move the cursor to the end
            });
        });
    }

    private void listen_to_cancel_button_third_screen(HelloController helloController) {
        helloController.cancel_video.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                helloController.show_the_result_screen.setVisible(false);
                helloController.choose_surat_screen.setVisible(true);
                reset_all_of_the_advanced_settings(helloController);
                clear_temp_directory();
            }
        });
    }

    private String capatilize_first_letter(String text) {
        if (text.isEmpty()) {
            return "";
        }
        if (Character.isLetter(text.charAt(0))) {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        } else {
            return text;
        }
    }

    private void get_all_of_the_recitors(HelloController helloController) {
        HttpUrl httpurl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.mp3quran.net")
                .addPathSegment("verse")
                .addPathSegment("verse_en.json")
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url(httpurl)
                .method("GET", null)
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();
            ArrayList<Reciters_info> arrayList_with_reciters_info = process_the_audio_recirotrs(jsonString);
            set_up_the_recitors_list_view(helloController, arrayList_with_reciters_info);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Reciters_info> process_the_audio_recirotrs(String json_text) {
        JsonNode nameNode = return_name_node(json_text);
        ArrayNode arrayNode = (ArrayNode) nameNode.get("reciters_verse");
        HashMap<String, Integer> hashMap_counter = new HashMap<>();
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode local = arrayNode.get(i);
            hashMap_counter.put(local.get("name").asText(), hashMap_counter.getOrDefault(local.get("name").asText(), 0) + 1);
        }
        ArrayList<Reciters_info> reciters_infoArrayList = new ArrayList<>();
        Reciters_info english_reciter = null;
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode local = arrayNode.get(i);
            Reciters_info recitersInfo;
            if (!local.get("audio_url_bit_rate_128").asText().contains("https") && !local.get("audio_url_bit_rate_64").asText().contains("https") && !local.get("audio_url_bit_rate_32_").asText().contains("https")) {
                continue;
            }
            if (hashMap_counter.get(local.get("name").asText()) == 1) {
                recitersInfo = new Reciters_info(local.get("id").asInt(), local.get("name").asText(), local.get("name").asText().replace("Shaik ", ""), local.get("audio_url_bit_rate_32_").asText(), local.get("audio_url_bit_rate_64").asText(), local.get("audio_url_bit_rate_128").asText());
            } else {
                recitersInfo = new Reciters_info(local.get("id").asInt(), local.get("name").asText(), local.get("name").asText().replace("Shaik ", "").concat(" - ").concat(local.get("rewaya").asText()).concat(" - ").concat(local.get("musshaf_type").asText()), local.get("audio_url_bit_rate_32_").asText(), local.get("audio_url_bit_rate_64").asText(), local.get("audio_url_bit_rate_128").asText());
            }
            if (local.get("id").asInt() == 224 && local.get("name").asText().equals("(English) Translated by Sahih International Recited by Ibrahim Walk")) {
                english_reciter = recitersInfo;
            } else {
                reciters_infoArrayList.add(recitersInfo);
            }
        }
        reciters_infoArrayList.sort(new Comparator<Reciters_info>() {
            @Override
            public int compare(Reciters_info o1, Reciters_info o2) {
                return o1.getDisplayed_name().compareTo(o2.getDisplayed_name());
            }
        });
        if (english_reciter != null) {
            reciters_infoArrayList.add(english_reciter);
        }
        return reciters_infoArrayList;
    }

    private void set_up_the_recitors_list_view(HelloController helloController, List<Reciters_info> reciters_info_list) {
        ObservableList<Reciters_info> items = FXCollections.observableArrayList();
        items.addAll(reciters_info_list);
        helloController.list_view_with_the_recitors.setItems(items);
        helloController.list_view_with_the_recitors.setCellFactory(lv -> new ListCell<Reciters_info>() {
            @Override
            protected void updateItem(Reciters_info item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDisplayed_name());  // assuming getName() returns the name property
                    setAlignment(Pos.CENTER); // or Pos.CENTER_LEFT, Pos.CENTER_RIGHT
                }
            }
        });
    }

    private void listen_to_the_recitor_list_view_click(HelloController helloController) {
        helloController.list_view_with_the_recitors.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sound_path = "";
                helloController.choose_sound_third_screen.setText("Upload Sound");
            }
        });
    }

    private double get_duration() {
        return media.getDuration().toMillis();
    }

    private void get_the_sound_and_concat_them_into_one(HelloController helloController) {
        if (sound_path.isEmpty()) {
            boolean did_i_ever_fail_a_recitation = false;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .build();
            client.dispatcher().setMaxRequests(16); // increase concurrency
            client.dispatcher().setMaxRequestsPerHost(8);
            int start_ayat = return_start_ayat(helloController);
            int end_ayat = return_end_ayat(helloController);
            int number_of_ayats = end_ayat - start_ayat + 1;
            int number_of_threads = Math.max(Runtime.getRuntime().availableProcessors() * 4, number_of_ayats);
            BlockingQueue<String> verseQueue = new ArrayBlockingQueue<>(number_of_ayats);
            durations = new Long[number_of_ayats];
            end_of_the_picture_durations = new Long[number_of_ayats];
            HashMap<String, Integer> tie_verses_to_indexes = new HashMap<>();
            String base_url = "";
            if (!helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_128_bits().isEmpty()) {
                base_url = helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_128_bits();
            } else if (!helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_64_bits().isEmpty()) {
                base_url = helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_64_bits();
            } else if (!helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_32_bits().isEmpty()) {
                base_url = helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_32_bits();
            }
            String surat_number = String.format("%03d", helloController.choose_the_surat.getSelectionModel().getSelectedIndex() + 1);
            base_url = base_url.concat(surat_number);
            for (int i = start_ayat; i <= end_ayat; i++) {
                String ayat_number = String.format("%03d", i);
                String full_ayat = base_url.concat(ayat_number).concat(".mp3");
                verseQueue.offer(full_ayat);
                tie_verses_to_indexes.put(full_ayat, i);
            }
            ExecutorService executor = Executors.newFixedThreadPool(number_of_threads);
            for (int i = 0; i < number_of_threads; i++) {
                executor.submit(() -> {
                    while (true) {
                        try {
                            String verse_url = verseQueue.poll(100, TimeUnit.MILLISECONDS);
                            if (verse_url == null) {
                                break; // Exit if no more verses to process
                            }
                            int verse_number = tie_verses_to_indexes.get(verse_url);
                            Request request = new Request.Builder().url(verse_url).build();
                            try (Response response = client.newCall(request).execute()) {
                                if (!response.isSuccessful()) {
                                    /*if (!did_i_ever_fail_a_recitation) {
                                        show_alert("Failed to parse a recitation.");
                                        did_i_ever_fail_a_recitation = true;
                                    }*/
                                    continue;
                                }
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                                try (InputStream input = response.body().byteStream()) {
                                    byte[] temp = new byte[16384];
                                    int bytesRead;
                                    while ((bytesRead = input.read(temp)) != -1) {
                                        buffer.write(temp, 0, bytesRead);
                                    }
                                }
                                byte[] mp3Bytes = buffer.toByteArray();
                                File tempFile = new File("temp/sound", String.format("%03d.mp3", verse_number));
                                tempFile.deleteOnExit();
                                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                                    fos.write(mp3Bytes);
                                }
                                durations[verse_number - start_ayat] = getDurationWithFFmpeg(tempFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                });
            }
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.MINUTES); // Wait for all threads to finish
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            File tempFile = new File("temp/sound", String.format("%03d.mp3", start_ayat));
            audio_frequncy_of_the_sound = get_frequency_of_audio(tempFile.getAbsolutePath());
            int get_the_number_of_audio_channels_local = set_the_number_of_audio_channels(getNumberOfChannels(tempFile.getAbsolutePath()));

            if (number_of_ayats > 0) {
                end_of_the_picture_durations[0] = durations[0];
                for (int i = 1; i < number_of_ayats; i++) {
                    end_of_the_picture_durations[i] = durations[i] + end_of_the_picture_durations[i - 1];
                }
            }
            File listFile = new File("temp/sound", "list.txt");
            listFile.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(listFile)) {
                for (int i = start_ayat; i <= end_ayat; i++) {
                    String filename = String.format("%03d.mp3", i);
                    writer.println("file '" + filename + "'");
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "ffmpeg", "-f", "concat", "-safe", "0",
                        "-i", "temp/sound/list.txt",
                        "-c:a", "pcm_s16le",  // <-- WAV encoding
                        "-ar", String.valueOf(audio_frequncy_of_the_sound),       // <-- 44.1kHz sample rate (standard)
                        "-ac", String.valueOf(get_the_number_of_audio_channels_local),           // <-- 2 channels (stereo); use "1" if you want mono
                        "temp/sound/combined.wav"
                );
                pb.redirectErrorStream(true); // Combine stderr with stdout
                Process process = pb.start();
                int exitCode = process.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                if (exitCode != 0) {
                    System.err.println("FFmpeg failed with exit code " + exitCode);
                    System.err.println("FFmpeg output:\n" + output);
                    show_alert("Audio encoding failed. FFMPEG");
                } else {
                    File out_put_file = new File("temp/sound/combined.wav");
                    out_put_file.deleteOnExit();
                    sound_path = out_put_file.getAbsolutePath();
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void clear_temp_directory() {
        File base_images_file = new File("temp/images/base/");
        File edited_images_file = new File("temp/images/edited/");
        File scaled_images_file = new File("temp/images/scaled/");
        File sounds_files = new File("temp/sound/");
        File converted_images_file = new File("temp/converted images/");
        if (base_images_file.exists() && base_images_file.isDirectory()) {
            try {
                FileUtils.cleanDirectory(base_images_file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (edited_images_file.exists() && edited_images_file.isDirectory()) {
            try {
                FileUtils.cleanDirectory(edited_images_file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (sounds_files.exists() && sounds_files.isDirectory()) {
            try {
                FileUtils.cleanDirectory(sounds_files);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (scaled_images_file.exists() && scaled_images_file.isDirectory()) {
            try {
                FileUtils.cleanDirectory(scaled_images_file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (converted_images_file.exists() && converted_images_file.isDirectory()) {
            try {
                FileUtils.cleanDirectory(converted_images_file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private long getDurationWithFFmpeg(File mp3File) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe", "-v", "error",
                    "-show_entries", "format=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    mp3File.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                if (line != null) {
                    double seconds = Double.parseDouble(line.trim());
                    return (long) (seconds * 1000); // convert to milliseconds
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to get duration: " + e.getMessage());
        }
        return 0;
    }

    private void set_the_time_total_time(HelloController helloController, double time) {
        helloController.total_duration_of_media.setText(formatTime((long) time));
    }

    private void listen_to_set_image_to_all(HelloController helloController) {
        helloController.set_image_to_all.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                BufferedImage current_image = chatgpt_responses.get(selected_verse).getBase_64_image();
                for (int i = 0; i < chatgpt_responses.size(); i++) {
                    chatgpt_responses.get(i).setImage_edited(true);
                    if (i != selected_verse) {
                        chatgpt_responses.get(i).setBase_64_image(current_image);
                    }
                }
                for (int i = 0; i < chatgpt_responses.size(); i++) {
                    if (i != selected_verse) {
                        add_the_text_to_the_photo(helloController, chatgpt_responses.get(i).getAyatSettings(), i);
                    }
                }
                helloController.list_view_with_the_verses_preview.refresh();
            }
        });
    }

    private void make_temp_dir() {
        File directory = new File("temp/");
        File images = new File("temp/images");
        File sound = new File("temp/sound");
        File images_base = new File("temp/images/base");
        File images_edited = new File("temp/images/edited");
        File scaled_images_file = new File("temp/images/scaled");
        File converted_images_file = new File("temp/converted images");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!images.exists()) {
            images.mkdirs();
        }
        if (!sound.exists()) {
            sound.mkdirs();
        }
        if (!images_base.exists()) {
            images_base.mkdirs();
        }
        if (!images_edited.exists()) {
            images_edited.mkdirs();
        }
        if (!scaled_images_file.exists()) {
            scaled_images_file.mkdirs();
        }
        if (!converted_images_file.exists()) {
            converted_images_file.mkdirs();
        }
    }

    private BufferedImage return_buffer_image_from_file(String file_path) {
        BufferedImage image = null;
        try {
            File input = new File(file_path);
            if (input.exists()) {
                image = ImageIO.read(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private Image buffer_image_to_image(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private BufferedImage image_to_buffered_image(Image image) {
        return SwingFXUtils.fromFXImage(image, null);
    }

    private void check_if_scroll_bar_is_visible(HelloController helloController) {
        ChangeListener<Bounds> listener = new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds oldBounds, Bounds newBounds) {
                if (newBounds.getWidth() > 0 && newBounds.getHeight() > 0) {
                    ScrollBar hBar = (ScrollBar) helloController.list_view_with_the_verses_preview.lookup(".scroll-bar:horizontal");
                    ScrollBar vBar = (ScrollBar) helloController.list_view_with_the_verses_preview.lookup(".scroll-bar:vertical");
                    if (hBar != null && hBar.isVisible() && hBar.getMax() > 0 && vBar != null && vBar.isVisible() && vBar.getMax() > 0) {
                        helloController.list_view_with_the_verses_preview.setMaxHeight(helloController.list_view_with_the_verses_preview.getHeight() + hBar.getHeight());
                    }
                    helloController.list_view_with_the_verses_preview.layoutBoundsProperty().removeListener(this);
                }
            }
        };
        helloController.list_view_with_the_verses_preview.layoutBoundsProperty().addListener(listener);
    }

    private void scroll_to_specific_verse_time() {
        if (selected_verse == 0) {
            mediaPlayer.seek(Duration.millis(0));
        } else {
            mediaPlayer.seek(Duration.millis(chatgpt_responses.get(selected_verse - 1).getTime_in_milliseconds()));
        }
    }

    private String formatTime(double timeInMillis) {
        int totalSeconds = (int) (timeInMillis / 1000);
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    private Pic_aspect_ratio return_the_aspect_ratio_as_an_object(HelloController helloController) {
        return Pic_aspect_ratio.aspect_vertical_9_16;
    }

    private int getNumberOfChannels(String audioFilePath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-select_streams", "a:0",
                    "-show_entries", "stream=channels",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    audioFilePath
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            process.waitFor();

            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if error
    }

    private int getExifOrientation(File file) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
            }
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (MetadataException e) {
            throw new RuntimeException(e);
        }
        return 1; // Default
    }

    private BufferedImage return_the_rotated_image(BufferedImage bufferedImage, int orientation) {
        AffineTransform t = new AffineTransform();
        switch (orientation) {
            case 1:
                break;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-bufferedImage.getWidth(), 0);
                break;
            case 3: // PI rotation
                t.translate(bufferedImage.getWidth(), bufferedImage.getHeight());
                t.rotate(Math.PI);
                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -bufferedImage.getHeight());
                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                t.translate(bufferedImage.getHeight(), 0);
                t.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-bufferedImage.getHeight(), 0);
                t.translate(0, bufferedImage.getWidth());
                t.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                t.translate(0, bufferedImage.getWidth());
                t.rotate(3 * Math.PI / 2);
                break;
        }
        AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BICUBIC);

        BufferedImage destinationImage = op.createCompatibleDestImage(bufferedImage, (bufferedImage.getType() == BufferedImage.TYPE_BYTE_GRAY) ? bufferedImage.getColorModel() : null);
        Graphics2D g = destinationImage.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
        destinationImage = op.filter(bufferedImage, destinationImage);
        return destinationImage;
    }

    private void start_and_unstart_the_media_player(double set_time) {
        mediaPlayer.setOnPlaying(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setOnPlaying(null);
                pause_and_listen_to_it(set_time);
            }
        });
        mediaPlayer.setMute(true);
        mediaPlayer.play();
    }

    private void listen_to_slider_value_updated(HelloController helloController) {
        helloController.sound_slider_fourth_screen.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
                update_the_duration_time(helloController, newValue.doubleValue());
            }
        });
    }

    private BufferedImage get_the_right_basic_image_aspect_ratio(Pic_aspect_ratio pic_aspect_ratio) {
        if (pic_aspect_ratio == Pic_aspect_ratio.aspect_vertical_9_16) {
            return Base64_image.getInstance().vertical_place_holder;
        } else if (pic_aspect_ratio == Pic_aspect_ratio.aspect_horizontal_16_9) {
            return Base64_image.getInstance().horizontal_place_holder;
        } else if (pic_aspect_ratio == Pic_aspect_ratio.aspect_square_1_1) {
            return Base64_image.getInstance().square_place_holder;
        }
        return Base64_image.getInstance().vertical_place_holder;
    }

    private void copy_the_images(HelloController helloController, BufferedImage image_to_write) {
        String format = "bmp";
        int start_ayat = return_start_ayat(helloController);
        int end_ayat = return_end_ayat(helloController);
        int number_of_ayats = end_ayat - start_ayat + 1;
        byte[] imageBytes;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image_to_write, "bmp", baos);
            baos.flush();
            imageBytes = baos.toByteArray();
            baos.close(); // good practice
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int number_of_threads = Math.max(16, Math.max(Runtime.getRuntime().availableProcessors() * 2, number_of_ayats));
        //number_of_threads = 32;
        BlockingQueue<String[]> blockingQueue = new ArrayBlockingQueue<>(number_of_ayats * 3);
        for (int i = start_ayat; i <= end_ayat; i++) {
            blockingQueue.offer(new String[]{"base", String.valueOf(i)});
        }
        for (int i = start_ayat; i <= end_ayat; i++) {
            blockingQueue.offer(new String[]{"edited", String.valueOf(i)});
        }
        for (int i = start_ayat; i <= end_ayat; i++) {
            blockingQueue.offer(new String[]{"scaled", String.valueOf(i)});
        }
        ExecutorService executor = Executors.newFixedThreadPool(number_of_threads);
        for (int i = 0; i < number_of_threads; i++) {
            executor.submit(() -> {
                while (true) {
                    try {
                        String[] item = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
                        if (item == null) {
                            break; // Exit if no more verses to process
                        }
                        Path targetPath = Paths.get("temp/images", item[0], item[1] + "." + format);
                        File file = targetPath.toFile();
                        file.deleteOnExit();
                        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetPath.toFile()))) {
                            bos.write(imageBytes);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES); // Wait for all threads to finish
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void set_the_duration_to_reflect_end_of_media(HelloController helloController) {
        helloController.duration_of_media.setText(formatTime(get_duration()));
    }

    private void stop_and_start_the_media_again() {
        mediaPlayer.setOnStopped(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setOnStopped(null);
                start_and_unstart_the_media_player(get_duration());
            }
        });
        mediaPlayer.stop();
    }

    private void pause_and_listen_to_it(double set_time) {
        mediaPlayer.setOnPaused(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.setOnPaused(null);
                mediaPlayer.setMute(false);
                mediaPlayer.seek(Duration.millis(set_time));
            }
        });
        mediaPlayer.pause();
    }

    /*private void listen_to_image_click(HelloController helloController) {
        helloController.chatgpt_image_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                play_or_pause_the_video_after_click(helloController);
            }
        });
    }*/

    private String convert_mp3_to_wav(String file_path) {
        String output_file_path = "temp/sound/converted.wav";
        audio_frequncy_of_the_sound = get_frequency_of_audio(file_path);
        int number_of_audio_channels_local = set_the_number_of_audio_channels(getNumberOfChannels(output_file_path));
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", file_path,              // <-- the path to the one MP3 file
                "-c:a", "pcm_s16le",               // WAV encoding
                "-ar", String.valueOf(audio_frequncy_of_the_sound),                    // 44.1kHz sample rate
                "-ac", String.valueOf(number_of_audio_channels_local), // 2 or 1 channels
                output_file_path                  // example: "temp/sound/combined.wav"
        );
        pb.redirectErrorStream(true); // Combine stderr with stdout
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                show_alert("Audio encoding failed. FFMPEG. Can't convert from mp3 to wav");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return output_file_path;
    }

    private int set_the_number_of_audio_channels(int get_the_number_of_audio_channels) {
        if (get_the_number_of_audio_channels == -1) {
            number_of_audio_channels = 2;
        } else {
            number_of_audio_channels = get_the_number_of_audio_channels;
        }
        return number_of_audio_channels;
    }

    private int get_frequency_of_audio(String audioFilePath) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe",
                    "-v", "error",
                    "-select_streams", "a:0",
                    "-show_entries", "stream=sample_rate",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    audioFilePath
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            process.waitFor();

            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if error
    }

    private boolean is_this_a_mac_device() {
        if (System.getProperty("os.name") == null) {
            return false;
        }
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    private void convertHeicToJpg(File old_file, File new_file) {
        try {
            String format = "jpg";
            if (new_file.getName().toLowerCase().endsWith("png")) {
                format = "png";
            } else if (new_file.getName().toLowerCase().endsWith("jpg")) {
                format = "jpeg";
            }
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "sips",
                    "-s", "format", format,
                    old_file.getAbsolutePath(),
                    "--out",
                    new_file.getAbsolutePath()
            );
            processBuilder.redirectErrorStream(true); // Merge error and output
            Process process = processBuilder.start();

            // Optional: Read output if you want to debug
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                show_alert("HEIC to JPG image conversion failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_up_first_and_last_ayat_combobox(HelloController helloController, int verse_count) {
        ObservableList<Integer> items = FXCollections.observableArrayList();
        for (int i = 1; i <= verse_count; i++) {
            items.add(i);
        }
        helloController.combobox_to_choose_starting_ayat.setItems(items);
        helloController.combobox_to_choose_last_ayat.setItems(items);
    }

    private void initialize_the_combo_box_start_ayat(HelloController helloController) {
        helloController.combobox_to_choose_starting_ayat.getSelectionModel().selectFirst();
        helloController.combobox_to_choose_last_ayat.getSelectionModel().selectFirst();
    }

    private int return_start_ayat(HelloController helloController) {
        return helloController.combobox_to_choose_starting_ayat.getSelectionModel().getSelectedItem();
    }

    private int return_end_ayat(HelloController helloController) {
        return helloController.combobox_to_choose_last_ayat.getSelectionModel().getSelectedItem();
    }

    private boolean is_it_only_one_ayat_selected(HelloController helloController) {
        if (return_start_ayat(helloController) == return_end_ayat(helloController)) {
            return true;
        } else {
            return false;
        }
    }

    private void scroll_to_top_of_combo_box(ComboBox<Integer> comboBox) {
        Skin<?> skin = comboBox.getSkin();
        if (skin instanceof ComboBoxListViewSkin) {
            @SuppressWarnings("unchecked")
            ComboBoxListViewSkin<String> comboBoxListViewSkin = (ComboBoxListViewSkin<String>) skin;
            ListView<String> listView = (ListView<String>) comboBoxListViewSkin.getPopupContent();
            if (listView != null) {
                listView.scrollTo(0);
            }
        }
    }

    private void set_the_icons(HelloController helloController) {
        helloController.next_photo_chat_gpt_result.setGraphic(return_region_for_svg(get_the_svg_path("arrow_forward_ios"), 25D));
        helloController.previous_photo_chat_gpt_result.setGraphic(return_region_for_svg(get_the_svg_path("arrow_back_ios"), 25D));
        helloController.full_screen_button_fourth_screen.setGraphic(return_region_for_svg(get_the_svg_path("fullscreen"), 25D));
        helloController.cancel_video.setGraphic(return_region_for_svg(get_the_svg_path("arrow_back_with_line"), 27.5));
        set_the_play_pause_button(helloController, "play");
    }

    private String getSvgPathContent(String resourcePath) {
        String final_resource_path = "/icons/".concat(resourcePath).concat(".svg");
        try (InputStream inputStream = getClass().getResourceAsStream(final_resource_path)) {
            if (inputStream == null) {
                System.err.println("Could not find SVG file: " + resourcePath);
                return null;
            }
            String svgData = new String(inputStream.readAllBytes());

            // Regex to find the d="..." inside <path>
            Pattern pattern = Pattern.compile("d\\s*=\\s*\"([^\"]+)\"");
            Matcher matcher = pattern.matcher(svgData);

            if (matcher.find()) {
                return matcher.group(1); // The actual path data for SVGPath
            } else {
                System.err.println("No <path> with d attribute found in SVG.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Region return_region_for_svg(SVGPath svgPath, double max_width_height) {
        double[] width_and_height = return_real_width_and_height_of_svg(svgPath, max_width_height);
        Region region = new Region();
        region.setShape(svgPath);
        region.setPrefSize(width_and_height[0], width_and_height[1]);
        region.setMaxSize(width_and_height[0], width_and_height[1]);
        region.setStyle("-fx-background-color: black;");
        return region;
    }

    private double[] return_real_width_and_height_of_svg(SVGPath svgPath, double max_width_height) {
        Bounds bounds = svgPath.getBoundsInLocal();
        double width = bounds.getWidth();
        double height = bounds.getHeight();
        if (width > height) {
            return new double[]{max_width_height, max_width_height * (height / width)};
        } else if (width == height) {
            return new double[]{max_width_height, max_width_height};
        } else {
            return new double[]{max_width_height * (width / height), max_width_height};
        }
    }

    private SVGPath get_the_svg_path(String file_name) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(getSvgPathContent(file_name)); // <-- only the path string
        svgPath.setFill(javafx.scene.paint.Paint.valueOf("#000000"));
        return svgPath;
    }

    private void listen_to_enter_click_on_select_surat_listview(HelloController helloController) {
        helloController.choose_the_surat.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                helloController.choose_surat_screen.setVisible(false);
                helloController.choose_ayat_screen.setVisible(true);
                set_up_second_screen(helloController, helloController.choose_the_surat.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void listen_to_list_click_list_view(HelloController helloController) {
        empty_tile_pane_context_menu = new ContextMenu();
        MenuItem item1 = new MenuItem("Upload media");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                upload_media_has_been_clicked(helloController);
            }
        });
        empty_tile_pane_context_menu.getItems().addAll(item1);
        helloController.tile_pane_media_pool.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    empty_tile_pane_context_menu.show(helloController.tile_pane_media_pool, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                } else {
                    empty_tile_pane_context_menu.hide(); // Optional: hide it on other clicks
                }
            }
        });
    }

    private void listen_to_upload_media_button(HelloController helloController) {
        helloController.add_media_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                upload_media_has_been_clicked(helloController);
                            }
                        });
                    }
                }, 85, TimeUnit.MILLISECONDS);
                executor.shutdown();
            }
        });
    }

    private void upload_media_has_been_clicked(HelloController helloController) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter image_filter;
        if (is_this_a_mac_device()) {
            image_filter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg, *.jpeg, *.heic)", "*.png", "*.PNG", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.heic", "*.HEIC");
        } else {
            image_filter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg, *.jpeg)", "*.png", "*.PNG", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG");

        }
        fileChooser.getExtensionFilters().addAll(image_filter);
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        add_the_images_to_the_media_pool_in_the_back_ground(helloController, files);
    }

    /*private void add_the_images_to_the_media_pool_in_the_back_ground(HelloController helloController, List<File> files) {
        if (files != null && !files.isEmpty()) {
            helloController.progress_indicator_media_pool.setVisible(true);
            helloController.scroll_pane_hosting_tile_pane_media_pool.setDisable(true);
            helloController.upload_media_text.setDisable(true);
            List<Media_pool> arrayList_with_media_pool = Collections.synchronizedList(new ArrayList<>());
            BlockingQueue<File> file_blocking_queue = new ArrayBlockingQueue<>(files.size());
            file_blocking_queue.addAll(files);
            int number_of_processors = Runtime.getRuntime().availableProcessors();
            int number_of_threads = Math.min(Math.ceilDiv(number_of_processors, 2), files.size());
            ExecutorService executor = Executors.newFixedThreadPool(number_of_threads);
            CountDownLatch latch = new CountDownLatch(number_of_threads);
            for(int i = 0;i<number_of_threads;i++){
                executor.submit(() -> {
                    try {
                        while (true) {
                            try {
                                boolean did_the_image_get_down_scaled = false;
                                File image_file = file_blocking_queue.poll(100, TimeUnit.MILLISECONDS);
                                if (image_file == null) {
                                    break;
                                }
                                String fileName_lower_case = image_file.getName().toLowerCase();
                                Image image;
                                if (fileName_lower_case.endsWith("heic")) {
                                    File new_jpg_file = new File("temp/converted images/".concat(UUID.randomUUID().toString()).concat(".png"));
                                    new_jpg_file.deleteOnExit();
                                    convertHeicToJpg(image_file, new_jpg_file);
                                    image = new Image(new FileInputStream(new_jpg_file));
                                } else {
                                    image = new Image(new FileInputStream(image_file));
                                }
                                String file_id = UUID.randomUUID().toString();
                                BufferedImage bufferedImage = image_to_buffered_image(image);
                                int orientation = getExifOrientation(image_file);
                                if (orientation == 3 || orientation == 6 || orientation == 8) {
                                    bufferedImage = return_the_rotated_image(bufferedImage, orientation);
                                }
                                Pic_aspect_ratio picAspectRatio = return_the_aspect_ratio_as_an_object(helloController);
                                Media_pool mediaPool_item;
                                if (is_this_the_correct_ratio(picAspectRatio, bufferedImage)) {
                                    write_the_raw_file(bufferedImage, "temp/images/base", file_id);
                                    if (do_i_need_to_down_scale(bufferedImage, picAspectRatio)) {
                                        did_the_image_get_down_scaled = true;
                                        write_the_raw_file(return_resized_downscale_buffer_image(bufferedImage, picAspectRatio), "temp/images/scaled", file_id);
                                    }
                                    mediaPool_item = new Media_pool(file_id, create_a_thumbnail(bufferedImage, picAspectRatio), image_file.getName(), did_the_image_get_down_scaled);
                                } else {
                                    BufferedImage filled_with_black = fill_the_back_ground_with_color(bufferedImage, new Color(0, 0, 0, 255));
                                    BufferedImage filled_transparent = fill_the_back_ground_with_color(bufferedImage, new Color(0, 0, 0, 0));
                                    write_the_raw_file(filled_with_black, "temp/images/base", file_id);
                                    if (do_i_need_to_down_scale(filled_with_black, picAspectRatio)) {
                                        did_the_image_get_down_scaled = true;
                                        write_the_raw_file(return_resized_downscale_buffer_image(filled_with_black, picAspectRatio), "temp/images/scaled", file_id);
                                    }
                                    mediaPool_item = new Media_pool(file_id, create_a_thumbnail(filled_transparent, picAspectRatio), image_file.getName(), did_the_image_get_down_scaled);
                                }
                                arrayList_with_media_pool.add(mediaPool_item);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } finally {
                        latch.countDown(); // Signal task is done
                    }
                });
            }
            executor.shutdown();
            new Thread(() -> {
                try {
                    latch.await(); // Wait until all tasks call countDown()
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0;i<arrayList_with_media_pool.size();i++){
                                add_image_to_tile_pane(helloController, arrayList_with_media_pool.get(i));
                            }
                            helloController.upload_media_text.setDisable(false);
                            helloController.progress_indicator_media_pool.setVisible(false);
                            helloController.scroll_pane_hosting_tile_pane_media_pool.setDisable(false);
                            hide_or_show_media_pool(helloController);
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }*/

    private void add_the_images_to_the_media_pool_in_the_back_ground(HelloController helloController, List<File> files) {
        if (files != null && !files.isEmpty()) {
            helloController.progress_indicator_media_pool.setVisible(true);
            helloController.scroll_pane_hosting_tile_pane_media_pool.setDisable(true);
            helloController.upload_media_text.setDisable(true);
            List<Media_pool> arrayList_with_media_pool = new ArrayList<>();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    for(int i = 0;i<files.size();i++){
                        File image_file = files.get(i);
                        boolean did_the_image_get_down_scaled = false;
                        String fileName_lower_case = image_file.getName().toLowerCase();
                        Image image;
                        if (fileName_lower_case.endsWith("heic")) {
                            File new_jpg_file = new File("temp/converted images/".concat(UUID.randomUUID().toString()).concat(".png"));
                            new_jpg_file.deleteOnExit();
                            convertHeicToJpg(image_file, new_jpg_file);
                            image = new Image(new FileInputStream(new_jpg_file));
                        } else {
                            image = new Image(new FileInputStream(image_file));
                        }
                        String file_id = UUID.randomUUID().toString();
                        BufferedImage bufferedImage = image_to_buffered_image(image);
                        int orientation = getExifOrientation(image_file);
                        if (orientation == 3 || orientation == 6 || orientation == 8) {
                            bufferedImage = return_the_rotated_image(bufferedImage, orientation);
                        }
                        Pic_aspect_ratio picAspectRatio = return_the_aspect_ratio_as_an_object(helloController);
                        Media_pool mediaPool_item;
                        if (is_this_the_correct_ratio(picAspectRatio, bufferedImage)) {
                            write_the_raw_file(bufferedImage, "temp/images/base", file_id);
                            if (do_i_need_to_down_scale(bufferedImage, picAspectRatio)) {
                                did_the_image_get_down_scaled = true;
                                write_the_raw_file(return_resized_downscale_buffer_image(bufferedImage, picAspectRatio), "temp/images/scaled", file_id);
                            }
                            mediaPool_item = new Media_pool(file_id, create_a_thumbnail(bufferedImage, picAspectRatio), image_file.getName(), did_the_image_get_down_scaled);
                        } else {
                            BufferedImage filled_with_black = fill_the_back_ground_with_color(bufferedImage, new Color(0, 0, 0, 255));
                            BufferedImage filled_transparent = fill_the_back_ground_with_color(bufferedImage, new Color(0, 0, 0, 0));
                            write_the_raw_file(filled_with_black, "temp/images/base", file_id);
                            if (do_i_need_to_down_scale(filled_with_black, picAspectRatio)) {
                                did_the_image_get_down_scaled = true;
                                write_the_raw_file(return_resized_downscale_buffer_image(filled_with_black, picAspectRatio), "temp/images/scaled", file_id);
                            }
                            mediaPool_item = new Media_pool(file_id, create_a_thumbnail(filled_transparent, picAspectRatio), image_file.getName(), did_the_image_get_down_scaled);
                        }
                        arrayList_with_media_pool.add(mediaPool_item);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < arrayList_with_media_pool.size(); i++) {
                                add_image_to_tile_pane(helloController, arrayList_with_media_pool.get(i));
                            }
                            helloController.upload_media_text.setDisable(false);
                            helloController.progress_indicator_media_pool.setVisible(false);
                            helloController.scroll_pane_hosting_tile_pane_media_pool.setDisable(false);
                            hide_or_show_media_pool(helloController);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.shutdown();
        }
    }

    private void write_the_raw_file(BufferedImage bufferedImage, String file_path, String name) {
        int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
        try (FileOutputStream fos = new FileOutputStream(file_path.concat("/").concat(name).concat(".raw"))) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(pixels.length * 4);
            byteBuffer.order(ByteOrder.BIG_ENDIAN); // or LITTLE_ENDIAN depending on your platform
            for (int pixel : pixels) {
                byteBuffer.putInt(pixel);
            }
            fos.write(byteBuffer.array());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean do_i_need_to_down_scale(BufferedImage bufferedImage, Pic_aspect_ratio pic_aspect_ratio) {
        if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_vertical_9_16)) {
            if (bufferedImage.getWidth() > 1080 || bufferedImage.getHeight() > 1920) {
                return true;
            }
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_horizontal_16_9)) {
            if (bufferedImage.getWidth() > 1920 || bufferedImage.getHeight() > 1080) {
                return true;
            }
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_square_1_1)) {
            if (bufferedImage.getWidth() > 1080 || bufferedImage.getHeight() > 1080) {
                return true;
            }
        }
        return false;
    }

    private BufferedImage return_resized_downscale_buffer_image(BufferedImage bufferedImage, Pic_aspect_ratio pic_aspect_ratio) {
        int[] height_and_width = return_width_and_height_based_on_ratio(pic_aspect_ratio);
        int width = height_and_width[0];
        int height = height_and_width[1];
        if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_vertical_9_16)) {
            bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.QUALITY, width, height);
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_horizontal_16_9)) {
            bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.QUALITY, width, height);
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_square_1_1)) {
            bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.QUALITY, width, height);
        }
        return bufferedImage;
    }

    private Image create_a_thumbnail(BufferedImage bufferedImage, Pic_aspect_ratio pic_aspect_ratio) {
        int[] width_and_height = return_width_and_height_based_on_ratio(pic_aspect_ratio);
        int width = width_and_height[0] / 4;
        int height = width_and_height[1] / 4;
        try {
            BufferedImage thumbnail = Thumbnails.of(bufferedImage)
                    .size(width, height)
                    .scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
                    .asBufferedImage();
            return buffer_image_to_image(thumbnail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void add_image_to_tile_pane(HelloController helloController, Media_pool mediaPool) {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-cursor: OPEN_HAND;");
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        ImageView imageView = new ImageView(mediaPool.getThumbnail());
        imageView.setFitWidth(image_view_in_tile_pane_width);
        imageView.setFitHeight(image_view_in_tile_pane_height);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        Label label = new Label(mediaPool.getOriginal_image_name());
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(imageView.getFitWidth());
        label.setWrapText(false);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        vBox.getChildren().setAll(imageView, label);
        stackPane.getChildren().add(vBox);
        stackPane.setUserData(mediaPool);
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Remove media");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                helloController.tile_pane_media_pool.getChildren().remove(stackPane);
                hide_or_show_media_pool(helloController);
            }
        });
        contextMenu.getItems().add(item1);
        stackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    contextMenu.show(stackPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
                mouseEvent.consume();
                if (empty_tile_pane_context_menu != null) {
                    empty_tile_pane_context_menu.hide();
                }
            }
        });
        helloController.tile_pane_media_pool.getChildren().add(stackPane);
    }

    private void set_the_width_of_the_left_and_right(HelloController helloController) {
        helloController.center_group_grid_pane.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> obs, Bounds oldVal, Bounds newVal) {
                double width_of_the_screen = Screen.getPrimary().getBounds().getWidth();
                double width_of_center = newVal.getWidth();
                double width_of_left_and_right = (width_of_the_screen - width_of_center) / 2;
                helloController.left_stack_pane_in_grid_pane.setPrefWidth(width_of_left_and_right);
                helloController.left_stack_pane_in_grid_pane.setMaxWidth(width_of_left_and_right);
                helloController.right_stack_pane_in_grid_pane.setPrefWidth(width_of_left_and_right);
                helloController.right_stack_pane_in_grid_pane.setMaxWidth(width_of_left_and_right);
                helloController.center_group_grid_pane.layoutBoundsProperty().removeListener(this);
            }
        });
    }

    private void listen_to_top_and_bottom_pane_size_change_fourth_screen(HelloController helloController, Scene scene) {
        double screen_height;
        if (scene.getHeight() > 0) {
            screen_height = scene.getHeight();
        } else {
            screen_height = Screen.getPrimary().getBounds().getHeight() / 2;
        }
        if (helloController.top_pane_fourth_screen.getHeight() > 0) {
            max_hight_of_top_pane_fourth_screen = Math.max(helloController.top_pane_fourth_screen.getHeight(), max_hight_of_top_pane_fourth_screen);
            reseize_the_image_view(helloController, screen_height);
        }
        if (helloController.bottom_stack_pane_fourth_screen.getHeight() > 0) {
            max_hight_of_bottom_pane_fourth_screen = Math.max(max_hight_of_bottom_pane_fourth_screen, helloController.bottom_stack_pane_fourth_screen.getHeight());
            reseize_the_image_view(helloController, screen_height);
        }
        helloController.top_pane_fourth_screen.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds old_bounds, Bounds new_bounds) {
                max_hight_of_top_pane_fourth_screen = Math.max(max_hight_of_top_pane_fourth_screen, new_bounds.getHeight());
                if (!helloController.show_the_result_screen.isVisible()) {
                    if (scene.getHeight() == 0) {
                        reseize_the_image_view(helloController, Screen.getPrimary().getBounds().getHeight() / 2);
                    } else {
                        reseize_the_image_view(helloController, scene.getHeight());
                    }
                }
            }
        });
        helloController.bottom_stack_pane_fourth_screen.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds old_bounds, Bounds new_bounds) {
                max_hight_of_bottom_pane_fourth_screen = Math.max(max_hight_of_bottom_pane_fourth_screen, new_bounds.getHeight());
                if (!helloController.show_the_result_screen.isVisible()) {
                    if (scene.getHeight() == 0) {
                        reseize_the_image_view(helloController, Screen.getPrimary().getBounds().getHeight() / 2);
                    } else {
                        reseize_the_image_view(helloController, scene.getHeight());
                    }
                }
            }
        });
    }

    private void listen_to_whole_screen_resize(Scene scene, HelloController helloController) {
        if (scene.getHeight() > 0) {
            reseize_the_image_view(helloController, scene.getHeight());
        }
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_number, Number new_number) {
                reseize_the_image_view(helloController, new_number.doubleValue());
            }
        });
    }

    private void reseize_the_image_view(HelloController helloController, double main_stage_height) {
        double height_of_the_screen = main_stage_height - (helloController.what_verse_is_this.getHeight() + 15 + max_hight_of_top_pane_fourth_screen + max_hight_of_bottom_pane_fourth_screen);
        double width_of_chat_gpt_image_view = (height_of_the_screen / 16) * 9;
        if (height_of_the_screen < 1 || width_of_chat_gpt_image_view < 1) {
            return;
        }
        helloController.chatgpt_image_view.setFitHeight(height_of_the_screen);
        helloController.chatgpt_image_view.setFitWidth(width_of_chat_gpt_image_view);
    }

    private void hide_or_show_media_pool(HelloController helloController) {
        if (helloController.tile_pane_media_pool.getChildren().isEmpty()) {
            helloController.upload_media_text.setManaged(true);
            helloController.upload_media_text.setVisible(true);
        } else {
            helloController.upload_media_text.setManaged(false);
            helloController.upload_media_text.setVisible(false);
        }
    }

    private void set_the_play_pause_button(HelloController helloController, String type) {
        if (type.equals("play")) {
            helloController.play_sound.setGraphic(return_region_for_svg(get_the_svg_path("play_icon"), play_pause_button_size));
            helloController.play_sound.getProperties().put("type", "play");

        } else if (type.equals("pause")) {
            helloController.play_sound.setGraphic(return_region_for_svg(get_the_svg_path("pause_icon"), play_pause_button_size));
            helloController.play_sound.getProperties().put("type", "pause");
        }
    }

    private void set_the_size_of_the_tile_pane(HelloController helloController) {

    }

    private int[] return_width_and_height_based_on_ratio(Pic_aspect_ratio pic_aspect_ratio) {
        int[] height_and_width = new int[2];
        if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_vertical_9_16)) {
            height_and_width[0] = 1080;
            height_and_width[1] = 1920;
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_horizontal_16_9)) {
            height_and_width[0] = 1920;
            height_and_width[1] = 1080;
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_square_1_1)) {
            height_and_width[0] = 1080;
            height_and_width[1] = 1080;
        }
        if (height_and_width[0] == 0 && height_and_width[1] == 0) {
            return new int[]{1080, 1920};
        } else {
            return height_and_width;
        }
    }

    private void listen_to_tile_pane_size_change(HelloController helloController) {
        helloController.tile_pane_media_pool.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds old_bounds, Bounds new_bounds) {
                double width = new_bounds.getWidth();
                int number_of_items = (int) width / image_view_in_tile_pane_width;
                if (number_of_items == 0 || number_of_items == 1) {
                    return;
                }
                double space_left = Math.floor(width % image_view_in_tile_pane_width);
                helloController.tile_pane_media_pool.setPrefColumns(number_of_items);
                helloController.tile_pane_media_pool.setHgap(Math.floor(space_left / (number_of_items - 1)));
            }
        });
    }

    private boolean is_this_the_correct_ratio(Pic_aspect_ratio pic_aspect_ratio, BufferedImage bufferedImage) {
        if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_vertical_9_16)) {
            if (bufferedImage.getWidth() * 16D == bufferedImage.getHeight() * 9D) {
                return true;
            } else {
                return false;
            }
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_horizontal_16_9)) {
            if (bufferedImage.getWidth() * 9D == bufferedImage.getHeight() * 16D) {
                return true;
            } else {
                return false;
            }
        } else if (pic_aspect_ratio.equals(Pic_aspect_ratio.aspect_square_1_1)) {
            if (bufferedImage.getHeight() == bufferedImage.getWidth()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /*private BufferedImage set_the_background_of_the_image_to_black(BufferedImage bufferedImage) {
        double max_width = bufferedImage.getWidth() * 16D;
        double max_height = bufferedImage.getHeight() * 9D;
        double new_image_height;
        double new_image_width;
        if (max_width > max_height) {
            new_image_width = bufferedImage.getWidth();
            new_image_height = (bufferedImage.getWidth() / 9D) * 16D;
            int height_difference = (int) (new_image_height - bufferedImage.getHeight());
            int add_one = 0;
            if (height_difference % 2 == 1) {
                add_one = 1;
            }
            WritableImage writable_image = new WritableImage((int) new_image_width, (int) new_image_height);
            PixelWriter pixel_writer = writable_image.getPixelWriter();
            int[] black_array_area_one = new int[((int) new_image_width * ((height_difference / 2) + add_one))];
            Arrays.fill(black_array_area_one,0xFF000000);
            int[] black_array_area_two = new int[((int) new_image_width * (height_difference / 2))];
            Arrays.fill(black_array_area_two,0xFF000000);
            pixel_writer.setPixels(0, 0, (int) new_image_width, (height_difference / 2) + add_one, PixelFormat.getIntArgbInstance(), black_array_area_one, 0, 0);
            int[] originalData = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
            pixel_writer.setPixels(0, (height_difference / 2) + add_one, (int) new_image_width, bufferedImage.getHeight(), PixelFormat.getIntArgbInstance(),originalData, 0, 0);
            pixel_writer.setPixels(0, (height_difference / 2) + add_one + bufferedImage.getHeight(),  (int) new_image_width, (height_difference / 2), PixelFormat.getIntArgbInstance(),black_array_area_two, 0, 0);
        } else {

        }
    }*/

    private BufferedImage fill_the_back_ground_with_color(BufferedImage bufferedImage, Color color) {
        double max_width = bufferedImage.getWidth() * 16D;
        double max_height = bufferedImage.getHeight() * 9D;
        BufferedImage return_me;
        if (max_width > max_height) {
            int targetWidth = bufferedImage.getWidth();
            int targetHeight = targetWidth * 16 / 9;  // Calculate the new height for a 9:16 ratio
            return_me = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = return_me.createGraphics();
            g.setColor(color);
            g.fillRect(0, 0, targetWidth, targetHeight);  // Fill the background with black
            int buffer_at_the_top = (targetHeight - bufferedImage.getHeight()) / 2;
            g.drawImage(bufferedImage, 0, buffer_at_the_top, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
            g.dispose();
        } else {
            int targetWidth = bufferedImage.getHeight() * 9 / 16;
            int targetHeight = bufferedImage.getHeight();  // Calculate the new height for a 9:16 ratio
            return_me = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = return_me.createGraphics();
            g.setColor(color);
            g.fillRect(0, 0, targetWidth, targetHeight);  // Fill the background with black
            int buffer_at_the_left = (targetWidth - bufferedImage.getWidth()) / 2;
            g.drawImage(bufferedImage, buffer_at_the_left, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
            g.dispose();
        }
        return return_me;
    }

    /*private Rectangle2D return_the_rectangle_2d_for_media_pool(Image image) {
        double max_width = image.getWidth() * 16D;
        double max_height = image.getHeight() * 9D;
        Rectangle2D rectangle2D;
        if (max_width > max_height) {
            double new_height = image.getWidth() * 16D / 9D;
            double offset = -((new_height - image.getHeight())/2);
            rectangle2D = new Rectangle2D(0, offset, image.getWidth(), new_height);
        } else {
            double new_width = image.getHeight() * 9D/16D;
            double offset = -((new_width - image.getWidth())/2);
            rectangle2D = new Rectangle2D(offset, 0, new_width, image.getHeight());
        }
        return rectangle2D;
    }*/

    private void create_the_time_line(HelloController helloController){
        Pane main_pane = helloController.time_line_pane;
        final double pixels_in_between_each_line = 10;
        final double line_length = 5;
        final double time_between_every_line = 50;
        final double long_line_length = 15;
        final double half_long_line_length = 10;
        int number_of_dividors = (int) Math.ceil(get_duration()/time_between_every_line);

        for(int i = 0;i<number_of_dividors;i++){
            Line line_to_separate;
            if((time_between_every_line*i)%1000 == 0){
                line_to_separate = new Line(i*pixels_in_between_each_line,0,i*pixels_in_between_each_line,long_line_length);
            } else if((time_between_every_line*i)%500 == 0) {
                line_to_separate = new Line(i*pixels_in_between_each_line,0,i*pixels_in_between_each_line,half_long_line_length);
            } else {
                line_to_separate = new Line(i*pixels_in_between_each_line,0,i*pixels_in_between_each_line,line_length);
            }
            line_to_separate.setStroke(javafx.scene.paint.Color.BLACK);
            line_to_separate.setStrokeWidth(0.5);
            main_pane.getChildren().add(line_to_separate);
        }
    }
}