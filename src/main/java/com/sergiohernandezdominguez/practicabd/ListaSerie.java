package com.sergiohernandezdominguez.practicabd;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListaSerie extends AppCompatActivity{
    GridView gridView;
    ArrayList<Serie> listaSerie;
    ListaSerieAdaptador adaptador=null;
    private final int CODIGO_GALERIA=888;
    ImageView imagenSerie;
    MediaPlayer sonidoActualizar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_series);
        gridView=(GridView)findViewById(R.id.gridView);
        listaSerie=new ArrayList<>();
        adaptador=new ListaSerieAdaptador(this,R.layout.serie,listaSerie);
        gridView.setAdapter(adaptador);
        sonidoActualizar=MediaPlayer.create(this,R.raw.disparo);


        //OBTENER TODOS LOS DATOS DE LA BD
        final Cursor cursor=InicioActivity.sqLiteHelper.getDatos("SELECT * FROM SERIES");
        listaSerie.clear();
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            String nombreSerie=cursor.getString(1);
            String numeroTemporada=cursor.getString(2);
            String numeroCapitulo=cursor.getString(3);
            byte[] imagenPortada=cursor.getBlob(4);
            String valoracion=cursor.getString(5);
            listaSerie.add(new Serie(id,nombreSerie,numeroTemporada,numeroCapitulo, valoracion,imagenPortada));
        }
        adaptador.notifyDataSetChanged();
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] opciones={"Actualizar","Borrar"};
                AlertDialog.Builder dialog=new AlertDialog.Builder(ListaSerie.this);
                dialog.setTitle("Elige una opcion");
                dialog.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            //ACTUALIZAMOS LOS DATOS
                            Cursor c = InicioActivity.sqLiteHelper.getDatos("SELECT id FROM SERIES");
                            ArrayList<Integer> arrayID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrayID.add(c.getInt(0));
                            }
                            mostrarDialogoActualizar(ListaSerie.this, arrayID.get(position));
                        } else {
                            //BORRAR DATOS
                            Cursor c = InicioActivity.sqLiteHelper.getDatos("SELECT id FROM SERIES");
                            ArrayList<Integer> arrayID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrayID.add(c.getInt(0));
                            }
                            mostrarDialogoBorrar(arrayID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

    }
    private void mostrarDialogoActualizar(Activity activity, final int position){
        final Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.actualizar_serie);
        dialog.setTitle("Actualizar Serie");
        imagenSerie=(ImageView)dialog.findViewById(R.id.imagenPortadaActualizar);
        final EditText nombreSerieActualizar =(EditText)dialog.findViewById(R.id.nombreSerieActualizar);
        final EditText numeroTemporadaActualizar=(EditText)dialog.findViewById(R.id.numeroTemporadaActualizar);
        final EditText numeroCapituloActualizar=(EditText)dialog.findViewById(R.id.numeroCapituloActualizar);
        final EditText valoracionActualizar=(EditText)dialog.findViewById(R.id.valoracionActualizar);
        Button botonActualizar=(Button)dialog.findViewById(R.id.botonActualizar);

        //CAMBIAR ANCHO Y ALTO DE DIALOG
        int ancho= (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        int alto= (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.8);
        dialog.getWindow().setLayout(ancho,alto);
        dialog.show();
        imagenSerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(ListaSerie.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODIGO_GALERIA);
            }
        });
        botonActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    sonidoActualizar.start();
                    InicioActivity.sqLiteHelper.actualizarDatos(nombreSerieActualizar.getText().toString().trim(),
                            numeroTemporadaActualizar.getText().toString().trim(),
                            numeroCapituloActualizar.getText().toString().trim(),
                            InicioActivity.pasarImagenByte(imagenSerie),
                            valoracionActualizar.getText().toString().trim(),
                            position);
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Actualizacion exitosa",Toast.LENGTH_SHORT).show();
                }catch (Exception error){
                    Log.e("Error actualizacion: ",error.getMessage());
                }
                actualizarListaSerie();
            }
        });
    }
    private void mostrarDialogoBorrar(final int idSerie){
        AlertDialog.Builder dialogoBorrar=new AlertDialog.Builder(ListaSerie.this);
        dialogoBorrar.setTitle("Borrar Serie");
        dialogoBorrar.setMessage("Confirmar borrado?");
        dialogoBorrar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    InicioActivity.sqLiteHelper.borrarDatos(idSerie);
                    Toast.makeText(getApplicationContext(), "Borrado exitosamente", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Error borrado: ", e.getMessage());
                }
                actualizarListaSerie();
            }
        });
        dialogoBorrar.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogoBorrar.show();

    }
    private void actualizarListaSerie(){
        Cursor cursor=InicioActivity.sqLiteHelper.getDatos("SELECT * FROM SERIES");
        listaSerie.clear();
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            String nombreSerie=cursor.getString(1);
            String numeroTemporada=cursor.getString(2);
            String numeroCapitulo=cursor.getString(3);
            byte[] imagenPortada=cursor.getBlob(4);
            String valoracion=cursor.getString( 5);
            listaSerie.add(new Serie(id,nombreSerie,numeroTemporada,numeroCapitulo,valoracion,imagenPortada));
        }
        adaptador.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CODIGO_GALERIA){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CODIGO_GALERIA);
            }else{
                Toast.makeText(getApplicationContext(),"No posees permisos necesarios para acceder a galeria",Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==CODIGO_GALERIA && resultCode==RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                imagenSerie.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
