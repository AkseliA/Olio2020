/* Author: Akseli Aula
 * Environment: Android Studio
 * Assignment week 9*/

package com.example.week9_smartpost;
public class SmartPost {
    private String ID;
    private String name;
    private String address;
    private String city;
    private String postalcode;
    private String country;
    private String availability;


    public SmartPost(String place_id, String fname, String faddress, String fcity, String postcode, String fcountry, String favailability ){
        ID = place_id;
        name = fname;
        address = faddress;
        city = fcity;
        postalcode = postcode;
        country = fcountry;
        availability = favailability;
    }

    public String getName(){
        return name;
    }

    public String getAvailability() {
        return availability;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        String s = String.format(name + " - " + city);
        return s;
    }

}
