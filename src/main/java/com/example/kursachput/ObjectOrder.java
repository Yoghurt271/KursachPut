package com.example.kursachput;

import java.time.LocalDate;
import java.util.Date;

public class ObjectOrder {

    int id;
    LocalDate date;
    String name;
    double prise;
    int kolVo;

    public void setId(int id) {
        this.id = id;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrise(double prise) {
        this.prise = prise;
    }
    public void setKolVo(int kolVo) { this.kolVo = kolVo; }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getName() {
        return name;
    }
    public double getPrise() {
        return prise;
    }
    public int getKolVo() {
        return kolVo;
    }


    public ObjectOrder(int id,String name, int kolVo, double prise, LocalDate date){
        this.id = id;
        this.name = name;
        this.kolVo = kolVo;
        this.prise = prise;
        this.date = date;

    }
}
