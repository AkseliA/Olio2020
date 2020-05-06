package com.example.olio_ht;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class AuthenticationActivity extends AppCompatActivity {
    TextView authNmbrText;
    EditText authNmbrInput;
    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        //Initialize
        authNmbrText = findViewById(R.id.randomNmbrTxt);
        okBtn = findViewById(R.id.ok_button);
        authNmbrInput = findViewById(R.id.authenticationNmbrInput);

        //Sets random number when the activity is opened
        setRandomNumber();


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check;
                check = checkInput(authNmbrInput.getText().toString(), authNmbrText.getText().toString());

                if (!check) {
                    setRandomNumber();
                    Toast.makeText(AuthenticationActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
                    authNmbrInput.setText("");
                } else {
                    Intent intent = new Intent(AuthenticationActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void setRandomNumber() {
        int auth = generateRandomAuth();
        authNmbrText.setText(String.valueOf(auth));
    }

    public int generateRandomAuth() {
        int authNmbr = 0;
        //No seed, so the number changes everytime
        Random gen = new Random();
        authNmbr = 100000 + gen.nextInt(900000);
        return authNmbr;
    }

    public boolean checkInput(String input, String wanted) {
        return input.equals(wanted);
    }
}
