package com.example.kursachput;

import java.time.LocalDate;
import java.util.Date;

public class ObjectOrder {

    int id;

    String name;
    double prise;
    int kolVo;
    LocalDate date;

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrise(double prise) {
        this.prise = prise;
    }
    public void setKolVo(int kolVo) { this.kolVo = kolVo; }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() { return id; }
    public String getName() {
        return name;
    }
    public double getPrise() {
        return prise;
    }
    public int getKolVo() {
        return kolVo;
    }
    public LocalDate getDate() { return date; }


    public ObjectOrder(int id,String name, double prise,int kolVo,  LocalDate date){
        this.id = id;
        this.name = name;
        this.prise = prise;
        this.kolVo = kolVo;
        this.date = date;

    }
}
