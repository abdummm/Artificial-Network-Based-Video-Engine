module com.example.quranfx {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.swing;

    requires org.jsoup;

    requires org.bytedeco.javacv;
    requires org.bytedeco.ffmpeg;

    requires org.apache.commons.io;
    requires net.coobird.thumbnailator;
    requires com.drew.metadata;
    requires imgscalr.lib;
    requires com.jfoenix;

    requires io.github.humbleui.skija.shared;
    requires java.prefs;
    requires java.net.http;
    requires jave.core;
    requires java.desktop;
    requires org.controlsfx.controls;
    requires MaterialFX;
    requires tools.jackson.databind;
    requires okhttp3;
    requires annotations;

    opens com.example.quranfx to javafx.fxml;
    exports com.example.quranfx;
}