package com.sergiohernandezdominguez.practicabd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaSerieAdaptador extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Serie> listaSerie;

    public ListaSerieAdaptador(Context context, int layout, ArrayList<Serie> listaSerie) {
        this.context = context;
        this.layout = layout;
        this.listaSerie = listaSerie;
    }

    @Override
    public int getCount() {
        return listaSerie.size();
    }

    @Override
    public Object getItem(int position) {
        return listaSerie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    private class ViewHolder{
        ImageView imagenPortadaLista;
        TextView nombreSerieLista,numeroCapituloLista, numeroTemporadaLista, valoracionLista;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row=view;
        ViewHolder holder=new ViewHolder();
        if(row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout,null);
            holder.imagenPortadaLista=(ImageView)row.findViewById(R.id.imagenPortadaLista);
            holder.nombreSerieLista=(TextView)row.findViewById(R.id.nombreSerieLista);
            holder.numeroCapituloLista=(TextView)row.findViewById(R.id.numeroCapituloLista);
            holder.numeroTemporadaLista=(TextView)row.findViewById(R.id.numeroTemporadaLista);
            holder.valoracionLista=(TextView) row.findViewById(R.id.valoracionLista);
            row.setTag(holder);
        }else{
            holder=(ViewHolder)row.getTag();
        }
        Serie serie=listaSerie.get(position);
        holder.nombreSerieLista.setText(serie.getNombreSerie());
        holder.numeroTemporadaLista.setText(serie.getNumeroTemporada());
        holder.numeroCapituloLista.setText(serie.getNumeroCapitulo());
        holder.valoracionLista.setText(serie.getValoracion());
        byte[] imagenSerie=serie.getImagen();
        Bitmap bitmap= BitmapFactory.decodeByteArray(imagenSerie,0,imagenSerie.length);
        holder.imagenPortadaLista.setImageBitmap(bitmap);
        return row;
    }
}
