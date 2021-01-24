package com.example.healthbuddy;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Meditate_Fragment extends Fragment {


    private long timeCountInMilliSeconds = 60000;

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

//    String[] iMeditation ={
//            "Get Settled." , "Breathe deeply", "Consider the 'why' ", "Observe the breath" , "Allow your mind to be free."
//    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mediate_fragment, container, false);

        progressBarCircle = v.findViewById(R.id.progressBarCircle);
        editTextMinute =  v.findViewById(R.id.editTextMinute);
        textViewTime = v.findViewById(R.id.textViewTime);
        imageViewReset = v.findViewById(R.id.imageViewReset);
        imageViewStartStop = v.findViewById(R.id.imageViewStartStop);
//        MedText = v.findViewById(R.id.medText);

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

        return v;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(),
                    getString(R.string.message_minutes),
                    Toast.LENGTH_LONG).show();
        }
        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                if(mediaPlayer== null){
                    mediaPlayer = MediaPlayer.create(Objects.requireNonNull(getActivity()).getApplicationContext(),
                            R.raw.softrain);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();


                        }
                    });
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
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
                stopmusic();
            }

        }.start();
        countDownTimer.start();
    }

    private void stopmusic(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();

            mediaPlayer=null;
        }
    }
    private void stopCountDownTimer() {
        countDownTimer.cancel();
        stopmusic();
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
        stopmusic();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopmusic();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopmusic();
    }
}
