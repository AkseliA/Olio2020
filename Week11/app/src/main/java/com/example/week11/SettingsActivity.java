package com.example.week11;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {
    Spinner colorSpnr;
    Spinner fontSpnr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        colorSpnr = findViewById(R.id.colorSpinner);
        fontSpnr = findViewById(R.id.fontSpinner);


        setSpinners();



    }
    public void setSpinners(){
        //ColorSpinner
        ArrayAdapter colorAdapter = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpnr.setAdapter(colorAdapter);

        //Fontsize Spinner
        ArrayAdapter fontsizeAdapter = ArrayAdapter.createFromResource(this, R.array.fontsize_array, android.R.layout.simple_spinner_item);
        fontsizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpnr.setAdapter(fontsizeAdapter);
    }
}
