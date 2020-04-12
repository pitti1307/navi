package com.example.routenfuehrungsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class CustomListAdapterMenu extends ArrayAdapter<Tour> {
    ArrayList<Tour> tours; //Arraylist besteht aus Objekten der Klasse Product
    Context context;
    int resource;


    public CustomListAdapterMenu(Context context, int resource, ArrayList<Tour> tours) {
        super(context, resource, tours);
        this.tours = tours;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_layout_menu, null, true);

        }

        final Tour tour= getItem(position);



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DestinationsActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Destinations", tour.getDestinations());
                context.startActivity(intent);


            }
        });

        TextView txtName = convertView.findViewById(R.id.txtName);
        txtName.setText(tour.getName());





        return  convertView;
    }
}

