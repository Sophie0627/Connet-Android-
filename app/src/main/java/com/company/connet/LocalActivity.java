package com.company.connet;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.cunoraz.gifview.library.GifView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class LocalActivity extends AppCompatActivity {

    int pageID = 0;
    WebView webView;
    GifView gifView;
    Button btn_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            ConnectToWiFi("DinoAdmin", "DinoAdmin", getApplicationContext());
        } catch(Exception e)
        {
            Toast.makeText(this, "Unable to connect to WiFi. Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(this, "Connected to WiFi successfully.", Toast.LENGTH_LONG).show();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                OnBackPressed();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        FloatingActionButton local_back = findViewById(R.id.local_back);
        local_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBackPressed();
            }
        });

        btn_help = findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setupWebView();

        pageID = getIntent().getIntExtra("id", 0);
        switch (pageID) {
            case 1:
                webView.loadUrl("10.10.100.100/home");
                break;
            case 2:
                webView.loadUrl("10.10.100.10");
        }
    }

    static public void ConnectToWiFi(String ssid, String key, Context context)
    {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", ssid);
        wifiConfig.preSharedKey = String.format("\"%s\"", key);
        WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        int old_networkID = wifiManager.getConnectionInfo().getNetworkId();
        wifiManager.removeNetwork(old_networkID);
        wifiManager.saveConfiguration();
        int new_networkID = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(new_networkID, true);
        wifiManager.reconnect();
    }

    private void setupWebView()
    {
        webView = findViewById(R.id.local_webview);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        gifView = findViewById(R.id.load_gif);
        gifView.setGifResource(R.drawable.loading);

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(LocalActivity.this, error.getDescription().toString(), Toast.LENGTH_LONG).show();
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                gifView.setVisibility(View.VISIBLE);
                gifView.play();

                if(url.equals("10.10.100.100/home") || url.equals("10.10.100.10"))
                    btn_help.setVisibility(View.VISIBLE);
                else btn_help.setVisibility(View.GONE);

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                gifView.pause();
                gifView.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }

                Toast.makeText(LocalActivity.this, message, Toast.LENGTH_LONG).show();
                handler.proceed();

                //super.onReceivedSslError(view, handler, error);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("alert", message);
                Toast.makeText(LocalActivity.this, message, Toast.LENGTH_LONG).show();
                result.confirm();
                return true;
            }
        });
    }

    // Override OnBackPressed
    public void OnBackPressed()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(LocalActivity.this);
        alert.setTitle("Notification");
        LayoutInflater inflater = LocalActivity.this.getLayoutInflater();
        alert.setView(inflater.inflate(R.layout.dialog_local, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LocalActivity.this.finish();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = alert.create();
        dialog.show();
    }
}