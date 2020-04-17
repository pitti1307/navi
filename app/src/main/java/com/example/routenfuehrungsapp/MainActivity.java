package com.example.routenfuehrungsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CustomListAdapterMenu adapter;
    ArrayList<Tour> tours;
    ListView listView;
    private String m_Text = "";
    String text;
    Intent myFileIntent;
    String path="/storage/emulated/0/Download/Birkenwerder.csv";
    ImageView imageView;
    String path2="/document/msf:25";
    String path3="/data/user/0/com.example.routenfuehrungsapp/files/Birkenwerder.csv";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", 0);
        text = sharedPreferences.getString("userName", "");

        showAlertDialog();

        listView = findViewById(R.id.listView);

        downloadJSON("https://zustellservice-ludwigsfelde.de/api.php");
        verifyStoragePermissions(this);
        manualAdressInput();
        importCSV();
    }
    private static String getFilePathForN(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getFilesDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void manualAdressInput(){
        final EditText editText = findViewById(R.id.editText);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        ImageView imageView = findViewById(R.id.searchBtn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q="+input));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String input = editText.getText().toString();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("geo:0,0?q="+input));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    return true;
                }

                return false;
            }
        });
    }

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
                    ArrayList<Destination> destinations = new ArrayList<>();
                    ArrayList<String> tourInfo = new ArrayList<>();
                    tourInfo.add("Hallo");

                    while ((nextLine = reader.readNext()) != null) {
                        if(nextLine[0].equals("") || nextLine[1].equals("") || nextLine[1].equals("Name")){
                            continue;
                        }
                        Destination destination = new Destination(nextLine[1], nextLine[2], nextLine[3], nextLine[4]);
                        destinations.add(destination);

                    }
                    Tour tour = new Tour("Test", destinations, tourInfo );
                    tours.add(tour);
                    adapter.notifyDataSetChanged();

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
        TextView csvImportBtn = findViewById(R.id.textView);
        csvImportBtn.setOnClickListener(new View.OnClickListener() {
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

                    readCSV(getFilePathForN(data.getData(),this));

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
       tours = new ArrayList<>();
        Iterator<String> keys = jsonObject.keys();
        ArrayList<String> keysArray = new ArrayList<>();

        while(keys.hasNext()) {
            String key = keys.next();
            keysArray.add(key);
        }

        for(int i = 0; i<keysArray.size(); i++){
            JSONArray jsonArray= jsonObject.getJSONArray(keysArray.get(i));
            ArrayList<Destination> destinations = new ArrayList<>();
            ArrayList<String> tourInfo = new ArrayList<>();
            for(int j = 0; j<jsonArray.length()-6; j++) {

                JSONObject destinationObject = jsonArray.getJSONObject(j);

                String adress=destinationObject.getString("Adresse");
                String name = destinationObject.getString("Name");
                String sort = destinationObject.getString("Sorte");
                String info = destinationObject.getString("Info's");
                Destination destination = new Destination(adress, name, sort,info);
                destinations.add(destination);

            }
            for(int k= jsonArray.length()-5; k<jsonArray.length(); k++){
                JSONObject destinationObject = jsonArray.getJSONObject(k);
                String infoLine = destinationObject.getString("Adresse") + ": "+ destinationObject.getString("Name");
                tourInfo.add(infoLine);
            }
            String tourName = keysArray.get(i).replace("_", " ");
            Tour tour = new Tour(tourName, destinations, tourInfo);
            tours.add(tour);
        }


        adapter = new CustomListAdapterMenu (getApplicationContext(), R.layout.custom_list_layout, tours);

        listView.setAdapter(adapter);


    }

}
