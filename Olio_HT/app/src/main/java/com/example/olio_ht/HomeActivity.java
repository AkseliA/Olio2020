package com.example.olio_ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    RecyclerView accRecyclerView;
    RecyclerView cardRecyclerView;
    RecyclerView.LayoutManager accRecyclerLayoutMgr, cardRecyclerLayoutMgr;
    FirebaseAuth fbAuth;
    DatabaseReference reference;
    FirebaseDatabase fbDatabase;
    TextView welcomeTxt, navHeaderName, navHeaderEmail;
    View header;
    Button addAccBtn;
    ItemSpacingDecorator accSpacingDecorator, cardSpacingDecorator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);

        accRecyclerView = findViewById(R.id.accountsRecyclerView);
        cardRecyclerView = findViewById(R.id.cardsRecyclerView);
        addAccBtn = findViewById(R.id.addAccBtn);
        navHeaderEmail = header.findViewById(R.id.nav_userEmailTxt);
        navHeaderName = header.findViewById(R.id.nav_userNameTxt);
        welcomeTxt = findViewById(R.id.display_userTxt);
        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fbAuth = FirebaseAuth.getInstance();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                //LOGOUT
                if (id == R.id.logout) {
                    signOut();
                }
                //SETTINGS
                if (id == R.id.settings) {
                    startActivity(new Intent(HomeActivity.this, UserSettingsActivity.class));
                }
                if (id == R.id.depositItem) {
                    startActivity(new Intent(HomeActivity.this, DepositActivity.class));
                }
                return true;
            }
        });


        String userId = fbAuth.getUid();
        fbDatabase = FirebaseDatabase.getInstance();
        reference = fbDatabase.getReference().child("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                welcomeTxt.setText(String.format("%s %s!", getResources().getString(R.string.Welcome), user.getFirst_name()));
                navHeaderName.setText(user.getFirst_name() + " " + user.getLast_name());
                navHeaderEmail.setText(user.getEmail());


                //fetch accounts if user has opened accounts
                String creditAccNmbr = user.getCredit_account();
                String debitAccNmbr = user.getDebit_account();
                String savingsAccNmbr = user.getSavings_account();
                fetchUsersAccounts(creditAccNmbr, debitAccNmbr, savingsAccNmbr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        addAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move to an activity to create a new account
                startActivity(new Intent(HomeActivity.this, AddAccountActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    //This function gets user's accounts and cards from the firebase database and creates arraylists to display them using recyclerview
    public void fetchUsersAccounts(final String creditAccNmbr, final String debitAccNmbr, final String savingsAccNmbr) {
        String userId = fbAuth.getUid();
        fbDatabase = FirebaseDatabase.getInstance();
        reference = fbDatabase.getReference().child("Accounts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String accNmbr, card;
                int credLimit;
                double accBalance, interest;
                Boolean payments;
                ArrayList<Account> accountAlist = new ArrayList<>();
                ArrayList<Card> cardAlist = new ArrayList<>();
                cardAlist.clear();
                accountAlist.clear();
                Card newCard = null;
                Account newAccount = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Retreive credit acc
                    if (ds.getKey().equals(creditAccNmbr)) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        accNmbr = map.get("accountNumber").toString();
                        credLimit = Integer.parseInt(map.get("limit").toString());
                        accBalance = Double.parseDouble(map.get("balance").toString());
                        card = map.get("cardNumber").toString();
                        payments = (Boolean) map.get("makePayments");

                        newAccount = new CreditAccount(accNmbr, accBalance, card, payments, credLimit);
                        if (!card.equals("0")) {
                            newCard = new Card(card, "Credit card");
                        }

                        //debit account
                    } else if (ds.getKey().equals(debitAccNmbr)) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        accNmbr = map.get("accountNumber").toString();
                        accBalance = Double.parseDouble(map.get("balance").toString());
                        card = map.get("cardNumber").toString();
                        payments = (Boolean) map.get("makePayments");

                        newAccount = new DebitAccount(accNmbr, accBalance, card, payments);
                        if (!card.equals("0")) {
                            newCard = new Card(card, "Debit card");
                        }

                        //Savings account
                    } else if (ds.getKey().equals(savingsAccNmbr)) {
                        Map<String, Object> map = (Map<String, Object>) ds.getValue();
                        accNmbr = map.get("accountNumber").toString();
                        accBalance = Double.parseDouble(map.get("balance").toString());
                        card = map.get("cardNumber").toString();
                        payments = (Boolean) map.get("makePayments");
                        interest = Double.parseDouble(map.get("interest").toString());
                        newAccount = new SavingsAccount(accNmbr, accBalance, card, payments, interest);
                    }
                    //If account was created and it's not in the list already ->add to list
                    if (newAccount != null) {
                        if (checkAccList(accountAlist, newAccount.getAccountNumber()))
                            accountAlist.add(newAccount);
                    }
                    //If card was created and it's not in the list already ->add to list
                    if (newCard != null) {
                        if (checkCardList(cardAlist, newCard.getCardNumber())) {
                            cardAlist.add(newCard);
                        }
                    }
                }
                displayCards(cardAlist);
                displayAccounts(accountAlist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void displayAccounts(ArrayList<Account> accountArrayList) {
        //Linear layout manager
        accRecyclerLayoutMgr = new LinearLayoutManager(this);
        accRecyclerView.setLayoutManager(accRecyclerLayoutMgr);

        //Spacing for items (Remove old decoration)
        if(accSpacingDecorator != null){
            accRecyclerView.removeItemDecoration(accSpacingDecorator);
        }else{
            accSpacingDecorator = new ItemSpacingDecorator(4);
            accRecyclerView.addItemDecoration(accSpacingDecorator);
        }
        //Specify adapter fo
        // r recyclerView
        accRecyclerView.setAdapter(new AccountRecyclerViewAdapter(this, accountArrayList));
    }

    public void displayCards(ArrayList<Card> cardArrayList) {
        //Linear layout manager
        cardRecyclerLayoutMgr = new LinearLayoutManager(this);
        cardRecyclerView.setLayoutManager(cardRecyclerLayoutMgr);

        //Spacing for items (Remove old decoration)
        if(cardSpacingDecorator != null){
            cardRecyclerView.removeItemDecoration(cardSpacingDecorator);
        }else{
            cardSpacingDecorator = new ItemSpacingDecorator(4);
            cardRecyclerView.addItemDecoration(cardSpacingDecorator);
        }

        //Specify adapter for recyclerView
        cardRecyclerView.setAdapter(new CardRecyclerViewAdapter(this, cardArrayList));
    }

    //This function checks if arraylist already contains an object with the same accountnumber. If it doesn't contain the function returns TRUE
    public boolean checkAccList(ArrayList<Account> aList, String accountNumber) {
        for (Account account : aList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return false;
            }
        }
        return true;
    }

    //This function checks if arraylist already contains an object with the same cardNumber. If it doesn't contain the function returns TRUE
    public boolean checkCardList(ArrayList<Card> aList, String cardNumber) {
        for (Card card : aList) {
            if (card.getCardNumber().equals(cardNumber)) {
                return false;
            }
        }
        return true;
    }


}
