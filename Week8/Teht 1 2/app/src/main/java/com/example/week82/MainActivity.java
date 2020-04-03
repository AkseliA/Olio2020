package com.example.week82;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button addBtn;
    Button buyBtn;
    Button returnBtn;
    TextView text;
    BottleDispenser dispenser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dispenser = BottleDispenser.getInstance();
        addBtn = findViewById(R.id.addMoneyBtn);
        buyBtn = findViewById(R.id.buyBottleBtn);
        returnBtn = findViewById(R.id.returnMoneyBtn);
        text = findViewById(R.id.editText);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney();

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
    public void addMoney(){
        /*Adds 1 money*/
        int amount = 1;
        dispenser.addMoney(text, amount);

    }
    public void buyBottle(){
        dispenser.buyBottle(text);
    }
    public void returnMoney(){
        dispenser.returnMoney(text);
    }
}
