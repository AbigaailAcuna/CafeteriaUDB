package com.example.cafeteriaudb.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
    private Spinner spinner1,dia;
    EditText descripcion,plato,precio;
    private Button btnAdd, btnshow;
    private  ProgressBar progressBar;
    private ImageView imageView;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Menu").child("Almuerzos");
    private DatabaseReference root1 = FirebaseDatabase.getInstance().getReference().child("Menu").child("Cena");
    private DatabaseReference root2 = FirebaseDatabase.getInstance().getReference().child("Menu").child("Desayunos");


    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    private Uri imageUri;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        btnRegresar = findViewById(R.id.btnRegresar);
        btnAdd = findViewById(R.id.btnAdd);
        imageView = findViewById(R.id.imagev);
        progressBar = findViewById(R.id.progressBar1);
        descripcion = (EditText)findViewById(R.id.txtDescripcion);
        spinner1 =(Spinner)findViewById(R.id.spinnerTipo);
        plato = (EditText)findViewById(R.id.txtnombre);
        precio = (EditText)findViewById(R.id.txtPrecio);
        dia = (Spinner)findViewById(R.id.spinnerDia);
        String [] opciones = {"Almuerzos","Cena","Desayunos"};
        String [] opciones1 = {"Lunes","Martes","Miercoles","Jueves","Viernes","sabado"};

        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,opciones);
        spinner1.setAdapter(adapter);
        ArrayAdapter <String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,opciones1);
        dia.setAdapter(adapter1);
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

                    String selection = spinner1.getSelectedItem().toString();
                    String selection1 = dia.getSelectedItem().toString();


                    @Override
                    public void onSuccess(Uri uri) {
                        if(selection.equals("Almuerzos")){
                            MainModel model= new MainModel(uri.toString(),descripcion.getText().toString(),plato.getText().toString(),precio.getText().toString(),dia.getSelectedItem().toString());

                            String modelId = root.push().getKey();
                            root.child(modelId).setValue(model);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AgregarActivity.this,"Platillo agregado correctamente",Toast.LENGTH_SHORT).show();
                            Toast.makeText(AgregarActivity.this,"selecciono almuerzos", Toast.LENGTH_SHORT).show();
                        }else if(selection.equals("Cena")){

                            MainModel model= new MainModel(uri.toString(),descripcion.getText().toString(),plato.getText().toString(),precio.getText().toString(),dia.getSelectedItem().toString());

                            String modelId = root.push().getKey();
                            root1.child(modelId).setValue(model);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AgregarActivity.this,"Platillo agregado correctamente",Toast.LENGTH_SHORT).show();
                            Toast.makeText(AgregarActivity.this,"selecciono Cena", Toast.LENGTH_SHORT).show();
                        }
                        else if(selection.equals("Desayunos")){

                            MainModel model= new MainModel(uri.toString(),descripcion.getText().toString(),plato.getText().toString(),precio.getText().toString(),dia.getSelectedItem().toString());

                            String modelId = root.push().getKey();
                            root2.child(modelId).setValue(model);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(AgregarActivity.this,"Platillo agregado correctamente",Toast.LENGTH_SHORT).show();
                            Toast.makeText(AgregarActivity.this,"selecciono Desayuno", Toast.LENGTH_SHORT).show();
                        };

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