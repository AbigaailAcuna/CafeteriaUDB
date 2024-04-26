package com.example.cafeteriaudb.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.cafeteriaudb.R;
import com.example.cafeteriaudb.adapters.MainAdapter;
import com.example.cafeteriaudb.adapters.MainAdapterAdmin;
import com.example.cafeteriaudb.modelos.MainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAdmin extends AppCompatActivity {

    private RecyclerView desayunosRecyclerView, almuerzosRecyclerView, cenaRecyclerView;
    private TextView desayunosText, almuerzosText, cenaText;

    private List<MainModel> desayunosList, almuerzosList, cenaList;
    private MainAdapterAdmin desayunosAdapter, almuerzosAdapter, cenaAdapter;
    private TextView nodesayunosText, noalmuerzosText, nocenaText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        // Referencias a RecyclerViews y TextViews
        desayunosRecyclerView = findViewById(R.id.desayunosRecyclerView);
        almuerzosRecyclerView = findViewById(R.id.almuerzosRecyclerView);
        cenaRecyclerView = findViewById(R.id.cenaRecyclerView);
        desayunosText = findViewById(R.id.desayunosText);
        almuerzosText = findViewById(R.id.almuerzosText);
        cenaText = findViewById(R.id.cenaText);
        nodesayunosText = findViewById(R.id.nodesayunosText);
        noalmuerzosText = findViewById(R.id.noalmuerzosText);
        nocenaText = findViewById(R.id.nocenaText);

        // Configuración de layout para RecyclerViews
        desayunosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        almuerzosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cenaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicialización de listas de modelos
        desayunosList = new ArrayList<>();
        almuerzosList = new ArrayList<>();
        cenaList = new ArrayList<>();

        // Inicialización de adaptadores
        desayunosAdapter = new MainAdapterAdmin(desayunosList);
        desayunosAdapter.setCategory("Desayunos");
        almuerzosAdapter = new MainAdapterAdmin(almuerzosList);
        almuerzosAdapter.setCategory("Almuerzos");
        cenaAdapter = new MainAdapterAdmin(cenaList);
        cenaAdapter.setCategory("Cena");


        // Configuración de adaptadores para RecyclerViews
        desayunosRecyclerView.setAdapter(desayunosAdapter);
        almuerzosRecyclerView.setAdapter(almuerzosAdapter);
        cenaRecyclerView.setAdapter(cenaAdapter);

        // Cargar datos de la base de datos
        loadMenu("Desayunos", desayunosList, desayunosAdapter, desayunosText, nodesayunosText);
        loadMenu("Almuerzos", almuerzosList, almuerzosAdapter, almuerzosText, noalmuerzosText);
        loadMenu("Cena", cenaList, cenaAdapter, cenaText, nocenaText);

    }

    private void loadMenu(String category, List<MainModel> list, MainAdapterAdmin adapter, TextView textView, TextView notextView) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Menu").child(category);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                boolean item = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainModel model = snapshot.getValue(MainModel.class);
                    list.add(model);
                    item = true;
                }
                adapter.notifyDataSetChanged();
                if(!item){
                    notextView.setVisibility(View.VISIBLE);
                    notextView.setText("No se han agregado platos para "+ category);

                }else{
                    notextView.setVisibility(View.GONE);
                    textView.setText(category); // Mostrar el texto indicando el tipo de comida
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error
            }
        });
    }

    public void onInicioButtonClick(View view) {
        // Ya esta en pantalla inicio, no hace nada
    }

    public void onCuentaButtonClick(View view) {
        // Abrir la actividad Historial
        startActivity(new Intent(MainActivityAdmin.this, CuentaActivity.class));
    }
    public void onAgregarButtonClick(View view) {
        // Abrir la actividad Historial
        startActivity(new Intent(MainActivityAdmin.this, AgregarActivity.class));
}
}