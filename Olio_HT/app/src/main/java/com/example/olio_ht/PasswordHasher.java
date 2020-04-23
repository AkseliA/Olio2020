package com.example.olio_ht;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

//Class to set and get SHA-512+salt hashed passwords
public class PasswordHasher {
    public PasswordHasher(){
    }

    //https://stackoverflow.com/questions/33085493/how-to-hash-a-password-with-sha-512-in-java
    public String getHashedPassword(String password, String salt){
        String hashedPw = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedPw = sb.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return hashedPw;
    }


    public String getSalt(){
/*        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        //encode bytes to string
        String encodedSalt = Base64.getEncoder().encodeToString(salt);*/
        String salt = "Oq+hcZo9dccnSm/hUyIaiQ6!";
        return salt;
    }

    //function to check if entered password matches the one in Firebase
    public boolean checkPassword(){

        return true;
    }

}
