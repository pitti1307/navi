package com.example.routenfuehrungsapp;


import java.io.Serializable;

public class Destination implements Serializable {
    private String adress;
    private String name;
    private String sort;
    private String info;

    public Destination(String adress, String name, String sort, String info){
        this.adress = adress;
        this.name= name;
        this.sort=sort;
        this.info=info;


    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

