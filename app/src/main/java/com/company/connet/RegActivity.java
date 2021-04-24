package com.company.connet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cunoraz.gifview.library.GifView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;

public class RegActivity extends AppCompatActivity {

    WebView webView;
    GifView gifView;
    int pageID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String url = null;

        pageID = getIntent().getIntExtra("id", 0);
        switch (pageID) {
            case 1:
                getSupportActionBar().setTitle("DINO + REGISTRATION");
                url = "https://www.connetcontrolcenter.com/DinoPlus/";
                break;
            case 2:
                getSupportActionBar().setTitle("DINO REGISTRATION");
                url = "https://www.connetcontrolcenter.com/Dino/";
                break;
            case 3:
                getSupportActionBar().setTitle("INSTALLATION CHECK");
                url = "https://www.connetcontrolcenter.com/DinoInstaller/";
                break;
            default:
                finish();
        }

        setupWebView(url);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                OnBackPressed();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        FloatingActionButton reg_back = findViewById(R.id.reg_back);
        reg_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBackPressed();
            }
        });

        FloatingActionButton reg_close = findViewById(R.id.reg_close);
        reg_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(RegActivity.this);
                alert.setTitle("Alert");
                alert.setMessage("Do you really want to exit?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(RegActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });
    }

    private void setupWebView(String url)
    {
        if(url == null)
            return;

        webView = findViewById(R.id.webview);
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
                Toast.makeText(RegActivity.this, error.getDescription().toString(), Toast.LENGTH_LONG).show();
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                gifView.setVisibility(View.VISIBLE);
                gifView.play();
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

                Toast.makeText(RegActivity.this, message, Toast.LENGTH_LONG).show();
                handler.proceed();

                //super.onReceivedSslError(view, handler, error);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("alert", message);
                Toast.makeText(RegActivity.this, message, Toast.LENGTH_LONG).show();
                result.confirm();
                return true;
            }
        });

        webView.loadUrl(url);
    }

    // Override OnBackPressed
    public void OnBackPressed()
    {
        if(pageID < 3) {
            AlertDialog.Builder alert = new AlertDialog.Builder(RegActivity.this);
            alert.setTitle("Notification");
            LayoutInflater inflater = RegActivity.this.getLayoutInflater();
            alert.setView(inflater.inflate(R.layout.dialog_registration, null))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RegActivity.this.finish();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            /*
            CarouselView carouselView;
            int[] carouselImages = {R.drawable.carousel1, R.drawable.carousel2};
            carouselView = findViewById(R.id.carouselView);
            carouselView.setPageCount(carouselImages.length);
            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    imageView.setImageResource(carouselImages[position]);
                }
            };
            carouselView.setImageListener(imageListener);
             */

            AlertDialog dialog = alert.create();
            dialog.show();
        }
        else {
            RegActivity.this.finish();
        }
    }
}