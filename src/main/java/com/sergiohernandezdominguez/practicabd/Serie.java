package com.sergiohernandezdominguez.practicabd;

public class Serie {
    private int id;
    private String nombreSerie;
    private String numeroTemporada;
    private String numeroCapitulo;
    private String valoracion;
    private byte[] imagen;

    public Serie(int id, String nombreSerie, String numeroTemporada, String numeroCapitulo, String valoracion, byte[] imagen) {
        this.id = id;
        this.nombreSerie = nombreSerie;
        this.numeroTemporada = numeroTemporada;
        this.numeroCapitulo = numeroCapitulo;
        this.valoracion = valoracion;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreSerie() {
        return nombreSerie;
    }

    public void setNombreSerie(String nombreSerie) {
        this.nombreSerie = nombreSerie;
    }

    public String getNumeroTemporada() {
        return numeroTemporada;
    }

    public void setNumeroTemporada(String numeroTemporada) {
        this.numeroTemporada = numeroTemporada;
    }

    public String getNumeroCapitulo() {
        return numeroCapitulo;
    }

    public void setNumeroCapitulo(String numeroCapitulo) {
        this.numeroCapitulo = numeroCapitulo;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
