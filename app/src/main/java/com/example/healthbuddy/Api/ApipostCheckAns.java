package com.example.healthbuddy.Api;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.example.healthbuddy.Model.QuestionData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApipostCheckAns extends AsyncTask<Void, Void, JSONObject> {

    String url = "https://mentalheaalthapi.azurewebsites.net/Checkquestionans";

     public CheckAnsResponse checkAnsResponse = null;
    ProgressDialog dialog;
    WeakReference<Context> weakReference;

     private JSONObject send;
    String Loadingtext;
    public ApipostCheckAns(Context sContext, JSONObject send,String Loadingtext) {
        weakReference = new WeakReference<>(sContext);
        this.send = send;
        this.Loadingtext = Loadingtext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        questionsResponse = (QuestionsResponse) weakReference.get();
        Context sContext = weakReference.get();
 ;
        dialog = new ProgressDialog(sContext);
        dialog.setMessage(Loadingtext);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    protected JSONObject doInBackground(Void... voids) {




        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(MEDIA_TYPE, send.toString());
        Request request = new Request.Builder()
                .url(url).post(body).build();

        Response response;

        JSONObject responseJson = null;
        String responseString;

        try {
            response = client.newCall(request).execute();
            responseString = Objects.requireNonNull(response.body()).string();
            Log.e("Questions", responseString);

            return new JSONObject(responseString);
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }







        return null;
    }

    @Override
    protected void onPostExecute(JSONObject aVoid) {
        super.onPostExecute(aVoid);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        checkAnsResponse.getAnsResponse(aVoid);

    }
}


