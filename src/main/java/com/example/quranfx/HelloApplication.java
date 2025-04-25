package com.example.quranfx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import okhttp3.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.jsoup.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;


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


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth() / 2, Screen.getPrimary().getBounds().getHeight() / 2);
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
        set_the_width_of_play_pause_button(helloController);
        listen_to_slide_clicked(helloController);
        listen_to_full_screen_button(helloController);
        set_the_text_field_formatter_of_milliseconds_end(helloController);
        listen_to_copy_duration(helloController);
        listen_to_millisecond_for_each_ayat_focus_change(helloController);
        listen_to_time_update_of_each_ayat_and_update_it_in_list(helloController);
        listen_to_enter_pressed_time_for_each_ayat(helloController);
        listen_to_genereate_chat_gpt_checkbox(helloController);
        only_allow_digits_and_dash_for_ayat_input(helloController);
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
        set_the_stroke(helloController);
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

    private void call_verses_api(HelloController helloController, int id, String ayat, int page) {
        HttpUrl httpurl = new HttpUrl.Builder()
                .scheme("https")
                .host("api.quran.com")
                .addPathSegment("api")
                .addPathSegment("v4")
                .addPathSegment("verses")
                .addPathSegment("by_chapter")
                .addPathSegment(String.valueOf(id + 1))
                .addQueryParameter("language", "en")
                .addQueryParameter("translations", "131")
                .addQueryParameter("translation_fields", "resource_id,text")
                .addQueryParameter("per_page", "50")
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("fields", "text_uthmani,audio")
                //.addQueryParameter("audio", )
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
            add_all_of_the_verses_to_the_list_after(helloController, ayat, verses_string, page);
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
            System.out.println(e.getMessage());
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
                helloController.choose_surat_screen.setVisible(false);
                helloController.choose_ayat_screen.setVisible(true);
                set_up_second_screen(helloController, helloController.choose_the_surat.getSelectionModel().getSelectedIndex());
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

    private String is_the_ayat_format_correct(String ayat, int id) {
        if (ayat.isEmpty()) {
            return "Empty";
        }
        JsonNode nameNode = return_name_node(chapters_string);
        int verse_count = Integer.parseInt(String.valueOf(nameNode.get("chapters").get(id).get("verses_count")));
        ayat = ayat.replace(" ", "");
        if (ayat.matches("[1-9]+[0-9]*[-]{1}[1-9]+[0-9]*") || ayat.matches("[1-9]+") || ayat.matches("[1-9][0-9]*")) {
            if (StringUtils.countMatches(ayat, "-") == 0) {
                if (Integer.parseInt(ayat) > verse_count) {
                    return "too_many_verses";
                } else {
                    return "ok";
                }
            } else if (StringUtils.countMatches(ayat, "-") == 1) {
                int start_ayat = return_start_ayat(ayat);
                int end_ayat = return_end_ayat(ayat);
                if (end_ayat > verse_count) {
                    return "too_many_verses";
                } else if (end_ayat < start_ayat) {
                    return "second_arguemnt_bigger";
                } else {
                    return "ok";
                }
            } else {
                return "too_many_dashes";
            }
        } else {
            return "incorrect_format";
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

    private void set_up_third_screen(HelloController helloController, String ayat, int id) {
        JsonNode nameNode = return_name_node(chapters_string);
        String surat_name = String.valueOf(nameNode.get("chapters").get(id).get("name_simple"));
        String generating_message;
        int start_ayat = return_start_ayat(ayat);
        int end_ayat = return_end_ayat(ayat);
        if (ayat.contains("-")) {
            ayat = String.valueOf(start_ayat).concat(" - ").concat(String.valueOf(end_ayat));
            generating_message = "Generating ayats ".concat(ayat).concat(" for surat ".concat(surat_name).concat("."));
        } else {
            generating_message = "Generating ayat ".concat(ayat).concat(" for surat ".concat(surat_name).concat("."));
        }
        make_the_generating_text(helloController, generating_message);
        initial_number_of_ayats = end_ayat - start_ayat + 1;
        int start_ayat_section = (int) Math.ceil(start_ayat / 50D);
        int end_ayat_section = (int) Math.ceil(end_ayat / 50D);
        for (int i = start_ayat_section; i <= end_ayat_section; i++) {
            call_verses_api(helloController, id, ayat, i);
        }
        set_number_of_verses_left(helloController, initial_number_of_ayats);
        if (helloController.generate_chat_gpt_images.isSelected()) {

        }
        //run_every_second(helloController);
    }

    private void add_all_of_the_verses_to_the_list_after(HelloController helloController, String ayat, String verses_string, int page) {
        JsonNode nameNode = return_name_node(verses_string);
        ArrayNode arrayNode = (ArrayNode) nameNode.get("verses");
        int start_ayat = return_start_ayat(ayat);
        int end_ayat = return_end_ayat(ayat);
        if (ayat.contains("-")) {
            for (int i = 0; i < arrayNode.size(); i++) {
                int ayat_number = Integer.parseInt(String.valueOf(arrayNode.get(i).get("verse_number")));
                String arabic_ayat = String.valueOf(arrayNode.get(i).get("text_uthmani"));
                if (ayat_number >= start_ayat && ayat_number <= end_ayat) {
                    ArrayNode translations_array_node = (ArrayNode) arrayNode.get(i).get("translations");
                    add_to_array_list_with_verses(String.valueOf(Jsoup.parse(String.valueOf(translations_array_node.get(0).get("text"))).text()), ayat_number, arabic_ayat);
                    if (!helloController.generate_chat_gpt_images.isSelected() && array_list_with_verses.size() == initial_number_of_ayats) {
                        for (int j = 0; j < array_list_with_verses.size(); j++) {
                            BufferedImage image_holder = return_buffer_image_from_file(Base64_image.getInstance().file_path_vertical_place_holder);
                            if (helloController.size_of_image.getValue().equals("9:16")) {
                                image_holder = return_buffer_image_from_file(Base64_image.getInstance().file_path_vertical_place_holder);
                            } else if (helloController.size_of_image.getValue().equals("16:9")) {
                                image_holder = return_buffer_image_from_file(Base64_image.getInstance().file_path_horizontal_place_holder);
                            } else if (helloController.size_of_image.getValue().equals("1:1")) {
                                image_holder = return_buffer_image_from_file(Base64_image.getInstance().file_path_square_place_holder);
                            }
                            if (durations == null || durations.length == 0) {
                                chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), image_holder, Long.MAX_VALUE, new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse())));
                            } else {
                                if (j == 0) {
                                    chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), image_holder, durations[j], new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse())));
                                } else {
                                    chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), image_holder, end_of_the_picture_durations[j], new Ayat_settings(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse())));
                                }
                            }
                            set_up_the_fourth_screen(helloController);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < arrayNode.size(); i++) {
                int ayat_number = Integer.parseInt(String.valueOf(arrayNode.get(i).get("verse_number")));
                String arabic_ayat = String.valueOf(arrayNode.get(i).get("text_uthmani"));
                if (ayat_number == start_ayat) {
                    ArrayNode translations_array_node = (ArrayNode) arrayNode.get(i).get("translations");
                    add_to_array_list_with_verses(String.valueOf(Jsoup.parse(String.valueOf(translations_array_node.get(0).get("text"))).text()), /*start_ayat*/ ayat_number, arabic_ayat);
                    break;
                }
            }
        }
    }

    private int return_start_ayat(String ayat) {
        if (ayat.contains("-")) {
            String[] split_ayat = ayat.replace(" ", "").split("-");
            return Integer.parseInt(split_ayat[0]);
        } else {
            return Integer.parseInt(ayat);
        }
    }

    private int return_end_ayat(String ayat) {
        if (ayat.contains("-")) {
            String[] split_ayat = ayat.replace(" ", "").split("-");
            return Integer.parseInt(split_ayat[1]);
        } else {
            return Integer.parseInt(ayat);
        }
    }

    private void add_time_to_array_list(long time) {
        if (array_list_with_times.size() < number_of_prompts_per_minute) {
            array_list_with_times.add(time);
        } else {
            array_list_with_times.remove(0);
            array_list_with_times.add(time);
        }
    }

    private long get_oldest_chat_gpt_request() {
        if (array_list_with_times.size() >= number_of_prompts_per_minute) {
            return array_list_with_times.get(0);
        } else {
            return 0;
        }
    }

    private void run_every_second(HelloController helloController) {
        Runnable helloRunnable = new Runnable() {
            public void run() {
                if (chat_gpt_processed_ayats == initial_number_of_ayats) {
                    executor.shutdownNow();
                    set_up_the_fourth_screen(helloController);
                } else {
                    if (helloController.generate_chat_gpt_images.isSelected()) {
                        if (System.currentTimeMillis() - get_oldest_chat_gpt_request() >= 65000L) {

                        }
                    } else {

                    }
                    if (/*System.currentTimeMillis() - get_oldest_chat_gpt_request() >= 65000L*/true) {
                        chat_gpt_processed_ayats++;
                        Verse_class verseClass = get_verse(helloController);
                        if (verseClass != null) {
                            if (helloController.generate_chat_gpt_images.isSelected()) {
                                try {
                                    call_the_chatgpt_api(helloController, verseClass.getVerse(), verseClass.getVerse_number());
                                    add_time_to_array_list(System.currentTimeMillis());
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                Image image_holder = Base64_image.getInstance().vertical_place_holder;
                                if (helloController.size_of_image.getValue().equals("9:16")) {
                                    image_holder = Base64_image.getInstance().vertical_place_holder;
                                } else if (helloController.size_of_image.getValue().equals("16:9")) {
                                    image_holder = Base64_image.getInstance().horizontal_place_holder;
                                } else if (helloController.size_of_image.getValue().equals("1:1")) {
                                    image_holder = Base64_image.getInstance().square_place_holder;
                                }
                                //chatgpt_responses.add(new Verse_class_final(verseClass.getVerse(), verseClass.getVerse_number(), image_holder, verseClass.getVerse_number() * 1000, new Ayat_settings(),));
                            }
                        }
                    }
                }
            }
        };
        executor.scheduleAtFixedRate(helloRunnable, 0, 1, TimeUnit.SECONDS);
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
        helloController.enter_the_ayats_wanted.setText("");
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

        helloController.play_sound.setText("Play");

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
        helloController.upload_image_button_for_each_ayat.setText("Upload Image");
        selected_verse = 0;
        sound_path = "";
        array_list_with_times.clear();
        array_list_with_verses.clear();
        chatgpt_responses.clear();

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
                String result = is_the_ayat_format_correct(helloController.enter_the_ayats_wanted.getText(), helloController.choose_the_surat.getSelectionModel().getSelectedIndex());
                if (result.equals("ok") && (!sound_path.isEmpty() || !helloController.list_view_with_the_recitors.getSelectionModel().isEmpty())) {
                    helloController.generating_screen.setVisible(true);
                    helloController.choose_ayat_screen.setVisible(false);
                    get_the_sound_and_concat_them_into_one(helloController);
                    set_up_third_screen(helloController, helloController.enter_the_ayats_wanted.getText(), helloController.choose_the_surat.getSelectionModel().getSelectedIndex());
                } else if (result.equals("incorrect_format")) {
                    show_alert("The format or the number of the ayat you entered is incorrect. Please follow this format: \"15-25\" or \"12\". Verses started at 1");
                } else if (result.equals("too_many_verses")) {
                    show_alert("The verse you entered is greater than the max verse of the surat");
                } else if (result.equals("second_arguemnt_bigger")) {
                    show_alert("The ending ayat is less than the starting ayat, please be sure to make sure that the ending ayat is bigger than the starting ayat");
                } else if (result.equals("Empty")) {
                    show_alert("The ayat section can't be left empty");
                } else if (result.equals("too_many_dashes")) {
                    show_alert("There is more than one dash \"-\" in the ayat section, please only include one.");
                } else if (sound_path.isEmpty() && helloController.list_view_with_the_recitors.getSelectionModel().isEmpty()) {
                    show_alert("Please select a sound before proceeding. You can do so by uploading a sound or by simply selecting a reciter.");
                }
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
        update_the_information_about_the_ayat(helloController, 0);
        //set_the_english_text_of_the_ayat_in_the_image_view(helloController, 0);
        set_up_the_media(helloController);
        set_the_max_of_the_slider_and_set_time_of_last_ayat(helloController);
        set_the_media_player_listener(helloController);
        set_the_first_text_field_of_first_ayat(helloController);
        listen_to_end_of_audio_fourth_screen(helloController);
        listen_to_slider_audio(helloController);
        set_up_the_width_and_height_of_the_image_in_fourth_screen(helloController);
    }

    private void set_the_visibility_of_the_buttons(HelloController helloController, int position) {
        if (position <= 0) {
            helloController.previous_photo_chat_gpt_result.setVisible(false);
        } else {
            helloController.previous_photo_chat_gpt_result.setVisible(true);
        }
        if (position >= chatgpt_responses.size() - 1) {
            helloController.next_photo_chat_gpt_result.setVisible(false);
        } else {
            helloController.next_photo_chat_gpt_result.setVisible(true);
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
            }
        });
    }

    private void previous_photo_click_listen(HelloController helloController) {
        helloController.previous_photo_chat_gpt_result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selected_verse--;
                the_verse_changed(helloController, selected_verse);
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
                    if (helloController.size_of_image.getValue().equals("9:16")) {
                        imageView.setFitHeight(80);
                        imageView.setFitWidth(45);
                        imageView.setImage(item.getThumbnail_vertical());
                    } else if (helloController.size_of_image.getValue().equals("16:9")) {
                        imageView.setFitWidth(80);
                        imageView.setFitHeight(45);
                        imageView.setImage(item.getThumbnail_horizontal());
                    } else if (helloController.size_of_image.getValue().equals("1:1")) {
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
                    }
                    break;
                case RIGHT:
                    if (selected_verse < chatgpt_responses.size() - 1) {
                        selected_verse++;
                        the_verse_changed(helloController, selected_verse);
                    }
                    break;
                default:
                    // Handle other keys or ignore
                    break;
            }
        });
    }

    private void the_verse_changed(HelloController helloController, int selected_verse) {
        long startTime = System.nanoTime();
        set_the_visibility_of_the_buttons(helloController, selected_verse);
        add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
        set_the_image_fourth_screen(helloController, selected_verse);
        set_selected_verse_text(helloController, selected_verse);
        select_the_correct_verse_in_the_list_view(helloController, selected_verse);
        update_the_information_about_the_ayat(helloController, selected_verse);
        update_the_text_field_based_on_previous_values(helloController, selected_verse);
        update_the_name_of_the_image_button_fourth_screen(helloController, selected_verse);
        set_the_english_text_of_the_ayat_in_the_image_view(helloController, selected_verse);
        set_the_ayat_settings(helloController, selected_verse);
        hide_and_show_the_time_text_field_editor(helloController, selected_verse);
    }

    private void update_the_information_about_the_ayat(HelloController helloController, int verse_selected) {
        String english_text = chatgpt_responses.get(verse_selected).getVerse();
        helloController.showing_the_engligh_translation_at_the_right.setText(english_text);
    }

    private void set_up_the_media(HelloController helloController) {
        media = new Media(Paths.get(sound_path).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    private void listen_to_play(HelloController helloController) {
        helloController.play_sound.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (helloController.play_sound.getText().equals("Play")) {
                    if (mediaPlayer.getCurrentTime().toMillis() >= get_duration()) {
                        mediaPlayer.seek(Duration.ZERO);
                    }
                    helloController.play_sound.setText("Pause");
                    mediaPlayer.play();
                } else if (helloController.play_sound.getText().equals("Pause")) {
                    helloController.play_sound.setText("Play");
                    mediaPlayer.pause();
                }
            }
        });
    }

    private void set_the_width_of_play_pause_button(HelloController helloController) {
        Text text = new Text("Pause");
        text.setFont(helloController.play_sound.getFont());
        helloController.play_sound.setPrefWidth(text.getLayoutBounds().getWidth() + 20);
    }

    private void set_the_media_player_listener(HelloController helloController) {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                if (!is_the_slider_being_held_right_now) {
                    helloController.sound_slider_fourth_screen.setValue(newValue.toMillis());
                }
                update_the_duration_time(helloController, newValue.toMillis());
                change_the_image_based_on_audio_fourth_screen(helloController, newValue.toMillis());
            }
        });
    }

    private void set_the_max_of_the_slider_and_set_time_of_last_ayat(HelloController helloController) {
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                helloController.sound_slider_fourth_screen.setMax(get_duration());
                chatgpt_responses.get(chatgpt_responses.size() - 1).setTime_in_milliseconds((long) get_duration());
                set_the_time_total_time(helloController,get_duration());
            }
        });
    }

    private void listen_to_end_of_audio_fourth_screen(HelloController helloController) {
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                helloController.play_sound.setText("Play");
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
        int totalSeconds = (int) TimeUnit.MILLISECONDS.toSeconds((long) total_millis);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String minutes_string;
        String seconds_string;
        if (minutes < 10) {
            minutes_string = "0".concat(String.valueOf(minutes));
        } else {
            minutes_string = String.valueOf(minutes);
        }
        if (seconds < 10) {
            seconds_string = "0".concat(String.valueOf(seconds));
        } else {
            seconds_string = String.valueOf(seconds);
        }
        helloController.duration_of_media.setText(minutes_string.concat(":").concat(seconds_string));
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

    private void only_allow_digits_and_dash_for_ayat_input(HelloController helloController) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0123456789-]*")) {  // Regex to allow only 1-9, space, and dash
                return change;
            }
            return null; // Reject the change
        };

        TextFormatter<String> formatter = new TextFormatter<>(filter);
        helloController.enter_the_ayats_wanted.setTextFormatter(formatter);
    }

    private void set_up_the_width_and_height_of_the_image_in_fourth_screen(HelloController helloController) {
        if (helloController.size_of_image.getValue().equals("9:16")) {
            helloController.chatgpt_image_view.setFitWidth(360);
            helloController.chatgpt_image_view.setFitHeight(640);
        } else if (helloController.size_of_image.getValue().equals("16:9")) {
            helloController.chatgpt_image_view.setFitWidth(640);
            helloController.chatgpt_image_view.setFitHeight(360);
        } else if (helloController.size_of_image.getValue().equals("1:1")) {
            helloController.chatgpt_image_view.setFitWidth(360);
            helloController.chatgpt_image_view.setFitHeight(360);
        }
    }

    private void uplaod_image_button_for_each_ayat_listen(HelloController helloController) {
        helloController.upload_image_button_for_each_ayat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean too_many_images_selected = false;
                FileChooser fileChooser = new FileChooser();
                //Set extension filter
                FileChooser.ExtensionFilter image_filter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpg, *.jpeg, *.heic)", "*.png", "*.PNG", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG", "*.heic", "*.HEIC");
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
                            Image image = new Image(new FileInputStream(files.get(i)));
                            if ((helloController.size_of_image.getValue().equals("1:1") && image.getWidth() == image.getHeight()) || (helloController.size_of_image.getValue().equals("9:16") && image.getWidth() * 16D == image.getHeight() * 9D) || (helloController.size_of_image.getValue().equals("16:9") && image.getWidth() * 9D == image.getHeight() * 16D)) {
                                chatgpt_responses.get(selected_verse + i).setBase_64_image(image_to_buffered_image(image));
                            } else {
                                if (helloController.size_of_image.getValue().equals("9:16")) {
                                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
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
                                } else if (helloController.size_of_image.getValue().equals("16:9")) {

                                } else if (helloController.size_of_image.getValue().equals("1:1")) {

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
        if(chatgpt_responses.get(selected_verse).isImage_edited()){
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
                FileChooser.ExtensionFilter extFilterMP3 = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
                FileChooser.ExtensionFilter extFilterWAV = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
                FileChooser.ExtensionFilter extFilterFLAC = new FileChooser.ExtensionFilter("FLAC files (*.flac)", "*.flac");
                fileChooser.getExtensionFilters().addAll(extFilterMP3, extFilterWAV, extFilterFLAC);
                File file = fileChooser.showOpenDialog(null);
                if (file != null) {
                    sound_path = file.getAbsolutePath();
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
        int captureWidth = 0;
        int captureHeight = 0;
        if (helloController.size_of_image.getValue().equals("9:16")) {
            captureWidth = 1024;
            captureHeight = 1820;
        } else if (helloController.size_of_image.getValue().equals("16:9")) {
            captureWidth = 1820;
            captureHeight = 1024;
        } else if (helloController.size_of_image.getValue().equals("1:1")) {
            captureWidth = 1024;
            captureHeight = 1024;
        }
        int frameRate = 30;

        int audioBitrate = 1024000; // Standard quality
        int sampleRate = 48000;    // CD quality audio
        int channels = 2;          // Stereo

        // Configure the FFmpegFrameRecorder
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(videoFileName, captureWidth, captureHeight, channels);
        recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
        recorder.setAudioCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_AAC);
        recorder.setFormat("mp4");
        recorder.setFrameRate(frameRate);
        recorder.setSampleRate(sampleRate);
        recorder.setVideoOption("preset", "veryslow");
        recorder.setVideoOption("crf", "10");
        recorder.setVideoOption("tune", "stillimage");
        recorder.setVideoBitrate(20000000);
        //recorder.setVideoOption("profile", "high");
        recorder.setVideoOption("level", "5.1");
        recorder.setAudioBitrate(audioBitrate);
        recorder.setAudioChannels(channels);
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
                //StackPane.setAlignment(helloController.verse_input_field, Pos.TOP_CENTER);
            }
        });
        helloController.position_of_english_text_button_center.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected(helloController);
                helloController.position_of_english_text_button_center.setSelected(true);
                //StackPane.setAlignment(helloController.verse_input_field, Pos.CENTER);
            }
        });
        helloController.position_of_english_text_button_bottom_center.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                set_all_of_the_english_text_position_buttons_not_selected(helloController);
                helloController.position_of_english_text_button_bottom_center.setSelected(true);
                //StackPane.setAlignment(helloController.verse_input_field, Pos.BOTTOM_CENTER);
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

    private void set_the_stroke(HelloController helloController) {
        Text text = new Text(helloController.verse_input_field.getText());
        text.setStroke(javafx.scene.paint.Color.BLACK);
        text.setStrokeWidth(1);
        text.setFill(javafx.scene.paint.Color.WHITE); // Set text color
        helloController.verse_input_field.setGraphic(text);
    }

    private void add_the_text_to_the_photo(HelloController helloController, Ayat_settings ayatSettings, int selected_verse) {
        BufferedImage bufferedImage = chatgpt_responses.get(selected_verse).getBase_64_image();
        float brightnessFactor = (float) (ayatSettings.getBrightness_of_image() / 100.0); // e.g., 0.6 for 60%

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
                strings_to_be_drawn = wrapText(chatgpt_responses.get(selected_verse).getVerse(), metrics, bufferedImage.getWidth());
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
                strings_to_be_drawn = wrapText(remove_qoutes_from_arabic_text(chatgpt_responses.get(selected_verse).getArabic_verse()), metrics, bufferedImage.getWidth());
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
                strings_to_be_drawn = wrapText(remove_qoutes_from_arabic_text(surat_name_selected), metrics, bufferedImage.getWidth());
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

    public ArrayList<String> wrapText(String text, FontMetrics metrics, int maxWidth) {
        String[] text_split = text.split(" ");
        String currentLine = "";
        ArrayList<String> new_strings = new ArrayList<>();
        for (int i = 0; i < text_split.length; i++) {
            int width = metrics.stringWidth(currentLine.concat(text_split[i]).concat(" ")) + 15;
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
            if(!local.get("audio_url_bit_rate_128").asText().contains("https") && !local.get("audio_url_bit_rate_64").asText().contains("https")&& !local.get("audio_url_bit_rate_32_").asText().contains("https")){
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
            OkHttpClient client = new OkHttpClient();
            String ayat = helloController.enter_the_ayats_wanted.getText();
            int start_ayat = return_start_ayat(ayat);
            int end_ayat = return_end_ayat(ayat);
            int number_of_ayats = end_ayat - start_ayat + 1;
            String[] mp3Urls = new String[number_of_ayats];
            durations = new Long[number_of_ayats];
            end_of_the_picture_durations = new Long[number_of_ayats];

            String base_url = "";
            if (!helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_128_bits().isEmpty()) {
                base_url = helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_128_bits();
            } else if (!helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_64_bits().isEmpty()) {
                base_url = helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_64_bits();
            } else if (!helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_32_bits().isEmpty()) {
                base_url = helloController.list_view_with_the_recitors.getSelectionModel().getSelectedItems().get(0).getLink_for_32_bits();
            }
            String surat_number = String.format("%03d", helloController.choose_the_surat.getSelectionModel().getSelectedIndex() + 1);
            int counter = 0;
            for (int i = start_ayat; i <= end_ayat; i++) {
                String ayat_number = String.format("%03d", i);
                mp3Urls[counter] = base_url.concat(surat_number).concat(ayat_number).concat(".mp3");
                counter++;
            }
            for (int i = 0; i < number_of_ayats; i++) {
                Request request = new Request.Builder().url(mp3Urls[i]).build();
                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        if (!did_i_ever_fail_a_recitation) {
                            show_alert("Failed to parse a recitation.");
                            did_i_ever_fail_a_recitation = true;
                        }
                        continue;
                    }
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    try (InputStream input = response.body().byteStream()) {
                        byte[] temp = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = input.read(temp)) != -1) {
                            buffer.write(temp, 0, bytesRead);
                        }
                    }
                    byte[] mp3Bytes = buffer.toByteArray();
                    File tempFile = new File("temp/sound", String.format("%03d.mp3", i));
                    tempFile.deleteOnExit();
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        fos.write(mp3Bytes);
                    }
                    durations[i] = getDurationWithFFmpeg(tempFile);
                    if (i == 0) {
                        end_of_the_picture_durations[i] = durations[i];
                    } else {
                        end_of_the_picture_durations[i] = durations[i] + end_of_the_picture_durations[i - 1];
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File listFile = new File("temp/sound/","list.txt");
            listFile.deleteOnExit();
            try (PrintWriter writer = new PrintWriter(listFile)) {
                for (int i = 0; i < number_of_ayats; i++) {
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
                        "-c", "copy",
                        "temp/sound/combined.mp3"
                );
                pb.redirectErrorStream(true); // Combine stderr with stdout
                Process process = pb.start();
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    show_alert("Audio encoding failed. FFMPEG");
                } else {
                    File out_put_file = new File("temp/sound/combined.mp3");
                    out_put_file.deleteOnExit();
                    sound_path = out_put_file.getAbsolutePath();
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Image resizeImage(Image input, double targetWidth, double targetHeight) {
        WritableImage output = new WritableImage((int) targetWidth, (int) targetHeight);
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        double scaleX = input.getWidth() / targetWidth;
        double scaleY = input.getHeight() / targetHeight;

        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                int pixelX = (int) (x * scaleX);
                int pixelY = (int) (y * scaleY);
                writer.setArgb(x, y, reader.getArgb(pixelX, pixelY));
            }
        }
        return output;
    }

    private void clear_temp_directory() {
        File base_images_file = new File("temp/images/base/");
        File edited_images_file = new File("temp/images/edited/");
        File sounds_files = new File("temp/sound/");
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

    private void set_the_time_total_time(HelloController helloController,double time){
        int totalSeconds = (int) TimeUnit.MILLISECONDS.toSeconds((long) time);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        String minutes_string = String.format("%02d",minutes);
        String seconds_string = String.format("%02d",seconds);
        helloController.total_duration_of_media.setText(minutes_string.concat(":").concat(seconds_string));
    }

    private void listen_to_set_image_to_all(HelloController helloController){
        helloController.set_image_to_all.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                BufferedImage current_image = chatgpt_responses.get(selected_verse).getBase_64_image();
                for(int i = 0;i<chatgpt_responses.size();i++){
                    if(i!=selected_verse){
                        chatgpt_responses.get(i).setBase_64_image(current_image);
                    }
                }
                for(int i = 0;i<chatgpt_responses.size();i++){
                    if(i!=selected_verse){
                        add_the_text_to_the_photo(helloController, chatgpt_responses.get(i).getAyatSettings(), i);
                    }
                }
                helloController.list_view_with_the_verses_preview.refresh();
            }
        });
    }

    private void make_temp_dir(){
        File directory = new File("temp/");
        File images = new File("temp/images");
        File sound = new File("temp/sound");
        File images_base = new File("temp/images/base");
        File images_edited = new File("temp/images/edited");
        if (!directory.exists()){
            directory.mkdirs();
        }
        if (!images.exists()){
            images.mkdirs();
        }
        if (!sound.exists()){
            sound.mkdirs();
        }
        if (!images_base.exists()){
            images_base.mkdirs();
        }
        if (!images_edited.exists()){
            images_edited.mkdirs();
        }
    }

    private BufferedImage return_buffer_image_from_file(String file_path){
        BufferedImage image = null;
        try {
            File input = new File(file_path);
            if(input.exists()){
                image = ImageIO.read(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private Image buffer_image_to_image(BufferedImage bufferedImage){
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

    private BufferedImage image_to_buffered_image(Image image){
        return SwingFXUtils.fromFXImage(image, null);
    }

    private void check_if_scroll_bar_is_visible(){

    }
}