package com.example.cafeteriaudb.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriaudb.R;
import com.example.cafeteriaudb.activities.AgregarActivity;
import com.example.cafeteriaudb.activities.EditarActivity;
import com.example.cafeteriaudb.activities.MainActivityAdmin;
import com.example.cafeteriaudb.modelos.MainModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainAdapterAdmin extends RecyclerView.Adapter<MainAdapterAdmin.MyViewHolder> {

    private List<MainModel> mainModels;

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

        holder.plato.setText(model.getPlato());
        holder.descripcion.setText(model.getDescripcion());
        holder.precio.setText(model.getPrecio());

        // Agregar clics a los botones
        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un AlertDialog con la información del platillo
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Eliminar platillo");
                builder.setMessage("¿Estás seguro de que deseas eliminar este platillo?");

                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // implementar la lógica para eliminar el platillo
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
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

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView plato, descripcion, precio;
        Button btnEliminar, btnEditar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            plato = itemView.findViewById(R.id.platoTxt);
            descripcion = itemView.findViewById(R.id.descripcionTxt);
            precio = itemView.findViewById(R.id.precioTxt);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}
