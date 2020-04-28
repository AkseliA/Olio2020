package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {
    TextView displayAccTxt;
    Switch transactionSwitch, cardSwitch;
    Button deleteAccBtn, confirmBtn;
    Intent intent;
    FirebaseAuth fbAuth;
    DatabaseReference reference;
    FirebaseDatabase fbDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        //initialize
        displayAccTxt = findViewById(R.id.displayAccount_txt);
        transactionSwitch = findViewById(R.id.transaction_switch);
        cardSwitch = findViewById(R.id.create_a_card_switch);
        deleteAccBtn = findViewById(R.id.delete_the_acc_btn);
        confirmBtn = findViewById(R.id.confirm_btn);
        intent = getIntent();

        //retrieve extras from intent
        String type = intent.getStringExtra("account_type");
        String number = intent.getStringExtra("account_number");
        //Set displayAcctxt
        displayAccTxt.setText(type + ": " + number);

        //TRANSACTION HISTORY PUUTTUU

        //Load account info + data
        loadAccountInfo(number, type);




    }

    //Loads account information and launches method to set switches correctly.
    public void loadAccountInfo(String accNmbr, final String type){
        fbDatabase = FirebaseDatabase.getInstance();
        reference = fbDatabase.getReference().child("Accounts").child(accNmbr);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String accNmbr;
                int credLimit;
                double accBalance, interest;
                Boolean payments, card;
                Account newAccount = null;
                if(!dataSnapshot.exists()){
                    Toast.makeText(AccountSettingsActivity.this, "Oops something went wrong!", Toast.LENGTH_SHORT).show();
                }else{
                    //Create new object based on type
                    if(type.contains("Credit")){
                        newAccount = dataSnapshot.getValue(CreditAccount.class);
                    }else if(type.contains("Debit")){
                        newAccount = dataSnapshot.getValue(DebitAccount.class);
                    }else{
                        newAccount = dataSnapshot.getValue(SavingsAccount.class);
                    }
                }
                setSwitches(newAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AccountSettingsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setSwitches(Account acc){
        //If has card
        if(acc.isCard()){
            cardSwitch.setChecked(true);
        }else{
            cardSwitch.setChecked(false);
        }

        //if can make payments
        if(acc.isMakePayments()){
            transactionSwitch.setChecked(true);
        }else{
            transactionSwitch.setChecked(false);
        }

    }
}
