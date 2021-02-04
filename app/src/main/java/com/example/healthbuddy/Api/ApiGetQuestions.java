package com.example.healthbuddy.Api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.healthbuddy.Model.QuestionData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiGetQuestions extends AsyncTask<Void, Void, ArrayList> {

    String url =   "https://mentalheaalthapi.azurewebsites.net/question";

    ArrayList<QuestionData> QuestionList = new ArrayList<>();
    public QuestionsResponse questionsResponse = null;
    ProgressDialog dialog;
    WeakReference<Context> weakReference;
    SharedPreferences Prefs;
    public static final String SHARED_PREF = "shared_prefs";
    private String uid;

    public ApiGetQuestions(Context sContext, String uid) {
        weakReference = new WeakReference<>(sContext);
        this.uid = uid;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        questionsResponse = (QuestionsResponse) weakReference.get();
        Context sContext = weakReference.get();
        Prefs = sContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Date date = new Date(System.currentTimeMillis());
        Prefs.edit().putLong("FirstTime", date.getTime()).apply();
        dialog = new ProgressDialog(sContext);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected ArrayList doInBackground(Void... voids) {


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(MEDIA_TYPE, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url).post(body).build();

        Response response;

        JSONObject responseJson = null;
        String responseString;

        try {
            response = client.newCall(request).execute();
            responseString = Objects.requireNonNull(response.body()).string();
            Log.e("Questions", responseString);

            responseJson = new JSONObject(responseString);
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        try {
             String mMessage = responseJson.getString("msg");

            Log.e("MESSAGE", mMessage);
            responseJson = new JSONObject(mMessage);



            for (int i=0;i<5;i++){
                ArrayList<String> arrayList = new ArrayList<>();
                Map<String, String> mp = new HashMap<>();
                JSONArray jsonArray = (JSONArray) responseJson.get(String.valueOf(i));
                arrayList.add(String.valueOf(jsonArray.get(0)));
                arrayList.add(String.valueOf(jsonArray.get(1)));
                mp.put(arrayList.get(0), arrayList.get(1));
                QuestionList.add(new QuestionData(arrayList.get(0),mp));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }





        return QuestionList;
    }

    @Override
    protected void onPostExecute(ArrayList aVoid) {
        super.onPostExecute(aVoid);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
           questionsResponse.getQuestions(QuestionList);

    }
}


