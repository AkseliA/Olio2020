package com.example.week10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    WebView web;
    Button goBtn;
    ImageButton refreshBtn;
    ImageButton backBtn;
    ImageButton forwardBtn;
    EditText urlTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.webView);
        goBtn = findViewById(R.id.goBtn);
        urlTxt = findViewById(R.id.urlTxt);
        refreshBtn = findViewById(R.id.refrest_btn);
        backBtn = findViewById(R.id.back_btn);
        forwardBtn = findViewById(R.id.forward_btn);



        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlTxt.getText().toString();
                loadUrl(url);
            }
        });

        //Reload page when pressed
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                web.reload();
            }
        });


    }
    public void loadUrl(String url){
        String myUrl = "http://" + url;
        web.loadUrl(myUrl);
    }
}
