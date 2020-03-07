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

import java.util.Random;

public class questions_fragment extends Fragment implements View.OnClickListener {

    Button btn1, btn2, btn3, btn4;
    String option = null;
    TextView quotetext;
    TextView txt;
    int flag = 0;

    static int count = 1;
    static int i = 0;

    String info[] = {
            "Turnout of the quarrel?","I won the debate", "compromised for peace", "forgot the " +
            "fight and started talking","fight led to complications in relationship",
            "Anticipation of next morning?","Excited (big day)", "As usual (regular stuffs)",
            "Nothing much","Exhausted","Do I like and enjoy my current physical state?",
            "Absolutely", "Look to improve everyday", "Do look for motivation", "low self-esteem",
            "When were you the most happy?", "Morning", "Afternoon", "Evening","Night", "Where " +
            "you anxious, worried or scared about anything?","Never", "Sometimes", "Often",
            "Constantly", "Did you felt restless, agitated, frantic or tense?", "Never", "A few " +
            "times","Often", "Constantly", "Did your worry was out of your control?","Never", "A few " +
            "times","Often", "Constantly"


    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.questions_fragment, container, false);
        String[] qtnQuotes = {
                "No medicine cures what happiness cannot.", "They say a person needs just three " +
                "things to be truly happy in this world: Someone to love, something to do, and something to hope for.",
                "Let us be grateful to the people who make us happy; they are the charming " +
                        "gardeners who make our souls blossom.", "Happiness is a warm puppy.",
                "There's nothing like deep breaths after laughing that hard. Nothing in the world" +
                        " like a sore stomach for the right reasons.",
                "Sanity and happiness are an impossible combination.", "You cannot protect " +
                "yourself from sadness without protecting yourself from happiness.", "It isn't what you have or who you are or where you are or what you are doing that makes you happy or unhappy. It is what you think about it.",
                "Happiness is a state of mind. It's just according to the way you look at things."
        };
        quotetext = v.findViewById(R.id.quote_text_id);
        txt = v.findViewById(R.id.textView);
        btn1 = v.findViewById(R.id.option1_id);
        btn2 = v.findViewById(R.id.option2_id);
        btn3 = v.findViewById(R.id.option3_id);
        btn4 = v.findViewById(R.id.option4_id);


        Random rand = new Random();
        int x = rand.nextInt(qtnQuotes.length);
        quotetext.setText(qtnQuotes[x]);


        if(count >= info.length){
            Toast.makeText(getActivity().getApplicationContext() , "You Are All Caught Up!", Toast.LENGTH_LONG).show();
        }
        else{
            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
            btn3.setOnClickListener(this);
            btn4.setOnClickListener(this);
        }

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


//        if(flag == 1){
//
//            Log.e("Selected Option" , option);
//            ViewGroup parent=(ViewGroup) quotetext.getParent();                 //get parent of the TextView
//            parent.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);            //enable animations
//            quotetext.setText("Hey! You Clicked something");
//
//            Toast.makeText(getActivity().getApplicationContext() , "Option Selected" , Toast.LENGTH_SHORT).show();
//
//        }


        return v;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        String optiontext = null;

        switch (id) {
            case R.id.option1_id:
                optiontext = btn1.getText().toString();
                break;
            case R.id.option2_id:
                optiontext = btn2.getText().toString();
                break;
            case R.id.option3_id:
                optiontext = btn3.getText().toString();
                break;
            case R.id.option4_id:
                optiontext = btn4.getText().toString();
                break;
            default:

                break;

        }

        if(info.length > count)
        {
            txt.setText(info[i]);
            i++;
            btn1.setText(info[i]);
            i++;
            btn2.setText(info[i]);
            i++;
            btn3.setText(info[i]);
            i++;
            btn4.setText(info[i]);
            i++;
            count ++;
        }
        else{
            Toast.makeText(getActivity().getApplicationContext() , "You Are All Caught Up!", Toast.LENGTH_LONG).show();

        }

//        }
//
//        if(count == 5){
//            ApigetQuestions apigetQuestions = new ApigetQuestions(getActivity().getApplicationContext());
//            apigetQuestions.execute();
//            count ++;
//
//
//            info = ApigetQuestions.informtaion;
//
//            for(int i = 0 ;i < info.length ; i ++){
//                System.out.println(info[i]);
//            }
//            i = 0;
//        }
//
//        if(count > 5){
//
//            txt.setText(info[i]);
//            i++;
//            btn1.setText(info[i]);
//            i++;
//            btn2.setText(info[i]);
//            i++;
//            btn3.setText(info[i]);
//            i++;
//            btn4.setText(info[i]);
//
//        }
//
//    }
    }
}
