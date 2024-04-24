package com.example.cafeteriaudb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cafeteriaudb.R;

public class CuentaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
    }

    public void onCuentaButtonClick(View view) {
        // Ya esta en pantalla inicio, no hace nada
    }

    public void onInicioButtonClick(View view) {
        // Abrir la actividad Historial
        startActivity(new Intent(CuentaActivity.this, MainActivityAdmin.class));
    }
}