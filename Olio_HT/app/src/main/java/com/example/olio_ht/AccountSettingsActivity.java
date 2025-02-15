package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AccountSettingsActivity extends AppCompatActivity {
    TextView displayAccTxt;
    Switch transactionSwitch, cardSwitch;
    Button deleteAccBtn, confirmBtn;
    EditText newCreditLimitTxt;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerLayoutMgr;
    Intent intent;
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    DatabaseReference reference, userRef;
    FirebaseDatabase fbDatabase;
    String type, number, currCardNumber;
    InputOutputXml ioXml;
    Context context;
    DateTimeFormatter dtf;
    LocalDateTime now;
    ItemSpacingDecorator itemSpacingDecorator;

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
        newCreditLimitTxt = findViewById(R.id.newCredit_limit_txt);
        recyclerView = findViewById(R.id.transaction_recyclerView);

        intent = getIntent();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        fbAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();
        ioXml = new InputOutputXml();

        //retrieve extras from intent
        type = intent.getStringExtra("account_type");
        number = intent.getStringExtra("account_number");
        currCardNumber = intent.getStringExtra("card_number");
        //Set displayAcctxt
        String textToDisplay = type + ": " + number;
        displayAccTxt.setText(textToDisplay);



        //Load account info + data
        loadAccountInfo(number, type);

        //Display transactions
        displayTransactions();

        //DeleteAcc button onClickListener
        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndShowAlertDialog();
            }
        });

        //OnClickListener for confirmButton
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String credLimit = newCreditLimitTxt.getText().toString();
                if (credLimit.equals("")) {
                    credLimit = "0";
                }
                editAccountSettings(number, credLimit);
                finish();
            }
        });
    }

    //Loads account information and launches method to set switches correctly.
    public void loadAccountInfo(String accNmbr, final String type) {
        fbDatabase = FirebaseDatabase.getInstance();
        reference = fbDatabase.getReference().child("Accounts").child(accNmbr);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Account newAccount = null;
                if (!dataSnapshot.exists()) {
                    Toast.makeText(AccountSettingsActivity.this, "Oops something went wrong!", Toast.LENGTH_SHORT).show();
                } else {
                    //Create new object based on type
                    if (type.contains("Credit")) {
                        newAccount = dataSnapshot.getValue(CreditAccount.class);
                    } else if (type.contains("Debit")) {
                        newAccount = dataSnapshot.getValue(DebitAccount.class);
                        newCreditLimitTxt.setVisibility(View.INVISIBLE);
                    } else {
                        newAccount = dataSnapshot.getValue(SavingsAccount.class);
                        cardSwitch.setVisibility(View.INVISIBLE);
                        newCreditLimitTxt.setVisibility(View.INVISIBLE);
                    }
                }
                setOptions(newAccount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AccountSettingsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setOptions(Account acc) {
        //If has card
        if (!acc.getCardNumber().equals("0")) {
            cardSwitch.setChecked(true);
        } else {
            cardSwitch.setChecked(false);
        }

        //if can make payments
        if (acc.isMakePayments()) {
            transactionSwitch.setChecked(true);
        } else {
            transactionSwitch.setChecked(false);
        }

    }

    //When delete account button is pressed, show alertDialog
    //Source: https://stackoverflow.com/questions/25670051/how-to-create-yes-no-alert-dialog-in-fragment-in-android
    public void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AccountSettingsActivity.this);
        builder.setTitle("Are you sure? Changes are irreversible.");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Yes, delete account
                dialog.dismiss();
                deleteAccountFromDb(number);
                //Return to HomeActivity
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Do nothing.
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteAccountFromDb(String account_number) {
        //Modify type_account value to 0 from child "Users"
        String userId = fbAuth.getUid();
        userRef = fbDatabase.getReference().child("Users").child(userId);
        Map updateAcc = new HashMap();
        //get childname with String type
        if (type.contains("Credit")) {
            updateAcc.put("credit_account", "0");
        } else if (type.contains("Debit")) {
            updateAcc.put("debit_account", "0");
        } else {
            updateAcc.put("savings_account", "0");
        }
        userRef.updateChildren(updateAcc);

        //remove it from child "Accounts"
        reference = fbDatabase.getReference().child("Accounts");
        reference.child(account_number).removeValue();
    }

    public void editAccountSettings(String account_number, String creditLimit) {
        boolean makePayments = transactionSwitch.isChecked();
        boolean hasCard = cardSwitch.isChecked();
        int credLimit = Integer.parseInt(creditLimit);
        reference = fbDatabase.getReference().child("Accounts").child(account_number);

        Map editAcc = new HashMap();

        //if hasCard is ON and there's no card already ->Create a new card
        if (hasCard && currCardNumber.equals("0")) {
            CreateCard newCard = new CreateCard();
            //New cardNumber is based on number of the account.
            String newCardNmbr = newCard.generateNewCard(account_number);
            editAcc.put("cardNumber", newCardNmbr);
        }
        // If hascard is set off and there's a card already -> deletes the card
        if (!hasCard && !currCardNumber.equals("0")) {
            editAcc.put("cardNumber", "0");
        }


        editAcc.put("makePayments", makePayments);
        //editAcc.put("cardNumber");
        if (credLimit > 0) {
            editAcc.put("limit", credLimit);
            //New transaction when limit is changed
            String action = "Credit limit set: " + creditLimit;
            createTransaction(action, account_number);

        }
        reference.updateChildren(editAcc);
    }
    //Creates new transcation object and writes it to an Xml
    public void createTransaction(String action, String account_Number) {
        //Datetime
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMANY);
        String formattedDate = simpleDateFormat.format(cal.getTime());

        context = getApplicationContext();
        //New transaction
        Transaction newTransaction = new Transaction(action, formattedDate, "", "", account_Number);
        ioXml = new InputOutputXml();
        ioXml.writeTransaction(context, newTransaction);
    }
    public void displayTransactions() {
        ArrayList<Transaction> transArrList = new ArrayList<>();
        transArrList = ioXml.readTransactionXml(context, number);
        //Reversing arraylist so that newest actions are at the top of the recyclerview
        Collections.reverse(transArrList);
        //Linear layout manager
        recyclerLayoutMgr = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutMgr);

        //Spacing for items (Remove old decoration if there is one already)
        if(itemSpacingDecorator != null){
            recyclerView.removeItemDecoration(itemSpacingDecorator);
        }else{
            itemSpacingDecorator = new ItemSpacingDecorator(4);
            recyclerView.addItemDecoration(itemSpacingDecorator);
        }
        //Specify adapter for recyclerView
        recyclerView.setAdapter(new TransactionRecyclerViewAdapter(this, transArrList));
    }
}
