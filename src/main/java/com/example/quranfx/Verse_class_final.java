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

    private Ayat_settings ayatSettings;

    public Verse_class_final(String verse, Integer verse_number, BufferedImage base_64_image, long time_in_milliseconds, Ayat_settings ayatSettings, String arabic_verse) {
        this.verse = verse;
        this.verse_number = verse_number;
        this.time_in_milliseconds = time_in_milliseconds;
        this.ayatSettings = ayatSettings;
        this.arabic_verse = arabic_verse;
        save_image_to_disk(base_64_image,verse_number,"base");
        save_image_to_disk(base_64_image,verse_number,"edited");
        set_the_thumbnail(base_64_image);
        this.image_edited = false;
    }

    public String getVerse() {
        return verse;
    }

    public Integer getVerse_number() {
        return verse_number;
    }

    public BufferedImage getBase_64_image() {
        return read_image_from_disk(this.verse_number,"base");
    }

    public long getTime_in_milliseconds() {
        return time_in_milliseconds;
    }

    public void setTime_in_milliseconds(long time_in_milliseconds) {
        this.time_in_milliseconds = time_in_milliseconds;
    }

    public void setBase_64_image(BufferedImage base_64_image) {
        save_image_to_disk(base_64_image,this.verse_number,"base");
        save_image_to_disk(base_64_image,this.verse_number,"edited");
        set_the_thumbnail(base_64_image);
        base_64_image.flush();
    }


    public BufferedImage getEditied_base_64_image() {
        return read_image_from_disk(this.verse_number,"edited");
    }

    public void setEditied_base_64_image(BufferedImage editied_base_64_image) {
        save_image_to_disk(editied_base_64_image,this.verse_number,"edited");
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

    private void save_image_to_disk(BufferedImage bufferedImage,int verse_number,String sub_file){
        String format = "bmp";
        try {
            Path outputPath = Paths.get("temp", "images", sub_file, verse_number + "." + format);
            File outputFile = outputPath.toFile();
            ImageIO.write(bufferedImage, format, outputFile); // format = "png", "jpg", etc.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage read_image_from_disk(int verse_number,String sub_file){
        BufferedImage image = null;
        String format = "bmp";
        try {
            Path inputPath = Paths.get("temp", "images", sub_file, verse_number + "." + format);
            File input = inputPath.toFile();
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

    private void set_the_thumbnail(BufferedImage bufferedImage){
        try {
            BufferedImage thumbnail = Thumbnails.of(bufferedImage)
                    .size(45, 80)
                    .asBufferedImage();
            this.thumbnail_vertical = buffer_image_to_image(thumbnail);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isImage_edited() {
        return image_edited;
    }

    public void setImage_edited(boolean image_edited) {
        this.image_edited = image_edited;
    }
}
