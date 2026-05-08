module com.example.quranfx {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;

    requires okhttp3;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.jsoup;

    requires org.bytedeco.javacv;
    requires org.bytedeco.ffmpeg;

    requires org.apache.commons.io;
    requires thumbnailator;
    requires metadata.extractor;
    requires imgscalr.lib;
    requires com.jfoenix;

    requires io.github.humbleui.skija.shared;
    requires java.prefs;
    requires java.net.http;
    requires jave.core;
    requires annotations;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires MaterialFX;

    opens com.example.quranfx to javafx.fxml;
    exports com.example.quranfx;
}