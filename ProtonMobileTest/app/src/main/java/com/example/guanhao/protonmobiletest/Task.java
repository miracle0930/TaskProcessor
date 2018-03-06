package com.example.guanhao.protonmobiletest;

import android.graphics.Color;
import android.os.CountDownTimer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by guanhao on 2018/2/7.
 */

public class Task implements Serializable {

    private String name;
    private String description;
    private List<String> keywordsList;
    private Set<String> keywordsSet;
    private String status;
    private String buttonText;
    private int buttonColor;
    private CountDownTimer timer;

    Task(String name) {
        this.name = name;
        this.keywordsList = new ArrayList<>();
        this.keywordsSet = new HashSet<>();
        this.status = "Ready";
        this.buttonText = "Cancel";
        this.buttonColor = Color.RED;
        this.timer = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setKeywordsList(List<String> keywordsList) {
        this.keywordsList = keywordsList;
    }

    void setKeywordsSet(Set<String> keywordsSet) {
        this.keywordsSet = keywordsSet;
    }

    void setStatus(String status) {
        this.status = status;
    }

    void setButtonText(String text) {
        this.buttonText = text;
    }

    void setButtonColor(int color) {
        this.buttonColor = color;
    }

    void setTimer(CountDownTimer timer) {
        this.timer = timer;
    }

    public String getName() {
        return this.name;
    }

    String getDescription() {
        return this.description;
    }

    List<String> getKeywordsList() {
        return this.keywordsList;
    }

    Set<String> getKeywordsSet() {
        return this.keywordsSet;
    }

    String getStatus() {
        return this.status;
    }

    String getButtonText() {
        return this.buttonText;
    }

    int getButtonColor() {
        return this.buttonColor;
    }

    CountDownTimer getTimer() {
        return this.timer;
    }


    @Override
    public String toString() {
        return "name: " + this.name + ", description: " + this.description + ", keywords: " + this.keywordsList;
    }
}
