package com.example.week10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    WebView web;
    Button goBtn;
    EditText urlTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.webView);
        goBtn = findViewById(R.id.goBtn);
        urlTxt = findViewById(R.id.urlTxt);


        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlTxt.getText().toString();
                loadUrl(url);
            }
        });


    }
    public void loadUrl(String url){
        String myUrl = "http://" + url;
        web.loadUrl(myUrl);
    }
}
