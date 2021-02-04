package com.example.healthbuddy.UI;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.healthbuddy.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class Meditate_Fragment extends Fragment {

    Button mMeditation;

    LottieAnimationView lottieAnimationView;


    // SetGoal Card
    CardView mSetGoalCard;
    TextView textView;
    EditText getTime;
    ImageView img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.mediate_fragment, container, false);

        // initialise
        mMeditation = v.findViewById(R.id.startMeditation_id);
        lottieAnimationView = v.findViewById(R.id.congo);
        mSetGoalCard = v.findViewById(R.id.setGoal_Card);
        textView = v.findViewById(R.id.textView17);
        getTime = v.findViewById(R.id.editText3);
        img = v.findViewById(R.id.imageView3);


        mSetGoalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getVisibility() == View.VISIBLE){
                    textView.setVisibility(View.GONE);
                    getTime.setVisibility(View.GONE);
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_24px));
                }else{
                    textView.setVisibility(View.VISIBLE);
                    getTime.setVisibility(View.VISIBLE);
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_24px));
                }
            }
        });


        mMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return v;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }





}
