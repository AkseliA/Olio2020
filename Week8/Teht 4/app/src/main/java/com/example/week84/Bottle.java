package com.example.week84;

public class Bottle {

    private String name;
    private String manufacturer;
    private double total_energy;
    private double price;
    private double size;

    public Bottle() {
    }


    public Bottle(String fname, String manuf, double totE, double fprice, double fsize){
        name = fname;
        manufacturer = manuf;
        total_energy = totE;
        price = fprice;
        size = fsize;
    }

    public String getName(){
        return name;

    }

    public String getManufacturer(){
        return manufacturer;
    }

    public double getEnergy(){
        return total_energy;
    }


    public double getPrice() {
        return price;
    }


    public double getSize() {
        return size;
    }

    @Override
    public String toString(){
        String s = String.format("%s %sl %s€", name, size, price);
        return s;
    }

}
