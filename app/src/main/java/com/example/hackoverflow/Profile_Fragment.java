package com.example.hackoverflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Profile_Fragment extends Fragment {

    ImageView expandImage;
    CardView  cardView2 , cardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.profile_fragment, container, false);

      //  listView = v.findViewById(R.id.listView_);

//        expandImage = v.findViewById(R.id.expandimage_id);
//        cardView2 = v.findViewById(R.id.cardView2);
//        cardView = v.findViewById(R.id.cardView);
//
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(cardView2.getVisibility() == View.VISIBLE){
//
//                    expandImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_24px));
//                    cardView2.setVisibility(View.GONE);
//
//                }
//                else{
//                   expandImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_24px));
//                    cardView2.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        return v;
    }
}
