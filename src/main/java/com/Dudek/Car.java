package com.Dudek;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Car {



    private int id;
    private UUID uuid;
    private int year;
    private int price;
    private String model;
    private String color;
    private ArrayList<Airbag> airbag;
    private boolean isInvoice = false;
    private String image;
    private int vat;
    private String customDate;

    public Car(int id, int year, String model, String color, int price, ArrayList<Airbag> airbag, String image, int vat) {
        this.airbag = airbag;
        this.price = price;
        this.id = id;
        this.year = year;
        this.model = model;
        this.color = color;
        this.image = image;
        this.vat = vat;
        this.customDate = generateSellDate();
    }


    //gettery

    public UUID getUUID() { return uuid; }

    public int getYear() { return year; }

    public int getPrice() { return price; }

    public String getModel() { return model; }

    public String getColor() { return color; }

    public ArrayList<Airbag> getAirbag() { return airbag; }

    public String getImage() { return image; }

    public int getVat() { return vat; }


    //settery

    public void setId(int id) { this.id = id; }

    public void setUUID(UUID uuid) { this.uuid = uuid; }

    public void setYear(int year) { this.year = year; }

    public void setModel(String model) { this.model = model; }

    public void setImage(String image) { this.image = image; }

    public void setVat(int vat) { this.vat = vat; }

    public void setCustomDate(String customDate) { this.customDate = customDate; }

    public void setFirstCustomDate() { this.customDate = generateSellDate(); }


    public String generateSellDate(){
        Random rand = new Random();
        int minDay = (int) LocalDate.of(this.year, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2020, 1, 1).toEpochDay();
        long randomDay = minDay + rand.nextInt(maxDay - minDay);
        return  String.valueOf(LocalDate.ofEpochDay(randomDay));
    }


    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", year=" + year +
                ", price=" + price +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", airbag=" + airbag +
                ", isInvoice=" + isInvoice +
                '}';
    }

}
