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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=Berliner Str. 68 16540 Hohen Neuendorf"));
        //startActivity(intent);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("Clicked");
            }
        });


     //  downloadJSON("http://10.0.2.2:8080/api.php");
          downloadJSON("https://zustellservice-ludwigsfelde.de/api.php");

        // downloadJSON("https://stackoverflow.com/questions/13775103/how-do-i-know-if-i-have-successfully-connected-to-the-url-i-opened-a-connection");
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
                   loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    System.out.println(urlWebService);
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


                    System.out.println(sb.toString());
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
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
