package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText email_in, name_in, password1_in, password2_in;
    Button singUpBtn;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initi...
        email_in = findViewById(R.id.email_txt);
        name_in = findViewById(R.id.username_txt);
        password1_in = findViewById(R.id.password1_txt);
        password2_in = findViewById(R.id.password2_txt);
        singUpBtn = findViewById(R.id.signUpBtn);


        fbAuth = FirebaseAuth.getInstance();


        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_in.getText().toString();
                String name = name_in.getText().toString();
                String password1 = password1_in.getText().toString();
                String password2 = password2_in.getText().toString();

                if(email.isEmpty()){
                    email_in.setError("Enter email address");
                    email_in.requestFocus();
                }
                else if(name.isEmpty()){
                    name_in.setError("Enter name");
                    name_in.requestFocus();
                }else if(!password1.equals(password2)){
                    Toast.makeText(RegisterActivity.this, "Passwords didn't match!", Toast.LENGTH_SHORT).show();

                }else{
                    fbAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "There was an error creating an account.", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }
}
