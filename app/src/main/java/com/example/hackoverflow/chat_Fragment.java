package com.example.hackoverflow;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chat_Fragment extends Fragment {

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;

    String mMessage = null;

    String responce_from_server = null;
    int flag = 1;

    ProgressBar loadingView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.chat_fragment, container, false);

        listView = v.findViewById(R.id.msgview);
        loadingView = v.findViewById(R.id.loading_spinner);

        chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.left);
        listView.setAdapter(chatArrayAdapter);

        if(savedInstanceState == null){
            chatArrayAdapter.add(new ChatMessage(true , "Hello Tushar,"));
            chatArrayAdapter.add(new ChatMessage(true , "How can We Help You?"));
            chatArrayAdapter.add(new ChatMessage(true , "Have Some Questions? Ask!"));
        }


        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.slide_down), 2.0f); //0.5f == time between appearance of listview items.
        listView.setLayoutAnimation(lac);

        listView.startLayoutAnimation();
        buttonSend = v.findViewById(R.id.send);

        chatText = v.findViewById(R.id.msg);


        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });


            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    if(chatText.getText().toString().trim().equals("")){

                    }
                    else{
                        side = false;
                        sendChatMessage();

//                        apirequest apirequest = new apirequest();
//                        apirequest.execute(chatText.getText().toString().trim());


                    }

                }
            });

        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });


        if(responce_from_server != null){
            side = true;
            addChatMessage();
        }else{
           side = false;
        }

        return v;
    }


    private boolean sendChatMessage() {

        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");

        return true;
    }

    private boolean addChatMessage(){
        chatArrayAdapter.add(new ChatMessage(side , responce_from_server));

        return true;
    }


    // async task to connect to the api for generting request

    public class apirequest extends AsyncTask<String , Void , Void>{

        String url = "abc.com"; // url for the api
        Context context = getActivity().getApplicationContext();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... voids) {

            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            OkHttpClient client = new OkHttpClient();
            String message = voids[0];

            JSONObject object = new JSONObject();

            try {
                object.put("message", message);
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

                mMessage = response.body().string();

                Log.e("MESSAGE " , mMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            responce_from_server = mMessage;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
