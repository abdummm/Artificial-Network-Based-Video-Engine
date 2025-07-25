package com.example.quranfx;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Duration;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import okhttp3.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.jsoup.*;

import javax.imageio.ImageIO;


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
    private HashMap<String, Media_pool> hashMap_with_media_pool_items = new HashMap<>();

    private final static int image_view_in_tile_pane_width = 90;
    private final static int image_view_in_tile_pane_height = 160;
    private final static String help_email = "abdomakesappshelp@gmail.com";
    private final static double blurry_circle_raduis = 30D;
    private final static double play_pause_button_size = 20D;
    ;
    //private final static double circle_button_radius = 20D;
    private final static int how_long_does_it_take_for_tool_tip_to_show_up = 500;

    private long lastKnownMediaTime = 0;
    private long lastKnownSystemTime = 0;
    private AnimationTimer timer = null;
    private Last_shown_Image id_of_the_last_image = new Last_shown_Image("", Type_of_Image.FULL_QUALITY);


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Screen.getPrimary().getBounds().getWidth() * 0.55, Screen.getPrimary().getBounds().getHeight() * 0.55);
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
        listen_to_play(helloController);
        //set_the_width_of_play_pause_button(helloController);
        listen_to_full_screen_button(helloController);
        listen_to_genereate_chat_gpt_checkbox(helloController);
        upload_sound_listen(helloController);
        listen_to_create_video_button(helloController);
        listen_to_cancel_button_third_screen(helloController);
        get_all_of_the_recitors(helloController);
        listen_to_the_recitor_list_view_click(helloController);
        make_temp_dir();
        clear_temp_directory();
        set_the_icons(helloController);
        listen_to_enter_click_on_select_surat_listview(helloController);
        listen_to_list_click_list_view(helloController);
        listen_to_upload_media_button(helloController);
        set_the_width_of_the_left_and_right(helloController);
        listen_to_top_and_bottom_pane_size_change_fourth_screen(helloController, scene);
        listen_to_whole_screen_resize(scene, helloController);
        //listen_to_tile_pane_resized(helloController);
        listen_to_tile_pane_size_change(helloController);
        set_cache_hints_of_scroll_pane_time_line(helloController);
        listen_to_give_feed_back_button(helloController);
        black_out_the_image_view_at_the_start(helloController);
        listen_to_chatgpt_image_view_on_mouse_enetered_and_left(helloController);
        play_the_sound_on_chatgpt_image_clicked(helloController);
        add_tool_tip_to_create_button(helloController);
        add_cancel_video_tool_tip(helloController);
        set_up_glossy_pause_button(helloController);
        make_the_blurry_chatgpt_image_always_have_the_same_size_as_non_blurry(helloController);
        listen_to_blurry_image_view_size_change_and_center_the_clip(helloController);
        make_play_button_circle(helloController);
        add_tool_tip_to_next_verse(helloController);
        add_tool_tip_to_previous_verse(helloController);
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
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
                add_to_array_list_with_verses(Jsoup.parse(String.valueOf(translations_array_node.get(0).get("text"))).text(), ayat_number, arabic_ayat);
                if (array_list_with_verses.size() == initial_number_of_ayats) {
                    for (int j = 0; j < array_list_with_verses.size(); j++) {
                        if (durations == null || durations.length == 0) {
                            chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), j * TimeUnit.SECONDS.toNanos(1), TimeUnit.SECONDS.toNanos(1)));
                        } else {
                            chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), end_of_the_picture_durations[j], durations[j]));
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
                add_to_array_list_with_verses(Jsoup.parse(String.valueOf(translations_array_node.get(0).get("text"))).text(), ayat_number, arabic_ayat);
                for (int j = 0; j < array_list_with_verses.size(); j++) {
                    if (durations == null || durations.length == 0) {
                        chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), 0, TimeUnit.SECONDS.toNanos(1)));
                    } else {
                        chatgpt_responses.add(new Verse_class_final(capatilize_first_letter(update_the_verse_text(array_list_with_verses.get(j).getVerse())), array_list_with_verses.get(j).getVerse_number(), remove_qoutes_from_arabic_text(array_list_with_verses.get(j).getArabic_verse()), 0, durations[0]));
                    }
                }
                set_up_the_fourth_screen(helloController);
                break;
            }
        }
    }


    private void add_to_array_list_with_verses(String verse, int ayat_number, String arabic_verse) {
        Verse_class verseClass = new Verse_class(surat_name_selected, verse, ayat_number, arabic_verse);
        array_list_with_verses.add(verseClass);
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


        set_the_play_pause_button(helloController, "play");

        //helloController.sound_slider_fourth_screen.setValue(0);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (durations != null) {
            Arrays.fill(durations, null);
        }
        if (end_of_the_picture_durations != null) {
            Arrays.fill(end_of_the_picture_durations, null);
        }
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
                //copy_the_images(helloController, get_the_right_basic_image_aspect_ratio(return_the_aspect_ratio_as_an_object(helloController)));
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
        //helloController.show_the_result_screen.setVisible(true);
        //helloController.pane_holding_the_fourth_screen.setVisible(true);
        helloController.show_the_result_screen_stack_pane.setVisible(true);
        set_the_image_fourth_screen(helloController, 0);
        set_the_visibility_of_the_buttons(helloController, 0);
        set_selected_verse_text(helloController, 0);
        //set_the_english_text_of_the_ayat_in_the_image_view(helloController, 0);
        set_up_the_media(helloController);
        mediaPlayer_status_changed(helloController);
        set_the_media_player_listener(helloController);
        listen_to_end_of_audio_fourth_screen(helloController);
        set_up_the_width_and_height_of_the_image_in_fourth_screen(helloController);
        animation_timer(helloController);
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

    }

    private void next_photo_click_listen(HelloController helloController) {
        helloController.next_photo_chat_gpt_result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selected_verse++;
                the_verse_changed(helloController, selected_verse);
                scroll_to_specific_verse_time(helloController);
                set_the_chatgpt_image_view(helloController, return_the_image_from_time(helloController.time_line_pane, chatgpt_responses.get(selected_verse).getStart_millisecond()), Type_of_Image.FULL_QUALITY);
            }
        });
    }

    private void previous_photo_click_listen(HelloController helloController) {
        helloController.previous_photo_chat_gpt_result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selected_verse--;
                the_verse_changed(helloController, selected_verse);
                scroll_to_specific_verse_time(helloController);
                set_the_chatgpt_image_view(helloController, return_the_image_from_time(helloController.time_line_pane, chatgpt_responses.get(selected_verse).getStart_millisecond()), Type_of_Image.FULL_QUALITY);
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

    private void the_verse_changed(HelloController helloController, int selected_verse) {
        set_the_visibility_of_the_buttons(helloController, selected_verse);
        //add_the_text_to_the_photo(helloController, chatgpt_responses.get(selected_verse).getAyatSettings(), selected_verse);
        set_the_image_fourth_screen(helloController, selected_verse);
        set_selected_verse_text(helloController, selected_verse);
    }

    private void set_up_the_media(HelloController helloController) {
        media = new Media(Paths.get(sound_path).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(1);
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
            play_the_media_player(helloController);
        } else if (helloController.play_sound.getProperties().get("type").equals("pause")) {
            pause_the_media_player(helloController);
        }
    }

    private void set_the_media_player_listener(HelloController helloController) {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                change_the_image_based_on_audio_fourth_screen(helloController, newValue.toMillis() + 10);
                lastKnownMediaTime = TimeUnit.MILLISECONDS.toNanos((long) newValue.toMillis());
                lastKnownSystemTime = System.currentTimeMillis();
            }
        });
    }

    private void media_is_ready(HelloController helloController) {
//        helloController.sound_slider_fourth_screen.setMax(get_duration());
        if (!did_this_play_already) {
            //start_and_unstart_the_media_player(0);
        }
        did_this_play_already = true;
        create_the_time_line(helloController);
    }

    private void listen_to_end_of_audio_fourth_screen(HelloController helloController) {
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                selected_verse = chatgpt_responses.size() - 1;
                set_the_play_pause_button(helloController, "play");
//                helloController.sound_slider_fourth_screen.setValue(helloController.sound_slider_fourth_screen.getMax());
                timer.stop();
                update_the_time_line_indicator(helloController.time_line_pane, get_duration());
                //stop_and_start_the_media_again();
            }
        });
    }

    private void listen_to_full_screen_button(HelloController helloController) {
        helloController.full_screen_button_fourth_screen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            }
        });
    }

    private void change_the_image_based_on_audio_fourth_screen(HelloController helloController, Double time_of_audio_millis) {

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

    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        else return "";
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
                        audio_frequncy_of_the_sound = get_frequency_of_audio(file.getAbsolutePath());
                        sound_path = convert_mp3_to_wav(file, "converted.wav").getAbsolutePath();
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
        /*String videoFileName = "/Users/abdelrahmanabdelkader/Downloads/output.mp4";
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
        }*/
    }

    /*private void add_the_text_to_the_photo(HelloController helloController, Ayat_settings ayatSettings, int selected_verse) {
        BufferedImage bufferedImage = chatgpt_responses.get(selected_verse).getBase_64_image();
        float brightnessFactor = (float) (ayatSettings.getBrightness_of_image() / 100.0);
        RescaleOp rescaleOp = new RescaleOp(brightnessFactor, 0, null);
        rescaleOp.filter(bufferedImage, bufferedImage);
        Graphics2D g = bufferedImage.createGraphics();
        *//*{
            double difference = 100 - ayatSettings.getBrightness_of_image();
            double real_difference = (difference / 100) * 255;
            Color dimColor = new Color(0, 0, 0, (int) real_difference);
            g.setColor(dimColor);
            g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        }*//*
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
    }*/

    private ArrayList<String> wrapText(String text, FontMetrics metrics, int maxWidth, int margin_left_and_right) {
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

    private String remove_qoutes_from_arabic_text(String arabic_verse) {
        arabic_verse = arabic_verse.replaceAll("\"", "");
        return arabic_verse;
    }


    private void listen_to_cancel_button_third_screen(HelloController helloController) {
        helloController.cancel_video.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //helloController.show_the_result_screen.setVisible(false);
                //helloController.pane_holding_the_fourth_screen.setVisible(false);
                helloController.show_the_result_screen_stack_pane.setVisible(false);
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

    private long get_duration() {
        return TimeUnit.MILLISECONDS.toNanos((long) media.getDuration().toMillis());
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
                                File new_wav_file = convert_mp3_to_wav(tempFile, String.format("%03d.wav", verse_number));
                                new_wav_file.deleteOnExit();
                                durations[verse_number - start_ayat] = getDurationWithFFmpeg(new_wav_file);
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
            File tempFile = new File("temp/sound", String.format("%03d.wav", start_ayat));
            audio_frequncy_of_the_sound = get_frequency_of_audio(tempFile.getAbsolutePath());
            int get_the_number_of_audio_channels_local = set_the_number_of_audio_channels(getNumberOfChannels(tempFile.getAbsolutePath()));
            end_of_the_picture_durations[0] = 0L;
            if (number_of_ayats > 1) {
                for (int i = 1; i < number_of_ayats; i++) {
                    end_of_the_picture_durations[i] = durations[i - 1] + end_of_the_picture_durations[i - 1];
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

    private long getDurationWithFFmpeg(File wavFile) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "ffprobe", "-v", "error",
                    "-show_entries", "stream=duration",
                    "-of", "default=noprint_wrappers=1:nokey=1",
                    wavFile.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                if (line != null) {
                    double seconds = Double.parseDouble(line.trim());
                    return (long) (seconds * 1000000000L); // convert to milliseconds
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to get duration: " + e.getMessage());
        }
        return 0;
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

    private void scroll_to_specific_verse_time(HelloController helloController) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) helloController.time_line_pane.getUserData();
        long time_in_milliseconds = chatgpt_responses.get(selected_verse).getStart_millisecond();
        update_the_time_line_indicator(helloController.time_line_pane, time_in_milliseconds);
        force_the_time_line_indicator_to_be_at_the_middle(helloController.scroll_pane_hosting_the_time_line, time_line_pane_data.getPolygon().getLayoutX());
        mediaPlayer.seek(Duration.millis(time_in_milliseconds));
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

    /*private void copy_the_images(HelloController helloController, BufferedImage image_to_write) {
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
    }*/

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

    private File convert_mp3_to_wav(File input_file, String output_name) {
        String output_file_path = "temp/sound/".concat(output_name);
        int audio_frequncy_of_the_sound = get_frequency_of_audio(input_file.getAbsolutePath());
        int number_of_audio_channels_local = set_the_number_of_audio_channels(getNumberOfChannels(input_file.getAbsolutePath()));
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", input_file.getAbsolutePath(),              // <-- the path to the one MP3 file
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
        return new File(output_file_path);
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
        helloController.next_photo_chat_gpt_result.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        helloController.next_photo_chat_gpt_result.setAlignment(Pos.CENTER);

        helloController.previous_photo_chat_gpt_result.setGraphic(return_region_for_svg(get_the_svg_path("arrow_back_ios"), 25D));
        helloController.previous_photo_chat_gpt_result.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        helloController.previous_photo_chat_gpt_result.setAlignment(Pos.CENTER);

        helloController.full_screen_button_fourth_screen.setGraphic(return_region_for_svg(get_the_svg_path("fullscreen"), 25D));
        helloController.full_screen_button_fourth_screen.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        helloController.full_screen_button_fourth_screen.setAlignment(Pos.CENTER);

        helloController.cancel_video.setGraphic(return_region_for_svg(get_the_svg_path("arrow_back_with_line"), 25D));
        helloController.cancel_video.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        helloController.cancel_video.setAlignment(Pos.CENTER);

        helloController.create_video_final.setGraphic(return_region_for_svg(get_the_svg_path("rocket_launch"), 25D));
        helloController.create_video_final.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        helloController.create_video_final.setAlignment(Pos.CENTER);

        set_the_play_pause_button(helloController, "play");
        helloController.play_sound.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        helloController.play_sound.setAlignment(Pos.CENTER);
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
        region.setMinSize(width_and_height[0], width_and_height[1]);
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
                /*ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
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
                executor.shutdown();*/
                upload_media_has_been_clicked(helloController);
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

    private void add_the_images_to_the_media_pool_in_the_back_ground(HelloController helloController, List<File> files) {
        if (files != null && !files.isEmpty()) {
            helloController.progress_indicator_media_pool.setVisible(true);
            helloController.scroll_pane_hosting_tile_pane_media_pool.setDisable(true);
            helloController.upload_media_text.setDisable(true);
            List<Media_pool> arrayList_with_media_pool = new ArrayList<>();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    for (int i = 0; i < files.size(); i++) {
                        File image_file = files.get(i);
                        boolean did_the_image_get_down_scaled = false;
                        String fileName_lower_case = image_file.getName().toLowerCase();
                        Image image;
                        if (fileName_lower_case.endsWith("heic")) {
                            File new_jpg_file = new File("temp/converted images/".concat(UUID.randomUUID().toString()).concat(".jpg"));
                            new_jpg_file.deleteOnExit();
                            convertHeicToJpg(image_file, new_jpg_file);
                            image = new Image(new FileInputStream(new_jpg_file));
                        } else if (fileName_lower_case.endsWith("png")) {
                            File new_jpg_file = new File("temp/converted images/".concat(UUID.randomUUID().toString()).concat(".jpg"));
                            new_jpg_file.deleteOnExit();
                            convert_png_to_jpg(image_file, new_jpg_file);
                            image = new Image(new FileInputStream(new_jpg_file));
                        } else {
                            image = new Image(new FileInputStream(image_file));
                        }
                        String image_id = UUID.randomUUID().toString();
                        BufferedImage bufferedImage = image_to_buffered_image(image);
                        int orientation = getExifOrientation(image_file);
                        if (orientation == 3 || orientation == 6 || orientation == 8) {
                            bufferedImage = return_the_rotated_image(bufferedImage, orientation);
                        }
                        Pic_aspect_ratio picAspectRatio = return_the_aspect_ratio_as_an_object(helloController);
                        Media_pool mediaPool_item;
                        bufferedImage = return_argb_buffered_image(bufferedImage);
                        if (is_this_the_correct_ratio(picAspectRatio, bufferedImage)) {
                            write_the_raw_file(bufferedImage, "temp/images/base", image_id);
                            if (do_i_need_to_down_scale(bufferedImage, picAspectRatio)) {
                                did_the_image_get_down_scaled = true;
                                BufferedImage down_sized_buffed_image = return_argb_buffered_image(return_resized_downscale_buffer_image(bufferedImage, picAspectRatio));
                                write_the_raw_file(down_sized_buffed_image, "temp/images/scaled", image_id);
                            }
                            mediaPool_item = new Media_pool(image_id, create_a_thumbnail(bufferedImage, picAspectRatio), image_file.getName(), did_the_image_get_down_scaled, bufferedImage.getWidth(), bufferedImage.getHeight());
                        } else {
                            BufferedImage filled_with_black = fill_the_back_ground_with_color(bufferedImage, new Color(0, 0, 0, 255));
                            BufferedImage filled_transparent = fill_the_back_ground_with_color(bufferedImage, new Color(0, 0, 0, 0));
                            write_the_raw_file(filled_with_black, "temp/images/base", image_id);
                            if (do_i_need_to_down_scale(filled_with_black, picAspectRatio)) {
                                did_the_image_get_down_scaled = true;
                                BufferedImage down_sized_buffed_image = return_argb_buffered_image(return_resized_downscale_buffer_image(filled_with_black, picAspectRatio));
                                write_the_raw_file(down_sized_buffed_image, "temp/images/scaled", image_id);
                            }
                            mediaPool_item = new Media_pool(image_id, create_a_thumbnail(filled_transparent, picAspectRatio), create_a_thumbnail(filled_with_black, picAspectRatio), image_file.getName(), did_the_image_get_down_scaled, filled_with_black.getWidth(), filled_with_black.getHeight());
                        }
                        arrayList_with_media_pool.add(mediaPool_item);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < arrayList_with_media_pool.size(); i++) {
                                add_image_to_tile_pane(helloController, arrayList_with_media_pool.get(i));
                                hashMap_with_media_pool_items.put(arrayList_with_media_pool.get(i).getId(), arrayList_with_media_pool.get(i));
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
        File file = new File(file_path.concat("/").concat(name).concat(".raw"));
        file.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(file)) {
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
        //stackPane.setStyle("-fx-cursor: OPEN_HAND;");
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);
        ImageView imageView = new ImageView(mediaPool.getThumbnail());
        imageView.setFitHeight(image_view_in_tile_pane_height);
        imageView.setFitWidth(image_view_in_tile_pane_width);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCursor(Cursor.OPEN_HAND);
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
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    Time_line_pane_data time_line_pane_data = (Time_line_pane_data) helloController.time_line_pane.getUserData();
                    Rectangle created_rectangle = create_and_return_time_line_rectangle(helloController.time_line_pane, nanoseconds_to_pixels(time_line_pane_data, TimeUnit.SECONDS.toNanos(1)));
                    created_rectangle.setVisible(false);
                    Shape_object_time_line shapeObjectTimeLine = new Shape_object_time_line(0, nanoseconds_to_pixels(time_line_pane_data, TimeUnit.SECONDS.toNanos(1)), created_rectangle, mediaPool.getId());
                    set_up_the_image_rectangle(created_rectangle, mediaPool.getThumbnail(), helloController.time_line_pane);
                    configure_the_image_rectangle(shapeObjectTimeLine, helloController, helloController.time_line_pane, mediaPool.getThumbnail());
                    imageView.setCursor(Cursor.CLOSED_HAND);
                    double real_x_pos = mouseEvent.getSceneX() - mouseEvent.getX();
                    double real_y_pos = mouseEvent.getSceneY() - mouseEvent.getY();
                    ImageView ghost = new ImageView(imageView.getImage());
                    ghost.setFitWidth(image_view_in_tile_pane_width);
                    ghost.setFitHeight(image_view_in_tile_pane_height);
                    ghost.setOpacity(0.75);
                    ghost.setLayoutX(real_x_pos);
                    ghost.setLayoutY(real_y_pos);
                    Media_pool_item_dragged media_pool_item_dragged = new Media_pool_item_dragged(ghost, mouseEvent.getSceneX(), mouseEvent.getSceneY(), mediaPool.getId(), shapeObjectTimeLine, return_all_of_the_image_timings_sorted(time_line_pane_data.getTree_set_containing_all_of_the_items()));
                    helloController.pane_holding_the_fourth_screen.getChildren().add(ghost);
                    imageView.setUserData(media_pool_item_dragged);
                }
            }
        });
        imageView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    Media_pool_item_dragged media_pool_item_dragged = (Media_pool_item_dragged) imageView.getUserData();
                    if (media_pool_item_dragged != null) {
                        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) helloController.time_line_pane.getUserData();
                        ImageView ghost = media_pool_item_dragged.getImageView();
                        double old_x_pos = media_pool_item_dragged.getX_pos();
                        double old_y_pos = media_pool_item_dragged.getY_pos();
                        double x_pos_local = helloController.time_line_pane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY()).getX();
                        double image_width_in_time_line = nanoseconds_to_pixels(time_line_pane_data, TimeUnit.SECONDS.toNanos(1));
                        Shape_object_time_line shapeObjectTimeLine = media_pool_item_dragged.getShapeObjectTimeLine();
                        if (!helloController.pane_holding_the_fourth_screen.getChildren().contains(ghost)) {
                            helloController.pane_holding_the_fourth_screen.getChildren().add(ghost);
                        }
                        ghost.setTranslateX(mouseEvent.getSceneX() - old_x_pos);
                        ghost.setTranslateY(mouseEvent.getSceneY() - old_y_pos);
                        if (media_pool_item_dragged.isHas_this_been_dragged() || am_i_in_time_line_boundries(helloController, mouseEvent.getSceneX(), mouseEvent.getSceneY(), media_pool_item_dragged)) {
                            if (!shapeObjectTimeLine.getShape().isVisible()) {
                                shapeObjectTimeLine.getShape().setVisible(true);
                            }
                            double start = move_the_rectangle(helloController.time_line_pane, (Rectangle) shapeObjectTimeLine.getShape(), x_pos_local);
                            shapeObjectTimeLine.setStart(start);
                            shapeObjectTimeLine.setEnd(start + ((Rectangle) shapeObjectTimeLine.getShape()).getWidth());
                        }
                        double min_x_pos_local;
                        double max_x_pos_local;
                        if (x_pos_local - image_width_in_time_line / 2 >= time_line_pane_data.getTime_line_base_line() && x_pos_local + image_width_in_time_line / 2 <= time_line_pane_data.getTime_line_end_base_line()) {
                            min_x_pos_local = x_pos_local - image_width_in_time_line / 2;
                            max_x_pos_local = x_pos_local + image_width_in_time_line / 2;
                        } else {
                            if (x_pos_local - image_width_in_time_line / 2 < time_line_pane_data.getTime_line_base_line()) {
                                min_x_pos_local = time_line_pane_data.getTime_line_base_line();
                                max_x_pos_local = time_line_pane_data.getTime_line_base_line() + image_width_in_time_line;
                            } else {
                                max_x_pos_local = time_line_pane_data.getTime_line_end_base_line();
                                min_x_pos_local = time_line_pane_data.getTime_line_end_base_line() - image_width_in_time_line;
                            }
                        }
                        boolean should_i_be_visible = am_i_in_time_line_boundries(helloController, mouseEvent.getSceneX(), mouseEvent.getSceneY(), media_pool_item_dragged) && !is_there_is_a_collosion_2(time_line_pane_data.getTree_set_containing_all_of_the_items(), min_x_pos_local, max_x_pos_local);
                        if (should_i_be_visible) {
                            set_the_opacity_of_the_rectangle_in_time_line_pane((Rectangle) shapeObjectTimeLine.getShape(), 1);
                            double polygon_x_pos = return_polygon_middle_position(time_line_pane_data);
                            if (polygon_x_pos >= min_x_pos_local && polygon_x_pos <= max_x_pos_local) {
                                set_the_chatgpt_image_view(helloController, media_pool_item_dragged.getImage_key_uuid(), Type_of_Image.THUMBNAIL_QUALITY);
                                media_pool_item_dragged.setDid_this_change_the_image(true);
                            } else if (media_pool_item_dragged.isDid_this_change_the_image()) {
                                set_the_chatgpt_image_view(helloController, "black", Type_of_Image.FULL_QUALITY);
                            }
                        } else {
                            set_the_opacity_of_the_rectangle_in_time_line_pane((Rectangle) shapeObjectTimeLine.getShape(), 0.4D);
                            if (media_pool_item_dragged.isDid_this_change_the_image()) {
                                set_the_chatgpt_image_view(helloController, "black", Type_of_Image.FULL_QUALITY);
                            }
                        }
                    }
                }
            }
        });
        imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imageView.setCursor(Cursor.OPEN_HAND);
                Media_pool_item_dragged media_pool_item_dragged = (Media_pool_item_dragged) imageView.getUserData();
                if (media_pool_item_dragged != null) {
                    Time_line_pane_data time_line_pane_data = (Time_line_pane_data) helloController.time_line_pane.getUserData();
                    ImageView ghost = media_pool_item_dragged.getImageView();
                    helloController.pane_holding_the_fourth_screen.getChildren().remove(ghost);
                    double x_pos_local = helloController.time_line_pane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY()).getX();
                    double image_width_in_time_line = nanoseconds_to_pixels(time_line_pane_data, TimeUnit.SECONDS.toNanos(1));
                    double min_x_pos_local;
                    double max_x_pos_local;
                    if (x_pos_local - image_width_in_time_line / 2 >= time_line_pane_data.getTime_line_base_line() && x_pos_local + image_width_in_time_line / 2 <= time_line_pane_data.getTime_line_end_base_line()) {
                        min_x_pos_local = x_pos_local - image_width_in_time_line / 2;
                        max_x_pos_local = x_pos_local + image_width_in_time_line / 2;
                    } else {
                        if (x_pos_local - image_width_in_time_line / 2 < time_line_pane_data.getTime_line_base_line()) {
                            min_x_pos_local = time_line_pane_data.getTime_line_base_line();
                            max_x_pos_local = time_line_pane_data.getTime_line_base_line() + image_width_in_time_line;
                        } else {
                            max_x_pos_local = time_line_pane_data.getTime_line_end_base_line();
                            min_x_pos_local = time_line_pane_data.getTime_line_end_base_line() - image_width_in_time_line;
                        }
                    }
                    if (am_i_in_time_line_boundries(helloController, mouseEvent.getSceneX(), mouseEvent.getSceneY(), media_pool_item_dragged) && !is_there_is_a_collosion(media_pool_item_dragged.getSorted_array(), min_x_pos_local, max_x_pos_local)) {
                        time_line_pane_data.getTree_set_containing_all_of_the_items().add(media_pool_item_dragged.getShapeObjectTimeLine());
                    } else {
                        helloController.time_line_pane.getChildren().remove(media_pool_item_dragged.getShapeObjectTimeLine().getShape());
                    }
                    double polygon_x_pos = return_polygon_middle_position(time_line_pane_data);
                    if (polygon_x_pos >= min_x_pos_local && polygon_x_pos <= max_x_pos_local) {
                        set_the_chatgpt_image_view(helloController, media_pool_item_dragged.getImage_key_uuid(), Type_of_Image.FULL_QUALITY);
                    } else {
                        set_the_chatgpt_image_view(helloController, return_the_image_on_click(helloController.time_line_pane, polygon_x_pos), Type_of_Image.FULL_QUALITY);
                    }
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
                if (!helloController.show_the_result_screen_stack_pane.isVisible()) {
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
                if (!helloController.show_the_result_screen_stack_pane.isVisible()) {
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
        helloController.stack_pane_of_image_view_and_text.setPrefHeight(height_of_the_screen);
        helloController.stack_pane_of_image_view_and_text.setMinHeight(height_of_the_screen);
        helloController.stack_pane_of_image_view_and_text.setMaxHeight(height_of_the_screen);
        helloController.stack_pane_of_image_view_and_text.setPrefWidth(width_of_chat_gpt_image_view);
        helloController.stack_pane_of_image_view_and_text.setMinWidth(width_of_chat_gpt_image_view);
        helloController.stack_pane_of_image_view_and_text.setMaxWidth(width_of_chat_gpt_image_view);
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
            Region region = new Region();
            helloController.play_sound.setGraphic(return_region_for_svg(get_the_svg_path("pause_icon"), play_pause_button_size));
            helloController.play_sound.getProperties().put("type", "pause");
        }
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

    private void create_the_time_line(HelloController helloController) {
        Pane main_pane = helloController.time_line_pane;
        Time_line_pane_data time_line_pane_data = new Time_line_pane_data();
        main_pane.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(40, 40, 46), CornerRadii.EMPTY, Insets.EMPTY)));
        final double pixels_in_between_each_line = 10;
        final long time_between_every_line = TimeUnit.MILLISECONDS.toNanos(50);
        final double long_line_length = 20;
        final double half_long_line_length = 13;
        final double line_length = 7.5;
        final double line_thickness = 1.3;
        time_line_pane_data.setPixels_in_between_each_line(pixels_in_between_each_line);
        time_line_pane_data.setTime_between_every_line(time_between_every_line);
        final javafx.scene.paint.Color long_line_color = javafx.scene.paint.Color.rgb(100, 101, 103);
        final javafx.scene.paint.Color mid_line_color = javafx.scene.paint.Color.rgb(89, 95, 103);
        final javafx.scene.paint.Color short_line_color = javafx.scene.paint.Color.rgb(66, 71, 78);
        final javafx.scene.paint.Color time_text_color = javafx.scene.paint.Color.rgb(146, 146, 146);
        final javafx.scene.paint.Color time_line_indicitor_color = javafx.scene.paint.Color.rgb(206, 47, 40);
        int number_of_dividors = (int) Math.ceilDiv(get_duration(), time_between_every_line) + 1;
        double base_time_line = pixels_in_between_each_line * 11;
        time_line_pane_data.setTime_line_base_line(base_time_line);
        main_pane.setUserData(time_line_pane_data);
        draw_the_rectangle_time_line_pane(0, base_time_line, main_pane.getPrefHeight(), main_pane);
        for (int i = 0; i < 11; i++) {
            if ((i == 1)) {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(i * pixels_in_between_each_line, half_long_line_length, mid_line_color, line_thickness));
            } else {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(i * pixels_in_between_each_line, line_length, short_line_color, line_thickness));
            }
        }
        for (int i = 0; i < number_of_dividors; i++) {
            double x_pos = (i * pixels_in_between_each_line) + base_time_line;
            if ((time_between_every_line * i) % TimeUnit.MILLISECONDS.toNanos(1000) == 0) {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(x_pos, long_line_length, long_line_color, line_thickness));
                main_pane.getChildren().add(add_the_text_to_time_line(time_between_every_line * i, x_pos, line_length, time_text_color));
            } else if ((time_between_every_line * i) % TimeUnit.MILLISECONDS.toNanos(500) == 0) {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(x_pos, half_long_line_length, mid_line_color, line_thickness));
            } else {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(x_pos, line_length, short_line_color, line_thickness));
            }
        }
        draw_the_rectangle_time_line_pane(base_time_line + (number_of_dividors - 1) * pixels_in_between_each_line, base_time_line, main_pane.getPrefHeight(), main_pane);
        double base_line_for_the_end_rectangle = base_time_line + (number_of_dividors) * pixels_in_between_each_line;
        for (int i = 0; i < 11; i++) {
            double x_pos = i * pixels_in_between_each_line + base_line_for_the_end_rectangle;
            if ((time_between_every_line * (i + number_of_dividors)) % TimeUnit.MILLISECONDS.toNanos(1000) == 0) {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(x_pos, long_line_length, long_line_color, line_thickness));
                main_pane.getChildren().add(add_the_text_to_time_line(time_between_every_line * (number_of_dividors + i), x_pos, line_length, time_text_color));
            } else if ((time_between_every_line * (i + number_of_dividors)) % TimeUnit.MILLISECONDS.toNanos(500) == 0) {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(x_pos, half_long_line_length, mid_line_color, line_thickness));
            } else {
                main_pane.getChildren().add(draw_the_line_on_the_time_line(x_pos, line_length, short_line_color, line_thickness));
            }
        }
        time_line_pane_data.setTime_line_end_base_line(base_time_line + (number_of_dividors - 1) * pixels_in_between_each_line);
        set_up_the_verses_time_line(helloController, main_pane, base_time_line, pixels_in_between_each_line, time_between_every_line);
        set_up_time_line_indicator(main_pane, base_time_line, time_line_indicitor_color);
        listen_to_time_line_clicked(helloController, main_pane);
        listen_to_mouse_movement_over_time_line_indicator(helloController, main_pane);
        listen_to_mouse_moving_in_time_line_pane(main_pane);
    }

    private void draw_the_rectangle_time_line_pane(double start_x, double width, double height, Pane pane) {
        Rectangle rectangle = new Rectangle(start_x, 0, width, height);
        rectangle.setStrokeWidth(0);
        rectangle.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, 0.25));
        pane.getChildren().add(rectangle);
    }

    private Line draw_the_line_on_the_time_line(double x_pos, double line_length, javafx.scene.paint.Color color, double width) {
        Line line_to_separate = new Line(x_pos, 0, x_pos, line_length);
        line_to_separate.setStroke(color);
        line_to_separate.setStrokeWidth(width);
        //line_to_separate.setSmooth(false);
        return line_to_separate;
    }

    private Text add_the_text_to_time_line(long millisecond, double line_end, double line_length, javafx.scene.paint.Color color) {
        Text time_label = new Text(convertnanosecondsToTime(millisecond));
        //time_label.setSmooth(false);
        time_label.setFont(new Font(11));
        Bounds bounds = time_label.getLayoutBounds();
        time_label.setX(line_end + 5);
        time_label.setY(line_length - bounds.getMinY() + 2.5);
        time_label.setFill(color);
        return time_label;
    }

    private String convertnanosecondsToTime(long nanoseconds) {
        long milliseconds = nanoseconds / 1_000_000L;
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    private void set_cache_hints_of_scroll_pane_time_line(HelloController helloController) {
        helloController.scroll_pane_hosting_tile_pane_media_pool.setCache(true);
        helloController.scroll_pane_hosting_tile_pane_media_pool.setCacheHint(CacheHint.SPEED);
    }

    private void set_up_the_verses_time_line(HelloController helloController, Pane pane, double base_time_line, double pixels_in_between_each_line, long time_between_every_line) {
        //double adjustor = pixels_in_between_each_line / time_between_every_line;
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        for (int i = 0; i < chatgpt_responses.size(); i++) {
            Text verse_text = new Text("Verse ".concat(String.valueOf(chatgpt_responses.get(i).getVerse_number())));
            double start_x = base_time_line + (nanoseconds_to_pixels(time_line_pane_data, chatgpt_responses.get(i).getStart_millisecond()));
            StackPane stackPane = new StackPane();
            stackPane.setPrefWidth(nanoseconds_to_pixels(time_line_pane_data, chatgpt_responses.get(i).getDuration()));
            stackPane.setPrefHeight(30);
            stackPane.setLayoutX(start_x);
            stackPane.setLayoutY(30);
            Rectangle rectangle = new Rectangle(nanoseconds_to_pixels(time_line_pane_data, chatgpt_responses.get(i).getDuration()), 20);
            rectangle.setStrokeWidth(1);
            rectangle.setStroke(javafx.scene.paint.Color.BLACK);
            rectangle.setArcHeight(5);
            rectangle.setArcWidth(5);
            rectangle.setFill(javafx.scene.paint.Color.WHITE);
            stackPane.getChildren().addAll(rectangle, verse_text);
            pane.getChildren().add(stackPane);
        }
    }

    private void set_up_time_line_indicator(Pane pane, double start_x, javafx.scene.paint.Color color) {
        final double full_length = 12.5D;
        final double partial_length = 7.5D;
        final double rectangle_width = 2.5D;
        final double time_line_indicator_width = 15D;
        final double right_side_of_rectangle = ((time_line_indicator_width - rectangle_width) / 2) + rectangle_width;
        final double left_side_of_rectangle = (time_line_indicator_width - rectangle_width) / 2;
        final double full_pane_height = pane.getHeight();
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        javafx.scene.shape.Polygon polygon = new Polygon();
        polygon.setSmooth(true);
        polygon.getPoints().addAll(
                0D, 0D,     // Top-left
                time_line_indicator_width, 0D,
                time_line_indicator_width, partial_length,
                right_side_of_rectangle, full_length,
                right_side_of_rectangle, full_pane_height,
                left_side_of_rectangle, full_pane_height,
                left_side_of_rectangle, full_length,
                0D, partial_length
        );
        //polygon.setFill(javafx.scene.paint.Color.rgb(206, 47, 40));
        polygon.setFill(color);
        polygon.setLayoutX(start_x - (time_line_indicator_width / 2));
        polygon.setLayoutY(0);
        pane.getChildren().add(polygon);
        time_line_pane_data.setPolygon(polygon);
        time_line_pane_data.setPolygon_width(time_line_indicator_width);
    }

    private void update_the_time_line_indicator(Pane pane, long milliseconds) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        double pixels_in_between_each_line = time_line_pane_data.getPixels_in_between_each_line();
        long time_between_every_line = time_line_pane_data.getTime_between_every_line();
        double adjustor = pixels_in_between_each_line / time_between_every_line;
        double time_line_base_line = time_line_pane_data.getTime_line_base_line();
        javafx.scene.shape.Polygon polygon = time_line_pane_data.getPolygon();
        double half_polygon = time_line_pane_data.getPolygon_width() / 2;
        if (polygon == null) {
            show_alert("Time line not rendered correctly. Please restart app.");
            return;
        }
        double new_x = (milliseconds * adjustor) - half_polygon + time_line_base_line;
        polygon.setLayoutX(new_x);
    }

    private void listen_to_time_line_clicked(HelloController helloController, Pane pane) {
        Time_line_pane_data time_line_pane_data = ((Time_line_pane_data) pane.getUserData());
        double y_drag_area = time_line_pane_data.getMouse_drag_y_area();
        double base_time_line = time_line_pane_data.getTime_line_base_line();
        double end_time_line = time_line_pane_data.getTime_line_end_base_line();
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getY() <= y_drag_area && mouseEvent.getX() >= base_time_line && mouseEvent.getX() <= end_time_line) {
                    time_line_clicked(helloController, pane, mouseEvent.getX());
                    which_verse_am_i_on_milliseconds(helloController, pixels_to_nanoseconds(time_line_pane_data, mouseEvent.getX() - time_line_pane_data.getTime_line_base_line()));
                    set_the_chatgpt_image_view(helloController, return_the_image_on_click(pane, mouseEvent.getX()), Type_of_Image.FULL_QUALITY);
                }
            }
        });
    }

    private void time_line_clicked(HelloController helloController, Pane pane, double x_position) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        javafx.scene.shape.Polygon polygon = time_line_pane_data.getPolygon();
        double base_time_line = time_line_pane_data.getTime_line_base_line();
        double half_polygon = time_line_pane_data.getPolygon_width() / 2;
        double end_time_line = time_line_pane_data.getTime_line_end_base_line();
        if (polygon == null) {
            show_alert("Time line not rendered correctly. Please restart app.");
            return;
        }
        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            pause_the_media_player(helloController);
        }
        if (x_position < base_time_line) {
            seek_media_based_on_time_line_changed(pane, 0);
            polygon.setLayoutX(base_time_line - half_polygon);
            return;
        }
        if (x_position > end_time_line) {
            seek_media_based_on_time_line_changed(pane, end_time_line - base_time_line);
            polygon.setLayoutX(end_time_line - half_polygon);
            return;
        }
        seek_media_based_on_time_line_changed(pane, x_position - base_time_line);

        polygon.setLayoutX(x_position - half_polygon);
    }

    private void listen_to_mouse_movement_over_time_line_indicator(HelloController helloController, Pane pane) {
        Time_line_pane_data time_line_pane_data = ((Time_line_pane_data) pane.getUserData());
        Polygon polygon = time_line_pane_data.getPolygon();
        double y_area = time_line_pane_data.getMouse_drag_y_area();
        polygon.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (polygon.getCursor() == null || !polygon.getCursor().equals(Cursor.CLOSED_HAND)) {
                    polygon.setCursor(Cursor.OPEN_HAND);
                }
            }
        });
        polygon.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (polygon.getCursor() == null || !polygon.getCursor().equals(Cursor.CLOSED_HAND)) {
                    polygon.setCursor(Cursor.DEFAULT);
                }
            }
        });
        polygon.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                polygon.setCursor(Cursor.CLOSED_HAND);
            }
        });
        polygon.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                /*double scroll_pane_x_pos = mouseEvent.getSceneX();
                double scroll_pane_y_pos = mouseEvent.getSceneY();*/
                Point2D local = helloController.time_line_pane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                if (polygon.contains(polygon.parentToLocal(local))) {
                    polygon.setCursor(Cursor.OPEN_HAND);
                } else {
                    polygon.setCursor(Cursor.DEFAULT);
                }
                set_the_chatgpt_image_view(helloController, return_the_image_on_click(pane, local.getX()), Type_of_Image.FULL_QUALITY);
            }
        });
        polygon.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double x_position = pane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY()).getX();
                time_line_clicked(helloController, pane, x_position);
                if (x_position - time_line_pane_data.getTime_line_base_line() >= 0) {
                    which_verse_am_i_on_milliseconds(helloController, pixels_to_nanoseconds(time_line_pane_data, x_position - time_line_pane_data.getTime_line_base_line()));
                }
                set_the_chatgpt_image_view(helloController, return_the_image_on_click(pane, x_position), Type_of_Image.THUMBNAIL_QUALITY);
            }
        });
    }

    private void listen_to_mouse_moving_in_time_line_pane(Pane pane) {
        Time_line_pane_data time_line_pane_data = ((Time_line_pane_data) pane.getUserData());
        Polygon polygon = time_line_pane_data.getPolygon();
        double y_area = time_line_pane_data.getMouse_drag_y_area();
        double base_time_line = time_line_pane_data.getTime_line_base_line();
        double end_time_line = time_line_pane_data.getTime_line_end_base_line();
        pane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getY() <= y_area && mouseEvent.getX() >= base_time_line && mouseEvent.getX() <= end_time_line) {
                    if (polygon.getCursor() == null || !polygon.getCursor().equals(Cursor.CLOSED_HAND)) {
                        pane.setCursor(Cursor.HAND);
                    }
                } else {
                    if (polygon.getCursor() == null || !polygon.getCursor().equals(Cursor.CLOSED_HAND)) {
                        pane.setCursor(Cursor.DEFAULT);
                    }
                }
            }
        });
    }

    private void seek_media_based_on_time_line_changed(Pane pane, double x_position) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        double pixels_in_between_each_line = time_line_pane_data.getPixels_in_between_each_line();
        long time_between_every_line = time_line_pane_data.getTime_between_every_line();
        double adjustor = time_between_every_line / pixels_in_between_each_line;
        mediaPlayer.seek(new Duration(TimeUnit.NANOSECONDS.toMillis((long) (x_position * adjustor))));
    }

    private void animation_timer(HelloController helloController) {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && lastKnownSystemTime > 0) {
                    Time_line_pane_data time_line_pane_data = (Time_line_pane_data) helloController.time_line_pane.getUserData();
                    double elapsed = (System.currentTimeMillis() - lastKnownSystemTime); // seconds
                    update_the_time_line_indicator(helloController.time_line_pane, (lastKnownMediaTime + TimeUnit.MILLISECONDS.toNanos((long) elapsed)));
                    set_the_time_line_indicator_to_the_middle(helloController.scroll_pane_hosting_the_time_line, time_line_pane_data.getPolygon().getLayoutX());
                    is_it_time_to_change_verses(helloController, (lastKnownMediaTime + TimeUnit.MILLISECONDS.toNanos((long) elapsed)));
                }
            }
        };
    }

    private void listen_to_give_feed_back_button(HelloController helloController) {
        helloController.give_feedback_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                showEmailPopupWithReply();
            }
        });
    }

    private void showEmailPopupWithReply() {
        Stage feedbackStage = new Stage();
        feedbackStage.initOwner(main_stage);
        feedbackStage.initStyle(StageStyle.DECORATED);
        //feedbackStage.initModality(Modality.WINDOW_MODAL); // optional
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label main_text = new Label("If you have any feedback, suggestions or bug reports, please send them to the email below. ");
        Label email_address = new Label(help_email);
        JFXButton copy_button = new JFXButton("Copy email address");

        VBox.setMargin(main_text, new Insets(0, 10, 0, 10));
        VBox.setMargin(email_address, new Insets(10, 10, 0, 10));
        VBox.setMargin(copy_button, new Insets(25, 10, 0, 10));
        main_text.setAlignment(Pos.CENTER);
        main_text.setTextAlignment(TextAlignment.CENTER); // center each line of text
        main_text.setWrapText(true);
        copy_button.setStyle("-fx-background-color: #000000; -fx-text-fill: #FFFFFF;");
        copy_button.setFocusTraversable(false);

        copy_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                copyToClipboard(help_email);
                showToast(feedbackStage, "Copied", 2500);
            }
        });
        vBox.getChildren().addAll(main_text, email_address, copy_button);
        Scene scene = new Scene(vBox, 450, 225);
        feedbackStage.setScene(scene);
        feedbackStage.setTitle("Send Feedback");
        feedbackStage.show();
    }

    private void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }

    private void showToast(Stage ownerStage, String message, int durationMillis) {
        Popup popup = new Popup();
        Label toastLabel = new Label(message);
        toastLabel.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.75); " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;" +
                        "-fx-font-size: 12px;"
        );
        toastLabel.setOpacity(0.9);
        popup.getContent().add(toastLabel);
        popup.setAutoHide(true);

        // Centered at bottom of the window
        popup.show(ownerStage);
        popup.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - toastLabel.getWidth() / 2);
        popup.setY(ownerStage.getY() + 0.925 * ownerStage.getHeight() - toastLabel.getHeight());

        // Hide after duration
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(durationMillis), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                popup.hide();
            }
        }));
        timeline.play();
    }

    private void set_the_time_line_indicator_to_the_middle(ScrollPane scrollPane, double x_position) {
        // Viewport width (the visible area)
        double viewportWidth = scrollPane.getViewportBounds().getWidth();

        // Current horizontal scroll position as a percentage (0.0 to 1.0)
        double hValue = scrollPane.getHvalue();

        // Total width of the content
        double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
        double contentWidth_for_h_value = contentWidth - viewportWidth;
        // Actual x-range visible in the viewport
        double minVisibleX = hValue * (contentWidth_for_h_value);
        double half_visible_width = viewportWidth / 2;
        //double max_x_position = pane.getWidth() - half_visible_width;
        if (x_position > (minVisibleX + half_visible_width) && scrollPane.getHvalue() < 1) {
            double h_value = (x_position - half_visible_width) / (contentWidth_for_h_value);
            scrollPane.setHvalue(h_value);
        }
        //double maxVisibleX = minVisibleX + viewportWidth;
    }

    private void force_the_time_line_indicator_to_be_at_the_middle(ScrollPane scrollPane, double x_position) {
        double viewportWidth = scrollPane.getViewportBounds().getWidth();
        double contentWidth = scrollPane.getContent().getBoundsInLocal().getWidth();
        double contentWidth_for_h_value = contentWidth - viewportWidth;
        double half_visible_width = viewportWidth / 2;
        double h_value = (x_position - half_visible_width) / (contentWidth_for_h_value);
        if (h_value < 0) {
            h_value = 0;
        }
        if (h_value > 1) {
            h_value = 1;
        }
        scrollPane.setHvalue(h_value);
    }

    private void pause_the_media_player(HelloController helloController) {
        mediaPlayer.pause();
    }

    private void play_the_media_player(HelloController helloController) {
        mediaPlayer.play();
    }

    private void which_verse_am_i_on_milliseconds(HelloController helloController, long milliseconds) {
        int index = Arrays.binarySearch(end_of_the_picture_durations, milliseconds);
        if (index >= 0) {
            selected_verse = index;
            the_verse_changed(helloController, selected_verse);
        } else {
            selected_verse = (index * -1) - 2;
            the_verse_changed(helloController, selected_verse);
        }
    }

    private double nanoseconds_to_pixels(Time_line_pane_data time_line_pane_data, long milliseconds) {
        double adjustor = time_line_pane_data.getPixels_in_between_each_line() / time_line_pane_data.getTime_between_every_line();
        return milliseconds * adjustor;
    }

    private long pixels_to_nanoseconds(Time_line_pane_data time_line_pane_data, double pixels) {
        double adjustor = time_line_pane_data.getTime_between_every_line() / time_line_pane_data.getPixels_in_between_each_line();
        return (long) (pixels * adjustor);
    }

    private void is_it_time_to_change_verses(HelloController helloController, double milliseconds) {
        if (chatgpt_responses.size() - 1 == selected_verse) {
            return;
        }
        if (milliseconds > chatgpt_responses.get(selected_verse + 1).getStart_millisecond()) {
            selected_verse++;
            the_verse_changed(helloController, selected_verse);
        }
    }

    private void mediaPlayer_status_changed(HelloController helloController) {
        mediaPlayer.statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
            @Override
            public void changed(ObservableValue<? extends MediaPlayer.Status> observableValue, MediaPlayer.Status old_status, MediaPlayer.Status new_status) {
                if (new_status.equals(MediaPlayer.Status.READY)) {
                    media_is_ready(helloController);
                } else if (new_status.equals(MediaPlayer.Status.PLAYING)) {
                    lastKnownSystemTime = 0;
                    timer.start();
                    set_the_play_pause_button(helloController, "pause");
                    if (old_status != null && old_status.equals(MediaPlayer.Status.STOPPED)) {
                        selected_verse = 0;
                        the_verse_changed(helloController, selected_verse);
                    }
                } else if (new_status.equals(MediaPlayer.Status.PAUSED)) {
                    timer.stop();
                    set_the_play_pause_button(helloController, "play");
                } else if (new_status.equals(MediaPlayer.Status.STOPPED)) {
                    //timer.stop();
                }
            }
        });
    }

    private void convert_png_to_jpg(File old_file, File new_file) {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", old_file.getAbsolutePath(),
                "-vf", "format=yuva444p,geq='if(lte(alpha(X,Y),16),255,p(X,Y))':'if(lte(alpha(X,Y),16),128,p(X,Y))':'if(lte(alpha(X,Y),16),128,p(X,Y))'",
                new_file.getAbsolutePath()
        );
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);
        try {
            Process process = pb.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        show_alert("A problem happened when converting pngs to jpgs. Please restart the program.");
                    }
                });
                throw new RuntimeException("FFmpeg failed to convert image!");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean am_i_in_time_line_boundries(HelloController helloController, double x_pos, double y_pos, Media_pool_item_dragged media_pool_item_dragged) {
        Point2D scene_x_and_y = helloController.scroll_pane_hosting_the_time_line.localToScene(0, 0);
        double min_x = scene_x_and_y.getX();
        double max_x = min_x + helloController.scroll_pane_hosting_the_time_line.getViewportBounds().getWidth();
        double min_y = scene_x_and_y.getY();
        double max_y = min_y + helloController.scroll_pane_hosting_the_time_line.getViewportBounds().getHeight();
        if (x_pos >= min_x && x_pos <= max_x && y_pos >= min_y && y_pos <= max_y) {
            media_pool_item_dragged.setHas_this_been_dragged(true);
            return true;
        } else {
            return false;
        }
    }

    private Rectangle create_and_return_time_line_rectangle(Pane pane, double width) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        double height = 60;
        double x_pos = time_line_pane_data.getTime_line_base_line();
        /*x_pos -= (width / 2);
        x_pos = Math.max(x_pos, time_line_pane_data.getTime_line_base_line());
        if (x_pos + width >= time_line_pane_data.getTime_line_end_base_line()) {
            x_pos = time_line_pane_data.getTime_line_end_base_line() - width;
        }*/
        Rectangle rectangle;
        rectangle = new Rectangle(width, height);
        pane.getChildren().add(pane.getChildren().size() - 1, rectangle);
        rectangle.setY(60);
        rectangle.setX(x_pos);
        return rectangle;
    }

    private double move_the_rectangle(Pane pane, Rectangle rectangle, double x_pos) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        double width = rectangle.getWidth();
        x_pos -= (width / 2);
        x_pos = Math.max(x_pos, time_line_pane_data.getTime_line_base_line());
        if (x_pos + width >= time_line_pane_data.getTime_line_end_base_line()) {
            x_pos = time_line_pane_data.getTime_line_end_base_line() - width;
        }
        rectangle.setX(x_pos);
        return x_pos;
    }

    private void remove_the_image_from_time_line_hash_map(Pane pane, Shape_object_time_line shapeObjectTimeLine) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        time_line_pane_data.getTree_set_containing_all_of_the_items().remove(shapeObjectTimeLine);
        pane.getChildren().remove(shapeObjectTimeLine.getShape());
    }

    private void set_up_the_image_rectangle(Rectangle rectangle, Image image, Pane pane) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        ImagePattern pattern = new ImagePattern(create_image_for_the_time_line_thumbnail(remove_transparent_pixels(image), (int) rectangle.getWidth(), (int) rectangle.getHeight()), 0, 0, 1, 1, true);
        rectangle.setStrokeWidth(1);
        rectangle.setStroke(javafx.scene.paint.Color.BLACK);
        rectangle.setArcHeight(5);
        rectangle.setArcWidth(5);
        rectangle.setFill(pattern);
    }

    private Image create_image_for_the_time_line_thumbnail(Image image, int width, int height) {
        try {
            BufferedImage thumbnail = Thumbnails.of(image_to_buffered_image(image))
                    .height(height)
                    .keepAspectRatio(true)
                    .scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
                    .outputQuality(1)
                    .asBufferedImage();
            WritableImage writableImage = new WritableImage(width, height);
            PixelWriter pixelWriter = writableImage.getPixelWriter();
            PixelReader pixelReader = buffer_image_to_image(thumbnail).getPixelReader();
            int number_of_images = Math.floorDiv(width, thumbnail.getWidth());
            for (int k = 0; k < number_of_images; k++) {
                pixelWriter.setPixels(k * thumbnail.getWidth(), 0, thumbnail.getWidth(), thumbnail.getHeight(), pixelReader, 0, 0);
            }
            if (width % thumbnail.getWidth() != 0) {
                pixelWriter.setPixels(number_of_images * thumbnail.getWidth(), 0, width % thumbnail.getWidth(), thumbnail.getHeight(), pixelReader, 0, 0);
            }
            return writableImage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Image remove_transparent_pixels(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int start_left_to_right = -1;
        int end_left_to_right = -1;
        int start_top_to_bottom = -1;
        int end_top_to_bottom = -1;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (!pixelReader.getColor(j, i).equals(javafx.scene.paint.Color.TRANSPARENT)) {
                    if (start_left_to_right == -1) {
                        start_left_to_right = j;
                    }
                    if (j > end_left_to_right) {
                        end_left_to_right = j;
                    }
                    if (start_top_to_bottom == -1) {
                        start_top_to_bottom = i;
                    }
                    if (i > end_top_to_bottom) {
                        end_top_to_bottom = i;
                    }
                }
            }
        }
        if (end_left_to_right - start_left_to_right == 0 || end_top_to_bottom - start_top_to_bottom == 0) {

        }
        WritableImage new_image_to_be_returned = new WritableImage(end_left_to_right - start_left_to_right + 1, end_top_to_bottom - start_top_to_bottom + 1);
        PixelWriter pixelWriter = new_image_to_be_returned.getPixelWriter();
        int x_counter = 0;
        int y_counter = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            boolean found_a_non_transparent_pixel = false;
            for (int j = 0; j < image.getWidth(); j++) {
                if (!pixelReader.getColor(j, i).equals(javafx.scene.paint.Color.TRANSPARENT)) {
                    pixelWriter.setColor(x_counter, y_counter, pixelReader.getColor(j, i));
                    found_a_non_transparent_pixel = true;
                    x_counter++;
                }
            }
            x_counter = 0;
            if (found_a_non_transparent_pixel) {
                y_counter++;
            }
        }
        return new_image_to_be_returned;
    }

    private void configure_the_image_rectangle(Shape_object_time_line shapeObjectTimeLine, HelloController helloController, Pane pane, Image image) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Remove media");
        contextMenu.getItems().add(item1);
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                remove_the_image_from_time_line_hash_map(pane, shapeObjectTimeLine);
                clear_image_view_if_recatngle_is_in_boundries(helloController, pane, shapeObjectTimeLine);
            }
        });
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        Rectangle rectangle = (Rectangle) shapeObjectTimeLine.getShape();
        double change_cursor_to_double_arrow_buffer = 10;
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    contextMenu.show(rectangle, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                }
            }
        });
        rectangle.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                set_the_rectangle_mouse_cursor(helloController, mouseEvent.getSceneX(), mouseEvent.getSceneY(), shapeObjectTimeLine, change_cursor_to_double_arrow_buffer);
            }
        });
        rectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    double mouse_scene_x_translated = helloController.time_line_pane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY()).getX();
                    double local_x = mouse_scene_x_translated - shapeObjectTimeLine.getStart();
                    TreeSet<Shape_object_time_line> copy_of_all_items_tree_set = new TreeSet<>(time_line_pane_data.getTree_set_containing_all_of_the_items());
                    copy_of_all_items_tree_set.remove(shapeObjectTimeLine);
                    double[][] sorted_array_hashmap = return_all_of_the_image_timings_sorted(copy_of_all_items_tree_set);
                    if (get_type_of_movement(rectangle, local_x, change_cursor_to_double_arrow_buffer) == MovementType.START) {
                        Rectangle_changed_info rectangleChangedInfo = new Rectangle_changed_info(mouseEvent.getSceneX(), MovementType.START, shapeObjectTimeLine.getStart(), shapeObjectTimeLine.getEnd(), local_x, sorted_array_hashmap, shapeObjectTimeLine.getImage_id());
                        rectangle.setUserData(rectangleChangedInfo);
                    } else if (get_type_of_movement(rectangle, local_x, change_cursor_to_double_arrow_buffer) == MovementType.END) {
                        Rectangle_changed_info rectangleChangedInfo = new Rectangle_changed_info(mouseEvent.getSceneX(), MovementType.END, shapeObjectTimeLine.getStart(), shapeObjectTimeLine.getEnd(), local_x, sorted_array_hashmap, shapeObjectTimeLine.getImage_id());
                        rectangle.setUserData(rectangleChangedInfo);
                    } else {
                        Rectangle fake_rectanlge_less_opacity = deepCopyRectangle(rectangle);
                        fake_rectanlge_less_opacity.setOpacity(0.4D);
                        fake_rectanlge_less_opacity.setVisible(false);
                        pane.getChildren().add(fake_rectanlge_less_opacity);
                        Rectangle_changed_info rectangleChangedInfo = new Rectangle_changed_info(mouseEvent.getSceneX(), MovementType.MIDDLE, shapeObjectTimeLine.getStart(), shapeObjectTimeLine.getEnd(), local_x, sorted_array_hashmap, fake_rectanlge_less_opacity, shapeObjectTimeLine.getImage_id());
                        rectangle.setUserData(rectangleChangedInfo);
                        rectangle.setCursor(Cursor.CLOSED_HAND);
                    }
                }
            }
        });
        rectangle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    Rectangle_changed_info rectangleChangedInfo = (Rectangle_changed_info) rectangle.getUserData();
                    double mouse_scene_x_translated = helloController.time_line_pane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY()).getX();
                    double local_x = rectangleChangedInfo.getRelative_x();
                    if (rectangleChangedInfo.getType_of_movement() == MovementType.START) {
                        double mouse_difference = rectangleChangedInfo.getOriginal_x() - mouseEvent.getSceneX();
                        double new_start = rectangleChangedInfo.getOriginal_start_rectangle() - mouse_difference;
                        double new_width = rectangleChangedInfo.getOriginal_end_rectangle() - new_start;
                        if (rectangleChangedInfo.getOriginal_start_rectangle() - mouse_difference >= time_line_pane_data.getTime_line_base_line() && new_width >= 0) {
                            double[] collision_result = return_the_collision(rectangleChangedInfo.getSorted_array(), rectangleChangedInfo.getOriginal_start_rectangle() - mouse_difference, rectangleChangedInfo.getOriginal_end_rectangle(), CollisionSearchType.Start);
                            if (collision_result[0] < 0) {
                                rectangle.setX(rectangleChangedInfo.getOriginal_start_rectangle() - mouse_difference);
                                rectangle.setWidth(new_width);
                            } else {
                                rectangle.setX(collision_result[1] + 1);
                                rectangle.setWidth(rectangleChangedInfo.getOriginal_end_rectangle() - (collision_result[1] + 1));
                            }
                        } else if (rectangleChangedInfo.getOriginal_start_rectangle() - mouse_difference < time_line_pane_data.getTime_line_base_line()) {
                            if (!is_there_is_a_collosion(rectangleChangedInfo.getSorted_array(), time_line_pane_data.getTime_line_base_line(), rectangleChangedInfo.getOriginal_end_rectangle())) {
                                rectangle.setX(time_line_pane_data.getTime_line_base_line());
                                rectangle.setWidth(rectangleChangedInfo.getOriginal_end_rectangle() - time_line_pane_data.getTime_line_base_line());
                            }
                        } else if (new_width < 0) {
                            rectangle.setX(rectangleChangedInfo.getOriginal_end_rectangle());
                            rectangle.setWidth(0);
                        }
                        double polygon_pos = return_polygon_middle_position(time_line_pane_data);
                        if (rectangle.getX() <= polygon_pos && rectangle.getX() + rectangle.getWidth() >= polygon_pos) {
                            set_the_chatgpt_image_view(helloController, rectangleChangedInfo.getImage_id(), Type_of_Image.THUMBNAIL_QUALITY);
                            rectangleChangedInfo.setDid_we_ever_change_the_photo(true);
                        } else if (rectangleChangedInfo.isDid_we_ever_change_the_photo()) {
                            set_the_chatgpt_image_view(helloController, "black", Type_of_Image.THUMBNAIL_QUALITY);
                        }
                    } else if (rectangleChangedInfo.getType_of_movement() == MovementType.MIDDLE) {
                        if (mouse_scene_x_translated - local_x >= time_line_pane_data.getTime_line_base_line() && mouse_scene_x_translated - local_x + rectangle.getWidth() <= time_line_pane_data.getTime_line_end_base_line()) {
                            double[] collision_result = return_the_collision(rectangleChangedInfo.getSorted_array(), mouse_scene_x_translated - local_x, mouse_scene_x_translated - local_x + rectangle.getWidth(), CollisionSearchType.End);
                            double x_pos = mouseEvent.getSceneX() - rectangleChangedInfo.getOriginal_x() + rectangleChangedInfo.getOriginal_start_rectangle();
                            if (collision_result[0] < 0) {
                                rectangle.setX(x_pos);
                                rectangleChangedInfo.getFake_rectangle().setVisible(false);
                            } else {
                                Rectangle fake_rectangle = rectangleChangedInfo.getFake_rectangle();
                                fake_rectangle.setVisible(true);
                                fake_rectangle.setX(x_pos);
                                if (rectangle.getX() < collision_result[0]) {
                                    if (!is_there_is_a_collosion(rectangleChangedInfo.getSorted_array(), collision_result[0] - 1 - rectangle.getWidth(), collision_result[0] - 1)) {
                                        rectangle.setX(collision_result[0] - 1 - rectangle.getWidth());
                                    }
                                } else {
                                    if (!is_there_is_a_collosion(rectangleChangedInfo.getSorted_array(), collision_result[1] + 1, collision_result[1] + 1 + rectangle.getWidth())) {
                                        rectangle.setX(collision_result[1] + 1);
                                    }
                                }
                            }
                        } else if (mouse_scene_x_translated - local_x < time_line_pane_data.getTime_line_base_line()) {
                            if (!is_there_is_a_collosion(rectangleChangedInfo.getSorted_array(), time_line_pane_data.getTime_line_base_line(), time_line_pane_data.getTime_line_base_line() + rectangle.getWidth())) {
                                rectangle.setX(time_line_pane_data.getTime_line_base_line());
                            }
                            rectangleChangedInfo.getFake_rectangle().setX(time_line_pane_data.getTime_line_base_line());
                        } else if (mouse_scene_x_translated - local_x + rectangle.getWidth() > time_line_pane_data.getTime_line_end_base_line()) {
                            if (!is_there_is_a_collosion(rectangleChangedInfo.getSorted_array(), time_line_pane_data.getTime_line_end_base_line() - rectangle.getWidth(), time_line_pane_data.getTime_line_base_line())) {
                                rectangle.setX(time_line_pane_data.getTime_line_end_base_line() - rectangle.getWidth());
                            }
                            rectangleChangedInfo.getFake_rectangle().setX(time_line_pane_data.getTime_line_end_base_line() - rectangle.getWidth());
                        }
                        double polygon_pos = return_polygon_middle_position(time_line_pane_data);
                        if (rectangle.getX() <= polygon_pos && rectangle.getX() + rectangle.getWidth() >= polygon_pos) {
                            set_the_chatgpt_image_view(helloController, rectangleChangedInfo.getImage_id(), Type_of_Image.THUMBNAIL_QUALITY);
                            rectangleChangedInfo.setDid_we_ever_change_the_photo(true);
                        } else if (rectangleChangedInfo.isDid_we_ever_change_the_photo()) {
                            set_the_chatgpt_image_view(helloController, "black", Type_of_Image.THUMBNAIL_QUALITY);
                        }
                    } else if (rectangleChangedInfo.getType_of_movement() == MovementType.END) {
                        double original_width = rectangleChangedInfo.getOriginal_end_rectangle() - rectangleChangedInfo.getOriginal_start_rectangle();
                        double new_width = mouseEvent.getSceneX() - rectangleChangedInfo.getOriginal_x() + original_width;
                        double[] collision_result = return_the_collision(rectangleChangedInfo.getSorted_array(), rectangleChangedInfo.getOriginal_start_rectangle(), rectangleChangedInfo.getOriginal_start_rectangle() + new_width, CollisionSearchType.End);
                        if (rectangle.getX() + new_width <= time_line_pane_data.getTime_line_end_base_line() && new_width >= 0) {
                            if (collision_result[0] < 0) {
                                rectangle.setWidth(new_width);
                            } else {
                                rectangle.setWidth((collision_result[0] - 1) - rectangleChangedInfo.getOriginal_start_rectangle());
                            }
                        } else if (new_width < 0) {
                            rectangle.setWidth(0);
                        } else if (rectangle.getX() + new_width > time_line_pane_data.getTime_line_end_base_line()) {
                            if (!is_there_is_a_collosion(rectangleChangedInfo.getSorted_array(), rectangleChangedInfo.getOriginal_start_rectangle(), rectangleChangedInfo.getOriginal_start_rectangle() + time_line_pane_data.getTime_line_end_base_line() - rectangleChangedInfo.getOriginal_start_rectangle())) {
                                rectangle.setWidth(time_line_pane_data.getTime_line_end_base_line() - rectangleChangedInfo.getOriginal_start_rectangle());
                            }
                        }
                        double polygon_pos = return_polygon_middle_position(time_line_pane_data);
                        if (rectangle.getX() <= polygon_pos && rectangle.getX() + rectangle.getWidth() >= polygon_pos) {
                            set_the_chatgpt_image_view(helloController, rectangleChangedInfo.getImage_id(), Type_of_Image.THUMBNAIL_QUALITY);
                            rectangleChangedInfo.setDid_we_ever_change_the_photo(true);
                        } else if (rectangleChangedInfo.isDid_we_ever_change_the_photo()) {
                            set_the_chatgpt_image_view(helloController, "black", Type_of_Image.THUMBNAIL_QUALITY);
                        }
                    }
                }
            }
        });
        rectangle.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    Rectangle_changed_info rectangleChangedInfo = (Rectangle_changed_info) rectangle.getUserData();
                    double old_width = shapeObjectTimeLine.getEnd() - shapeObjectTimeLine.getStart();
                    if (rectangleChangedInfo.getType_of_movement() == MovementType.MIDDLE) { // added later
                        time_line_pane_data.getTree_set_containing_all_of_the_items().remove(shapeObjectTimeLine);
                    }
                    shapeObjectTimeLine.setStart(rectangle.getX());
                    shapeObjectTimeLine.setEnd(rectangle.getX() + rectangle.getWidth());
                    if (rectangleChangedInfo.getFake_rectangle() != null) {
                        pane.getChildren().remove(rectangleChangedInfo.getFake_rectangle());
                    }
                    check_if_i_am_in_right_rectangle_for_cursor(helloController, mouseEvent.getSceneX(), mouseEvent.getSceneY(), shapeObjectTimeLine, change_cursor_to_double_arrow_buffer, pane);
                    if (rectangle.getWidth() != old_width) {
                        set_up_the_image_rectangle(rectangle, image, pane);
                    }
                    if (rectangleChangedInfo.getType_of_movement() == MovementType.MIDDLE) {// added becuase its removed earlier
                        time_line_pane_data.getTree_set_containing_all_of_the_items().add(shapeObjectTimeLine);
                    }
                    double polygon_x_pos = return_polygon_middle_position(time_line_pane_data);
                    if (polygon_x_pos >= shapeObjectTimeLine.getStart() && polygon_x_pos <= shapeObjectTimeLine.getEnd()) {
                        set_the_chatgpt_image_view(helloController, shapeObjectTimeLine.getImage_id(), Type_of_Image.FULL_QUALITY);
                    } else {
                        set_the_chatgpt_image_view(helloController, return_the_image_on_click(helloController.time_line_pane, polygon_x_pos), Type_of_Image.FULL_QUALITY);
                    }
                }
            }
        });
    }

    private void set_the_rectangle_mouse_cursor(HelloController helloController, double scene_x, double scene_y, Shape_object_time_line shapeObjectTimeLine, double change_cursor_to_double_arrow_buffer) {
        Rectangle rectangle = (Rectangle) shapeObjectTimeLine.getShape();
        double mouse_scene_x_translated = helloController.time_line_pane.sceneToLocal(scene_x, scene_y).getX();
        double local_x = mouse_scene_x_translated - shapeObjectTimeLine.getStart();
        if (mouse_scene_x_translated >= shapeObjectTimeLine.getStart() && mouse_scene_x_translated <= shapeObjectTimeLine.getEnd()) {
            if (get_type_of_movement(rectangle, local_x, change_cursor_to_double_arrow_buffer) == MovementType.START || get_type_of_movement(rectangle, local_x, change_cursor_to_double_arrow_buffer) == MovementType.END) {
                rectangle.setCursor(Cursor.H_RESIZE);
            } else {
                rectangle.setCursor(Cursor.OPEN_HAND);
            }
        }
    }

    private void check_if_i_am_in_right_rectangle_for_cursor(HelloController helloController, double scene_x, double scene_y, Shape_object_time_line shapeObjectTimeLine, double change_cursor_to_double_arrow_buffer, Pane pane) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        double mouse_scene_x_translated = helloController.time_line_pane.sceneToLocal(scene_x, scene_y).getX();
        if (mouse_scene_x_translated >= shapeObjectTimeLine.getStart() && mouse_scene_x_translated <= shapeObjectTimeLine.getEnd()) {
            set_the_rectangle_mouse_cursor(helloController, scene_x, scene_y, shapeObjectTimeLine, change_cursor_to_double_arrow_buffer);
        } else {
            for (Shape_object_time_line shapeObjectTimeLine_from_tree_set : time_line_pane_data.getTree_set_containing_all_of_the_items()) {
                if (mouse_scene_x_translated >= shapeObjectTimeLine_from_tree_set.getStart() && mouse_scene_x_translated <= shapeObjectTimeLine_from_tree_set.getEnd()) {
                    set_the_rectangle_mouse_cursor(helloController, scene_x, scene_y, shapeObjectTimeLine_from_tree_set, change_cursor_to_double_arrow_buffer);
                    break;
                }
            }
        }
    }

    private MovementType get_type_of_movement(Rectangle rectangle, double local_x, double change_cursor_to_double_arrow_buffer) {
        if (local_x <= change_cursor_to_double_arrow_buffer) {
            return MovementType.START;
        } else if (rectangle.getWidth() - local_x <= change_cursor_to_double_arrow_buffer) {
            return MovementType.END;
        } else {
            return MovementType.MIDDLE;
        }
    }

    private void set_the_opacity_of_the_rectangle_in_time_line_pane(Rectangle rectangle, double opacity) {
        rectangle.setOpacity(opacity);
        /*HashMap<String, Shape_object_time_line> hashMap_with_all_of_the_items = time_line_pane_data.getTree_set_containing_all_of_the_items();
        if (hashMap_with_all_of_the_items.containsKey(uuid)) {
            Rectangle rectangle = (Rectangle) hashMap_with_all_of_the_items.get(uuid).getShape();
            rectangle.setOpacity(opacity);
        }*/
    }

    /*private boolean is_there_is_a_collosion(double[][] sorted_array, double start, double end) {
        for (int i = 0; i < sorted_array.length; i++) {
            double local_start = sorted_array[i][0];
            double local_end = sorted_array[i][1];
            if (start > local_end) {
                continue;
            }
            if (local_start > end) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }*/

    private boolean is_there_is_a_collosion(TreeSet<Shape_object_time_line> tree_set_with_all_of_the_items, double start, double end) {
        Shape_object_time_line start_ceiled_shape_object_time_line = tree_set_with_all_of_the_items.floor(new Shape_object_time_line(start));
        Shape_object_time_line end_ceiled_shape_object_time_line = tree_set_with_all_of_the_items.floor(new Shape_object_time_line(end));
        if(start_ceiled_shape_object_time_line == null){
            if(end_ceiled_shape_object_time_line == null){
                return false;
            } else {
                return true;
            }
        } else {
            if(!start_ceiled_shape_object_time_line.equals(end_ceiled_shape_object_time_line)){
                return true;
            } else {
                if(start_ceiled_shape_object_time_line.getEnd() < start){
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    private double[] return_the_collision(double[][] sorted_array, double start, double end, CollisionSearchType collisionSearchType) {
        if (collisionSearchType == CollisionSearchType.End) {
            for (int i = 0; i < sorted_array.length; i++) {
                double local_start = sorted_array[i][0];
                double local_end = sorted_array[i][1];
                if (start > local_end) {
                    continue;
                }
                if (local_start > end) {
                    return new double[]{-1, -1};
                } else {
                    return new double[]{local_start, local_end};
                }
            }
            return new double[]{-1, -1};
        } else {
            for (int i = sorted_array.length - 1; i >= 0; i--) {
                double local_start = sorted_array[i][0];
                double local_end = sorted_array[i][1];
                if (end < local_start) {
                    continue;
                }
                if (local_end < start) {
                    return new double[]{-1, -1};
                } else {
                    return new double[]{local_start, local_end};
                }
            }
            return new double[]{-1, -1};
        }
    }

    private double[][] return_all_of_the_image_timings_sorted(TreeSet<Shape_object_time_line> treeSet) {
        double[][] sorted_array = new double[treeSet.size()][2];
        int counter = 0;
        for (Shape_object_time_line shapeObjectTimeLine : treeSet) {
            sorted_array[counter] = new double[]{shapeObjectTimeLine.getStart(), shapeObjectTimeLine.getEnd()};
            counter++;
        }
        return sorted_array;
    }

    private void black_out_the_image_view_at_the_start(HelloController helloController) {
        set_the_chatgpt_image_view(helloController, "black", Type_of_Image.FULL_QUALITY);
    }

    private void listen_to_chatgpt_image_view_on_mouse_enetered_and_left(HelloController helloController) {
        helloController.stack_pane_of_image_view_and_text.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                helloController.blurry_chatgpt_image_view.setVisible(true);
                helloController.play_sound.setVisible(true);
            }
        });
        helloController.stack_pane_of_image_view_and_text.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                helloController.blurry_chatgpt_image_view.setVisible(false);
                helloController.play_sound.setVisible(false);
            }
        });
    }

    private void play_the_sound_on_chatgpt_image_clicked(HelloController helloController) {
        helloController.chatgpt_image_view.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                play_or_pause_the_video_after_click(helloController);
            }
        });
    }

    private void add_tool_tip_to_create_button(HelloController helloController) {
        Tooltip tooltip = new Tooltip("Create video");
        tooltip.setShowDelay(new Duration(500));
        Tooltip.install(helloController.create_video_final, tooltip);
    }

    private void add_cancel_video_tool_tip(HelloController helloController) {
        Tooltip tooltip = new Tooltip("Cancel video");
        tooltip.setShowDelay(new Duration(500));
        Tooltip.install(helloController.cancel_video, tooltip);
    }

    private void add_tool_tip_to_next_verse(HelloController helloController) {
        Tooltip tooltip = new Tooltip("Next verse");
        tooltip.setShowDelay(new Duration(500));
        Tooltip.install(helloController.next_photo_chat_gpt_result, tooltip);
    }

    private void add_tool_tip_to_previous_verse(HelloController helloController) {
        Tooltip tooltip = new Tooltip("Previous verse");
        tooltip.setShowDelay(new Duration(500));
        Tooltip.install(helloController.previous_photo_chat_gpt_result, tooltip);
    }

    private void set_up_glossy_pause_button(HelloController helloController) {
        GaussianBlur blur = new GaussianBlur(30); // Adjust the radius for more/less blur
        helloController.blurry_chatgpt_image_view.setEffect(blur);
        Circle circle = new Circle(helloController.blurry_chatgpt_image_view.getFitWidth() / 2, helloController.blurry_chatgpt_image_view.getFitHeight() / 2, blurry_circle_raduis);
        helloController.blurry_chatgpt_image_view.setClip(circle);
    }

    private void make_the_blurry_chatgpt_image_always_have_the_same_size_as_non_blurry(HelloController helloController) {
        helloController.blurry_chatgpt_image_view.fitWidthProperty().bind(helloController.chatgpt_image_view.fitWidthProperty());
        helloController.blurry_chatgpt_image_view.fitHeightProperty().bind(helloController.chatgpt_image_view.fitHeightProperty());
    }

    private void set_the_chatgpt_image_view(HelloController helloController, String image_id, Type_of_Image typeOfImage) {
        if (!id_of_the_last_image.getImage_id().equals(image_id) || (id_of_the_last_image.getTypeOfImage() == Type_of_Image.THUMBNAIL_QUALITY && typeOfImage == Type_of_Image.FULL_QUALITY)) {
            id_of_the_last_image = new Last_shown_Image(image_id, typeOfImage);
            if (image_id.equals("black")) {
                Image image = return_16_9_black_image();
                helloController.chatgpt_image_view.setImage(image);
                helloController.blurry_chatgpt_image_view.setImage(whiten_the_image(image_to_buffered_image(image), 0.7f, 60f));
            } else {
                Media_pool media_pool = hashMap_with_media_pool_items.get(image_id);
                helloController.chatgpt_image_view.setImage(media_pool.getBlacked_thumbnail());
                helloController.blurry_chatgpt_image_view.setImage(whiten_the_image(image_to_buffered_image(media_pool.getBlacked_thumbnail()), 0.7f, 60f));
                if (typeOfImage == Type_of_Image.FULL_QUALITY) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (media_pool.isDid_the_image_get_down_scaled()) {
                                java.nio.file.Path path = Paths.get("temp/images/scaled", image_id.concat(".raw"));
                                Image image = readRawImage(path.toString(), 1080, 1920);
                                helloController.chatgpt_image_view.setImage(image);
                            } else {
                                java.nio.file.Path path = Paths.get("temp/images/base", image_id.concat(".raw"));
                                Image image = readRawImage(path.toString(), media_pool.getWidth(), media_pool.getHeight());
                                helloController.chatgpt_image_view.setImage(image);
                            }
                        }
                    });
                }
            }
        }
    }

    private void listen_to_blurry_image_view_size_change_and_center_the_clip(HelloController helloController) {
        helloController.blurry_chatgpt_image_view.fitWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (helloController.blurry_chatgpt_image_view.getClip() != null) {
                    ((Circle) helloController.blurry_chatgpt_image_view.getClip()).setCenterX(t1.doubleValue() / 2);
                    helloController.play_sound.setLayoutX(t1.doubleValue() / 2 - blurry_circle_raduis);
                }
            }
        });
        helloController.blurry_chatgpt_image_view.fitHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (helloController.blurry_chatgpt_image_view.getClip() != null) {
                    double vertical_bias = 0.875D;
                    ((Circle) helloController.blurry_chatgpt_image_view.getClip()).setCenterY(t1.doubleValue() * vertical_bias);
                    helloController.play_sound.setLayoutY(t1.doubleValue() * vertical_bias - blurry_circle_raduis);
                }
            }
        });
    }

    private void make_play_button_circle(HelloController helloController) {
        helloController.play_sound.setPrefHeight(blurry_circle_raduis * 2);
        helloController.play_sound.setPrefWidth(blurry_circle_raduis * 2);
        helloController.play_sound.setShape(new Circle(blurry_circle_raduis));
    }

    private Rectangle deepCopyRectangle(Rectangle rectangle) {
        Rectangle copy = new Rectangle(
                rectangle.getX(),
                rectangle.getY(),
                rectangle.getWidth(),
                rectangle.getHeight()
        );
        copy.setFill(rectangle.getFill());
        copy.setStroke(rectangle.getStroke());
        copy.setStrokeWidth(rectangle.getStrokeWidth());
        copy.setArcWidth(rectangle.getArcWidth());
        copy.setArcHeight(rectangle.getArcHeight());
        copy.setOpacity(rectangle.getOpacity());
        copy.setRotate(rectangle.getRotate());
        copy.setEffect(rectangle.getEffect());
        return copy;
    }

    private Image return_16_9_black_image() {
        int width = 9;
        int height = 16;
        WritableImage writableImage = new WritableImage(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writableImage.getPixelWriter().setColor(i, j, javafx.scene.paint.Color.BLACK);
            }
        }
        return writableImage;
    }


    private Image readRawImage(String filePath, int width, int height) {
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            int read = fis.read(bytes);
            if (read != bytes.length) {
                throw new IOException("Could not read full file");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 2. Convert bytes to int[] (ARGB format)
        int[] pixels = new int[bytes.length / 4];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN); // Use the same as write!

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = buffer.getInt();
        }

        // 3. Create WritableImage and fill with pixels
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();

        int idx = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixels[idx++];
                //int a = (argb >> 24) & 0xff;
                int r = (argb >> 16) & 0xff;
                int g = (argb >> 8) & 0xff;
                int b = (argb) & 0xff;
                //writer.setColor(x, y, javafx.scene.paint.Color.rgb(r, g, b, a / 255.0));
                writer.setColor(x, y, javafx.scene.paint.Color.rgb(r, g, b));
            }
        }
        return image;
    }

    private BufferedImage return_argb_buffered_image(BufferedImage bufferedImage) {
        if (bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB) {
            return bufferedImage;
        } else {
            BufferedImage new_rgb_buffered_image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
            new_rgb_buffered_image.createGraphics().drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
            return new_rgb_buffered_image;
        }
    }

    private Image whiten_the_image(BufferedImage image, float scale, float offset) {
        // Scale factors and offsets
        RescaleOp op = new RescaleOp(
                new float[]{scale, scale, scale, 1.0f},
                new float[]{offset, offset, offset, 0f}, null);

        BufferedImage result = op.filter(image, null);
        return buffer_image_to_image(result);
    }

    private String return_the_image_on_click(Pane pane, double x_position) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        TreeSet<Shape_object_time_line> treeSet = time_line_pane_data.getTree_set_containing_all_of_the_items();
        x_position = return_min_max_or_normal_x_postion(time_line_pane_data, x_position);
        Shape_object_time_line fake_shapeObjectTimeLine = new Shape_object_time_line(x_position);
        Shape_object_time_line floored_item = treeSet.floor(fake_shapeObjectTimeLine);
        if (floored_item == null || x_position > floored_item.getEnd()) {
            return "black";
        } else {
            return floored_item.getImage_id();
        }
    }

    private String return_the_image_from_time(Pane pane, long nano_seconds) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        double x_pos = nanoseconds_to_pixels(time_line_pane_data, nano_seconds) + time_line_pane_data.getTime_line_base_line();
        return return_the_image_on_click(pane, x_pos);
    }

    private double return_min_max_or_normal_x_postion(Time_line_pane_data time_line_pane_data, double x_pos) {
        if (x_pos < time_line_pane_data.getTime_line_base_line()) {
            return time_line_pane_data.getTime_line_base_line();
        } else if (x_pos > time_line_pane_data.getTime_line_end_base_line()) {
            return time_line_pane_data.getTime_line_end_base_line();
        } else {
            return x_pos;
        }
    }

    private double return_polygon_middle_position(Time_line_pane_data time_line_pane_data) {
        double start = time_line_pane_data.getPolygon().getLayoutX();
        return start + time_line_pane_data.getPolygon_width() / 2;
    }

    private void clear_image_view_if_recatngle_is_in_boundries(HelloController helloController, Pane pane, Shape_object_time_line shapeObjectTimeLine) {
        Time_line_pane_data time_line_pane_data = (Time_line_pane_data) pane.getUserData();
        double x_pos = return_polygon_middle_position(time_line_pane_data);
        if (x_pos >= shapeObjectTimeLine.getStart() && x_pos <= shapeObjectTimeLine.getEnd()) {
            set_the_chatgpt_image_view(helloController, "black", Type_of_Image.FULL_QUALITY);
        }
    }
}