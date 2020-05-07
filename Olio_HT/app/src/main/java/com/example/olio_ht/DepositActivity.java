package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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

        //Onclicklistener for addMoney button
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

    //Gets users accounts from database and populates spinner with them.
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

        if(textAmount.equals("")){
            textAmount = "0";
        }
        double amount = 0;
        //Parse string amount into double
        amount = Double.parseDouble(textAmount);
        //Amount must be 0
        if (amount > 0) {
            //Retrieve account number from spinner item
            String spinnerItem = accountsSpnr.getSelectedItem().toString();
            String[] parts = spinnerItem.split(": ");
            String accountNumber = parts[1];

            editDBAccount(accountNumber, amount);
        } else {
            Toast.makeText(DepositActivity.this, "Amount can not be 0!", Toast.LENGTH_SHORT).show();
        }

    }

    //Retrieves old balance, and pushes new balance to Firebase. ALSO makes a transaction and writes it to XML.
    public void editDBAccount(final String account_Number, final double amount) {
        fbDatabase = FirebaseDatabase.getInstance();
        reference = fbDatabase.getReference().child("Accounts").child(account_Number);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] parts = accountsSpnr.getSelectedItem().toString().split(": ");
                String to = parts[0];

                //get old balance
                double oldBalance = Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                double newbalance = oldBalance + amount;


                //push newBalance to firebase
                userRef = fbDatabase.getReference().child("Accounts").child(account_Number);
                Map updateAcc = new HashMap();
                updateAcc.put("balance", newbalance);
                userRef.updateChildren(updateAcc);
                Toast.makeText(DepositActivity.this, "New balance: " + newbalance + "€", Toast.LENGTH_SHORT).show();


                //New transaction
                String action = "Deposit to " + to;
                createTransaction(action, amount, newbalance, account_Number);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(eventListener);
    }

    //Creates new transaction object and writes it to an Xml
    public void createTransaction(String action, Double amount, Double newBalance, String account_Number) {
        //Datetime
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);
        String formattedDate = simpleDateFormat.format(cal.getTime());

        context = getApplicationContext();
        //New transaction
        newTransaction = new Transaction(action, formattedDate, Double.toString(amount), Double.toString(newBalance), account_Number);
        ioXml = new InputOutputXml();
        ioXml.writeTransaction(context, newTransaction);
    }
}
