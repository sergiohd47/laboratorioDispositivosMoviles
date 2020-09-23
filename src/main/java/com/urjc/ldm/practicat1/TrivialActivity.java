package com.urjc.ldm.practicat1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TrivialActivity extends AppCompatActivity {

    private int puntuacionFinal, aciertosFinal, fallosFinal;
    private TextView txt_puntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivial);

        Bundle recibir = getIntent().getExtras();
        puntuacionFinal = recibir.getInt("puntos");
        aciertosFinal = recibir.getInt("aciertos");
        fallosFinal = recibir.getInt("fallos");

        txt_puntuacion = (TextView) findViewById(R.id.txt_puntuacion);

        if(aciertosFinal == 5){
            txt_puntuacion.setText("¡Wow! Ha acertado todas las preguntas, obteniendo la máxima puntuacion: "+puntuacionFinal+" puntos.\n\nGrac✞as por Jugar");
        }else if(fallosFinal == 5){
            txt_puntuacion.setText("¡Sorry! Ha fallado todas las preguntas, obteniendo "+puntuacionFinal+" puntos.\n\nGrac✞as por Jugar");
        }else{
            if(fallosFinal == 1){
                txt_puntuacion.setText("¡Casi lo consigue! Ha obtenido "+puntuacionFinal+" puntos, con "+aciertosFinal+" aciertos"+" y "+fallosFinal+" fallo.\n\nGrac✞as por Jugar");
            }else {
                txt_puntuacion.setText("Ha obtenido "+puntuacionFinal+" puntos, con "+aciertosFinal+" aciertos"+" y "+fallosFinal+" fallos.\n\nGrac✞as por Jugar");
            }
        }
    }

    public void volver(View view){
        finish();
    }

}
