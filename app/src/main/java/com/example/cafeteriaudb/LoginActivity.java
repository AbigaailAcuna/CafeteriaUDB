package com.example.cafeteriaudb;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cafeteriaudb.activities.MainActivityAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText input_correo;
    EditText input_contrasena;
    Button BtnIngresar;
    Button btnMostrarContrasena; // Botón para mostrar/ocultar contraseña

    FirebaseAuth firebaseAuth;

    String correo = "";
    String contrasena = "";

    boolean isPasswordVisible = false; // variable para rastrear si la contraseña es visible o no

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        input_correo = findViewById(R.id.input_correo);
        input_contrasena = findViewById(R.id.input_contrasena);
        BtnIngresar = findViewById(R.id.BtnIngresar);
        btnMostrarContrasena = findViewById(R.id.btnMostrarContrasena);
        btnMostrarContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);

        firebaseAuth = FirebaseAuth.getInstance();

        BtnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar();
            }
        });

        btnMostrarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void togglePasswordVisibility() {
        if (!isPasswordVisible) {
            // Si la contraseña está oculta, hacerla visible
            input_contrasena.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btnMostrarContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eyeslash, 0);
        } else {
            // Si la contraseña es visible, ocultarla
            input_contrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnMostrarContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);
        }
        isPasswordVisible = !isPasswordVisible;
    }

    private void validar(){
        //validamos campos vacíos
        correo = input_correo.getText().toString().trim();
        contrasena = input_contrasena.getText().toString().trim();

        if(TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrasena)){
            Toast.makeText(this, "Ingrese los campos", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
        }
        else {
            Login();
        }
    }

    private void Login() {
        firebaseAuth.signInWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivityAdmin.class));
                            Toast.makeText(LoginActivity.this, "Bienvenido Administrador " +user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Verifique su correo y contraseña", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
