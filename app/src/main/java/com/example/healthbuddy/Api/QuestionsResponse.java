package com.example.healthbuddy.Api;

import com.example.healthbuddy.Model.QuestionData;

import java.util.ArrayList;

public interface QuestionsResponse {

    void getQuestions(ArrayList<QuestionData> data);
}
