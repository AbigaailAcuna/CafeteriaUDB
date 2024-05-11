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
import android.widget.ProgressBar;

import com.example.cafeteriaudb.R;
import com.example.cafeteriaudb.modelos.MainModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AgregarActivity extends AppCompatActivity {

    Button btnRegresar;
    EditText descripcion,plato,precio,dia;
    private Button btnAdd, btnshow;
    private  ProgressBar progressBar;
    private ImageView imageView;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Menu").child("Almuerzos");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        btnRegresar = findViewById(R.id.btnRegresar);
        btnAdd = findViewById(R.id.btnAdd);
        btnshow = findViewById(R.id.btnshow);
        imageView = findViewById(R.id.imagev);
        progressBar = findViewById(R.id.progressBar1);
        descripcion = (EditText)findViewById(R.id.txtDescripcion);
        plato = (EditText)findViewById(R.id.txtnombre);
        precio = (EditText)findViewById(R.id.txtPrecio);
        dia = (EditText)findViewById(R.id.txtTipo);

        progressBar.setVisibility(View.INVISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,2);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                        Agregarafirebase(imageUri);
                }else{
                    Toast.makeText(AgregarActivity.this,"Selecciona una imagen",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AgregarActivity.this,MainActivityAdmin.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==2 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
    private void Agregarafirebase(Uri uri){

        StorageReference fileRef = reference.child(System.currentTimeMillis()+"." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        Map<String, Object> platilloMap = new HashMap<>();
                        platilloMap.put("Imagen",uri.toString());
                        platilloMap.put("Descripcion",descripcion.getText().toString());
                        platilloMap.put("Plato",plato.getText().toString());
                        platilloMap.put("Precio",precio.getText().toString());
                        platilloMap.put("Dia",dia.getText().toString());

                        MainModel model= new MainModel(uri.toString(),descripcion.getText().toString(),plato.getText().toString(),precio.getText().toString(),dia.getText().toString());

                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AgregarActivity.this,"Platillo agregado correctamente",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AgregarActivity.this, "Ingreso fallido",Toast.LENGTH_SHORT).show();
            }
        });
     }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}