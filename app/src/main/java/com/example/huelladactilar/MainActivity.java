package com.example.huelladactilar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import includes.toolbar;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog Dialog;
    TextView autori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dialog = new ProgressDialog(this);
        autori = findViewById(R.id.autori);
        final Button aunte= findViewById(R.id.aunte);
        autori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setMessage("¿Desea usar sus datos biométricos para acceder?")
                        .setCancelable(false).setPositiveButton("Acepto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aunte.setEnabled(true);
                        aunte.setVisibility(View.VISIBLE);
                    }
                }).setNegativeButton("Declinar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aunte.setEnabled(false);
                        aunte.setVisibility(View.INVISIBLE);
                        dialog.cancel();
                    }
                });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Uso de Datos biometricos");
                titulo.show();
            }
        });
        //toolbar.show(this,"Volver",true);
        final Executor executor = Executors.newSingleThreadExecutor();
        final BiometricPrompt biometricPrompt= new BiometricPrompt.Builder(this)
                .setTitle("Auntenticador por biometria")
                .setSubtitle("").setDescription("Inicie sesión usando sus credenciales biometricas").setNegativeButton("Cancelar", executor, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).build();

        final MainActivity activity = this;
        aunte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(new CancellationSignal(), executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Dialog.setMessage("Espere un momento");
                                Dialog.setCanceledOnTouchOutside(false);
                                Dialog.show();
                                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                                Dialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }
}
