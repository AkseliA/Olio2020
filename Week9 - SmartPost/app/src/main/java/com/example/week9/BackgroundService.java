package com.example.week9;



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

public class BackgroundService {

    private static BackgroundService bs;
    private ArrayList<Theatre> TheatreList = new ArrayList<Theatre>();

    public static BackgroundService getInstance(){
        if(bs == null){
            bs = new BackgroundService();
        }
        return bs;
    }
    private BackgroundService(){
        new fetchTheatreXML();
    }
    private class fetchTheatreXML extends Thread {
        @Override
        public void run() {
            String stringID = "";
            String location = "";
            int ID;

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            System.out.println("öö Lol");
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                String url = "http://www.finnkino.fi/xml/TheatreAreas/";
                Document document = builder.parse(url);
                document.getDocumentElement().normalize();

                NodeList nlist = document.getDocumentElement().getElementsByTagName("TheatreArea");

                for (int i = 0; i < nlist.getLength(); i++) {
                    Node node = nlist.item(i);

                    if (node.getNodeType() == node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        stringID = element.getElementsByTagName("ID").item(0).getTextContent();
                        location = element.getElementsByTagName("Name").item(0).getTextContent();

                        ID = Integer.parseInt(stringID);

                        System.out.print("ID: ");
                        System.out.println(element.getElementsByTagName("ID").item(0).getTextContent());
                        System.out.print("Location: ");
                        System.out.println(element.getElementsByTagName("Name").item(0).getTextContent());

                        Theatre cinema = new Theatre(ID, location);

                        TheatreList.add(cinema);


                    }
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } finally {
                System.out.println("¤¤¤¤¤¤¤¤¤¤TOIMI¤¤¤¤¤¤¤¤");
            }
        }
    }
}
