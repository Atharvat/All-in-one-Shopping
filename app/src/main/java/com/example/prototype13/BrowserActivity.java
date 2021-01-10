package com.example.prototype13;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Objects;

public class BrowserActivity extends AppCompatActivity {

    WebView mWebView;
    SwipeRefreshLayout swipe;
    String productURL;
    public int state = 0;
    int login = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        swipe = findViewById(R.id.swipe);

        //productURL = Objects.requireNonNull(getIntent().getExtras()).getString("URL")+"ref=as_li_ss_tl?ie=UTF8&linkCode=ll1&tag=atharvataga08-21&linkId=4f6c80447c643e6d0dc80006654c6882&language=en_IN";
        productURL = Objects.requireNonNull(getIntent().getExtras()).getString("URL");
        //Toast.makeText(this,productURL,Toast.LENGTH_LONG).show();
        if(productURL.equals("https://www.amazon.in/cpe/manageoneclick/")){
            ConstraintLayout c = findViewById(R.id.overlay);
            c.setVisibility(View.GONE);
        }
        ConstraintLayout c = findViewById(R.id.overlay);
        c.setVisibility(View.GONE);
        mWebView = findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(true);
        mWebView.loadUrl(productURL);
        swipe.setRefreshing(true);
        state =1;
        mWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {

                //reload();
               /* if(state==1){
                    view.loadUrl("javascript:document.getElementById('buy-now-button').click()");
                    state= state+1;
                }else if(login == 0){
                    view.loadUrl("javascript:document.getElementById('ap_email_login').setAttribute('value','attagalpallewar@gmail.com');y = document.querySelectorAll('#continue[aria-labelledby='continue-announce']'); y[0].click();");
                    login=login+1;
                }if(login == 1){
                    view.loadUrl("javascript:document.getElementById('ap_password').setAttribute('value','englishjunction');y = document.querySelectorAll('input[name=\"rememberMe\"]'); y[0].click();z = document.querySelectorAll('#signInSubmit'); z[0].click()");
                    login = login+1;
                }if(login == 2){
                    view.loadUrl("javascript:document.getElementById('buy-now-button').click()");
                    login=login+1;
                }if(state>=2&&login>=3){
                   payment();
                }
*/

                swipe.setRefreshing(false);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.loadUrl(productURL);
            }
        });

        Button b = findViewById(R.id.settings);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebview("https://www.amazon.in/cpe/manageoneclick/");

            }
        });
    }

    public void payment(){
        if(!productURL.equals("https://www.amazon.in/cpe/manageoneclick/")){
            LinearLayout l = findViewById(R.id.paymentMessage);
            l.setVisibility(View.VISIBLE);}
        ConstraintLayout c = findViewById(R.id.overlay);
        c.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void startWebview(String url){
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra("URL",url);
        startActivity(intent);
    }

    public void reload(){

        switch(state){
            case 1:
                state= state+1;
                mWebView.loadUrl("javascript:document.getElementById('buy-now-button').click()");
                break;
            case 2:
                state = state+1;
                mWebView.loadUrl("javascript:document.getElementById('ap_email_login').setAttribute('value','attagalpallewar@gmail.com');y = document.querySelectorAll('#continue[aria-labelledby=\"continue-announce\"]'); y[0].click();");
                mWebView.loadUrl("javascript:document.getElementById('ap_password').setAttribute('value','englishjunction');y = document.querySelectorAll('input[name=\"rememberMe\"]'); y[0].click();z = document.querySelectorAll('#signInSubmit'); z[0].click()");
                mWebView.loadUrl("javascript:document.getElementById('buy-now-button').click()");
                break;
            case 3:
                state = state+1;
                mWebView.loadUrl("javascript:document.getElementById('ap_email_login').setAttribute('value','attagalpallewar@gmail.com');y = document.querySelectorAll('#continue[aria-labelledby=\"continue-announce\"]'); y[0].click();");
                mWebView.loadUrl("javascript:document.getElementById('ap_password').setAttribute('value','englishjunction');y = document.querySelectorAll('input[name=\"rememberMe\"]'); y[0].click();z = document.querySelectorAll('#signInSubmit'); z[0].click()");
                mWebView.loadUrl("javascript:document.getElementById('buy-now-button').click()");
                break;
            case 4:
                state = state+1;
                mWebView.loadUrl("javascript:document.getElementById('ap_email_login').setAttribute('value','attagalpallewar@gmail.com');y = document.querySelectorAll('#continue[aria-labelledby=\"continue-announce\"]'); y[0].click();");
                mWebView.loadUrl("javascript:document.getElementById('ap_password').setAttribute('value','englishjunction');y = document.querySelectorAll('input[name=\"rememberMe\"]'); y[0].click();z = document.querySelectorAll('#signInSubmit'); z[0].click()");
                mWebView.loadUrl("javascript:document.getElementById('buy-now-button').click()");
                break;
            case 5: payment();


        }

    /*    switch(state){
            case 1:
                state= state+1;
                mWebView.loadUrl("y = document.querySelectorAll('._2iUM2T._2ysUDX'); y[0].scrollIntoView(true); y[0].click();");
                break;}*/
    }
}