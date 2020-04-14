package com.example.routenfuehrungsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class DestinationsActivity extends AppCompatActivity {
    ListView listView;
    CustomListAdapter adapter;
    ArrayList<Destination> destinations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);
        destinations = (ArrayList<Destination>) getIntent().getSerializableExtra("Destinations");
        listView = findViewById(R.id.listView);
       // listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new CustomListAdapter (getApplicationContext(), R.layout.custom_list_layout, destinations);
        listView.setAdapter(adapter);


    }
    @Override
    public void onBackPressed() {
        if (adapter.selectedItems.size()!=destinations.size()){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Tour beenden")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("Nein", null)
                    .show();
        }
        else{finish();}

    }
}
