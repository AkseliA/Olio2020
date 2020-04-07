package com.example.week10;

import java.util.ArrayList;

import java.util.ListIterator;

public class HistoryManager {
    private ArrayList<String> historyArray;
    private ListIterator<String> iter;
    public int position;

    public HistoryManager(){
        historyArray = new ArrayList<String>();
        position = 0;

    }

    public void addCurrentPage(String url) {
        if (historyArray.size() == 10) {
            removeOldest();

        }
        if (position >= 10) {
            position--;
        }

        //If entered a website after going back in history, deleting following pages
        if (position != historyArray.size()){
            int size = historyArray.size();
            System.out.println(size + " " + position);
            for (int j = size; j >= position ;j--){
                System.out.println(j);
                historyArray.remove(j-1);
            }
            position = historyArray.size();
        }

        historyArray.add(url);
        //position grows as list gets bigger.
        position++;

        System.out.println("##########################################################");
        System.out.println(position + "      " + historyArray.size());

    }

    public void removeOldest(){
        historyArray.remove(0);
    }

    public String getNextUrl(){
        String nUrl = "";
        //Move to last visited page
        if(position < historyArray.size()) {
            iter = historyArray.listIterator(position);


            if (iter.hasNext()) {
                nUrl = iter.next();
                position++;
            }
        }
        //System.out.println("##########################################################");
        //System.out.println("position: " + position);

        return nUrl;
    }

    public String getPreviousUrl(){
        String pUrl = "";

        if(position > 1){
            position--;
            iter = historyArray.listIterator(position);

            if(iter.hasPrevious()){
                pUrl = iter.previous();
            }
        }
        //System.out.println("##########################################################");
        //System.out.println("position: " + position);
        return pUrl;
    }
}
