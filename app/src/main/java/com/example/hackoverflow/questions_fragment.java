package com.example.hackoverflow;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class questions_fragment extends Fragment {

    Button btn1 , btn2 , btn3 , btn4;
    String option = null;
    TextView quotetext;
    int flag = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.questions_fragment, container, false);

//        btn1 = v.findViewById(R.id.option1_id);
//        btn2 = v.findViewById(R.id.option2_id);
//        btn3 = v.findViewById(R.id.option3_id);
//        btn4 = v.findViewById(R.id.option4_id);
        quotetext = v.findViewById(R.id.quote_text_id);
//
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                option = btn1.getText().toString().trim();
//                flag = 1;
//
//            }
//        });
//
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                option = btn2.getText().toString().trim();
//                flag = 1;
//            }
//        });
//
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                option = btn3.getText().toString().trim();
//                flag = 1;
//            }
//        });
//
//        btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                option = btn4.getText().toString().trim();
//                flag = 1;
//            }
//        });

        if(flag == 1){

            Log.e("Selected Option" , option);
            ViewGroup parent=(ViewGroup) quotetext.getParent();                 //get parent of the TextView
            parent.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);            //enable animations
            quotetext.setText("Hey! You Clicked something");

            Toast.makeText(getActivity().getApplicationContext() , "Option Selected" , Toast.LENGTH_SHORT).show();

        }


        return v;
    }
}
