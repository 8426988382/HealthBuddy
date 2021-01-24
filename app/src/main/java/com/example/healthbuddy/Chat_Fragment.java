package com.example.healthbuddy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

@SuppressWarnings("ALL")
public class Chat_Fragment extends Fragment implements TextToSpeech.OnInitListener , ResponseInterface{

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private TextToSpeech TTS;
    private ImageView speaker;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    String response_from_server= null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.chat_fragment, container, false);

        listView = v.findViewById(R.id.msgview);
        speaker = v.findViewById(R.id.speaker_id);
        Button buttonSend = v.findViewById(R.id.send);
        chatText = v.findViewById(R.id.msg);
        ImageView speakButton = v.findViewById(R.id.btnspeak);

        chatArrayAdapter = new ChatArrayAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), R.layout.left);
        listView.setAdapter(chatArrayAdapter);
        if (savedInstanceState == null) {
            chatArrayAdapter.add(new ChatMessage(true, "Hello There"));
            chatArrayAdapter.add(new ChatMessage(true, "I am your buddy"));
//            chatArrayAdapter.add(new ChatMessage(true, "How can We Help You?"));
//            chatArrayAdapter.add(new ChatMessage(true, "Have Some Questions?"));
//            chatArrayAdapter.add(new ChatMessage(true, "Please Ask!"));
        }

        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down), 2.0f); //0.5f == time between appearance of listview items.
        listView.setLayoutAnimation(lac);
        listView.startLayoutAnimation();

        TTS = new TextToSpeech(getActivity() , this);

        speaker.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View view) {

                if(TTS.isSpeaking()){
                    speaker.setImageDrawable(getResources().getDrawable(R.drawable.speacker));
                    stopIt();
                }
                else if(response_from_server != null){
                    speakIt();
                }

            }
        });


        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    try {
                        return sendChatMessage();
                    } catch (ExecutionException | InterruptedException e) {
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
                    // do nothing
                    chatText.setText("");
                } else {
                    try {
                        sendChatMessage();
                    } catch (ExecutionException | InterruptedException e) {
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

        speakButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                askSpeechInput();
                speaker.setImageDrawable(getResources().getDrawable(R.drawable.speacker));
            }
        });


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private boolean sendChatMessage() throws ExecutionException, InterruptedException {

        chatArrayAdapter.add(new ChatMessage(false, chatText.getText().toString()));

        String tosend =  chatText.getEditableText().toString();
        chatText.setText("");

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        String Uid;

        if(account != null){

            Uid= account.getId();
           new ApiSend(tosend, Uid, getActivity()){
               @Override
               protected void onPostExecute(String response) {
                   super.onPostExecute(response);
                   response_from_server = response;
                   chatArrayAdapter.add(new ChatMessage(true , response));
//                   listView.setAdapter(chatArrayAdapter);
               }
           }.execute();


            return  true;
        }else{
            return false;
        }
    }



    @Override
    public void getResponseMessage(String response) {
        Log.e(TAG, "getResponseMessage: "+response );

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
            }
//            } else {
//                speaker.setEnabled(true);
//                speakIt();
//            }

            TTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    speaker.setImageDrawable(getResources().getDrawable(R.drawable.icon_stop));
                }

                @Override
                public void onDone(String utteranceId) {
                    speaker.setImageDrawable(getResources().getDrawable(R.drawable.speacker));
                }

                @Override
                public void onError(String utteranceId) {

                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TTS.speak(response_from_server,TextToSpeech.QUEUE_FLUSH,null,TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
            }

        } else {
            Log.e("TTS", "InitializationError");
        }
    }

    public void speakIt()
    {
        String text = response_from_server;
        //speaker.setImageDrawable(getResources().getDrawable(R.drawable.icon_stop));
       // speaker.setImageDrawable(getResources().getDrawable(R.drawable.speacker));
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
                    assert result != null;
                    chatText.setText(result.get(0));
                }
                break;
            }

        }
    }


}
