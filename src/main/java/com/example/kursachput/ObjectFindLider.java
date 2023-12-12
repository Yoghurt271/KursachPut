package com.example.kursachput;

import java.sql.Blob;
import java.time.LocalDate;

public class ObjectFindLider {
    String name;
    double prise;
    Blob image;
    int kolVo;

    public void setName(String name) {
        this.name = name;
    }
    public void setPrise(double prise) {
        this.prise = prise;
    }
    public void setImage(Blob image) {
        this.image = image;
    }
    public void setKolVo(int kolVo) { this.kolVo = kolVo; }

    public String getName() { return name; }
    public double getPrise() { return prise; }
    public Blob getImage() {
        return image;
    }
    public int getKolVo() {
        return kolVo;
    }


    public ObjectFindLider(String name, double prise, Blob image, int kolVo){
        this.name = name;
        this.prise = prise;
        this.image = image;
        this.kolVo = kolVo;
    }


}
