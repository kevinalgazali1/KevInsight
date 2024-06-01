package com.example.kevinsight;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

public class WebViewActivity extends AppCompatActivity {
    Toolbar toolbar;
    WebView webView;
    LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webview);
        setSupportActionBar(toolbar);
        loading = findViewById(R.id.loadingweb);

        loading.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                loading.setProgress(newProgress);
                if (newProgress == 100) {
                    loading.setVisibility(View.GONE);
                } else {
                    loading.setVisibility(View.VISIBLE);
                }
            }
        });

        webView.loadUrl(url);
    }
}