package com.albaneloh.tev;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private static final String SITE_URL = "https://alban3886.github.io/";

    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setMediaPlaybackRequiresUserGesture(false);

        // Empêche le WebView d'avoir son propre effet de rebond,
        // qui pouvait aussi déclencher le SwipeRefreshLayout par erreur
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefresh.setRefreshing(false);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());

        swipeRefresh.setOnRefreshListener(() -> webView.reload());

        // CORRECTIF : le SwipeRefreshLayout ne doit se déclencher
        // que lorsque le WebView est vraiment tout en haut de la page.
        // Sans ça, tirer vers le bas n'importe où dans le contenu
        // (ex: dans le tableau de bord, le menu, etc.) était interprété
        // comme "tirer pour rafraîchir" et rechargeait toute la page.
        swipeRefresh.setOnChildScrollUpCallback((parent, child) -> webView.getScrollY() > 0);

        webView.loadUrl(SITE_URL);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
