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
import com.squareup.picasso.Picasso;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private List<MainModel> mainModels;

    public MainAdapter(List<MainModel> mainModels) {
        this.mainModels = mainModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
        holder.dia.setText(model.getDia());
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView dia, plato, descripcion, precio;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            dia = itemView.findViewById(R.id.DiaTxt);
            plato = itemView.findViewById(R.id.platoTxt);
            descripcion = itemView.findViewById(R.id.descripcionTxt);
            precio = itemView.findViewById(R.id.precioTxt);
 }
}
}