package com.example.healthbuddy;

import java.util.ArrayList;
import java.util.Map;

public class QuestionData {
    private String Question;
   private Map<String, String> mp;

    public QuestionData(String question, Map mp) {
        Question = question;
        this.mp = mp;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public Map<String, String> getMp() {
        return mp;
    }

    public void setMp(Map<String, String> mp) {
        this.mp = mp;
    }

    @Override
    public String toString() {
        return "QuestionData{" +
                "Question='" + Question + '\'' +
                ", mp=" + mp +
                '}';
    }
}
