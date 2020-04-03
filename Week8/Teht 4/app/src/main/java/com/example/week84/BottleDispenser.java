package com.example.week84;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class BottleDispenser {
    private int bottles;
    private float money;
    private ArrayList<Bottle> BottleList = new ArrayList<Bottle>();
    private ArrayAdapter<Bottle> adapter;
    private Spinner spinner;
    private Context context;
    private static BottleDispenser bd = null;


    public static BottleDispenser getInstance(Spinner spinner, Context context) {
        if (bd == null){
            bd = new BottleDispenser(spinner, context);
        }
        return bd;
    }

    public BottleDispenser(Spinner spinner, Context context) {
        spinner.findViewById(R.id.spinner);
        bottles = 5;

        money = 0;

        Bottle PepsiMax05 = new Bottle("Pepsi Max", "Pepsi", 0.3, 1.8, 0.5);
        Bottle PepsiMax15 = new Bottle("Pepsi Max", "Pepsi", 0.3,  2.2, 1.5);
        Bottle CocaColaZero05 = new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3, 2.0, 0.5);
        Bottle CocaColaZero15 = new Bottle("Coca-Cola Zero", "Coca-Cola", 0.3, 2.5, 1.5);
        Bottle FantaZero05 = new Bottle("Fanta Zero", "Coca-Cola", 0.3, 1.95, 0.5);

        BottleList.add(PepsiMax05);
        BottleList.add(PepsiMax15);
        BottleList.add(CocaColaZero05);
        BottleList.add(CocaColaZero15);
        BottleList.add(FantaZero05);
        this.context = context;
        adapter = new ArrayAdapter<Bottle>(context.getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, BottleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    public void getBottleList(TextView text) {
        text.findViewById(R.id.editText);
        for(int i = 0;i<bottles;i++) {
            text.append(i+1+". Name: "+BottleList.get(i).getName()+"\n\tSize: "+BottleList.get(i).getSize()+"\tPrice: "+BottleList.get(i).getPrice()+"\n");
            /*System.out.println(i+1+". Name: "+BottleList.get(i).getName()+"\n\tSize: "+BottleList.get(i).getSize()+"\tPrice: "+BottleList.get(i).getPrice());*/
        }
    }

    public void addMoney(TextView text, int amount) {
        money += amount;
        text.findViewById(R.id.editText);
        text.append("Klink! Added more money!\n");
        /*System.out.println("Klink! Added more money!");*/
    }



    public void buyBottle(TextView text, int Id) {
        text.findViewById(R.id.editText);

        Bottle bottle = new Bottle();
        if (BottleList.size() == 0) {
            text.append("The dispenser is empty! We must refill it first.\n");
            /*System.out.println("The dispenser is empty! We must refill it first.");*/
            bottles +=5;
        }
        if (money < BottleList.get(Id).getPrice()) {
            text.append("Add money first!\n");
            /*System.out.println("Add money first!");*/
        }
        else {
            text.append("KACHUNK! "+ BottleList.get(Id).getName() +" came out of the dispenser!\n");
            /*System.out.println("KACHUNK! "+ bottle.getName() +" came out of the dispenser!");*/
            bottles -= 1;
            money -= BottleList.get(Id).getPrice();
            BottleList.remove(Id);

        }
    }

    public void returnMoney(TextView text) {
        text.findViewById(R.id.editText);
        text.append("Klink klink. Money came out!\n");
        money = 0;

        /*System.out.println("Klink klink. Money came out!");*/

    }
    public float getBalance(){
        return money;
    }


}
