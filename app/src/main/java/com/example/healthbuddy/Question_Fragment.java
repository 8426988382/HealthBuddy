package com.example.healthbuddy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Question_Fragment extends Fragment implements View.OnClickListener {

    LottieAnimationView lottieAnimationView;
    LottieAnimationView allCaughtUp;
    public static final String SHARED_PREF = "shared_prefs";
    SharedPreferences Prefs;
    private ScrollView scrollView;
    TextView QuestionText, QuoteText;
    Button Option1, Option2, Option3, Option4, Option5;
    static int cnt = 0;


    static int score = 0;


    ArrayList<QuestionData> Questions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.questions_fragment, container, false);

        // initialise
        lottieAnimationView = v.findViewById(R.id.startid);
        allCaughtUp = v.findViewById(R.id.allcaughtup);
        Prefs = Objects.requireNonNull(this.getActivity()).getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        scrollView = v.findViewById(R.id.scrollView2);
        QuestionText = v.findViewById(R.id.textView);
        Option1 = v.findViewById(R.id.option1_id);
        Option2 = v.findViewById(R.id.option2_id);
        Option3 = v.findViewById(R.id.option3_id);
        Option4 = v.findViewById(R.id.option4_id);
        Option5 = v.findViewById(R.id.option5_id);
        QuoteText = v.findViewById(R.id.quote_text_id);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());

        final String Uid;

        if (account != null) {
            Uid = account.getId();

            lottieAnimationView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void onClick(View v) {
                    long firstTime = Prefs.getLong("FirstTime", Long.parseLong("0"));
                    long loginTime = Prefs.getLong("time", Long.parseLong("0"));
                    long diff = loginTime - firstTime;
                    long diffs = diff / (60 * 60 * 1000);
                    Log.e("LAST TIME DIFFERENCE", String.valueOf(diffs));
                    Log.e("First Time", String.valueOf(firstTime));
                    Log.e("Login Time", String.valueOf(loginTime));

                    lottieAnimationView.playAnimation();

                  //  int hrs = 86400000;

                    if (diffs >= 0) {
                        new ApiGetQuestions(getContext(), Uid) {
                            @Override
                            protected void onPostExecute(ArrayList aVoid) {
                                super.onPostExecute(aVoid);
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                Option1.setVisibility(View.GONE);
                                Option2.setVisibility(View.GONE);
                                Option3.setVisibility(View.GONE);
                                Option4.setVisibility(View.GONE);
                                Option5.setVisibility(View.GONE);

                                PerformAction(QuestionList);

                            }
                        }.execute();

                        lottieAnimationView.setVisibility(View.GONE);
                    } else {
                        lottieAnimationView.setVisibility(View.GONE);
                        allCaughtUp.setVisibility(View.VISIBLE);
                        allCaughtUp.playAnimation();
                    }
                }
            });


        } else {
            Toast.makeText(getContext(), "some error has occurred", Toast.LENGTH_SHORT).show();
        }

        return v;
    }


    void PerformAction(ArrayList<QuestionData> questionData) {
        this.Questions = questionData;

        Log.e("Questions Data", Questions.toString());

        scrollView.setVisibility(View.VISIBLE);

        QuoteText.setVisibility(View.GONE);
        Option1.setVisibility(View.GONE);
        Option2.setVisibility(View.GONE);
        Option3.setVisibility(View.GONE);
        Option4.setVisibility(View.GONE);
        Option5.setVisibility(View.GONE);

        if (cnt == 0) {
            QuestionText.setText(Questions.get(0).getQuestion());

            QuestionData questionData1 = Questions.get(0);
            int options = 0;

            for (Map.Entry<String, String> key : questionData1.getMp().entrySet()) {
                if (options == 0) {
                    Option1.setVisibility(View.VISIBLE);
                    Option1.setText(key.getKey());
                    options += 1;
                } else if (options == 1) {
                    Option2.setVisibility(View.VISIBLE);
                    Option2.setText(key.getKey());
                    options += 1;
                } else if (options == 2) {
                    Option3.setVisibility(View.VISIBLE);
                    Option3.setText(key.getKey());
                    options += 1;
                } else if (options == 3) {
                    Option4.setVisibility(View.VISIBLE);
                    Option4.setText(key.getKey());
                    options += 1;
                } else if (options == 4) {
                    Option5.setVisibility(View.VISIBLE);
                    Option5.setText(key.getKey());
                    options += 1;
                }
            }
        }


        Option1.setOnClickListener(this);
        Option2.setOnClickListener(this);
        Option3.setOnClickListener(this);
        Option4.setOnClickListener(this);
        Option5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        cnt += 1;

        int id = v.getId();
        QuestionData questionData2 = Questions.get(cnt - 1);


        switch (id) {
            case R.id.option1_id:
                score += Integer.parseInt((Objects.requireNonNull(questionData2.getMp().get(Option1.getText().toString()))));
                break;
            case R.id.option2_id:
                score += Integer.parseInt((Objects.requireNonNull(questionData2.getMp().get(Option2.getText().toString()))));
                break;
            case R.id.option3_id:
                score += Integer.parseInt((Objects.requireNonNull(questionData2.getMp().get(Option3.getText().toString()))));
                break;
            case R.id.option4_id:
                score += Integer.parseInt((Objects.requireNonNull(questionData2.getMp().get(Option4.getText().toString()))));
                break;
            case R.id.option5_id:
                score += Integer.parseInt((Objects.requireNonNull(questionData2.getMp().get(Option5.getText().toString()))));
                break;
            default:
                Log.e("Error", "Error in QuestionFragment");
        }



        Option1.setVisibility(View.GONE);
        Option2.setVisibility(View.GONE);
        Option3.setVisibility(View.GONE);
        Option4.setVisibility(View.GONE);
        Option5.setVisibility(View.GONE);

        Log.e("ID", String.valueOf(score));

        if (cnt <= 4) {
            Option1.setVisibility(View.GONE);
            Option2.setVisibility(View.GONE);
            Option3.setVisibility(View.GONE);
            Option4.setVisibility(View.GONE);
            Option5.setVisibility(View.GONE);
            QuestionText.setText(Questions.get(cnt).getQuestion());

            QuestionData questionData1 = Questions.get(cnt);
            int options = 0;

            for (Map.Entry<String, String> key : questionData1.getMp().entrySet()) {
                if (options == 0) {
                    Option1.setVisibility(View.VISIBLE);
                    Option1.setText(key.getKey());
                    options += 1;
                } else if (options == 1) {
                    Option2.setVisibility(View.VISIBLE);
                    Option2.setText(key.getKey());
                    options += 1;
                } else if (options == 2) {
                    Option3.setVisibility(View.VISIBLE);
                    Option3.setText(key.getKey());
                    options += 1;
                } else if (options == 3) {
                    Option4.setVisibility(View.VISIBLE);
                    Option4.setText(key.getKey());
                    options += 1;
                } else if (options == 4) {
                    Option5.setVisibility(View.VISIBLE);
                    Option5.setText(key.getKey());
                    options += 1;
                }
            }


        }


        if (cnt >= 5) {
            EndAction();
        }


    }

    @SuppressLint("SetTextI18n")
    void EndAction() {
        scrollView.setVisibility(View.GONE);
        allCaughtUp.setVisibility(View.VISIBLE);
        Option1.setVisibility(View.GONE);
        Option2.setVisibility(View.GONE);
        Option3.setVisibility(View.GONE);
        Option4.setVisibility(View.GONE);
        Option5.setVisibility(View.GONE);
        QuoteText.setVisibility(View.VISIBLE);
        QuoteText.setText("You are all Caught up!");
        Prefs.edit().putLong("scores", score).apply();
        allCaughtUp.playAnimation();
    }


    //
//    Button btn1, btn2, btn3, btn4;
//    TextView quotetext;
//    TextView txt;
//    int s1=4;
//    int s2=3;
//    int s3=2;
//    int s4=1;
//    LottieAnimationView view;
//
//
//
//    String key="";
//    SharedPreferences sharedPreferences;
//    static int count = -1;
//    static int i = 0;
//    SharedPreferences.Editor myEdit;
//    String info[] = {
//            "Turnout of the quarrel?","I won the debate", "compromised for peace", "forgot the " +
//            "fight and started talking","fight led to complications in relationship",
//            "Anticipation of next morning?","Excited (big day)", "As usual (regular stuffs)",
//            "Nothing much","Exhausted","Do I like and enjoy my current physical state?",
//            "Absolutely", "Look to improve everyday", "Do look for motivation", "low self-esteem",
//            "When were you the most happy?", "Morning", "Afternoon", "Evening","Night", "Where " +
//            "you anxious, worried or scared about anything?","Never", "Sometimes", "Often",
//            "Constantly", "Did you felt restless, agitated, frantic or tense?", "Never", "A few " +
//            "times","Often", "Constantly", "Did your worry was out of your control?","Never", "A few " +
//            "times","Often", "Constantly"
//    };
//
//
//    @SuppressLint("WrongConstant")
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.questions_fragment, container, false);
//        String[] qtnQuotes = {
//                "No medicine cures what happiness cannot.", "They say a person needs just three " +
//                "things to be truly happy in this world: Someone to love, something to do, and something to hope for.",
//                "Let us be grateful to the people who make us happy; they are the charming " +
//                        "gardeners who make our souls blossom.", "Happiness is a warm puppy.",
//                "There's nothing like deep breaths after laughing that hard. Nothing in the world" +
//                        " like a sore stomach for the right reasons.",
//                "Sanity and happiness are an impossible combination.", "You cannot protect " +
//                "yourself from sadness without protecting yourself from happiness.", "It isn't what you have or who you are or where you are or what you are doing that makes you happy or unhappy. It is what you think about it.",
//                "Happiness is a state of mind. It's just according to the way you look at things."
//        };
//
//
//
//
//          sharedPreferences = getActivity().getSharedPreferences("MySharedPref",
//                MODE_APPEND);
//          GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
//        key = account.getId();
//          myEdit = sharedPreferences.edit();
//
//
//
//        quotetext = v.findViewById(R.id.quote_text_id);
//        txt = v.findViewById(R.id.textView);
//        btn1 = v.findViewById(R.id.option1_id);
//        btn2 = v.findViewById(R.id.option2_id);
//        btn3 = v.findViewById(R.id.option3_id);
//        btn4 = v.findViewById(R.id.option4_id);
//
//
//
//        Random rand = new Random();
//        int x = rand.nextInt(qtnQuotes.length);
//        quotetext.setText(qtnQuotes[x]);
//
//        btn1.setOnClickListener(this);
//        btn2.setOnClickListener(this);
//        btn3.setOnClickListener(this);
//        btn4.setOnClickListener(this);
//
//
////
////        btn1.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                option = btn1.getText().toString().trim();
////                flag = 1;
////
////            }
////        });
////
////        btn2.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                option = btn2.getText().toString().trim();
////                flag = 1;
////            }
////        });
////
////        btn3.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                option = btn3.getText().toString().trim();
////                flag = 1;
////            }
////        });
////
////        btn4.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                option = btn4.getText().toString().trim();
////                flag = 1;
////            }
////        });
//
//
////        if(flag == 1){
////
////            Log.e("Selected Option" , option);
////            ViewGroup parent=(ViewGroup) quotetext.getParent();                 //get parent of the TextView
////            parent.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);            //enable animations
////            quotetext.setText("Hey! You Clicked something");
////
////            Toast.makeText(getActivity().getApplicationContext() , "Option Selected" , Toast.LENGTH_SHORT).show();
////
////        }
//
//
//        return v;
//    }
//
//    @Override
//    public void onClick(View v) {
//        Log.e(TAG, "onClick: 1 "+   sharedPreferences.getInt(key, 0) );
//
//        if(count >= info.length){
//            Toast.makeText(getActivity().getApplicationContext() , "You Are All Caught Up!", Toast.LENGTH_LONG).show();
//        }
//        else{
//            count = count +5;
//
//
//        int id = v.getId();
//
//        String optiontext = null;
//
//        switch (id) {
//            case R.id.option1_id:
//                myEdit.putInt(key,s1+sharedPreferences.getInt(key, 0)).commit();
//                        optiontext = btn1.getText().toString();
//                break;
//            case R.id.option2_id:
//                myEdit.putInt(key,s2+sharedPreferences.getInt(key, 0)).commit();
//
//                optiontext = btn2.getText().toString();
//                break;
//            case R.id.option3_id:
//                myEdit.putInt(key,s3+sharedPreferences.getInt(key, 0)).commit();
//
//                optiontext = btn3.getText().toString();
//                break;
//            case R.id.option4_id:
//                myEdit.putInt(key,s4+sharedPreferences.getInt(key, 0)).commit();
//
//                optiontext = btn4.getText().toString();
//                break;
//            default:
//
//                break;
//
//        }
//
//        if(info.length >= count)
//        {
//            txt.setText(info[i]);
//            i++;
//            btn1.setText(info[i]);
//            i++;
//            btn2.setText(info[i]);
//            i++;
//            btn3.setText(info[i]);
//            i++;
//            btn4.setText(info[i]);
//            i++;
//            Log.e(TAG, "onClick: "+i );
//            Log.e(TAG, "onClick: "+count );
//
//        }
//        else{
//            Toast.makeText(getActivity().getApplicationContext() , "You Are All Caught Up!", Toast.LENGTH_LONG).show();
//
//        }}
//    }
}
