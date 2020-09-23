package com.urjc.ldm.practicat1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuizRespuesta extends AppCompatActivity {

    private int correcto = 1;
    private TextView respuesta;
    private Button btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizrespuesta);

        Bundle recibir = getIntent().getExtras();
        correcto = recibir.getInt("correcto");

        respuesta = (TextView) findViewById(R.id.respuesta);
        btn4 = (Button) findViewById(R.id.volver);

        if(correcto == 0){
            respuesta.setText("Lo siento. Su respuesta es Incorrecta!\n\n-2 puntos");
            btn4.setText("¿Volver al Inicio?");
        } else if(correcto == 1) {
            respuesta.setText("¡¡¡Bien, su respuesta es Correcta!!!\n\n+3 puntos");
        } else{
            respuesta.setText("No ha elegido ninguna Pregunta.");
        }
    }

    public void siguiente(View view) {
        finish();
    }

    public void volver(View view){
        Intent volver = new Intent(this, MainActivity.class);
        startActivity(volver);
        finish();
    }
}
