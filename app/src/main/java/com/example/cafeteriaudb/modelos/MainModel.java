package com.example.cafeteriaudb.modelos;

public class MainModel {

    String Imagen;
    String Plato;
    String Descripcion;
    Double Precio;

    MainModel() {}

    public MainModel(String imagen, String descripcion, String plato, Double precio) {
        this.Imagen = imagen;
        this.Descripcion = descripcion;
        this.Plato = plato;
        this.Precio = precio;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getPlato() {
        return Plato;
    }

    public void setPlato(String plato) {
        Plato = plato;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double precio) {
        Precio = precio;
    }
}
