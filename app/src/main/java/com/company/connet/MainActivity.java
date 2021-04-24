package com.company.connet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI components
    Button btn_dino_plus_reg;
    Button btn_dino_reg;
    Button btn_dino_conf;
    Button btn_wifi_conf;
    Button btn_install_chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitComponents();
        SetupButtonListeners();

        Boolean isExit = getIntent().getBooleanExtra("EXIT", false);
        if(isExit)
            finish();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                OnBackPressed();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void InitComponents()
    {
        btn_dino_plus_reg = findViewById(R.id.btn_dino_plus_reg);
        btn_dino_reg = findViewById(R.id.btn_dino_reg);
        btn_dino_conf = findViewById(R.id.btn_dino_conf);
        btn_wifi_conf = findViewById(R.id.btn_wifi_conf);
        btn_install_chk = findViewById(R.id.btn_install_chk);
    }

    private void SetupButtonListeners()
    {
        btn_dino_plus_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 1;
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btn_dino_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 2;
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btn_dino_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 1;
                Intent intent = new Intent(MainActivity.this, ConfActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btn_wifi_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 2;
                Intent intent = new Intent(MainActivity.this, ConfActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btn_install_chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 3;
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    // Override OnBackPressed
    public void OnBackPressed()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Alert");
        alert.setMessage("Do you really want to exit?");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
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