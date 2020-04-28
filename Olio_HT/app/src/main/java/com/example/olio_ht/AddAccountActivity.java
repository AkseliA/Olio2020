package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AddAccountActivity extends AppCompatActivity {
    Spinner accTypeSpnr;
    EditText accNmbrTxt, creditLimitTxt, startingbalanceTxt;
    Button createAccBtn;
    FirebaseAuth fbAuth;
    DatabaseReference reference, UIDref;
    FirebaseDatabase fbDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);


        accTypeSpnr = findViewById(R.id.accType_Snpr);
        accNmbrTxt = findViewById(R.id.accNmbr_txt);
        creditLimitTxt = findViewById(R.id.creditLimit_txt);
        startingbalanceTxt = findViewById(R.id.setBalance_txt);
        fbAuth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();

        //Only show this textfield when creditAccount is chosen
        creditLimitTxt.setVisibility(View.INVISIBLE);


        createAccBtn = findViewById(R.id.createAcc_Btn);

        setAccTypeSpnr();
        accTypeSpnr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    creditLimitTxt.setVisibility(View.VISIBLE);
                } else {
                    creditLimitTxt.setText("");
                    creditLimitTxt.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!accNmbrTxt.getText().toString().equals("0")) {
                    compareAllAccNmbrs();
                } else {
                    Toast.makeText(AddAccountActivity.this, "Account number can not be 0!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void setAccTypeSpnr() {
        ArrayAdapter accTypeAdapter = ArrayAdapter.createFromResource(this, R.array.account_typeArray, android.R.layout.simple_spinner_item);
        accTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accTypeSpnr.setAdapter(accTypeAdapter);
    }

    public void createAccount() {
        String type = "";
        final String accNmbr;
        double startingBalance;
        int credLimit = 0;
        Account newAcc = null;
        accNmbr = accNmbrTxt.getText().toString();
        startingBalance = Double.parseDouble(startingbalanceTxt.getText().toString());

        //NOTE: By default boolean makepayments and card is set to false AND interest is 1.8(%)!
        if (!creditLimitTxt.getText().toString().equals("")) {
            credLimit = Integer.parseInt(creditLimitTxt.getText().toString());
        }
        if (accTypeSpnr.getSelectedItemPosition() == 1) {
            type = "credit_account";
            newAcc = new CreditAccount(accNmbr, startingBalance, false, false, credLimit);
        } else if (accTypeSpnr.getSelectedItemPosition() == 2) {
            type = "debit_account";
            newAcc = new DebitAccount(accNmbr, startingBalance, false, false);
        } else if (accTypeSpnr.getSelectedItemPosition() == 3) {
            type = "savings_account";
            newAcc = new SavingsAccount(accNmbr, startingBalance, false, false, 1.8);

        }


        //Add data to database.
        reference = fbDatabase.getReference().child("Accounts").child(accNmbr);
        reference.setValue(newAcc);
        String userId = fbAuth.getUid();
        UIDref = fbDatabase.getReference().child("Users").child(userId);
        Map updateAcc = new HashMap();
        updateAcc.put(type, accNmbr);
        UIDref.updateChildren(updateAcc);


        Toast.makeText(AddAccountActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddAccountActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    //Function to compare if account number is already used
    public void compareAllAccNmbrs() {
        final String accNmbr = accNmbrTxt.getText().toString();
        //Check if there's already an account with that number
        reference = fbDatabase.getReference().child("Accounts").child(accNmbr);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //There is no matches
                if (!dataSnapshot.exists()) {
                    compareUserAccounts(accNmbr);
                } else {
                    Toast.makeText(AddAccountActivity.this, "Account with that number already exists!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);
    }

    //Function to compare is user already has an account of that type.
    public void compareUserAccounts(String accNmbr) {
        String type = getType();
        String userId = fbAuth.getUid();
        UIDref = fbDatabase.getReference().child("Users").child(userId).child(type);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    createAccount();
                    //If account value is "0" user can create an active account
                } else if (dataSnapshot.exists() && dataSnapshot.getValue().equals("0")) {
                    createAccount();
                } else {
                    Toast.makeText(AddAccountActivity.this, "You already have an account of that type!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        UIDref.addListenerForSingleValueEvent(eventListener);
    }

    public String getType() {
        String type = "";
        if (accTypeSpnr.getSelectedItemPosition() == 1) {
            type = "credit_account";
        } else if (accTypeSpnr.getSelectedItemPosition() == 2) {
            type = "debit_account";
        } else if (accTypeSpnr.getSelectedItemPosition() == 3) {
            type = "savings_account";
            ;
        }
        return type;
    }


}
