package com.example.week75;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText filename;
    EditText text;
    Button load;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filename = findViewById(R.id.inputFilename);
        text = findViewById(R.id.typeText);

        load = findViewById(R.id.loadButton);
        load.setOnClickListener(new View.OnClickListener(){
            /*Load text file*/
            @Override
            public void onClick(View v){
                String name;
                String output;
                name = filename.getText().toString();

                output = loadFile(name);
                text.setText(output);
            }
        });

        save = findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            /*Save text to file*/
            @Override
            public void onClick(View v) {
                String name;
                String txt;
                name = filename.getText().toString();
                txt = text.getText().toString();
                saveFile(name, txt);
            }
        });
    }
    public void saveFile(String name, String txt){
        try{
            FileOutputStream fos = openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(txt.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String loadFile(String name){
            String output = "";
            int size;
            try{
                FileInputStream fis = openFileInput(name);
                size = fis.available();
                byte[] txtbuffer = new byte[size];
                fis.read(txtbuffer);
                fis.close();
                output = new String(txtbuffer);

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }catch (IOException e) {
                e.printStackTrace();
            }
            return output;
    }
}
