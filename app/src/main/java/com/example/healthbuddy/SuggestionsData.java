package com.example.healthbuddy;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

public class SuggestionsData implements Serializable {
    ArrayList<String> AudioBooks;
    ArrayList<String> GeneralActivities;
    ArrayList<String> TextBooks;

    public SuggestionsData(ArrayList<String> audioBooks, ArrayList<String> generalActivities, ArrayList<String> textBooks) {
        AudioBooks = audioBooks;
        GeneralActivities = generalActivities;
        TextBooks = textBooks;
    }

    public ArrayList<String> getAudioBooks() {
        return AudioBooks;
    }

    public void setAudioBooks(ArrayList<String> audioBooks) {
        AudioBooks = audioBooks;
    }

    public ArrayList<String> getGeneralActivities() {
        return GeneralActivities;
    }

    public void setGeneralActivities(ArrayList<String> generalActivities) {
        GeneralActivities = generalActivities;
    }

    public ArrayList<String> getTextBooks() {
        return TextBooks;
    }

    public void setTextBooks(ArrayList<String> textBooks) {
        TextBooks = textBooks;
    }

    @NotNull
    @Override
    public String toString() {
        return "SuggestionsData{" +
                "AudioBooks=" + AudioBooks +
                ", GeneralActivities=" + GeneralActivities +
                ", TextBooks=" + TextBooks +
                '}';
    }
}
