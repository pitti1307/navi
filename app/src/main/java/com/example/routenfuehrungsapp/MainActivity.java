package com.example.routenfuehrungsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Clicked");
            }
        });

          downloadJSON("https://zustellservice-ludwigsfelde.de/api.php");

    }

    private void downloadJSON(final String urlWebService) {
        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                   //loadIntoListView(s);
                    loadTours(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    System.out.println(con.getResponseCode());

                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String line = bufferedReader.readLine();
                    while (line != null){
                        sb.append(line);
                        line = bufferedReader.readLine();
                    }

                   // System.out.println(sb.toString());
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }
    private void loadTours(String json) throws JSONException {
       JSONObject jsonObject = new JSONObject(json);
       ArrayList<Tour> tours = new ArrayList<>();
        Iterator<String> keys = jsonObject.keys();

        ArrayList<String> keysArray = new ArrayList<>();

        while(keys.hasNext()) {
            String key = keys.next();
            keysArray.add(key);

        }

        for(int i = 0; i<keysArray.size(); i++){
            JSONArray jsonArray= jsonObject.getJSONArray(keysArray.get(i));
            ArrayList<Destination> destinations = new ArrayList<>();
            for(int j = 0; j<jsonArray.length()-6; j++) {

                JSONObject destinationObject = jsonArray.getJSONObject(j);

                String adress=destinationObject.getString("Adresse");
                String name = destinationObject.getString("Name");
                String sort = destinationObject.getString("Sorte");
                Destination destination = new Destination(adress, name, sort);
                destinations.add(destination);

            }
            Tour tour = new Tour(keysArray.get(i), destinations);
            tours.add(tour);
        }

        CustomListAdapterMenu adapter;
        adapter = new CustomListAdapterMenu (getApplicationContext(), R.layout.custom_list_layout, tours);

        //adapter = new CustomListAdapter (getApplicationContext(), R.layout.custom_list_layout, destinations);
        listView.setAdapter(adapter);


    }
    private void loadIntoListView(String json) throws JSONException {
      JSONArray jsonArray = new JSONArray(json);
       ArrayList<Destination> destinations = new ArrayList<>();
        for(int i = 0; i<jsonArray.length(); i++){
            JSONObject data= jsonArray.getJSONObject(i);
            String adress=data.getString("Adresse");
            String name = data.getString("Name");
            String sort = data.getString("Sorte");
            Destination destination = new Destination(adress, name, sort);
            destinations.add(destination);

        }
        CustomListAdapter adapter;
        adapter = new CustomListAdapter
                (getApplicationContext(), R.layout.custom_list_layout, destinations);
        listView.setAdapter(adapter);


        /*String[] stocks = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks[i] = obj.getString("name") + " " + obj.getString("price");
        }*/
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stocks);
        //listView.setAdapter(arrayAdapter);
    }
}
