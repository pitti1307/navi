package com.example.routenfuehrungsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
        String userName;
        String tour;
        Boolean sent=false;

        public CustomListAdapter( Context context, int resource, ArrayList<Destination> destinations, String tour) {
            super(context, resource, destinations);
            this.destinations = destinations;
            this.context = context;
            this.resource = resource;
            this.tour = tour;
        }
    private Drawable getDrawableWithRadius() {

        GradientDrawable gradientDrawable   =   new GradientDrawable();
        gradientDrawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
        gradientDrawable.setColor(Color.RED);

        return gradientDrawable;
    }
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //System.out.println(tour);

            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);
            }

            if(selectedItems.contains(position)){
                LinearLayout ll = convertView.findViewById(R.id.ll);
                ll.setBackgroundColor(Color.GREEN);

            }else{
                LinearLayout ll = convertView.findViewById(R.id.ll);
                ll.setBackground(ContextCompat.getDrawable(context, R.drawable.my_custom_background));
            }

            final Destination destination = getItem(position);
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

