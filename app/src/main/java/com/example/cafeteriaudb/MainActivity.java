package com.example.cafeteriaudb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.cafeteriaudb.adapters.MainAdapter;
import com.example.cafeteriaudb.modelos.MainModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

        // Inicialización de listas de modelos
        desayunosList = new ArrayList<>();
        almuerzosList = new ArrayList<>();
        cenaList = new ArrayList<>();

        // Inicialización de adaptadores
        desayunosAdapter = new MainAdapter(desayunosList);
        almuerzosAdapter = new MainAdapter(almuerzosList);
        cenaAdapter = new MainAdapter(cenaList);

        // Configuración de adaptadores para RecyclerViews
        desayunosRecyclerView.setAdapter(desayunosAdapter);
        almuerzosRecyclerView.setAdapter(almuerzosAdapter);
        cenaRecyclerView.setAdapter(cenaAdapter);

        // Cargar datos de la base de datos
        loadMenuForToday("Desayunos", desayunosList, desayunosAdapter, desayunosText);
        loadMenuForToday("Almuerzos", almuerzosList, almuerzosAdapter, almuerzosText);
        loadMenuForToday("Cena", cenaList, cenaAdapter, cenaText);

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar LoginActivity cuando se haga clic en el botón
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }


    private void loadMenuForToday(String category, List<MainModel> list, MainAdapter adapter, TextView textView) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Menu").child(category);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainModel model = snapshot.getValue(MainModel.class);
                    // Validar si el campo 'dia' del modelo coincide con el día actual
                    if (isToday(model.getDia())) {
                        list.add(model);
                    }
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

    // Método para verificar si un día coincide con el día actual
    private boolean isToday(String dayFromDatabase) {
        Calendar calendar = Calendar.getInstance();
        String[] daysOfWeek = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        int today = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Restamos 1 porque en el arreglo el índice empieza en 0
        String todayInSpanish = daysOfWeek[today];
        Log.d("Hoy", "Hoy is: " + todayInSpanish);
        Log.d("Dia en la base", "Dia segun la base es: " + dayFromDatabase);
        return todayInSpanish.equalsIgnoreCase(dayFromDatabase);
}


}