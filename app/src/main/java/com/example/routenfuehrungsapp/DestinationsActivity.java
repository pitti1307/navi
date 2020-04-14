package com.example.routenfuehrungsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DestinationsActivity extends AppCompatActivity {
    ListView listView;
    CustomListAdapter adapter;
    ArrayList<Destination> destinations;
    Map<String, String> map;
    JSONObject jsonObject;
    String userName, tour, dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);

        destinations = (ArrayList<Destination>) getIntent().getSerializableExtra("Destinations");
        tour = getIntent().getStringExtra("TourName");
        System.out.println(tour);
        listView = findViewById(R.id.listView);
       // listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new CustomListAdapter (getApplicationContext(), R.layout.custom_list_layout, destinations, tour);
        listView.setAdapter(adapter);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        dateString = dateFormat.format(date);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", 0);
        userName = sharedPreferences.getString("userName", "");



    }
    @Override
    public void onBackPressed() {
        if (adapter.selectedItems.size()!=destinations.size()){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Achtung! Tour noch nicht vollständig absolviert!")
                    .setMessage("Sind Sie sicher, dass Sie die Tour beenden möchten? Die jetzige Zeit wird dann als Ihre Arbeitszeit eingetragen!")
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Sender sender = new Sender(userName, tour, dateString);
                            sender.execute("https://zustellservice-ludwigsfelde.de/upload_api.php");
                        }
                    })
                    .setNegativeButton("Nein", null)
                    .show();
        }
        else{finish();}

    }


}
