package com.sergiohernandezdominguez.practicabd;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class InicioActivity extends AppCompatActivity {
    TextView textoCapitulo, textoTemporada, textoValoracion;
    EditText nombreSerie, numeroCapitulo, numeroTemporada, valoracion;
    ImageView imagenPortada;
    Button botonAñadir, botonListar, botonElegirPortada;
    MediaPlayer sonidoAñadir, sonidoListar, sonidoElegirPortada;
    final int CODIGO_GALERIA=999;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        inicializarElementos();
        sqLiteHelper=new SQLiteHelper(this,"SeriesBD.sqlite",null,1);
         sqLiteHelper.consultaDatos("CREATE TABLE IF NOT EXISTS SERIES(Id INTEGER PRIMARY KEY AUTOINCREMENT, nombreSerie VARCHAR," +
                 " numeroCapitulo VARCHAR, numeroTemporada VARCHAR, imagenPortada BLOB, valoracion VARCHAR)");
         botonElegirPortada.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sonidoElegirPortada.start();
                 ActivityCompat.requestPermissions(InicioActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                         CODIGO_GALERIA);
             }
         });
         botonAñadir.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                try{
                    sonidoAñadir.start();
                    sqLiteHelper.insertarDatos(nombreSerie.getText().toString().trim(),
                            numeroTemporada.getText().toString().trim(),
                            numeroCapitulo.getText().toString().trim(),
                            pasarImagenByte(imagenPortada),
                            valoracion.getText().toString().trim());
                    Toast.makeText(getApplicationContext(),"Añadido exitosamente",Toast.LENGTH_SHORT).show();
                    nombreSerie.setText("");
                    numeroTemporada.setText("");
                    numeroCapitulo.setText("");
                    valoracion.setText("");
                    imagenPortada.setImageResource(R.mipmap.ic_launcher);
                }catch (Exception e){
                    e.printStackTrace();
                }

             }
         });
         botonListar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sonidoListar.start();
                 Intent intento=new Intent(InicioActivity.this,ListaSerie.class);
                 startActivity(intento);

             }
         });
    }

    public static byte[] pasarImagenByte(ImageView imagen) {
       Bitmap bitmap=((BitmapDrawable)imagen.getDrawable()).getBitmap();
       ByteArrayOutputStream stream=new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
       byte[] byteArray=stream.toByteArray();
       return byteArray;
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
                imagenPortada.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void inicializarElementos(){
        textoCapitulo=(TextView) findViewById(R.id.textoCapitulo);
        textoTemporada=(TextView) findViewById(R.id.textoTemporada);
        textoValoracion=(TextView)findViewById(R.id.textoValoracion);
        nombreSerie=(EditText)findViewById(R.id.nombreSerieLista);
        numeroCapitulo=(EditText)findViewById(R.id.numeroCapituloLista);
        numeroTemporada=(EditText)findViewById(R.id.numeroTemporadaLista);
        valoracion=(EditText) findViewById(R.id.valoracion);
        imagenPortada=(ImageView)findViewById(R.id.imagenPortadaActualizar);
        botonAñadir=(Button)findViewById(R.id.botonAñadir);
        botonListar=(Button)findViewById(R.id.botonListar);
        botonElegirPortada=(Button)findViewById(R.id.botonElegirPortada);
        sonidoAñadir=MediaPlayer.create(this,R.raw.balas);
        sonidoElegirPortada=MediaPlayer.create(this,R.raw.disparolaser);
        sonidoListar=MediaPlayer.create(this,R.raw.explosion);

    }
}
