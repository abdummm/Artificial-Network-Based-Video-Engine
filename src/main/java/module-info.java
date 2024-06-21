module com.example.quranfx {
    requires javafx.controls;
    requires javafx.fxml;
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
    requires java.desktop;
    requires javafx.swing;
    requires org.bytedeco.javacv;
    requires org.bytedeco.ffmpeg;

    opens com.example.quranfx to javafx.fxml;
    exports com.example.quranfx;
}