package com.example.healthbuddy.UI;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.healthbuddy.Model.StreakData;
import com.example.healthbuddy.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class Meditate_Fragment extends Fragment {

    Button mMeditation;

    LottieAnimationView lottieAnimationView;
    TextView streakLevelText, streakNoText;
    Button mPremium;
    View Contextview;
    // SetGoal Card
//    CardView mSetGoalCard;
    TextView textView;
    EditText getgoalTime;
    ImageView img;
    RadioButton monRadio, tueRadio, wedRadio, thuRadio, friRadio, satRadio, sunRadio;
    private FirebaseAuth mAuth;
    ArrayList<StreakData> streakDataArrayList;
    Button setgoalbutton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LineChart mplinechart;
    ArrayList<Entry> chartData;
    ArrayList<String> xaxislable;

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        View v = inflater.inflate(R.layout.mediate_fragment, container, false);
        Contextview = v;
        // initialise
        mMeditation = v.findViewById(R.id.startMeditation_id);
        lottieAnimationView = v.findViewById(R.id.congo);
        mPremium = v.findViewById(R.id.button);
//        mSetGoalCard = v.findViewById(R.id.setGoal_Card);
//        textView = v.findViewById(R.id.textView17);
//        getgoalTime = v.findViewById(R.id.editText3);
//        img = v.findViewById(R.id.imageView3);
        chartData = new ArrayList<Entry>();

//        setgoalbutton = v.findViewById(R.id.button3);
        streakNoText = v.findViewById(R.id.textView14);
        streakLevelText = v.findViewById(R.id.textView12);
        monRadio = v.findViewById(R.id.radioButton1);
        tueRadio = v.findViewById(R.id.radioButton2);
        wedRadio = v.findViewById(R.id.radioButton3);
        thuRadio = v.findViewById(R.id.radioButton4);
        friRadio = v.findViewById(R.id.radioButton5);
        satRadio = v.findViewById(R.id.radioButton6);
        sunRadio = v.findViewById(R.id.radioButton7);
        Log.e("test", "onCreateView: ");
        streakDataArrayList = new ArrayList<>();
        xaxislable = new ArrayList<>();

        getFirebaseData();

        mMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetui(v);

            }
        });

        mPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Work in Progress", Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }


    private void updateUiFromData() {
        int strek = 0;
        if (streakDataArrayList != null) {
            Collections.reverse(streakDataArrayList);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            strek = 0;
            for (StreakData data : streakDataArrayList) {
                Log.e(TAG, "updateUiFromData: " + data.getDate());
                if (df.format(calendar.getTime()).equals(data.getDate())) {
                    if (data.getDay().equals("Monday"))
                        monRadio.toggle();
                    if (data.getDay().equals("Tuesday"))
                        tueRadio.toggle();
                    if (data.getDay().equals("Wednesday"))
                        wedRadio.toggle();
                    if (data.getDay().equals("Thursday"))
                        thuRadio.toggle();
                    if (data.getDay().equals("Friday"))
                        friRadio.toggle();
                    if (data.getDay().equals("Saturday"))
                        satRadio.toggle();
                    if (data.getDay().equals("Sunday"))
                        sunRadio.toggle();

                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                    strek += 1;
                } else
                    break;


            }

            streakNoText.setText(String.valueOf(strek));
            if (strek < 20) {
                streakLevelText.setText("Beginner");
            } else if (strek < 40) {
                streakLevelText.setText("Intermediate");
            } else
                streakLevelText.setText("Advance");
        }
        editor.putInt("streak", strek).apply();

        drawChart();
    }

    private void drawChart() {
        if (mplinechart != null) {


            mplinechart.clear();
//            mplinechart.validate();
        }
        mplinechart = Contextview.findViewById(R.id.chart1);

        LineDataSet lineDataSet = new LineDataSet(chartData, "Meditation everyday");
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setColor(ContextCompat.getColor(getContext(), R.color.green));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setCubicIntensity((float) 0);

        lineDataSet.setFillColor(ContextCompat.getColor(getContext(), R.color.green));

        YAxis rightaxis = mplinechart.getAxisRight();
        rightaxis.setEnabled(false);
//        mplinechart.setDrawGridBackground(true);
        XAxis xAxis = mplinechart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xaxislable));


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        data.setHighlightEnabled(false);
        data.setDrawValues(false);

        mplinechart.setData(data);
        mplinechart.getDescription().setEnabled(false);
//        mplinechart.getLegend() .setEnabled(false);
        mplinechart.getData().setHighlightEnabled(false);

        mplinechart.setDragEnabled(false);
        mplinechart.setDoubleTapToZoomEnabled(false);
        mplinechart.setScaleEnabled(false);
//        mplinechart.data.setDraw
        mplinechart.invalidate();

    }

    private void getFirebaseData() {
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR,-1);
//        Date c = calendar.getTime();
////        c.as;
//        System.out.println("Current time => " + c);
//        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

        DatabaseReference myRef = database.getReference(mAuth.getUid() + "/record");
        Query query = myRef.orderByChild("time").limitToLast(30);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x = 0;
                streakDataArrayList.clear();
                chartData.clear();
                chartData.add(new Entry(x++, 0));
                xaxislable.clear();
                xaxislable.add("");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    StreakData data = postSnapshot.getValue(StreakData.class);
                    data.setDate(postSnapshot.getKey());
                    xaxislable.add(data.getDate().substring(0, data.getDate().length() - 5));
                    streakDataArrayList.add(data);

                    chartData.add(new Entry(x++, data.getMeditationTime() / (60000)));
                    Log.d(TAG, "onDataChange: " + xaxislable.size());
                }
                chartData.add(new Entry(x++, 0));

                updateUiFromData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void bottomSheetui(View v) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme1);
        View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                .inflate(R.layout.setting_goal, (ConstraintLayout) v.findViewById(R.id.bottomsheetcontainer));
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
                GotoMeditation();
                bottomSheetDialog.dismiss();
            }
        });
        assert emo2 != null;
        emo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMeditation();
                bottomSheetDialog.dismiss();
            }
        });
        assert emo3 != null;
        emo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMeditation();
                bottomSheetDialog.dismiss();
            }
        });
        assert emo4 != null;
        emo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMeditation();
                bottomSheetDialog.dismiss();
            }
        });
        assert emo5 != null;
        emo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMeditation();
                bottomSheetDialog.dismiss();
            }
        });
        assert emo6 != null;
        emo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMeditation();
                bottomSheetDialog.dismiss();
            }
        });
        assert emo7 != null;
        emo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoMeditation();
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }


    private void GotoMeditation() {
        Intent intent = new Intent(getContext(), Meditation.class);
        startActivity(intent);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


}
