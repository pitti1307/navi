package com.example.routenfuehrungsapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class CustomListAdapterImported extends ArrayAdapter<Tour> {
    ArrayList<Tour> tours; //Arraylist besteht aus Objekten der Klasse Product
    Context context;
    int resource;

    public CustomListAdapterImported(Context context, int resource, ArrayList<Tour> tours) {
        super(context, resource, tours);
        this.tours = tours;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_layout_import, null, true);
        }


        final Tour tour= getItem(position);

        ImageView deleteImageView = convertView.findViewById(R.id.delete_icon);
        final View finalConvertView = convertView;
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());

                alertbox.setMessage("Sind Sie sicher, dass Sie die Tour löschen möchten?");
                alertbox.setTitle("Löschen");

                alertbox.setPositiveButton("Ja",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,
                                                int arg1) {


                                tours.remove(position);
                                notifyDataSetChanged();
                                TinyDB tinydb = new TinyDB(context);
                                tinydb.putListObject("ToursImported", tours);
                                if(tours.size()==0){

                                    ((Activity)context).findViewById(R.id.textView3).setVisibility(View.INVISIBLE);



                                }


                            }
                        });
                alertbox.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertbox.show();

            }
        });

        ImageView imageView = convertView.findViewById(R.id.info_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                StringBuilder sb = new StringBuilder();
                for(String info: tour.getInfos()){
                    sb.append(info);
                    sb.append("\n");
                }
                alertbox.setMessage(sb.toString());
                alertbox.setTitle("Gesamt");

                alertbox.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                            }
                        });
                alertbox.show();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DestinationsActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Destinations", tour.getDestinations());
                intent.putExtra("TourName", tour.getName());
                context.startActivity(intent);

            }
        });


        TextView txtName = convertView.findViewById(R.id.txtName);
        txtName.setText(tour.getName());

        return  convertView;
    }
}

