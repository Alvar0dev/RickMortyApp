package com.example.rickmortyapp.modelo;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Elemento implements Serializable {

    private int atriPK; 

    @SerializedName("id")
    private int atriInt1; 
    
    @SerializedName("name")
    private String atriString1; 
    
    @SerializedName("status")
    private String atriString2; 
    
    @SerializedName("image")
    private String atriString3; 
    
    @SerializedName("species")
    private String atriString4;

    private double atriDouble1; 
    private double atriDouble2;

    public Elemento() {}

    public int getAtriPK() { return atriPK; }
    public void setAtriPK(int atriPK) { this.atriPK = atriPK; }
    public int getAtriInt1() { return atriInt1; }
    public void setAtriInt1(int atriInt1) { this.atriInt1 = atriInt1; }
    public String getAtriString1() { return atriString1; }
    public void setAtriString1(String atriString1) { this.atriString1 = atriString1; }
    public String getAtriString2() { return atriString2; }
    public void setAtriString2(String atriString2) { this.atriString2 = atriString2; }
    public String getAtriString3() { return atriString3; }
    public void setAtriString3(String atriString3) { this.atriString3 = atriString3; }
    public String getAtriString4() { return atriString4; }
    public void setAtriString4(String atriString4) { this.atriString4 = atriString4; }
    public double getAtriDouble1() { return atriDouble1; }
    public void setAtriDouble1(double atriDouble1) { this.atriDouble1 = atriDouble1; }
    public double getAtriDouble2() { return atriDouble2; }
    public void setAtriDouble2(double atriDouble2) { this.atriDouble2 = atriDouble2; }
}