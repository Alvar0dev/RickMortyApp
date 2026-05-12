package com.example.rickmortyapp.modelo;

import com.google.gson.annotations.SerializedName;

public class Elemento {
    private int id;
    @SerializedName("name")
    private String atriString1;

    public Elemento(int id, String atriString1, String atriString2, String atriString3) {
        this.id = id;
        this.atriString1 = atriString1;
        this.atriString2 = atriString2;
        this.atriString3 = atriString3;
    }

    @SerializedName("status")
    private String atriString2;
    @SerializedName("image")
    private String atriString3;

    public Elemento() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAtriString1() {
        return atriString1;
    }

    public void setAtriString1(String atriString1) {
        this.atriString1 = atriString1;
    }

    public String getAtriString2() {
        return atriString2;
    }

    public void setAtriString2(String atriString2) {
        this.atriString2 = atriString2;
    }

    public String getAtriString3() {
        return atriString3;
    }

    public void setAtriString3(String atriString3) {
        this.atriString3 = atriString3;
    }
}
