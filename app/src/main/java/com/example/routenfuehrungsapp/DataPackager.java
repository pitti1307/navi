package com.example.routenfuehrungsapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class DataPackager {

    String name, tour, date;

    public DataPackager(String name, String tour, String date) {
        this.name = name;
        this.tour = tour;
        this.date = date;
    }

    //pack to jsonObject
    public String packData(){
        JSONObject jsonObject = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try{
            jsonObject.put("Name", name);
            jsonObject.put("Tour", tour);
            jsonObject.put("Date", date);

            Boolean firstValue=true;

            Iterator it=jsonObject.keys();

            do {
                String key=it.next().toString();
                String value=jsonObject.get(key).toString();

                if(firstValue)
                {
                    firstValue=false;
                }else
                {
                    packedData.append("&");
                }

                packedData.append(URLEncoder.encode(key,"UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value,"UTF-8"));

            }while (it.hasNext());

            return packedData.toString();


        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
