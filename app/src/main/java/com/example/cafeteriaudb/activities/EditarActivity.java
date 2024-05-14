package com.example.cafeteriaudb.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cafeteriaudb.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditarActivity extends AppCompatActivity {
    public String category;

    private EditText editNombre, editDescripcion, editDia, editPrecio, editCategoria;
    private ImageView editImagen;
    private Button btneditPlato, btnRegresar;
    private Uri imageUri;

    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        initializeUI();

        if (getIntent().hasExtra("nombrePlato")) {
            loadPlatoData();
        }

        btnRegresar.setOnClickListener(v -> startActivity(new Intent(EditarActivity.this, MainActivityAdmin.class)));

        btneditPlato.setOnClickListener(v -> updatePlatoInfo());
    }

    private void initializeUI() {
        btneditPlato = findViewById(R.id.btnEditPlato);
        btnRegresar = findViewById(R.id.btnRegresar);
        editNombre = findViewById(R.id.txtEditNombre);
        editDescripcion = findViewById(R.id.txtEditDescripcion);
        editDia = findViewById(R.id.txtEditDia);
        editPrecio = findViewById(R.id.txtEditPrecio);
        editImagen = findViewById(R.id.imagev);
        editCategoria = findViewById(R.id.txtEditCategoria);

        editImagen.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 2);
        });
    }

    private void loadPlatoData() {
        Bundle extras = getIntent().getExtras();
        String nombrePlato = extras.getString("nombrePlato");
        String descripcionPlato = extras.getString("descripcionPlato");
        String precioPlato = extras.getString("precioPlato");
        String diaPlato = extras.getString("diaPlato");
        category = extras.getString("categoria");

        editNombre.setText(nombrePlato);
        editDescripcion.setText(descripcionPlato);
        editDia.setText(diaPlato);
        editPrecio.setText(precioPlato);
        editCategoria.setText(extras.getString("categoria"));

    }

    private void updatePlatoInfo() {
        String nombrePlatoOriginal = getIntent().getStringExtra("nombrePlato");
        String nuevoNombre = editNombre.getText().toString();
        String nuevaDescripcion = editDescripcion.getText().toString();
        String nuevoDia = editDia.getText().toString();
        String nuevoPrecio = editPrecio.getText().toString();
        String nuevaCategoria = editCategoria.getText().toString();

        if (imageUri != null) {
            StorageReference fileRef = reference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> updateDatabase(nombrePlatoOriginal, nuevoNombre, nuevaDescripcion, nuevoDia, nuevoPrecio,nuevaCategoria, uri.toString()))
                            .addOnFailureListener(e -> Toast.makeText(EditarActivity.this, "Error al obtener la URL de la imagen", Toast.LENGTH_SHORT).show()))
                    .addOnFailureListener(e -> Toast.makeText(EditarActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show());
        } else {
            updateDatabase(nombrePlatoOriginal, nuevoNombre, nuevaDescripcion, nuevoDia, nuevoPrecio, nuevaCategoria, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            editImagen.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void updateDatabase(String nombrePlatoOriginal, String nuevoNombre, String nuevaDescripcion, String nuevoDia, String nuevoPrecio, String nuevaCategoria, String imageUrl) {
        DatabaseReference oldMenuRef = FirebaseDatabase.getInstance().getReference().child("Menu").child(category);
        DatabaseReference newMenuRef = FirebaseDatabase.getInstance().getReference().child("Menu").child(nuevaCategoria);

        Query query = oldMenuRef.orderByChild("plato").equalTo(nombrePlatoOriginal);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!category.equals(nuevaCategoria)) {
                        snapshot.getRef().removeValue(); // Remove from old category
                        DatabaseReference newPlatilloRef = newMenuRef.push(); // Add to new category
                        newPlatilloRef.setValue(snapshot.getValue(), (databaseError, databaseReference) -> {
                            if (databaseError != null) {
                                Toast.makeText(EditarActivity.this, "Error al mover el platillo", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EditarActivity.this, "Platillo movido y actualizado", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // Update fields in the existing category
                        snapshot.getRef().child("plato").setValue(nuevoNombre);
                        snapshot.getRef().child("descripcion").setValue(nuevaDescripcion);
                        snapshot.getRef().child("precio").setValue(nuevoPrecio);
                        snapshot.getRef().child("dia").setValue(nuevoDia);
                        if (imageUrl != null) {
                            snapshot.getRef().child("imagen").setValue(imageUrl);
                        }
                        Toast.makeText(EditarActivity.this, "Platillo actualizado", Toast.LENGTH_SHORT).show();
                    }
                }
                startActivity(new Intent(EditarActivity.this, MainActivityAdmin.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarActivity.this, "Error al actualizar el platillo", Toast.LENGTH_SHORT).show();
            }
        });
    }

}