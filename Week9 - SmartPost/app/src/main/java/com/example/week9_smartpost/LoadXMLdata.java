/* Author: Akseli Aula
 * Environment: Android Studio
 * Assignment week 9*/

package com.example.week9_smartpost;

import android.os.StrictMode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LoadXMLdata {
    private ArrayList<SmartPost> alist;
    public LoadXMLdata(){

    }

    public ArrayList<SmartPost> getData(String url, ArrayList<SmartPost> alist){

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(url);
            document.getDocumentElement().normalize();

            NodeList nlist = document.getDocumentElement().getElementsByTagName("item");

            for (int i=0; i< nlist.getLength(); i++){
                Node node = nlist.item(i);

                if(node.getNodeType() == node.ELEMENT_NODE){
                    Element element = (Element) node;

                    String sid = element.getElementsByTagName("place_id").item(0).getTextContent();
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    String address = element.getElementsByTagName("address").item(0).getTextContent();
                    String city = element.getElementsByTagName("city").item(0).getTextContent();
                    String spostalcode = element.getElementsByTagName("postalcode").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    String availability = element.getElementsByTagName("availability").item(0).getTextContent();


                    //ID and postalcode to int format
                    //int id = Integer.parseInt(sid);
                    //int postalcode = Integer.parseInt(spostalcode);

                    // Simplify names
                    if(country.equals("FI")){
                        name = getSimpleFiName(name);
                        availability = parseFiAvailability(availability);
                    }else{
                        name = getSimpleEeName(name, city);
                        availability = parseEeAvailability(availability);
                    }

                    //Replace days with numbers
                    //availability = availability.replace("Mon", "1");
                    //availability = availability.replace("Tue", "2");
                    //availability = availability.replace("Wed", "3");
                    //availability = availability.replace("Thu", "4");
                    //availability = availability.replace("Fri", "5");
                    //availability = availability.replace("Sat", "6");
                    //availability = availability.replace("Sun", "7");




                    city = city.substring(0,1).toUpperCase() + city.substring(1).toLowerCase();
                    SmartPost automat = new SmartPost(sid, name, address, city, spostalcode, country, availability);
                    alist.add(automat);

                    //System.out.println(name + " " +availability);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            System.out.println("##########DONE##########");
        }
        return alist;
    }


    public ArrayList<PickupTime> getPickupTimes(String url, ArrayList<PickupTime> pickupTimeArray) {

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(url);
            document.getDocumentElement().normalize();

            NodeList nlist = document.getDocumentElement().getElementsByTagName("item");

            for (int i = 0; i < nlist.getLength(); i++) {
                Node node = nlist.item(i);

                if (node.getNodeType() == node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String sid = element.getElementsByTagName("place_id").item(0).getTextContent();
                    int day = Integer.parseInt(element.getElementsByTagName("day").item(0).getTextContent());
                    String exprIn = element.getElementsByTagName("express_in").item(0).getTextContent();
                    String exprOut = element.getElementsByTagName("express_out").item(0).getTextContent();
                    exprOut = exprOut.replace(":", ".");

                    PickupTime pt = new PickupTime(sid, day, exprIn, exprOut);
                    pickupTimeArray.add(pt);

                    //System.out.println(exprOut);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            System.out.println("##########DONE##########");
        }
        return pickupTimeArray;
    }


    public String getSimpleFiName(String fname){
        String name = fname;
        String[] sname;

        sname = name.split(", ");
        int listlength = sname.length;
        name = sname[listlength-1].trim();

        return name;
    }
    public String getSimpleEeName(String fname, String city){
        String name = fname;
        String[] sname = name.split(" ");

        if(sname[0].equals(city)){
            name = name.replace(sname[0], "").trim();
        }

        return name;
    }
    public String parseFiAvailability(String favailability){
        String availability = favailability;

        availability = availability.replace("ma", "Mon");
        availability = availability.replace("pe", "Fri");
        availability = availability.replace("la", "Sat");
        availability = availability.replace("su", "Sun");
        availability = availability.replace("ti", "Tue");
        availability = availability.replace("to", "Thu");
        availability = availability.replace("ke", "Wed");

        //Making sure everything is in same format
        if (availability.contains("24h")){
            availability = "24h";
        }
        if (availability.contains(":")){
            availability = availability.replace(":", ".");
        }
        if(availability.contains(" - ")){
            availability = availability.replace(" - ", "-");
        }
        if(availability.contains(", ")) {
            availability = availability.replace(", ", ";");
        }
        if (availability.contains("0.00-0.00")){
            availability = "24h";
            System.out.println("Toimii");
        }
        return availability;
    }

    public String parseEeAvailability(String favailaility){
        String availability = favailaility;

        availability = availability.replace("E", "Mon");
        availability = availability.replace("N", "Thu");
        availability = availability.replace("R", "Fri");
        availability = availability.replace("L", "Sat");
        availability = availability.replace("P", "Sun");

        //Making sure everything is in same format
        if(availability.contains(":")){
            availability = availability.replace(":", ".");
        }
        if(availability.contains(" - ")){
            availability = availability.replaceAll(" - ", "-");
        }
        if(availability.contains(" -")){
            availability = availability.replace(" -", "-");
        }
        if (availability.contains("24h")){
            availability = "24h";
        }
        if(availability.contains(" kell")){
            availability = availability.replace(" kell", "");
        }
        if(availability.contains("0 S")){
            availability = availability.replace("0 S", "0;S");
        }
        if(availability.contains(", ")) {
            availability = availability.replace(", ", ";");
        }
        if(availability.contains("; ")){
            availability = availability.replace("; ", ";");
        }
        if(availability.contains(" – ")){
            availability = availability.replaceAll(" – ", "-");
        }

        //Fix formats that occurs only once in xml
        if(availability.contains(";Sat-Sun-")){
            availability = availability.replace(";Sat-Sun-", "");
        }
        if(availability.contains("Mon-Thu;Sun 8.00-22.00;Fri-Sat 8.00-22.00")){
            availability = availability.replace("Mon-Thu;Sun 8.00-22.00;Fri-Sat 8.00-22.00", "Mon-Sun 8.00-22.00");
        }
        if(availability.contains("00 21")){
            availability = availability.replace("00 21", "00-21");
        }
        if (availability.contains("–")){
            availability = availability.replace("–", "-");
        }
        return availability;
    }
}
