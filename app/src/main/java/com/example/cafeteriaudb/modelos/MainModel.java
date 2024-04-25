package com.example.cafeteriaudb.modelos;

public class MainModel {

    String Imagen;
    String Plato;
    String Descripcion;
    String Precio;

    String Dia;

    String Id;

    MainModel() {}

    public MainModel(String id, String imagen, String descripcion, String plato, String precio, String dia) {
        this.Id = id;
        this.Imagen = imagen;
        this.Descripcion = descripcion;
        this.Plato = plato;
        this.Precio = precio;
        this.Dia = dia;
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getPrecio() {
        return Precio;
    }

    public String getDia() {
        return Dia;
    }

    public void setDia(String dia) {
        Dia = dia;
    }

    public void setPrecio(String precio) {
        Precio =precio;
}
}