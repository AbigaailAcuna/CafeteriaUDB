package com.example.cafeteriaudb.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cafeteriaudb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditarActivity extends AppCompatActivity {
    public String category;

    private EditText editNombre;
    private EditText editDescripcion;
    private EditText editDia;
    private EditText editPrecio;
    private EditText editImagen;
    private Button btneditPlato;

    Button btnRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        Bundle extras = getIntent().getExtras();
        btneditPlato=findViewById(R.id.btnEditPlato);
        btnRegresar=findViewById(R.id.btnRegresar);

        String nombrePlato = "";

        if (extras != null) {
             nombrePlato = extras.getString("nombrePlato");
            String descripcionPlato = extras.getString("descripcionPlato");
            String precioPlato = extras.getString("precioPlato");
            String diaPlato = extras.getString("diaPlato");
            String imagenPlato = extras.getString("imagenPlato");
            category = extras.getString("categoria");

            editNombre=findViewById(R.id.txtEditNombre);
            editDescripcion=findViewById(R.id.txtEditDescripcion);
            editDia=findViewById(R.id.txtEditDia);
            editPrecio=findViewById(R.id.txtEditPrecio);
            editImagen=findViewById(R.id.txtEditImagen);
            editNombre.setText(nombrePlato);
            editDescripcion.setText(descripcionPlato);
            editDia.setText(diaPlato);
            editPrecio.setText(precioPlato);
            editImagen.setText(imagenPlato);

        }

        final String nombrePlatoFinal = nombrePlato; // Variable final para usar dentro del listener

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditarActivity.this,MainActivityAdmin.class));
            }
        });

        btneditPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nuevoNombre = editNombre.getText().toString();
                final String nuevaDescripcion = editDescripcion.getText().toString();
                final String nuevoDia = editDia.getText().toString();
                final String nuevoPrecio = editPrecio.getText().toString();
                final String nuevaImagen = editImagen.getText().toString();

                // Buscar el platillo por su nombre actual en Firebase
                DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference()
                        .child("Menu")
                        .child(category);
                Query query = menuRef.orderByChild("Plato").equalTo(nombrePlatoFinal);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Obtener la referencia y actualizar
                            DatabaseReference platilloRef = snapshot.getRef();
                            platilloRef.child("Plato").setValue(nuevoNombre);
                            platilloRef.child("Descripcion").setValue(nuevaDescripcion);
                            platilloRef.child("Precio").setValue(nuevoPrecio);
                            platilloRef.child("Dia").setValue(nuevoDia);
                            platilloRef.child("Imagen").setValue(nuevaImagen);

                            // Mostrar mensaje y regresar
                            Toast.makeText(EditarActivity.this, "Platillo actualizado", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditarActivity.this, MainActivityAdmin.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(EditarActivity.this, "Error al actualizar el platillo", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}