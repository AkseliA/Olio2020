package com.example.week85;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WriteFile {
    private String name;
    private FileOutputStream fos;
    public WriteFile(Context ctx, String s){

        name = "receipt.txt";
        try{
            fos = ctx.openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(s.getBytes());
            fos.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
