package com.example.cafeteriaudb.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafeteriaudb.R;
import com.example.cafeteriaudb.modelos.MainModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        // Carga la imagen utilizando Picasso
        Picasso.get()
                .load(model.getImagen())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal) // Reemplaza esto con tu imagen de placeholder
                .error(com.google.firebase.database.R.drawable.common_google_signin_btn_icon_dark) // Reemplaza esto con tu imagen de error
                .into(holder.imageView);

        holder.plato.setText(model.getPlato());
        holder.descripcion.setText(model.getDescripcion());
        holder.precio.setText(String.valueOf(model.getPrecio()));
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView plato, descripcion, precio;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img);
            plato = itemView.findViewById(R.id.platoTxt);
            descripcion = itemView.findViewById(R.id.descripcionTxt);
            precio = itemView.findViewById(R.id.precioTxt);
        }
    }
}

