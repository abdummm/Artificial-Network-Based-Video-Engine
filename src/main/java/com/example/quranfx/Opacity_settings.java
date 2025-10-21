package com.example.quranfx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.concurrent.TimeUnit;

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
        this.opacity_change_listener = new Listener_info(false, false, Listener_type.OPACITY_LISTENER);
        this.fade_in_change_listener = new Listener_info(false, false, Listener_type.FADE_IN_LISTENER);
        this.fade_out_change_listener = new Listener_info(false, false, Listener_type.FADE_OUT_LISTENER);
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

    public double return_total_opacity(double time_since_start, double time_till_end) {
        double opacity_multiplier = opacity / 100D;
        double fade_in_multiplier;
        double fade_out_multiplier;
        if (fade_in == 0) {
            fade_in_multiplier = 1;
        } else {
            fade_in_multiplier = Math.min(1, time_since_start / (fade_in * TimeUnit.SECONDS.toNanos(1)));
        }
        if (fade_out == 0) {
            fade_out_multiplier = 1;
        } else {
            fade_out_multiplier = Math.min(1, time_till_end / (fade_out * TimeUnit.SECONDS.toNanos(1)));
        }
        return 1 - (opacity_multiplier * fade_in_multiplier * fade_out_multiplier);
    }
}
