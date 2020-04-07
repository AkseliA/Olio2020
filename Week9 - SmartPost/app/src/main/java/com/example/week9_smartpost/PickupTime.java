/* Author: Akseli Aula
 * Environment: Android Studio
 * Assignment week 9*/

package com.example.week9_smartpost;

public class PickupTime {


    private String Id;
    private int day;
    private String express_in;
    private String express_out;

    public PickupTime(String place_id, int sday, String eIn, String eOut){
        Id = place_id;
        day = sday;
        express_in = eIn;
        express_out = eOut;
    }
    public String getId() {
        return Id;
    }
    public int getDay() {
        return day;
    }

    public String getExpress_in() {
        return express_in;
    }

    public String getExpress_out() {
        return express_out;
    }

}
