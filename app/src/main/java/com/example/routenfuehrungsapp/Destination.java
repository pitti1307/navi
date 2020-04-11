package com.example.routenfuehrungsapp;


public class Destination {
    private String adress;
    private String name;
    private String sort;

    public Destination(String adress, String name, String sort){
        this.adress = adress;
        this.name= name;
        this.sort=sort;

    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}

