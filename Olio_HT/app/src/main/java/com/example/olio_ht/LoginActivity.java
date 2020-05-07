package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button signIn, signUp;
    EditText email_txt, password_txt;
    FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener fbAuthStateListener;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize
        signIn = findViewById(R.id.signIn_btn);
        signUp = findViewById(R.id.signUp_btn);
        email_txt = findViewById(R.id.email_txt);
        password_txt = findViewById(R.id.password_txt);
        fbAuth = FirebaseAuth.getInstance();

        fbAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = fbAuth.getCurrentUser();
            }
        };

        context = getApplicationContext();
        InputOutputXml ioXml = new InputOutputXml();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(fbAuthStateListener);
    }

    //Takes email and password from editText fields. Creates hashed password and tries to login using firebase signInWithEmailAndPassword method.
    public void logIn() {
        String email = email_txt.getText().toString();
        String password = password_txt.getText().toString();

        PasswordHasher pwHash = new PasswordHasher();

        if (email.isEmpty()) {
            email_txt.setError("Enter email address");
            email_txt.requestFocus();

        }if (password.isEmpty()) {
            password_txt.setError("Enter password");
            password_txt.requestFocus();

        }
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Fill in your email and password", Toast.LENGTH_SHORT).show();

        } else {
            //For logging in we must generate hashedpassword from the input
            String salt = pwHash.getSalt();
            String hashedPw = pwHash.getHashedPassword(password, salt);

            fbAuth.signInWithEmailAndPassword(email, hashedPw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, AuthenticationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
