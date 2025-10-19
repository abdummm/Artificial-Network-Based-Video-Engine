package com.example.quranfx;

import javafx.beans.value.ChangeListener;

public class Listener_info {
    private ChangeListener<Number> change_listener;
    private boolean listener_set = false;
    private boolean listener_attached = false;
    private Listener_type listener_type;

    public Listener_info(boolean listener_set, boolean listener_attached, Listener_type listener_type) {
        this.listener_set = listener_set;
        this.listener_attached = listener_attached;
        this.listener_type = listener_type;
    }

    public ChangeListener<Number> getChange_listener() {
        return change_listener;
    }

    public void setChange_listener(ChangeListener<Number> change_listener) {
        this.change_listener = change_listener;
    }

    public boolean isListener_set() {
        return listener_set;
    }

    public void setListener_set(boolean listener_set) {
        this.listener_set = listener_set;
    }

    public boolean isListener_attached() {
        return listener_attached;
    }

    public void setListener_attached(boolean listener_attached) {
        this.listener_attached = listener_attached;
    }

    public Listener_type getListener_type() {
        return listener_type;
    }

    public void setListener_type(Listener_type listener_type) {
        this.listener_type = listener_type;
    }
}
