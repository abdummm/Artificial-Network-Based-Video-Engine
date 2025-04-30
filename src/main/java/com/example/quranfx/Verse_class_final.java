package com.example.quranfx;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Verse_class_final {

    private String verse;
    private String arabic_verse;
    private Integer verse_number;
    private long time_in_milliseconds;
    private Image thumbnail_vertical;
    private Image thumbnail_horizontal;
    private Image thumbnail_square;
    private boolean image_edited;
    private Pic_aspect_ratio pic_aspect_ratio;

    private Ayat_settings ayatSettings;

    public Verse_class_final(String verse, Integer verse_number, long time_in_milliseconds, Ayat_settings ayatSettings, String arabic_verse, Pic_aspect_ratio pic_aspect_ratio) {
        this.verse = verse;
        this.verse_number = verse_number;
        this.time_in_milliseconds = time_in_milliseconds;
        this.ayatSettings = ayatSettings;
        this.arabic_verse = arabic_verse;
        this.pic_aspect_ratio = pic_aspect_ratio;
        this.image_edited = false;
        //save_image_to_disk(get_the_right_basic_image_aspect_ratio(pic_aspect_ratio), verse_number, "base");
        //save_image_to_disk(get_the_right_basic_image_aspect_ratio(pic_aspect_ratio), verse_number, "edited");
        //copy_the_image("base");
        //copy_the_image("edited");
        set_the_thumbnail(get_the_right_basic_image_aspect_ratio(pic_aspect_ratio));
    }

    public Verse_class_final(String verse, Integer verse_number, BufferedImage base_64_image, long time_in_milliseconds, Ayat_settings ayatSettings, String arabic_verse, Pic_aspect_ratio pic_aspect_ratio) {
        this.verse = verse;
        this.verse_number = verse_number;
        this.time_in_milliseconds = time_in_milliseconds;
        this.ayatSettings = ayatSettings;
        this.arabic_verse = arabic_verse;
        this.pic_aspect_ratio = pic_aspect_ratio;
        this.image_edited = false;
        save_image_to_disk(base_64_image, verse_number, "base");
        save_image_to_disk(base_64_image, verse_number, "edited");
        set_the_thumbnail(base_64_image);
    }

    public String getVerse() {
        return verse;
    }

    public Integer getVerse_number() {
        return verse_number;
    }

    public BufferedImage getBase_64_image() {
        return read_image_from_disk(this.verse_number, "base");
    }

    public long getTime_in_milliseconds() {
        return time_in_milliseconds;
    }

    public void setTime_in_milliseconds(long time_in_milliseconds) {
        this.time_in_milliseconds = time_in_milliseconds;
    }

    public void setBase_64_image(BufferedImage base_64_image) {
        save_image_to_disk(base_64_image, this.verse_number, "base");
        save_image_to_disk(base_64_image, this.verse_number, "edited");
        set_the_thumbnail(base_64_image);
        base_64_image.flush();
    }


    public BufferedImage getEditied_base_64_image() {
        return read_image_from_disk(this.verse_number, "edited");
    }

    public void setEditied_base_64_image(BufferedImage editied_base_64_image) {
        save_image_to_disk(editied_base_64_image, this.verse_number, "edited");
        set_the_thumbnail(editied_base_64_image);
        editied_base_64_image.flush();
    }

    public Ayat_settings getAyatSettings() {
        return ayatSettings;
    }

    public String getArabic_verse() {
        return arabic_verse;
    }

    public Image getThumbnail_vertical() {
        return thumbnail_vertical;
    }

    public Image getThumbnail_horizontal() {
        return thumbnail_horizontal;
    }

    public Image getThumbnail_square() {
        return thumbnail_square;
    }

    public boolean isImage_edited() {
        return image_edited;
    }

    public void setImage_edited(boolean image_edited) {
        this.image_edited = image_edited;
    }

    private void save_image_to_disk(BufferedImage bufferedImage, int verse_number, String sub_file) {
        String format = "bmp";
        try {
            Path outputPath = Paths.get("temp", "images", sub_file, verse_number + "." + format);
            File outputFile = outputPath.toFile();
            ImageIO.write(bufferedImage, format, outputFile); // format = "png", "jpg", etc.
            outputFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage read_image_from_disk(int verse_number, String sub_file) {
        BufferedImage image = null;
        String format = "bmp";
        try {
            Path inputPath = Paths.get("temp", "images", sub_file, verse_number + "." + format);
            File input = inputPath.toFile();
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

    private void set_the_thumbnail(BufferedImage bufferedImage) {
        int width = 45;
        int height = 80;
        if (pic_aspect_ratio == Pic_aspect_ratio.aspect_vertical_9_16) {
            width = 45;
            height = 80;
        } else if (pic_aspect_ratio == Pic_aspect_ratio.aspect_horizontal_16_9) {
            width = 80;
            height = 45;
        } else if (pic_aspect_ratio == Pic_aspect_ratio.aspect_square_1_1) {
            width = 45;
            height = 45;
        }
        try {
            BufferedImage thumbnail = Thumbnails.of(bufferedImage)
                    .size(width, height)
                    .asBufferedImage();
            this.thumbnail_vertical = buffer_image_to_image(thumbnail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private void copy_the_image(String directory){
        String format = "bmp";
        try {
            Path target_file_path = Paths.get("temp","images", directory, verse_number + "." + format);
            Path source_file_path = Paths.get("temp", "default_image", "default_image.bmp");
            Files.copy(source_file_path,target_file_path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
