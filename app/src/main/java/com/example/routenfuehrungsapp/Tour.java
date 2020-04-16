package com.example.routenfuehrungsapp;

import java.util.ArrayList;

public class Tour {
    private String name;
    private ArrayList<Destination> destinations;
    private ArrayList<String> infos;

    public Tour(String name, ArrayList<Destination> destinations, ArrayList<String> infos){
        this.name = name;
        this.destinations = destinations;
        this.infos = infos;

    }

    public ArrayList<String> getInfos() {
        return infos;
    }

    public void setInfos(ArrayList<String> infos) {
        this.infos = infos;
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
