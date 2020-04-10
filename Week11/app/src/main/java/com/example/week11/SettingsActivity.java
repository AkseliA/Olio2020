/* Author: Akseli Aula
 * Environment: Android Studio
 * Assignment week 11*/

package com.example.week11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    Locale locale;
    Spinner colorSpnr;
    Spinner fontSpnr;
    Spinner languageSpnr;
    SeekBar widthSb;
    SeekBar heightSb;
    Switch lockSwitch;
    EditText name_txt;
    int width, height;
    int maxWidth = 400, minWidth = 150;
    int maxHeight = 300, minHeight = 100;
    int step = 10;
    boolean switchState;
    Toast popupMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        colorSpnr = findViewById(R.id.colorSpinner);
        fontSpnr = findViewById(R.id.fontSpinner);
        languageSpnr = findViewById(R.id.languageSpinner);
        widthSb = findViewById(R.id.widthSB);
        heightSb = findViewById(R.id.heightSB);
        lockSwitch = findViewById(R.id.lock_switch);
        name_txt = findViewById(R.id.settings_display_txt);



        lockSwitch.setChecked(false);


        setSpinners();

        //Language change listener
        languageSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    setLocale("fi");
                }
                if(position == 2){
                    setLocale("en");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Seekbar max values
        widthSb.setMax((maxWidth-minWidth)/step);
        heightSb.setMax((maxHeight-minHeight)/step);

        //WidthSeekbar Listener
        widthSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                width = minWidth + (progress*step);
                String w = "Width: ";
                broadcastMsg(w, width);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Height Seekbar listener
        heightSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                height = minHeight + (progress*step);
                String h = "Height: ";
                broadcastMsg(h, height);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


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

        //Language spinner
        ArrayAdapter languageAdapter = ArrayAdapter.createFromResource(this, R.array.language_array, android.R.layout.simple_spinner_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpnr.setAdapter(languageAdapter);
    }



    @Override
    public void onBackPressed(){
        String fontsize = fontSpnr.getSelectedItem().toString();
        String fontcolor = colorSpnr.getSelectedItem().toString();
        String name = name_txt.getText().toString();
        switchState = lockSwitch.isChecked();

        Intent intent = new Intent();
        intent.putExtra("fontSz", fontsize);
        intent.putExtra("fontClr", fontcolor);
        intent.putExtra("height", Integer.toString(height));
        intent.putExtra("width", Integer.toString(width));
        intent.putExtra("switch_boolean", switchState);
        intent.putExtra("display_name", name);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void setLocale(String lang){
        locale = new Locale(lang);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }

    public void broadcastMsg(String s, int i){
        if(popupMsg != null){
            popupMsg.cancel();
        }
        popupMsg = Toast.makeText(getApplicationContext(),s + i, Toast.LENGTH_SHORT);
        popupMsg.show();
    }



}
