package com.example.olio_ht;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserSettingsActivity extends AppCompatActivity {
    Button editBtn, changePwBtn;
    EditText currName, currSurname, currAddress, currEmail, currPhone, newPw1, newPw2;
    FirebaseAuth fbAuth;
    FirebaseDatabase fbDatabase;
    FirebaseUser fbUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        //Initialize
        currName = findViewById(R.id.currUsername_txt);
        currSurname = findViewById(R.id.currSurname_txt);
        currAddress = findViewById(R.id.currAddress_txt);
        currEmail = findViewById(R.id.currEmail_txt);
        currPhone = findViewById(R.id.currPhonenumber_txt);
        newPw1 = findViewById(R.id.newPassword1_txt);
        newPw2 = findViewById(R.id.newPassword2_txt);
        editBtn = findViewById(R.id.editBtn);
        changePwBtn = findViewById(R.id.editPwBtn);

        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        fbAuth = FirebaseAuth.getInstance();
        fbDatabase = FirebaseDatabase.getInstance();

        getUserInformation();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserInformation();
            }
        });

        changePwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserPassword();
            }
        });
    }

    //gets user information from firebase database and fills user info on editTexts.
    public void getUserInformation(){
        String userId = fbAuth.getUid();
        reference = fbDatabase.getReference().child("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                currName.setText(user.getFirst_name());
                currSurname.setText(user.getLast_name());
                currAddress.setText(user.getAddress());
                currPhone.setText(user.getPhone());
                currEmail.setText(user.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserSettingsActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //On button press gets new values from edittexts and pushes to firebase.
    public void editUserInformation() {
        String newName = currName.getText().toString();
        String newSurname = currSurname.getText().toString();
        String newEmail = currEmail.getText().toString();
        String newAddress = currAddress.getText().toString();
        String newPhone = currPhone.getText().toString();

        //Update dbUser if fields are not empty
        String userId = fbAuth.getUid();
        reference = fbDatabase.getReference().child("Users").child(userId);
        Map updateAcc = new HashMap();
        if(!newAddress.isEmpty()){
            updateAcc.put("address", newAddress);
        }
        if(!newEmail.isEmpty()){
            updateAcc.put("email", newEmail);
            fbUser.updateEmail(newEmail);
        }
        if(!newName.isEmpty()){
            updateAcc.put("first_name", newName);
        }
        if(!newSurname.isEmpty()){
            updateAcc.put("last_name", newSurname);
        }
        if(!newPhone.isEmpty()){
            updateAcc.put("phone", newPhone);
        }

        //if updateAcc is not empty
        if(!updateAcc.isEmpty()){
            reference.updateChildren(updateAcc);

        }
        finish();
    }

    //Function for changing user password in firebase
    public void changeUserPassword() {
        String newPassword1 = newPw1.getText().toString();
        String newPassword2 = newPw2.getText().toString();

        //Compare if they match.
        if (!newPassword1.equals(newPassword2)) {
            Toast.makeText(UserSettingsActivity.this, "Passwords doesn't match.", Toast.LENGTH_SHORT).show();

        } else {
            boolean valid = false;
            //Check if they meet the minimun requirements using the same method which is used in registartion.
            RegisterActivity register = new RegisterActivity();
            valid = register.checkPassword(newPassword1);

            //If the password is valid -> create a hashed password using PasswordHasher class (SHA-512 and salt)
            if (valid) {
                String hashedPw, salt;
                PasswordHasher passwordHasher = new PasswordHasher();
                salt = passwordHasher.getSalt();
                hashedPw = passwordHasher.getHashedPassword(newPassword1, salt);

                //Finally change password stored in database and finish activity.
                fbUser.updatePassword(hashedPw).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserSettingsActivity.this, "Password changed.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UserSettingsActivity.this, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            } else {
                Toast.makeText(UserSettingsActivity.this, "New password didn't match the minimum requirements.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
