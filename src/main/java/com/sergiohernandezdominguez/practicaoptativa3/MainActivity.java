package com.sergiohernandezdominguez.practicaoptativa3;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button botonComenzar, botonAyuda;
    MediaPlayer mediaHelp,mediaGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* FRAGMENTO PARA SACAR EL ANCHO Y EL ALTO DEL DISPOSITIVO
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels
        System.out.println("w: "+width);
        System.out.println("h: "+height);
        */
        botonComenzar=(Button)findViewById(R.id.botonComenzar);
        botonAyuda=(Button)findViewById(R.id.botonAyuda);
        mediaGame=MediaPlayer.create(this,R.raw.guerra);
        mediaHelp=MediaPlayer.create(this,R.raw.explosion);
        botonComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game=new Intent(MainActivity.this,Game.class);
                mediaGame.start();
                startActivity(game);
            }
        });
        botonAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help=new Intent(MainActivity.this,Help.class);
                mediaHelp.start();
                startActivity(help);

            }
        });
    }
}
