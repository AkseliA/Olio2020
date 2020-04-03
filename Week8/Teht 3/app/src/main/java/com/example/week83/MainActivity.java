package com.example.week83;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button addBtn;
    Button buyBtn;
    Button returnBtn;
    TextView text;
    TextView sbprogress;
    BottleDispenser dispenser;
    SeekBar seekbar;
    int amount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dispenser = BottleDispenser.getInstance();
        addBtn = findViewById(R.id.addMoneyBtn);
        buyBtn = findViewById(R.id.buyBottleBtn);
        returnBtn = findViewById(R.id.returnMoneyBtn);
        text = findViewById(R.id.editText);
        sbprogress = findViewById(R.id.SbProgress);
        seekbar = findViewById(R.id.seekBar);

        /*Seekbar rahanlisäyksen maksimiarvo (5)*/
        seekbar.setMax(5);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amount = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sbprogress.setText("Amount: "+ seekbar.getProgress()+"/5€");
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney(amount);
                seekbar.setProgress(0);
                sbprogress.setText("Amount: "+ seekbar.getProgress()+"/5€");

            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyBottle();
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnMoney();
            }
        });
    }


    public void addMoney(int amount){
        dispenser.addMoney(text, amount);


    }
    public void buyBottle(){
        dispenser.buyBottle(text);
    }
    public void returnMoney(){
        dispenser.returnMoney(text);
    }
}
