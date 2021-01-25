package com.example.healthbuddy.UI;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthbuddy.R;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        setContentView(R.layout.details);


        ListView listView = findViewById(R.id.list);

        assert bundle != null;
        ArrayList<String> data= bundle.getStringArrayList("data");


        assert data != null;
        ArrayAdapter<String> arrayListArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(arrayListArrayAdapter);

    }
}
