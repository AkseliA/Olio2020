/* Author: Akseli Aula
 * Environment: Android Studio
 * Assignment week 9*/

package com.example.week9_smartpost;

import android.os.StrictMode;

import java.util.ArrayList;

public class BackgroundService{

    private ArrayList<SmartPost> fiSpList;
    private ArrayList<SmartPost> eeSpList;
    private ArrayList<SmartPost> allSpList;
    private ArrayList<PickupTime> eePickupArray;
    private static BackgroundService bs;
    private LoadXMLdata loadXML;
    private boolean done = false;

    public static BackgroundService getInstance() {
        if(bs == null){
            bs = new BackgroundService();
        }
        return bs;
    }

    private BackgroundService(){
        allSpList = new ArrayList<SmartPost>();
        fiSpList = new ArrayList<SmartPost>();
        eeSpList = new ArrayList<SmartPost>();
        eePickupArray = new ArrayList<PickupTime>();
        new LoadSmartPost().run();
        }

    private class LoadSmartPost implements Runnable {

        @Override
        public void run(){
            String urlFI = "http://iseteenindus.smartpost.ee/api/?request=destinations&country=FI&type=APT";
            String urlEE = "http://iseteenindus.smartpost.ee/api/?request=destinations&country=EE&type=APT";
            String urlPickup = "http://iseteenindus.smartpost.ee/api/?request=courier&type=express";

            fiSpList.clear();
            eeSpList.clear();
            allSpList.clear();

            StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(threadPolicy);

            //Loads XML data to ArrayLists
            loadXML = new LoadXMLdata();
            eeSpList = loadXML.getData(urlEE, eeSpList);
            fiSpList = loadXML.getData(urlFI, fiSpList);
            eePickupArray = loadXML.getPickupTimes(urlPickup, eePickupArray);

            //Concatenate 2 arraylists into allSpList
            allSpList.addAll(eeSpList);
            allSpList.addAll(fiSpList);

            //First spinner item is "all"
            SmartPost first = new SmartPost("0", "All", "All", "All", "0", "All", "All");
            allSpList.add(0, first);
            fiSpList.add(0, first);
            eeSpList.add(0, first);

            done = true;
        }

    }
    public boolean isDone(){
        return done;
    }
    public ArrayList<SmartPost> getFiArrayList(){
        return fiSpList;
    }

    public ArrayList<SmartPost> getEeArrayList(){
        return eeSpList;
    }
    public ArrayList<SmartPost> getAllArrayList(){
        return allSpList;
    }
    public ArrayList<PickupTime> getEePickupArray() {
        return eePickupArray;
    }
}
