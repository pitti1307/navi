package com.example.routenfuehrungsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.apache.commons.lang3.ObjectUtils;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomListAdapter extends ArrayAdapter<Destination> {
        ArrayList<Destination> destinations; //Arraylist besteht aus Objekten der Klasse Product
        Context context;
        int resource;
        ArrayList<Integer> selectedItems= new ArrayList<>();
        SharedPreferences sharedPreferences;
        String userName, tour;
        boolean sent = false;


        public CustomListAdapter( Context context, int resource, ArrayList<Destination> destinations, String tour) {
            super(context, resource, destinations);
            this.destinations = destinations;
            this.context = context;
            this.resource = resource;
            this.tour = tour;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final Destination destination = getItem(position);
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);
            }
            LinearLayout ll = convertView.findViewById(R.id.ll);
            TextView infoTxt = convertView.findViewById(R.id.txtInfo);


            if(destination.getInfo().equals("")){

                infoTxt.setVisibility(View.GONE);

            }else{
                infoTxt.setVisibility(View.VISIBLE);
                infoTxt.setText(destination.getInfo());
            }

            if(selectedItems.contains(position)){
                ll.setBackground(ContextCompat.getDrawable(context, R.drawable.shape2));
                TextView textView1 = convertView.findViewById(R.id.txtName);
                TextView textView2 = convertView.findViewById(R.id.txtAdress);
                TextView textView3 = convertView.findViewById(R.id.txtSort);

                textView1.setTextColor(Color.BLACK);
                textView2.setTextColor(Color.BLACK);
                textView3.setTextColor(Color.BLACK);
                if(infoTxt!=null){
                    infoTxt.setTextColor(Color.BLACK);
                }


            }else{
                ll.setBackground(ContextCompat.getDrawable(context, R.drawable.shape1));
                TextView textView1 = convertView.findViewById(R.id.txtName);
                TextView textView2 = convertView.findViewById(R.id.txtAdress);
                TextView textView3 = convertView.findViewById(R.id.txtSort);

                textView1.setTextColor(Color.WHITE);
                textView2.setTextColor(Color.WHITE);
                textView3.setTextColor(Color.WHITE);
                if(infoTxt!=null){
                    infoTxt.setTextColor(Color.WHITE);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!selectedItems.contains(position)){
                    selectedItems.add(position);}

                    if(selectedItems.size()==destinations.size() && sent==false) {
                        sharedPreferences = context.getSharedPreferences("sharedPrefs", 0);
                        userName = sharedPreferences.getString("userName", "");
                        System.out.println(userName);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String dateString = dateFormat.format(date);
                        sent=true;
                        Sender sender = new Sender(userName, tour, dateString);
                        sender.execute("https://zustellservice-ludwigsfelde.de/upload_api.php");
                    }

                    notifyDataSetChanged();

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                           Uri.parse("google.navigation:q="+destination.getAdress()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            TextView txtName = convertView.findViewById(R.id.txtName);
            txtName.setText(destination.getName());

            TextView txtAdress = convertView.findViewById(R.id.txtAdress);
            txtAdress.setText(destination.getAdress());

            TextView txtSort = convertView.findViewById(R.id.txtSort);
            txtSort.setText(destination.getSort());

            //select all items - for testing
/*
            if(!selectedItems.contains(position)){
                selectedItems.add(position);}
            notifyDataSetChanged();
*/
            return  convertView;
        }
    }

