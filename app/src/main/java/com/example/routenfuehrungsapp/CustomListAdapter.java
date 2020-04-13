package com.example.routenfuehrungsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.ArrayList;


    public class CustomListAdapter extends ArrayAdapter<Destination> {
        ArrayList<Destination> destinations; //Arraylist besteht aus Objekten der Klasse Product
        Context context;
        int resource;
        ArrayList<Integer> selectedItems = new ArrayList<>();


        public CustomListAdapter( Context context, int resource, ArrayList<Destination> destinations) {
            super(context, resource, destinations);
            this.destinations = destinations;
            this.context = context;
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);

            }

            if(selectedItems.contains(position)){
                convertView.setBackgroundColor(Color.GREEN);

            }else{
                convertView.setBackgroundColor(Color.BLUE);
            }

            final Destination destination = getItem(position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItems.add(position);
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



            System.out.println(selectedItems.contains(position));
            return  convertView;
        }
    }

