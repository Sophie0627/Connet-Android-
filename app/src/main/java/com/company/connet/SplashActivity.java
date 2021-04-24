package com.company.connet;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(2 * 1000);
                    // Redirect to another intent
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    // Remove Activity
                    finish();
                } catch (Exception e) {

                }
            }
        };
        // Start the thread
        background.start();
    }
}