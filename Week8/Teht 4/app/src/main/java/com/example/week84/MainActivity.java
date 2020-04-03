package com.example.week84;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Context context;
    Button addBtn;
    Button buyBtn;
    Button returnBtn;
    TextView text;
    TextView sbprogress;
    TextView balancetxt;
    BottleDispenser dispenser;
    SeekBar seekbar;
    Spinner spinner;
    int amount = 0;
    int Id;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = getApplicationContext();
        addBtn = findViewById(R.id.addMoneyBtn);
        buyBtn = findViewById(R.id.buyBottleBtn);
        returnBtn = findViewById(R.id.returnMoneyBtn);
        text = findViewById(R.id.editText);
        sbprogress = findViewById(R.id.SbProgress);
        balancetxt = findViewById(R.id.balanceTxt);
        seekbar = findViewById(R.id.seekBar);
        spinner = findViewById(R.id.spinner);
        dispenser = BottleDispenser.getInstance(spinner, context);

        balancetxt.setText("Balance: "+dispenser.getBalance());

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
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyBottle(Id);
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnMoney();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Id = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void addMoney(int amount){
        dispenser.addMoney(text, amount);
        seekbar.setProgress(0);
        sbprogress.setText("Amount: "+ seekbar.getProgress()+"/5€");
        balancetxt.setText("Balance: "+dispenser.getBalance());

    }
    public void buyBottle(int Id){
        dispenser.buyBottle(text, Id);
        spinner.setSelection(0);
        DecimalFormat df = new DecimalFormat("0.00");
        balancetxt.setText("Balance: "+df.format(dispenser.getBalance()));
    }
    public void returnMoney(){
        dispenser.returnMoney(text);
        balancetxt.setText("Balance: "+dispenser.getBalance());
    }
}
