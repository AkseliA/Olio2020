package com.example.week10;

import java.util.ArrayList;

import java.util.ListIterator;

public class HistoryManager {
    private ArrayList<String> historyArray;
    private ListIterator<String> iter;
    private int position;

    public HistoryManager(){
        historyArray = new ArrayList<String>();
        position = 0;

    }

    public void addCurrentPage(String url){
        if(historyArray.size() == 10){
            removeOldest();

        }
        System.out.println("Added " + url);
        historyArray.add(url);
        //position grows as list gets bigger.
        position = historyArray.size();

    }

    public void removeOldest(){
        historyArray.remove(0);
    }

    public String getNextUrl(){
        String nUrl = "";

        //Move to last visited page
        if(position < historyArray.size()) {
            position++;
            iter = historyArray.listIterator(position);

            if (iter.hasNext()) {
                nUrl = iter.next();
            }
        }


        return nUrl;
    }

    public String getPreviousUrl(){
        String pUrl = "";

        if(position > 0){
            position--;
            iter = historyArray.listIterator(position);

            if(iter.hasPrevious()){
                pUrl = iter.previous();
            }
        }
        return pUrl;
    }
}
