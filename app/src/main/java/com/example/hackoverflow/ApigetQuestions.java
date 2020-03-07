package com.example.hackoverflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApigetQuestions extends AsyncTask<Void , Void , Void> {

    String url = "https://mental2.azurewebsites.net/suggestiontest";
    public static String[] informtaion = {};

    ProgressDialog dialog;

    Context sContext;

    public ApigetQuestions(Context sContext) {
        this.sContext = sContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        dialog = new ProgressDialog(sContext);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected Void doInBackground(Void... voids) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url).get().build();

        Response response = null;

        try {
            response = client.newCall(request).execute();


            String jsonstr = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonstr);

            JSONArray jsonArray = jsonObject.getJSONArray("msg");

            for(int i = 0 ;i < jsonArray.length() ; i ++){

                informtaion[i] = jsonArray.get(i).toString();

            }




        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
