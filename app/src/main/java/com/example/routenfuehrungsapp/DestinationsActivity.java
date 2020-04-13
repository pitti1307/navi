package com.example.routenfuehrungsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class DestinationsActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);
        ArrayList<Destination> destinations = (ArrayList<Destination>) getIntent().getSerializableExtra("Destinations");
        listView = findViewById(R.id.listView);
       // listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        CustomListAdapter adapter;

        adapter = new CustomListAdapter (getApplicationContext(), R.layout.custom_list_layout, destinations);
        listView.setAdapter(adapter);

    }
}
