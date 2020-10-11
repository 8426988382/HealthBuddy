package com.example.healthbuddy;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        ListView listView = findViewById(R.id.list);

        Bundle bundle = getIntent().getExtras();

        String type = bundle.getString("value");

        if(type.equals("1")){
            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , ApigetScores.AudioBooks);
            listView.setAdapter(arrayListArrayAdapter);
        }

        if(type.equals("2")){
            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , ApigetScores.TextBooks);
            listView.setAdapter(arrayListArrayAdapter);

        }

        if(type.equals("3")){
            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , ApigetScores.Meditations);
            listView.setAdapter(arrayListArrayAdapter);

        }
        if(type.equals("4")){
            ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , ApigetScores.Activity);
            listView.setAdapter(arrayListArrayAdapter);
        }


    }
}
