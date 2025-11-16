module com.example.quranfx {
    requires javafx.web;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires okhttp3;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.jsoup;
    requires javafx.swing;
    requires org.bytedeco.javacv;
    requires org.bytedeco.ffmpeg;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires thumbnailator;
    requires metadata.extractor;
    requires imgscalr.lib;
    requires com.jfoenix;
    requires org.bytedeco.opencv;
    requires atlantafx.base;
    requires MaterialFX;
    requires jdk.compiler;
    requires annotations;
    requires javafx.base;
    requires javafx.graphics;
    requires org.checkerframework.checker.qual;
    requires io.github.humbleui.skija.shared;
    requires io.github.humbleui.types;


    opens com.example.quranfx to javafx.fxml;
    exports com.example.quranfx;
}