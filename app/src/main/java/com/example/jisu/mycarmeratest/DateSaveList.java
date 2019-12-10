package com.example.jisu.mycarmeratest;

import java.io.Serializable;

public class DateSaveList implements Serializable {
    private static final long serialVersionUID = 1L;

    private String imagepsth;
    private String text;

    public DateSaveList() { }

    public DateSaveList(String imagepsth, String text) {
        this.imagepsth = imagepsth;
        this.text = text;
    }

    public String getImagepsth() {
        return imagepsth;
    }

    public void setImagepsth(String imagepsth) {
        this.imagepsth = imagepsth;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DateSaveList{" + "imagepsth='" + imagepsth + '\'' + ", text='" + text + '\'' + '}';
    }
}
