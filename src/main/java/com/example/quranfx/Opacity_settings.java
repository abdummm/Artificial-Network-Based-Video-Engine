package com.example.quranfx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Opacity_settings {
    private double opacity;
    private double fade_in;
    private double fade_out;
    private Listener_info opacity_change_listener;
    private Listener_info fade_in_change_listener;
    private Listener_info fade_out_change_listener;

    public Opacity_settings() {
        this.opacity = 100.0;
        this.fade_in = 0;
        this.fade_out = 0;
        this.opacity_change_listener = new Listener_info(false,false,Listener_type.OPACITY_LISTENER);
        this.fade_in_change_listener = new Listener_info(false,false,Listener_type.FADE_IN_LISTENER);
        this.fade_out_change_listener = new Listener_info(false,false,Listener_type.FADE_OUT_LISTENER);
    }

    public Listener_info getOpacity_change_listener() {
        return opacity_change_listener;
    }

    public Listener_info getFade_in_change_listener() {
        return fade_in_change_listener;
    }

    public Listener_info getFade_out_change_listener() {
        return fade_out_change_listener;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public double getFade_in() {
        return fade_in;
    }

    public void setFade_in(double fade_in) {
        this.fade_in = fade_in;
    }

    public double getFade_out() {
        return fade_out;
    }

    public void setFade_out(double fade_out) {
        this.fade_out = fade_out;
    }
}
