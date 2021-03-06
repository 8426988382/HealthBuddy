package com.example.healthbuddy.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIGetBotResponse extends AsyncTask<Void , Void , String>{

    String url = "https://mentalheaalthapi.azurewebsites.net/Bot";

    String message, id;
    String responce_from_server = null;
    ResponseInterface responseInterface= null;
    WeakReference<Context> contextRef;



    public APIGetBotResponse(String message, String id, Context context) {
        this.message = message;
        contextRef =new WeakReference<> (context);

        this.id= id;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        //responseInterface= (ResponseInterface) contextRef.get();

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();


        JSONObject object = new JSONObject();

        String output = message.substring(0, 1).toUpperCase() + message.substring(1);

        try {
            object.put("msg", output);
            object.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("JSON MESSAGE OBJ" , object.toString());


        RequestBody body = RequestBody.create(MEDIA_TYPE, object.toString());

        Request request = new Request.Builder()
                .url(url).post(body).build();

        Response response = null;
        String json = null;
        JSONObject jsonObject = null;

        try {
            response = client.newCall(request).execute();
            json = Objects.requireNonNull(response.body()).string();


            jsonObject = new JSONObject(json);

        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        assert json != null;
        Log.e("tag", json);



        String mMessage = null;
        try {
            assert jsonObject != null;
            mMessage = jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert mMessage != null;
        Log.e("MESSAGE" , mMessage);

        responce_from_server = mMessage;

        return responce_from_server;
    }


}
