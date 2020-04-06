package com.example.week10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView web;
    Button goBtn;
    Button js_Btn;
    ImageButton refreshBtn;
    ImageButton backBtn;
    ImageButton forwardBtn;
    EditText urlTxt;
    HistoryManager hm;
    String prevUrl = "";
    String nextUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        web = findViewById(R.id.webView);
        goBtn = findViewById(R.id.goBtn);
        js_Btn = findViewById(R.id.executeJs_btn);
        urlTxt = findViewById(R.id.urlTxt);
        refreshBtn = findViewById(R.id.refrest_btn);
        backBtn = findViewById(R.id.back_btn);
        forwardBtn = findViewById(R.id.forward_btn);
        hm = new HistoryManager();







        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = urlTxt.getText().toString();
                loadUrl(url);
                //Add to history list
                hm.addCurrentPage(url);
                //Javascript button is "execute" when not pressed.
                js_Btn.setText(R.string.Js_btn_execute);

            }
        });




        //Reload page when pressed
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currUrl = web.getUrl();
                web.loadUrl(currUrl);

                //Alternative way
                //web.reload();
                js_Btn.setText(R.string.Js_btn_execute);
            }
        });

        //Button used to execute javascripts
        js_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeJavaScript();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPrevUrl
                prevUrl = hm.getPreviousUrl();

                //Load prevUrl
                if(!prevUrl.equals("")) {
                    loadUrl(prevUrl);
                }

                //Alternative(better) way
                //if(web.canGoBack()){
                //    web.goBack();
                //}
            }
        });

        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GetNextUrl
                nextUrl = hm.getNextUrl();

                //Load nextUrl
                if(!nextUrl.equals("")){
                    loadUrl(nextUrl);
                }

                //Alternative(better) way
                //if(web.canGoForward()){
                //    web.goForward();
                //}

            }
        });


    }


    public void loadUrl(String url){
        String myUrl;

        if(url.equals("index.html")){
            myUrl = "file:///android_asset/index.html";
        }else if(url.startsWith("http://")){
            myUrl = url;
        }else{
            myUrl = "http://" + url;
        }
        web.loadUrl(myUrl);
        urlTxt.setText(myUrl);
    }

    public void executeJavaScript(){
        //If url is empty.
        if(web.getUrl() == null){
            Toast.makeText(getApplicationContext(), "This feature is not available on this site.", Toast.LENGTH_SHORT).show();
        }
        else if(web.getUrl().equals("file:///android_asset/index.html") && js_Btn.getText().equals("Execute")){
            //Run js function "shoutOut"
            web.evaluateJavascript("javascript:shoutOut()", null);
            js_Btn.setText(R.string.js_btn_restore);
        }else if(web.getUrl().equals("file:///android_asset/index.html") && js_Btn.getText().equals("Restore")){
            //Run js function initialize
            web.evaluateJavascript("javascript:initialize()", null);
            js_Btn.setText(R.string.Js_btn_execute);
        }else{
            Toast.makeText(getApplicationContext(), "This feature can only be used at index.html", Toast.LENGTH_SHORT).show();
        }
    }



}
