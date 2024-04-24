package com.example.cafeteriaudb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
<<<<<<< Updated upstream
import android.content.Intent;
=======

>>>>>>> Stashed changes
import com.example.cafeteriaudb.adapters.MainAdapter;
import com.example.cafeteriaudb.modelos.MainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView desayunosRecyclerView, almuerzosRecyclerView, cenaRecyclerView;
    private TextView desayunosText, almuerzosText, cenaText;

    private List<MainModel> desayunosList, almuerzosList, cenaList;
    private MainAdapter desayunosAdapter, almuerzosAdapter, cenaAdapter;

    TextView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a RecyclerViews y TextViews
        desayunosRecyclerView = findViewById(R.id.desayunosRecyclerView);
        almuerzosRecyclerView = findViewById(R.id.almuerzosRecyclerView);
        cenaRecyclerView = findViewById(R.id.cenaRecyclerView);
        desayunosText = findViewById(R.id.desayunosText);
        almuerzosText = findViewById(R.id.almuerzosText);
        cenaText = findViewById(R.id.cenaText);

        // Configuración de layout para RecyclerViews
        desayunosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        almuerzosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cenaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

<<<<<<< Updated upstream
        FirebaseRecyclerOptions<MainModel> options = new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(databaseRef.child("Almuerzos").orderByChild("Plato"), MainModel.class)
                .build();
=======
        // Inicialización de listas de modelos
        desayunosList = new ArrayList<>();
        almuerzosList = new ArrayList<>();
        cenaList = new ArrayList<>();
>>>>>>> Stashed changes

        // Inicialización de adaptadores
        desayunosAdapter = new MainAdapter(desayunosList);
        almuerzosAdapter = new MainAdapter(almuerzosList);
        cenaAdapter = new MainAdapter(cenaList);

        // Configuración de adaptadores para RecyclerViews
        desayunosRecyclerView.setAdapter(desayunosAdapter);
        almuerzosRecyclerView.setAdapter(almuerzosAdapter);
        cenaRecyclerView.setAdapter(cenaAdapter);

<<<<<<< Updated upstream
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar LoginActivity cuando se haga clic en el botón
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


=======
        // Cargar datos de la base de datos
        loadMenu("Desayunos", desayunosList, desayunosAdapter, desayunosText);
        loadMenu("Almuerzos", almuerzosList, almuerzosAdapter, almuerzosText);
        loadMenu("Cena", cenaList, cenaAdapter, cenaText);
>>>>>>> Stashed changes
    }

    private void loadMenu(String category, List<MainModel> list, MainAdapter adapter, TextView textView) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Menu").child(category);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainModel model = snapshot.getValue(MainModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
                textView.setText(category); // Mostrar el texto indicando el tipo de comida
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Manejar el error
            }
        });
    }
}
