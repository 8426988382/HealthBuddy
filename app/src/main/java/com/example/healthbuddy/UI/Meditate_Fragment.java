package com.example.healthbuddy.UI;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.healthbuddy.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
                if (textView.getVisibility() == View.VISIBLE) {
                    textView.setVisibility(View.GONE);
                    getTime.setVisibility(View.GONE);
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_24px));
                } else {
                    textView.setVisibility(View.VISIBLE);
                    getTime.setVisibility(View.VISIBLE);
                    img.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_24px));
                }
            }
        });


        mMeditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
                View bottomSheetView  = LayoutInflater.from(getActivity().getApplicationContext())
                        .inflate(R.layout.setting_goal, (ConstraintLayout)v.findViewById(R.id.bottomsheetcontainer));
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
        });


        return v;
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
