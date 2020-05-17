package com.example.hackoverflow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class chat_Fragment extends Fragment implements TextToSpeech.OnInitListener {

    public ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;


    ImageView pfile_img;


    private TextView voiceInput;
    private ImageView speakButton;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    String mMessage = null;

    String responce_from_server = null;
    int flag = 1;

    TextToSpeech TTS;



    private int shortAnimationDuration = 2;
    ProgressBar loadingView;

    ImageView speaker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.chat_fragment, container, false);


        listView = v.findViewById(R.id.msgview);
        speaker = v.findViewById(R.id.speaker_id);

//        loadingView = v.findViewById(R.id.loading_spinner);

        chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.left);
        listView.setAdapter(chatArrayAdapter);
        if (savedInstanceState == null) {
            chatArrayAdapter.add(new ChatMessage(true, "Hello There,"));
            chatArrayAdapter.add(new ChatMessage(true, "How can We Help You?"));
            chatArrayAdapter.add(new ChatMessage(true, "Have Some Questions? Ask!"));
        }


        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down), 2.0f); //0.5f == time between appearance of listview items.
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();


        buttonSend = v.findViewById(R.id.send);

        chatText = v.findViewById(R.id.msg);

        Context context = getActivity().getApplicationContext();

        TTS = new TextToSpeech(getActivity() , this);

        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TTS.isSpeaking()){
                    speaker.setImageDrawable(getResources().getDrawable(R.drawable.speacker));
                    stopIt();

                }
                else if(responce_from_server != null){
                    speakIt();
                    speaker.setImageDrawable(getResources().getDrawable(R.drawable.icon_stop));

                }
                else{

                }


            }
        });




        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        return sendChatMessage();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (chatText.getText().toString().trim().equals("")) {

                } else {
                    try {
                        sendChatMessage();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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

        speakButton = v.findViewById(R.id.btnspeak);

        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
                speaker.setImageDrawable(getResources().getDrawable(R.drawable.speacker));
            }
        });


        return v;
    }


    private boolean sendChatMessage() throws ExecutionException, InterruptedException {

        chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));

        String tosend =  chatText.getEditableText().toString();
        chatText.setText("");

        apisend apisend = new apisend(tosend);
        String response = apisend.execute().get();

        chatArrayAdapter.add(new ChatMessage(true , response));

        //  Log.e(TAG, "onCreateView: " + responce_from_server );



        return true;
    }


    public class apisend extends AsyncTask<Void , Void , String>{

        String url = "https://mental2.azurewebsites.net/Askme";

        String message = null;

        public apisend(String message) {
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            OkHttpClient client = new OkHttpClient();

            JSONObject object = new JSONObject();

            try {
                object.put("score" , "1");
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

            } catch (IOException e) {
                e.printStackTrace();
            }

            String json = null;
            JSONObject jsonObject = null;
            try {
                json = response.body().string();
                jsonObject = new JSONObject(json);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }



            String mMessage = null;
            try {
                mMessage = jsonObject.getString("msg");



            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("MESSAEG" , mMessage);

            responce_from_server = mMessage;

            return responce_from_server;
        }
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            int result = TTS.setLanguage(Locale.getDefault());
            TTS.setSpeechRate(-900);
            TTS.setPitch(0);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS_LANG_ERROR", "Language not supported");
            } else {
                speaker.setEnabled(true);
                speakIt();
            }
        } else {
            Log.e("TTS", "InitializationError");
        }
    }

    public void speakIt()
    {
        String text = responce_from_server;
        speaker.setImageDrawable(getResources().getDrawable(R.drawable.speacker));
        TTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);

    }

    public void stopIt(){

        TTS.stop();

    }


    private void askSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Hi speak something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    chatText.setText(result.get(0));
                }
                break;
            }

        }
    }


}
