package com.example.week82;

import android.widget.TextView;

import java.util.ArrayList;

public class BottleDispenser {
    private int bottles;
    private int money;
    private ArrayList<Bottle> BottleList = new ArrayList<Bottle>();
    private static BottleDispenser bd = new BottleDispenser();


    public static BottleDispenser getInstance() {
        return bd;
    }

    public BottleDispenser() {
        bottles = 5;

        money = 0;

        for(int i = 0;i<bottles;i++) {
            Bottle temp = new Bottle();
            BottleList.add(temp);
        }

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



    public void buyBottle(TextView text) {
        text.findViewById(R.id.editText);

        Bottle bottle = new Bottle();
        if (BottleList.size() == 0) {
            text.append("The dispenser is empty! We must refill it first.\n");
            /*System.out.println("The dispenser is empty! We must refill it first.");*/
            bottles +=5;
        }
        if (money < bottle.getPrice()) {
            text.append("Add money first!\n");
            /*System.out.println("Add money first!");*/
        }
        else {
            text.append("KACHUNK! "+ bottle.getName() +" came out of the dispenser!\n");
            /*System.out.println("KACHUNK! "+ bottle.getName() +" came out of the dispenser!");*/
            BottleList.remove(0);
            bottles -= 1;
            money -= 1;
        }
    }

    public void returnMoney(TextView text) {
        text.findViewById(R.id.editText);
        text.append("Klink klink. Money came out!\n");
        money = 0;

        /*System.out.println("Klink klink. Money came out!");*/

    }


}
