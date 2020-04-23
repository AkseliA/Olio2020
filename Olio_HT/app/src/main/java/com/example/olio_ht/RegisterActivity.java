package com.example.olio_ht;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText email_in, name_in, password1_in, password2_in, phoneNmbr_in;
    Button singUpBtn;
    FirebaseAuth fbAuth;
    String salt, email, name, password1, password2, phoneNmbr;
    PasswordHasher pwHasher = new PasswordHasher();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing
        email_in = findViewById(R.id.email_txt);
        name_in = findViewById(R.id.username_txt);
        password1_in = findViewById(R.id.password1_txt);
        password2_in = findViewById(R.id.password2_txt);
        singUpBtn = findViewById(R.id.signUpBtn);
        phoneNmbr_in = findViewById(R.id.phonenumber_txt);




        fbAuth = FirebaseAuth.getInstance();


        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                email = email_in.getText().toString();
                name = name_in.getText().toString();
                phoneNmbr = phoneNmbr_in.getText().toString();
                password1 = password1_in.getText().toString();
                password2 = password2_in.getText().toString();


                if(email.isEmpty()){
                    email_in.setError("Enter email address");
                    email_in.requestFocus();
                }
                else if(name.isEmpty()){
                    name_in.setError("Enter name");
                    name_in.requestFocus();

                }else if(phoneNmbr.isEmpty()){
                    name_in.setError("Enter phone number");
                    phoneNmbr_in.requestFocus();
                }else if(!password1.equals(password2)){
                    Toast.makeText(RegisterActivity.this, "Passwords didn't match!", Toast.LENGTH_SHORT).show();

                }else {
                    boolean check;
                    check = checkPassword(password1);
                    //if password doesn't contain atleast 1x number + 1x special character + small and big letter + 12 marks long
                    if (!check) {
                        Toast.makeText(RegisterActivity.this, "Password must be atleast 12 marks long and contain at minimum 1 number, 1 special character, small and big letters.", Toast.LENGTH_LONG).show();
                    } else{
                        createAccount(password1, email, name, phoneNmbr);
                    }
                }
            }
        });

    }
    public boolean checkPassword(String pw){
        boolean valid = false;
        boolean containsSpecial = false;
        boolean containsCapital = false;
        boolean containsLower = false;
        boolean containsNumber = false;
        boolean validLength = false;

        //Check length
        if(pw.length() >= 12){
            validLength = true;
        }
        //creating a char array for further checking
        char[] pwArray = pw.toCharArray();

        for (char c : pwArray) {
            //Check for number
            if(Character.isDigit(c)){
                containsNumber = true;

            //Check for uppercase
            }else if(Character.isUpperCase(c)){
                containsCapital = true;

            //Check for lowercase
            }else if(Character.isLowerCase(c)){
                containsLower = true;
            }
        }
        //Check for special character using pattern containing all non special characters.
        Pattern characters = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = characters.matcher(pw);
        if(match.find()){
            containsSpecial = true;
        }

        //Check if all are true
        if(containsSpecial && containsCapital && containsLower && containsNumber && validLength){
            valid = true;
        }
        return valid;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createAccount(String password1, final String email, final String name, final String phoneNmbr){
        salt = pwHasher.getSalt();
        final String hashedPw = pwHasher.getHashedPassword(password1, salt);

        fbAuth.createUserWithEmailAndPassword(email, hashedPw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "There was an error creating an account.", Toast.LENGTH_SHORT).show();
                } else {
                    //Store information to database
                    String user_id = fbAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                    Map newUser = new HashMap();
                    newUser.put("phone", phoneNmbr);
                    newUser.put("email", email);
                    newUser.put("name", name);
                    newUser.put("salt", salt);
                    current_user_db.setValue(newUser);

                    Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
