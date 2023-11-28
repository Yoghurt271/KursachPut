package com.example.kursachput;

import javafx.scene.image.Image;

import java.sql.Blob;

public class ObjectProd {

    String name;
    double size;
    double prise;
    Blob image;
    int kolVo;


    public ObjectProd(String name, double size, double prise, Blob image ,int kolVo){
        this.name = name;
        this.size = size;
        this.prise = prise;
        this.image = image;
        this.kolVo = kolVo;

    }

}
