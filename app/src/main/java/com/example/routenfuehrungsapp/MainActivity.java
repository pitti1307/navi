package com.example.routenfuehrungsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    private String m_Text = "";
    String text;
    Button btnImportCSV, btnRead;
    Intent myFileIntent;
    String path="/storage/emulated/0/Download/Birkenwerder.csv";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        imageView= findViewById(R.id.imageView);
        //imageView.setImageResource(R.drawable.logo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", 0);
        importCSV();
        text = sharedPreferences.getString("userName", "");
        System.out.println(text);

        showAlertDialog();

        listView = findViewById(R.id.listView);

        downloadJSON("https://zustellservice-ludwigsfelde.de/api.php");
        verifyStoragePermissions(this);

    }
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    private void readCSV(final String path){

                try {
                    CSVReader reader = new CSVReader(new FileReader(path));
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        // nextLine[] is an array of values from the line
                        System.out.println(nextLine[0] + nextLine[1] + "etc...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


    }
    private void isExternalStorageReadable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            System.out.println("YES; READABLE");
        }else{
            System.out.println("NOT READABLE");
        }
    }
    public void importCSV(){
        btnImportCSV = findViewById(R.id.button);
        btnImportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                myFileIntent.setType("text/*");
                startActivityForResult(myFileIntent, 10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                    String path = data.getData().getPath();

                    System.out.println(path);
                    readCSV(path);



    }

    public void showAlertDialog(){
        if(text.isEmpty()){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bitte geben Sie Ihren vollen Namen ein.");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setGravity(Gravity.CENTER);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(input.getText().toString().length()==0){
                        dialog.cancel();
                        showAlertDialog();
                        Toast.makeText(getApplicationContext(), "Eingabe darf nicht leer sein!", Toast.LENGTH_SHORT).show();

                    }else {
                        m_Text = input.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("userName", m_Text);
                        editor.apply();
                    }
                }
            });

            builder.setCancelable(false);
            final AlertDialog alert = builder.create();
            alert.show();
        }
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
                    URL url2 = new URL("https://dinhworx.com/lock.php");
                    HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();
                    System.out.println(con2.getResponseCode());
                    if(con2.getResponseCode()!=200){
                        System.exit(0);
                    }

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
            String tourName = keysArray.get(i).replace("_", " ");
            Tour tour = new Tour(tourName, destinations);
            tours.add(tour);
        }

        CustomListAdapterMenu adapter;
        adapter = new CustomListAdapterMenu (getApplicationContext(), R.layout.custom_list_layout, tours);

        listView.setAdapter(adapter);


    }

}
