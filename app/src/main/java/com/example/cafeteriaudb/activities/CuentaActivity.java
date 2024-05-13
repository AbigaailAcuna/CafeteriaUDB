package com.example.cafeteriaudb.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cafeteriaudb.MainActivity;
import com.example.cafeteriaudb.R;

public class CuentaActivity extends AppCompatActivity {

    Button btnout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);
        btnout = findViewById(R.id.btnout);

        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CuentaActivity.this, MainActivity.class));
            }
        });
    }


    public void onCuentaButtonClick(View view) {
        // Ya esta en pantalla inicio, no hace nada
    }


    public void onInicioButtonClick(View view) {
        // Abrir la actividad Historial
        startActivity(new Intent(CuentaActivity.this, MainActivityAdmin.class));
    }
}