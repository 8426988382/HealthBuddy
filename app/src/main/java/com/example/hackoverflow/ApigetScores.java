package com.example.hackoverflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class ApigetScores extends AsyncTask<Void , Void , Void> {

    String url = "https://mental2.azurewebsites.net/suggestion";
    int score;

    public static ArrayList<String> AudioBooks;
    public static ArrayList<String> TextBooks;
    public static ArrayList<String> Meditations;
    public static ArrayList<String> Activity;

    ProgressDialog dialog;

    Context sContext;

    public ApigetScores(Context context , int score) {
        this.sContext = context;
        this.score = score;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        AudioBooks = new ArrayList<String>();
        TextBooks = new ArrayList<String>();
        Meditations = new ArrayList<String>();
        Activity = new ArrayList<String>();
        dialog = new ProgressDialog(sContext);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        dialog.show();

    }


    @Override
    protected Void doInBackground(Void... voids) {


        String jsondata;
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        OkHttpClient client = new OkHttpClient();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("score" , score);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url).post(body).build();

        Response response = null;

        try {
            response = client.newCall(request).execute();

            jsondata = response.body().string();

            Log.e("MESSAGE " , jsondata);

            JSONObject jsonObject1 = new JSONObject(jsondata);

            JSONArray array1 = jsonObject1.getJSONArray("Audio Books");
            JSONArray array2 = jsonObject1.getJSONArray("Text Books");
            JSONArray array3 = jsonObject1.getJSONArray("Meditation Techniques");
            JSONArray array4 = jsonObject1.getJSONArray("ACtivities");
            Log.e(TAG, "doInBackground: "+array1.length() );
            if(array1.length() == 0){
                AudioBooks.add(null);
            }
            else{
                for(int i = 0; i < array1.length() ;i ++){
                    AudioBooks.add(array1.get(i).toString());
                }
            }

            if(array2.length() == 0){
                TextBooks.add(null);
            }
            else{
                for(int i = 0; i < array2.length() ;i ++){
                    TextBooks.add(array2.get(i).toString());
                }
            }


            if(array3.length() == 0){
                Meditations.add(null);
            }
            else{
                for(int i = 0; i < array3.length() ;i ++){
                    Meditations.add(array3.get(i).toString());
                }
            }

            if(array4.length() == 0){
                Activity.add(null);
            }
            else{
                for(int i = 0; i < array4.length() ;i ++){
                    Activity.add(array4.get(i).toString());
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(dialog.isShowing()){
            dialog.cancel();
        }
    }
}
