package com.sergiohernandezdominguez.practicabd;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button botonInicio, botonAyuda;
    ImageView imagenApp;
    MediaPlayer sonidoInicio, sonidoAyuda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PONER ICONO EN ACTION BAR
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        botonInicio=(Button)findViewById(R.id.botonInicio);
        botonAyuda=(Button)findViewById(R.id.botonAyuda);
        sonidoInicio=MediaPlayer.create(this,R.raw.chewbacca);
        sonidoAyuda=MediaPlayer.create(this,R.raw.flecha);
        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonidoInicio.start();
                Intent inicio= new Intent(MainActivity.this,InicioActivity.class);
                startActivity(inicio);
            }
        });
        botonAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sonidoAyuda.start();
                Intent ayuda=new Intent(MainActivity.this,AyudaActivity.class);
                startActivity(ayuda);
            }
        });
    }
}
