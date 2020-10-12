package com.example.healthbuddy;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.details);


        ListView listView = findViewById(R.id.list);

        String type = bundle.getString("value");

        if(type.equals("1")){

            ArrayList<String> AudioBooks= new ArrayList<>();

            AudioBooks.add("Mental Health: A Person-Centred Approach – Nicholas Procter, Helen P. Hamer, Denise McGarry, Rhonda L. Wilson, Terry Froggatt");
            AudioBooks.add(" How Your Mind Can Heal Your Body – David R. Hamilton");
            AudioBooks.add("Cognitive Behaviour Therapy: Basics and Beyond – Judith S. Beck, Aaron T. Beck");

            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , AudioBooks);
            listView.setAdapter(arrayListArrayAdapter);
        }

        if(type.equals("2")){

            ArrayList<String> TextBooks= new ArrayList<>();

            TextBooks.add("Mosby’s Pocketbook of Mental Health – Eimear Muir-Cochrane, Patricia Barkway, Debra Nizette");
            TextBooks.add("Personal Recovery and Mental Illness: A Guide for Mental Health Professionals – Mike Slade");
            TextBooks.add(" The Mind-Gut Connection: How the Hidden Conversation Within Our Bodies Impacts Our Mood, Our Choices, and Our Overall Health – Emeran Mayer");
            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , TextBooks);
            listView.setAdapter(arrayListArrayAdapter);

        }

        if(type.equals("3")){

            ArrayList<String> Meditations= new ArrayList<>();

            Meditations.add("No Suggestions for you");

            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , Meditations);
            listView.setAdapter(arrayListArrayAdapter);

        }
        if(type.equals("4")){

            ArrayList<String> Activities= new ArrayList<>();
            Activities.add("Improve your sleep");
            Activities.add("SWIMMING");
            Activities.add("SPIN BIKING AND CYCLING");



            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , Activities);
            listView.setAdapter(arrayListArrayAdapter);
        }


    }
}
