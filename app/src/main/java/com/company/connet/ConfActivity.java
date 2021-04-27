package com.company.connet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ConfActivity extends AppCompatActivity {

    Button btn_go;
    int pageID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                OnBackPressed();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        FloatingActionButton conf_back = findViewById(R.id.conf_back);
        conf_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBackPressed();
            }
        });

        pageID = getIntent().getIntExtra("id", 0);
        switch(pageID) {
            case 1:
                getSupportActionBar().setTitle("CONFIGURAZIONE DINO+");
                break;
            case 2:
                getSupportActionBar().setTitle("CONFIGURAZIONE DINO WIFI");
                break;
            default:
        }

        btn_go = findViewById(R.id.btn_go);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAirplaneModeOn())
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ConfActivity.this);
                    alert.setTitle("Attenzione");
                    alert.setMessage("La modalità “aereo” non è attiva. INDIETRO CONTINUA?");
                    alert.setPositiveButton("CONTINUA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(!isWifiOn())
                            {
                                AlertDialog.Builder alert_wifi = new AlertDialog.Builder(ConfActivity.this);
                                alert_wifi.setTitle("Attenzione");
                                alert_wifi.setMessage("WIFI non attivo. Attivare WIFI per procedure.");
                                alert_wifi.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog dialog_wifi = alert_wifi.create();
                                dialog_wifi.show();
                            }
                            else {
                                Intent intent = new Intent(ConfActivity.this, LocalActivity.class);
                                intent.putExtra("id", pageID);
                                startActivity(intent);
                            }
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }
                else {
                    if(!isWifiOn())
                    {
                        AlertDialog.Builder alert_wifi = new AlertDialog.Builder(ConfActivity.this);
                        alert_wifi.setTitle("Attenzione");
                        alert_wifi.setMessage("WIFI non attivo. Attivare WIFI per procedure.");
                        alert_wifi.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog_wifi = alert_wifi.create();
                        dialog_wifi.show();
                    }
                    else {
                        Intent intent = new Intent(ConfActivity.this, LocalActivity.class);
                        intent.putExtra("id", pageID);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    // Override OnBackPressed
    public void OnBackPressed()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(ConfActivity.this);
        alert.setTitle("Attenzione");
        alert.setMessage("Uscire e andare alla prima pagina?");
        alert.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConfActivity.this.finish();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    // Detect the state of AirplaneMode
    private boolean isAirplaneModeOn() {
        Context context = getApplicationContext();
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    // Detect the state of Wifi
    private boolean isWifiOn() {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifi.isWifiEnabled())
            return true;
        return false;
    }
}