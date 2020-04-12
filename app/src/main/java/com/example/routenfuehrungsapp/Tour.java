package com.example.routenfuehrungsapp;

import java.util.ArrayList;

public class Tour {
    private String name;
    private ArrayList<Destination> destinations;

    public Tour(String name, ArrayList<Destination> destinations){
        this.name = name;
        this.destinations = destinations;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(ArrayList<Destination> destinations) {
        this.destinations = destinations;
    }
}
