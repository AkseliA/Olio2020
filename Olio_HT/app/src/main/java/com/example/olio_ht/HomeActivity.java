package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    FirebaseAuth fbAuth;
    DatabaseReference rootRef;
    DatabaseReference userRef;
    TextView welcomeTxt, navHeaderName, navHeaderEmail;
    View header;
    Button addAccBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        navigationView = findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);

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
        String user_id = fbAuth.getCurrentUser().getUid();







        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                //LOGOUT
                if(id == R.id.logout){
                    signOut();
                }
                if(id == R.id.settings){
                    startActivity(new Intent(HomeActivity.this, UserSettingsActivity.class));
                }
                return true;
            }
        });

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Users").child(user_id);
        ValueEventListener eventListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user;
                user = dataSnapshot.getValue(User.class);
                welcomeTxt.setText(String.format("%s %s!", getResources().getString(R.string.Welcome), user.getFirst_name()));
                navHeaderName.setText(user.getFirst_name() + " " + user.getLast_name());
                navHeaderEmail.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        userRef.addListenerForSingleValueEvent(eventListener);

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
        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }


}
