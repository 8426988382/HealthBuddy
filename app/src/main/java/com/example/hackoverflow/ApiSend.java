package com.example.hackoverflow;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiSend extends AsyncTask<Void , Void , Void>{

    String url = "https://mentalfinal.azurewebsites.net/Askme";

    String message = null;

    public ApiSend(String message) {
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();


        Log.e("RECEIVED MESSAGE " , message);

        JSONObject object = new JSONObject();

        try {
            object.put("msg", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JSON MESSAGE OBJ" , object.toString());


        RequestBody body = RequestBody.create(MEDIA_TYPE, object.toString());

        Request request = new Request.Builder()
                .url(url).post(body).build();

        Response response = null;

        try {
            response = client.newCall(request).execute();

            String  mMessage = response.body().string();

            Log.e("MESSAGE " , mMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //responce_from_server = mMessage;


        return null;
    }
}
