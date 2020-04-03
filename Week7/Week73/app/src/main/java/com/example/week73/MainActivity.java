package com.example.week73;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView outputText;
    EditText inputText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputText = (TextView) findViewById(R.id.textView);
        inputText = (android.widget.EditText) findViewById(R.id.editText);

    }
    public void pressButton(View v){
        System.out.println("Hello World!");
        outputText.setText(inputText.getText());

    }
}
