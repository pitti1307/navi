package com.example.routenfuehrungsapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sender extends AsyncTask<String,Void,Void> {
    String name, tour, dateString;


    public Sender(String name, String tour, String dateString) {
        this.name = name;
        this.tour = tour;
        this.dateString = dateString;
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
