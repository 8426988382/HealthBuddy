package com.example.healthbuddy.Api;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.healthbuddy.Model.SuggestionsData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("deprecation")
public class ApiGetSuggestions extends AsyncTask<Void, Void, ArrayList<SuggestionsData>> {
    Context sContext;

    ProgressDialog dialog;
    private WeakReference<Context> weakReference;
    ArrayList<SuggestionsData> data = new ArrayList<>();
    SuggestionResponse suggestionResponse = null;
    Bitmap image;
    public ApiGetSuggestions(Context context, Bitmap image) {
        weakReference = new WeakReference<>(context);
          this.image = image;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        sContext= weakReference.get();

         dialog = new ProgressDialog(sContext);
        dialog.setMessage("Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected ArrayList<SuggestionsData> doInBackground(Void... voids) {
        suggestionResponse= (SuggestionResponse) weakReference.get();

        ArrayList<String> AudioBooks = new ArrayList<>();
        ArrayList<String> GeneralActivities = new ArrayList<>();
        ArrayList<String> TextBooks = new ArrayList<>();


        OkHttpClient client = new OkHttpClient();
        MediaType MEDIA_TYPE = MediaType.parse("application/json");


        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, jsonObject.toString());
        String url = "https://mentalheaalthapi.azurewebsites.net/suggestion";
        Request request = new Request.Builder()
                .url(url).post(body).build();

        Response response;

        String responseString;
        JSONObject jsonObject1;

        JSONObject AudioJson, GeneralJson, TextBookJson;


        try {
            response = client.newCall(request).execute();
            responseString = Objects.requireNonNull(response.body()).string();

            jsonObject1 = new JSONObject(responseString);

            AudioJson = new JSONObject(jsonObject1.getString("audiobook"));
            GeneralJson = new JSONObject(jsonObject1.getString("general"));
            TextBookJson = new JSONObject(jsonObject1.getString("testbook"));

            Log.e("Audio ", AudioJson.toString());
            Log.e("General ", GeneralJson.toString());
            Log.e("Text ", TextBookJson.toString());


            Iterator<String> key1 = AudioJson.keys();
            while (key1.hasNext()) {
                AudioBooks.add(key1.next());
            }
            Iterator<String> key2 = GeneralJson.keys();
            while (key2.hasNext()) {
                GeneralActivities.add(key2.next());
            }
            Iterator<String> key3 = TextBookJson.keys();
            while (key3.hasNext()) {
                TextBooks.add(key3.next());
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        data.add(new SuggestionsData(AudioBooks, GeneralActivities, TextBooks));
        return data;
    }
    @Override
    protected void onPostExecute(ArrayList<SuggestionsData> aVoid) {
        super.onPostExecute(aVoid);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        suggestionResponse.suggestionResponse(aVoid,image);
    }


}
