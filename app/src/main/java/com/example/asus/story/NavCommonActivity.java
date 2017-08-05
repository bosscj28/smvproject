package com.example.asus.story;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class NavCommonActivity extends AppCompatActivity {
    private TextView title;
    private ImageView back;
    private WebView webView;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_common);
        url="http://www.google.com";
        title=(TextView)findViewById(R.id.Title_Nav_Common);
        title.setText(getIntent().getStringExtra("Title"));
        back=(ImageView)findViewById(R.id.back_NavCommon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NavCommonActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        webView=(WebView)findViewById(R.id.Webview_Nav_common);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl(url);
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
