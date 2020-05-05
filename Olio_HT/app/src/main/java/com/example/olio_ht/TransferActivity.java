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

public class TransferActivity extends AppCompatActivity {
    Spinner fromAccSpnr, toAccSpnr;
    EditText amountToAddTxt;
    Button transferBtn;
    FirebaseAuth fbAuth;
    DatabaseReference referenceFrom, referenceTo, reference;
    FirebaseDatabase fbDatabase;
    InputOutputXml ioXml;
    Transaction newTransaction;
    Context context;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        //Initialize
        fromAccSpnr = findViewById(R.id.accountsFrom_Spnr2);
        toAccSpnr = findViewById(R.id.accountsTo_Spnr);
        amountToAddTxt = findViewById(R.id.amount_txt);
        transferBtn = findViewById(R.id.transfer_Btn);
        //Firebase stuff
        fbAuth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();
        userId = fbAuth.getUid();

        //To populate spinner with accounts must retrieve user's data from firebaseDb.
        retrieveUserFromDb();

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VIRHEENKÄSITTELY
                double amount = Double.parseDouble(amountToAddTxt.getText().toString());
                makeTransfer(amount);
            }
        });

    }

    //Retreives users accounts to display them on spinners.
    public void retrieveUserFromDb() {
        //Arraylist for spinner adapter
        final ArrayList<String> accAlistForSpnr = new ArrayList<String>();
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

                //Populate spinners
                setAccTypeSpinners(accAlistForSpnr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TransferActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setAccTypeSpinners(ArrayList<String> accTypeAlist) {
        //Set adapter
        ArrayAdapter accTypeAdapter = new ArrayAdapter(TransferActivity.this, android.R.layout.simple_spinner_item, accTypeAlist);
        accTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Populate spinners
        fromAccSpnr.setAdapter(accTypeAdapter);
        toAccSpnr.setAdapter(accTypeAdapter);
    }

    public void makeTransfer(double amount) {
        //Retrieve account numbers from spinner items
        String accountTo = toAccSpnr.getSelectedItem().toString();
        String[] partsT = accountTo.split(": ");
        String accountNumberTo = partsT[1];
        String typeTo = partsT[0];

        String accountFrom = fromAccSpnr.getSelectedItem().toString();
        String[] partsF = accountFrom.split(": ");
        String typeFrom = partsF[0];
        String accountNumberFrom = partsF[1];

        transferFromAccount(accountNumberFrom, typeFrom, typeTo, accountNumberTo, amount);
    }

    //Checks if "From" account has balance for the transfer. If it does proceeds to function which adds money "To" account.
    //Function also pushes new balance to database
    public void transferFromAccount(final String account_Number_From, final String typeFrom, final String typeTo, final String account_Number_To, final double amount) {
        fbDatabase = FirebaseDatabase.getInstance();
        referenceFrom = fbDatabase.getReference().child("Accounts").child(account_Number_From);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get old balance
                double oldBalance = Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                double newBalance = oldBalance - amount;
                String action = "Transfer to " + typeTo;
                //If creditAccount
                if (typeFrom.equals("Credit account")) {
                    if (newBalance <= Integer.parseInt(dataSnapshot.child("limit").getValue().toString())) {
                        Map updateAcc = new HashMap();
                        updateAcc.put("balance", newBalance);
                        referenceFrom.updateChildren(updateAcc);
                        transferToAccount(account_Number_To, typeFrom, amount);

                        //create transaction and save it to XML
                        createTransaction(action, amount, newBalance, account_Number_From);

                    } else {
                        Toast.makeText(TransferActivity.this, "Credit limit exceeded!", Toast.LENGTH_SHORT).show();
                    }

                    //If "normal" account
                } else if (oldBalance - amount >= 0) {
                    Map updateAcc = new HashMap();
                    updateAcc.put("balance", newBalance);
                    referenceFrom.updateChildren(updateAcc);
                    transferToAccount(account_Number_To, typeFrom, amount);

                    //create transaction and save it to XML
                    createTransaction(action, amount, newBalance, account_Number_From);
                } else {
                    Toast.makeText(TransferActivity.this, "You don't have enough money.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        referenceFrom.addListenerForSingleValueEvent(eventListener);
    }

    public void transferToAccount(final String account_Number_To, final String typeFrom, final double amount) {
        fbDatabase = FirebaseDatabase.getInstance();
        referenceTo = fbDatabase.getReference().child("Accounts").child(account_Number_To);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get old balance
                double oldBalance = Double.parseDouble(dataSnapshot.child("balance").getValue().toString());
                double newBalance = oldBalance + amount;

                //Update balance
                Map updateAcc = new HashMap();
                updateAcc.put("balance", newBalance);
                referenceTo.updateChildren(updateAcc);

                Toast.makeText(TransferActivity.this, "Transfer successful.", Toast.LENGTH_SHORT).show();

                //create transaction and save it to XML
                String action = "Transfer from " + typeFrom;
                createTransaction(action, amount, newBalance, account_Number_To);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        referenceTo.addListenerForSingleValueEvent(eventListener);
    }

    //Creates new transcation object and writes it to an Xml
    public void createTransaction(String action, Double amount, Double newbalance, String account_Number){
        //required info
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);

        context = getApplicationContext();
        //New transaction
        newTransaction = new Transaction(action, date, Double.toString(amount), Double.toString(newbalance), account_Number);
        ioXml = new InputOutputXml();
        ioXml.writeTransaction(context, newTransaction);
    }
}
