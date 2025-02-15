package com.example.olio_ht;
/* The vault - android banking application
 *  Author: Akseli Aula 0545267
 *  Object Oriented programming course final project
 *  2020 */
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText email_in, name_in, password1_in, password2_in, phoneNmbr_in, address_in, lastname_in;
    Button singUpBtn;
    FirebaseAuth fbAuth;
    String salt, email, name, password1, password2, phoneNmbr, surname, address;
    PasswordHasher pwHasher = new PasswordHasher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing components
        email_in = findViewById(R.id.email_txt);
        name_in = findViewById(R.id.username_txt);
        password1_in = findViewById(R.id.password1_txt);
        password2_in = findViewById(R.id.password2_txt);
        singUpBtn = findViewById(R.id.signUpBtn);
        phoneNmbr_in = findViewById(R.id.phonenumber_txt);
        lastname_in = findViewById(R.id.surname_txt);
        address_in = findViewById(R.id.address_txt);


        fbAuth = FirebaseAuth.getInstance();


        singUpBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (checkInputs()) {
                    createAccount(password1, email, name, surname, phoneNmbr, address);
                }
            }
        });
    }

    //Checks if all editTexts are filled and passwords match. If passwords match, will call function to checkPassword
    public boolean checkInputs() {
        boolean valid = false;
        email = email_in.getText().toString();
        name = name_in.getText().toString();
        surname = lastname_in.getText().toString();
        address = address_in.getText().toString();
        phoneNmbr = phoneNmbr_in.getText().toString();
        password1 = password1_in.getText().toString();
        password2 = password2_in.getText().toString();

        if (email.isEmpty()) {
            email_in.setError("Enter email!");
            valid = false;

        }
        if (name.isEmpty()) {
            name_in.setError("Enter firstname!");
            valid = false;

        }
        if (phoneNmbr.isEmpty()) {
            phoneNmbr_in.setError("Enter phonenumber!");
            valid = false;

        }
        if (surname.isEmpty()) {
            lastname_in.setError("Enter lastname!");
            valid = false;

        }
        if (address.isEmpty()) {
            address_in.setError("Enter address!");
            valid = false;

        }
        if (!password1.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "Passwords didn't match!", Toast.LENGTH_SHORT).show();
            valid = false;

        }
        if (!email.isEmpty() && !name.isEmpty() && !phoneNmbr.isEmpty() && !surname.isEmpty() && !address.isEmpty()) {
            boolean check;
            check = checkPassword(password1);
            //if password doesn't contain atleast 1x number + 1x special character + small and big letter + 12 marks long
            if (!check) {
                Toast.makeText(RegisterActivity.this, "Password must be atleast 12 marks long and contain at minimum 1 number, 1 special character, small and big letters.", Toast.LENGTH_LONG).show();
                valid = false;
            } else {
                valid = true;
            }
        }
        return valid;
    }

    //Checks so that password has minimum requirements and returns boolean.
    public boolean checkPassword(String pw) {
        boolean valid = false;
        boolean containsSpecial = false;
        boolean containsCapital = false;
        boolean containsLower = false;
        boolean containsNumber = false;
        boolean validLength = false;

        //Check length
        if (pw.length() >= 12) {
            validLength = true;
        }
        //creating a char array for further checking
        char[] pwArray = pw.toCharArray();

        for (char c : pwArray) {
            //Check for number
            if (Character.isDigit(c)) {
                containsNumber = true;

                //Check for uppercase
            } else if (Character.isUpperCase(c)) {
                containsCapital = true;

                //Check for lowercase
            } else if (Character.isLowerCase(c)) {
                containsLower = true;
            }
        }
        //Check for special character using pattern containing all non special characters.
        Pattern characters = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = characters.matcher(pw);
        if (match.find()) {
            containsSpecial = true;
        }

        //Check if all are true
        if (containsSpecial && containsCapital && containsLower && containsNumber && validLength) {
            valid = true;
        }
        return valid;
    }

    //Creates an account and stores it into firebase using createUserWithEmailAndPassword. If task is successfull will store additional information to database.
    public void createAccount(String password1, final String email, final String fName, final String lName, final String phoneNmbr, final String address) {
        salt = pwHasher.getSalt();
        final String hashedPw = pwHasher.getHashedPassword(password1, salt);

        fbAuth.createUserWithEmailAndPassword(email, hashedPw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "There was an error creating an account.", Toast.LENGTH_SHORT).show();
                } else {
                    //Store general account information to database
                    String user_id = fbAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                    User newUuser = new User(fName, lName, email, address, phoneNmbr, "0", "0", "0");
                    current_user_db.setValue(newUuser);

                    Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}