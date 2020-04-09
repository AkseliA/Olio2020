package com.example.week11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    TextView unEditTxt;
    TextView displayTxt;
    EditText editText;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unEditTxt = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        displayTxt = findViewById(R.id.display_txt);

        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.settings){
                    loadActivity(view);
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    public void loadActivity(View v){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);


        startActivityForResult(intent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                //Retreive values from settings activity
                String fontsize = data.getStringExtra("fontSz");
                String fontColor = data.getStringExtra("fontClr");
                String width = data.getStringExtra("width");
                String height = data.getStringExtra("height");
                boolean lockStatus = data.getBooleanExtra("switch_boolean", false);
                String name = data.getStringExtra("display_name");


                //Change attributes
                setAttributes(fontsize, fontColor, width, height, lockStatus, name);
            }
        }
    }

    public void setAttributes(String size, String color, String width, String height, boolean lockStatus, String name){

        if(!size.equals("Current")){
            unEditTxt.setTextSize(Integer.parseInt(size));
        }

        if(!color.equals("Current")){
            //Change color to desired one
            if(color.equals("White")){
                unEditTxt.setTextColor(Color.WHITE);
            }else if(color.equals("Black")){
                unEditTxt.setTextColor(Color.BLACK);
            }else if(color.equals("Yellow")){
                unEditTxt.setTextColor(Color.YELLOW);
            }else if(color.equals("Green")){
                unEditTxt.setTextColor(Color.GREEN);
            }else if(color.equals("Red")){
                unEditTxt.setTextColor(Color.RED);
            }else if(color.equals("Blue")){
                unEditTxt.setTextColor(Color.BLUE);
            }
        }

        if(!width.equals("0")){
            ViewGroup.LayoutParams params =  unEditTxt.getLayoutParams();
            int w = Integer.parseInt(width);
            w = dpToPx(w);
            params.width = w;
            unEditTxt.setLayoutParams(params);
        }

        if(!height.equals("0")){
            ViewGroup.LayoutParams params = unEditTxt.getLayoutParams();
            int h = Integer.parseInt(height);
            h = dpToPx(h);
            params.height = h;
            unEditTxt.setLayoutParams(params);
        }
        if(lockStatus){
            editText.setEnabled(false);
            unEditTxt.setText(editText.getText());
            editText.setText("");
        }else if(!lockStatus){
            editText.setEnabled(true);
            unEditTxt.setText(R.string.random_string);
        }if(!name.equals("")){
            displayTxt.setText(getString(R.string.hello) + name);
        }
    }


    public int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
