package com.example.olio_ht;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepositActivity extends AppCompatActivity {
    EditText amountToAdd;
    Spinner accountsSpnr;
    Button addBtn;
    FirebaseAuth fbAuth;
    DatabaseReference reference, userRef;
    FirebaseDatabase fbDatabase;
    InputOutputXml ioXml;
    Transaction newTransaction;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        //Initialize
        amountToAdd = findViewById(R.id.amount_txt);
        accountsSpnr = findViewById(R.id.accounts_Spnr);
        addBtn = findViewById(R.id.addMoneyBtn);

        fbAuth = FirebaseAuth.getInstance();

        //To populate spinner with accounts must retrieve user's data from firebaseDb.
        retrieveUserFromDb();

        //Onclicklistener vor addMoney button
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = accountsSpnr.getSelectedItemPosition();
                if (pos == 0) {
                    Toast.makeText(DepositActivity.this, "Select the account!", Toast.LENGTH_SHORT).show();
                } else {
                    makeDeposit();
                    amountToAdd.setText("");
                }

            }
        });

    }

    public void retrieveUserFromDb() {

        //Arraylist for spinner adapter
        final ArrayList<String> accAlistForSpnr = new ArrayList<String>();
        fbDatabase = FirebaseDatabase.getInstance();
        String userId = fbAuth.getUid();
        reference = fbDatabase.getReference().child("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //On datachange clear arraylist and add first element
                accAlistForSpnr.clear();
                accAlistForSpnr.add("Select account");

                User user = dataSnapshot.getValue(User.class);

                //fetch accounts if user has opened accounts
                String creditAccNmbr = user.getCredit_account();
                String debitAccNmbr = user.getDebit_account();
                String savingsAccNmbr = user.getSavings_account();

                //Add accounts to arraylist if number != 0
                if (!creditAccNmbr.equals("0")) {
                    accAlistForSpnr.add("Credit account: " + creditAccNmbr);
                }
                if (!debitAccNmbr.equals("0")) {
                    accAlistForSpnr.add("Debit account: " + debitAccNmbr);
                }
                if (!savingsAccNmbr.equals("0")) {
                    accAlistForSpnr.add("Savings account: " + savingsAccNmbr);
                }
                //Finally populate accountTypeSpinner
                setAccTypeSpnr(accAlistForSpnr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DepositActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAccTypeSpnr(ArrayList<String> accTypeAlist) {
        ArrayAdapter accTypeAdapter = new ArrayAdapter(DepositActivity.this, android.R.layout.simple_spinner_item, accTypeAlist);
        accTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountsSpnr.setAdapter(accTypeAdapter);
    }

    public void makeDeposit() {
        //Retrieve amount from edittext
        String textAmount = amountToAdd.getText().toString();
        double amount = 0;
        //Try to parse it into double
        try {
            amount = Double.parseDouble(textAmount);
        } catch (NumberFormatException e) {
            Toast.makeText(DepositActivity.this, "Amount must be in numbers!", Toast.LENGTH_SHORT).show();
        }

        //Retrieve account number from spinner item
        String spinnerItem = accountsSpnr.getSelectedItem().toString();
        String[] parts = spinnerItem.split(": ");
        String accountNumber = parts[1];

        editDBAccount(accountNumber, amount);

    }

    //Retrieves old balance, and pushes new balance to Firebase. ALSO makes a transaction and writes it to XML.
    public void editDBAccount(final String account_Number, final double amount) {
        fbDatabase = FirebaseDatabase.getInstance();
        reference = fbDatabase.getReference().child("Accounts").child(account_Number);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get old balance
                double oldBalance = Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                double newbalance = oldBalance + amount;


                //push newBalance to firebase
                userRef = fbDatabase.getReference().child("Accounts").child(account_Number);
                Map updateAcc = new HashMap();
                updateAcc.put("balance", newbalance);
                userRef.updateChildren(updateAcc);
                Toast.makeText(DepositActivity.this, "New balance: " + newbalance + "€", Toast.LENGTH_SHORT).show();


                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime now = LocalDateTime.now();
                String date = dtf.format(now);
                String[] parts = accountsSpnr.getSelectedItem().toString().split(": ");
                String to = parts[0];
                context = getApplicationContext();
                //New transaction
                String action = "Deposit to " + to;
                newTransaction = new Transaction(action, date, Double.toString(amount), Double.toString(newbalance), account_Number);
                ioXml = new InputOutputXml();
                ioXml.writeTransaction(context, newTransaction);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);
    }
}
