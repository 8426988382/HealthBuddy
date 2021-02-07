package com.example.healthbuddy.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.healthbuddy.Model.StreakData;
import com.example.healthbuddy.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.R.layout.simple_list_item_1;

public class Meditation extends AppCompatActivity {


    private long timeCountInMilliSeconds = 600000;
    private FirebaseAuth mAuth;
//    private TextView MedText;

    private enum TimerStatus {
        STARTED,




        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;


    // button for sounds
    Button MoreSounds;
    int musicTrackNo = 1;
    TextView mediaName ;
//    String[] iMeditation ={
//            "Get Settled." , "Breathe deeply", "Consider the 'why' ", "Observe the breath" , "Allow your mind to be free."
//    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = this.getWindow();
        mAuth = FirebaseAuth.getInstance();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(Meditation.this , R.color.azure));

        setContentView(R.layout.activity_meditation);

        progressBarCircle = findViewById(R.id.progressBarCircle);
        editTextMinute =  findViewById(R.id.editTextMinute);
        textViewTime = findViewById(R.id.textViewTime);
        imageViewReset = findViewById(R.id.imageViewReset);
        imageViewStartStop = findViewById(R.id.imageViewStartStop);
        MoreSounds = findViewById(R.id.button2);
//        MedText = v.findViewById(R.id.medText);
        mediaName = findViewById(R.id.textView15);
        imageViewReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        imageViewStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStop();
            }
        });



        MoreSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Meditation.this, R.style.BottomSheetDialogTheme1);
                View bottomSheetView  = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.moresounds, (ConstraintLayout)v.findViewById(R.id.bottomsheetmoresound));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                ListView listView = bottomSheetDialog.findViewById(R.id.list);

                String[] values = new String[] { "Sound 1", "Sound 2", "Sound 3" };

                final ArrayList<String> list = new ArrayList<String>(Arrays.asList(values));


                final StableArrayAdapter adapter = new StableArrayAdapter(Meditation.this,
                        android.R.layout.simple_list_item_1, list);

                assert listView != null;
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                          musicTrackNo = position+1;
                         bottomSheetDialog.dismiss();

                        if(musicTrackNo==1) {

                            mediaName.setText("Sound1");
                        }
                        else if(musicTrackNo==2) {

                            mediaName.setText("Sound2");
                        }
                        else if(musicTrackNo==3) {
                             mediaName.setText("Sound3");
                        }
                        if(mediaPlayer!=null)
                            reset();
                    }

                });


                bottomSheetDialog.show();

            }
        });
    }




    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }

    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // MedText.setVisibility(View.INVISIBLE);

            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();
            // showing the reset icon
            imageViewReset.setVisibility(View.VISIBLE);
            // changing play icon to stop icon
            imageViewStartStop.setImageResource(R.drawable.icon_stop);
            // making edit text not editable
            editTextMinute.setEnabled(false);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;

            // call to start the count down timer
            startCountDownTimer();

        } else {

            // MedText.setVisibility(View.VISIBLE);

            // hiding the reset icon
            imageViewReset.setVisibility(View.GONE);
            // changing stop icon to start icon
            imageViewStartStop.setImageResource(R.drawable.icon_start);
            // making edit text editable
            editTextMinute.setEnabled(true);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
        }
    }
    private void setTimerValues() {
        int time = 0;
        if (!editTextMinute.getText().toString().isEmpty()) {
            // fetching value from edit text and type cast to integer
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            // toast message to fill edit text
            Toast.makeText(Objects.requireNonNull(getApplication()).getApplicationContext(),
                    getString(R.string.message_minutes),
                    Toast.LENGTH_LONG).show();
        }
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 100;
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                if(mediaPlayer== null){
                    if(musicTrackNo==1) {
                        mediaPlayer = MediaPlayer.create(Objects.requireNonNull(getApplication()).getApplicationContext(),
                                R.raw.sound1);
                        mediaName.setText("Sound1");
                    }
                    else if(musicTrackNo==2) {
                        mediaPlayer = MediaPlayer.create(Objects.requireNonNull(getApplication()).getApplicationContext(),
                                R.raw.sound2);
                        mediaName.setText("Sound2");
                    }
                    else if(musicTrackNo==3) {
                        mediaPlayer = MediaPlayer.create(Objects.requireNonNull(getApplication()).getApplicationContext(),
                                R.raw.sound3);
                        mediaName.setText("Sound3");
                    }

                    mediaPlayer.setLooping(true);
//                    mediaPlayer.start();

//                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//
//
//                        }
//                    });
                }
                mediaPlayer.start();

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the reset icon
                imageViewReset.setVisibility(View.GONE);
                // changing stop icon to start icon
                imageViewStartStop.setImageResource(R.drawable.icon_start);
                // making edit text editable
                editTextMinute.setEnabled(true);
                 timerStatus = TimerStatus.STOPPED;
                stopmusic();

                streakcomplete();
                BottomSheetUi();
            }

        }.start();
        countDownTimer.start();
    }

    private void BottomSheetUi() {
             final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme1);
            View bottomSheetView  = LayoutInflater.from(this.getApplicationContext())
                    .inflate(R.layout.setting_goal, (ConstraintLayout)findViewById(R.id.bottomsheetcontainer));
            //bottomSheetDialog.setContentView(R.layout.setting_goal);
            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.setCanceledOnTouchOutside(false);



            LottieAnimationView emo1 = bottomSheetDialog.findViewById(R.id.emo1);
            LottieAnimationView emo2 = bottomSheetDialog.findViewById(R.id.lottieAnimationView4);
            LottieAnimationView emo3 = bottomSheetDialog.findViewById(R.id.lottieAnimationView5);
            LottieAnimationView emo4 = bottomSheetDialog.findViewById(R.id.lottieAnimationView8);
            LottieAnimationView emo5 = bottomSheetDialog.findViewById(R.id.lottieAnimationView6);
            LottieAnimationView emo6 = bottomSheetDialog.findViewById(R.id.lottieAnimationView7);
            LottieAnimationView emo7 = bottomSheetDialog.findViewById(R.id.lottieAnimationView9);

            assert emo1 != null;
            emo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    finish();
                }
            });
            assert emo2 != null;
            emo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    finish();
                }
            });
            assert emo3 != null;
            emo3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();

                    finish();
                }
            });
            assert emo4 != null;
            emo4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    finish();
                }
            });
            assert emo5 != null;
            emo5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    finish();
                }
            });
            assert emo6 != null;
            emo6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    finish();
                }
            });
            assert emo7 != null;
            emo7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    finish();
                }
            });


            bottomSheetDialog.show();
    }

    private void streakcomplete() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR,-3);
        Date c = calendar.getTime();
//        c.as;
        System.out.println("Current time => " + c);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        DatabaseReference myRef  = database.getReference(mAuth.getUid()+"/record/"+df.format(c));
        myRef.setValue(  new StreakData( sdf.format(c),df.format(c), (long) c.getTime(),timeCountInMilliSeconds*10));
    }

    private void stopmusic(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();

            mediaPlayer=null;
        }
    }
    private void stopCountDownTimer() {
        if(countDownTimer!=null) {
            countDownTimer.cancel();
            stopmusic();
        }
    }
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }
    private String hmsTimeFormatter(long milliSeconds) {

        @SuppressLint("DefaultLocale") String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;
    }



    @Override
    public void onStop() {
        super.onStop();
        stopCountDownTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCountDownTimer();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        stopCountDownTimer();
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
