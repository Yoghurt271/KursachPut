package com.example.kursachput;

import java.time.LocalDate;
import java.util.Date;

public class ObjectFinishOrder {

    int id;
    LocalDate date;
    double priseMid;
    double priseGen;

    public void setId(int id) {
        this.id = id;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setPriseMid(double priseMid) {
        this.priseMid = priseMid;
    }
    public void setPriseGen(double priseGen) { this.priseGen = priseGen; }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public double getPriseMid() {
        return priseMid;
    }
    public double getPriseGen() {
        return priseGen;
    }


    public ObjectFinishOrder(int id, LocalDate date, double priseMid, double priseGen){
        this.id = id;
        this.date = date;
        this.priseMid = priseMid;
        this.priseGen = priseGen;


    }

}
