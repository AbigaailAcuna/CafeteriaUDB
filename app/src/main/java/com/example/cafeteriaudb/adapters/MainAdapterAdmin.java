package com.example.cafeteriaudb.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriaudb.R;
import com.example.cafeteriaudb.activities.AgregarActivity;
import com.example.cafeteriaudb.activities.EditarActivity;
import com.example.cafeteriaudb.activities.MainActivityAdmin;
import com.example.cafeteriaudb.modelos.MainModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapterAdmin extends RecyclerView.Adapter<MainAdapterAdmin.MyViewHolder> {

    private List<MainModel> mainModels;
    public String category;

    public MainAdapterAdmin(List<MainModel> mainModels) {
        this.mainModels = mainModels;
    }

    @NonNull
    @Override
    public MainAdapterAdmin.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_admin, parent, false);
        return new MainAdapterAdmin.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapterAdmin.MyViewHolder holder, int position) {
        MainModel model = mainModels.get(position);

        // Cargar la imagen utilizando Picasso
        Picasso.get()
                .load(model.getImagen())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.imageView);
        holder.dia.setText(model.getDia());
        holder.plato.setText(model.getPlato());
        holder.descripcion.setText(model.getDescripcion());
        holder.precio.setText(model.getPrecio());

        // Agregar clics a los botones
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                // Mostrar un AlertDialog con la información del platillo
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Eliminar platillo");
                builder.setMessage("¿Estás seguro de que deseas eliminar este platillo?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener la referencia del elemento en la base de datos
                        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                                .child("Menu")
                                .child(category);

                        // Buscar el elemento en la base de datos por su contenido o posición
                        Query query = dbRef.orderByChild("Plato").equalTo(model.getPlato());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Si se elimina correctamente de la base de datos, también elimina del RecyclerView
                                                int position = mainModels.indexOf(model);
                                                mainModels.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position, mainModels.size());
                                                Toast.makeText(context, "Platillo eliminado", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Maneja el fallo
                                                Toast.makeText(context, "Error al eliminar el platillo", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Maneja el fallo
                                Toast.makeText(context, "Error al eliminar el platillo", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // No hacer nada, simplemente cerrar el diálogo
                    }
                });

                // Mostrar el AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });

        holder.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad de edición
                Intent intent = new Intent(v.getContext(), EditarActivity.class);
                // Puedes pasar datos adicionales si es necesario
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }
    public void setCategory(String category) {
        this.category = category;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView plato, descripcion, precio, dia;
        Button btnEliminar, btnEditar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dia = itemView.findViewById(R.id.DiaTxt);
            imageView = itemView.findViewById(R.id.img);
            plato = itemView.findViewById(R.id.platoTxt);
            descripcion = itemView.findViewById(R.id.descripcionTxt);
            precio = itemView.findViewById(R.id.precioTxt);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnEditar = itemView.findViewById(R.id.btnEditar);
 }
}
}