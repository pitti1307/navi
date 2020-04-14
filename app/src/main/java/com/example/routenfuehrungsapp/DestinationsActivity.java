package com.example.routenfuehrungsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    String name, tour, dateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations);
        destinations = (ArrayList<Destination>) getIntent().getSerializableExtra("Destinations");
        listView = findViewById(R.id.listView);
       // listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new CustomListAdapter (getApplicationContext(), R.layout.custom_list_layout, destinations);
        listView.setAdapter(adapter);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        dateString = dateFormat.format(date);

        map = new HashMap<>();
        map.put("ID", "1");
        map.put("Name", "Max Müller");
        map.put("Tour", "Birkenwerder");
        map.put("Datum", dateString);
        jsonObject = new JSONObject(map);
        name="Max";
        tour="Birkenwerder";


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
                            new CallAPI().execute("http://192.168.64.2/upload_api.php");
                        }
                    })
                    .setNegativeButton("Nein", null)
                    .show();
        }
        else{finish();}

    }
    public class CallAPI extends AsyncTask<String, Void, Void> {

        public CallAPI(){
            //set context variables if required
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            String urlString = params[0]; // URL to call

            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setConnectTimeout(20000);
                urlConnection.setReadTimeout(20000);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                OutputStream outputStream = urlConnection.getOutputStream();
                //WRITE
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));

                writer.write(new DataPackager(name,tour,dateString).packData());
                writer.flush();
                writer.close();
                outputStream.close();

               // urlConnection.connect();
                System.out.println(urlConnection.getResponseCode());

                if(urlConnection.getResponseCode()==urlConnection.HTTP_OK){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
                    StringBuffer response = new StringBuffer();

                    String line;

                    while ((line=bufferedReader.readLine()) != null){
                        response.append(line);
                    }
                    bufferedReader.close();

                    System.out.println(response.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
