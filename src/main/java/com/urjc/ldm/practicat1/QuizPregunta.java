package com.urjc.ldm.practicat1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizPregunta extends AppCompatActivity {
    private TextView pregunta;
    private RadioGroup group;
    private RadioButton resp_1, resp_2, resp_3, resp_4;
    private int numVeces = 0;
    private int numpregunta = 1;
    private int correcto = 0;
    private int aciertos = 0;
    private int fallos = 0;
    private int puntos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizpregunta);

        pregunta = (TextView) findViewById(R.id.pregunta);
        group = (RadioGroup) findViewById(R.id.radioGroup);
        resp_1 = (RadioButton) findViewById(R.id.resp_1);
        resp_2 = (RadioButton) findViewById(R.id.resp_2);
        resp_3 = (RadioButton) findViewById(R.id.resp_3);
        resp_4 = (RadioButton) findViewById(R.id.resp_4);

        quiz();
    }

    @Override
    protected void onResume() {
        super.onResume();

        numVeces++;
        if((numVeces >= 2)) {
            group.clearCheck();
            numpregunta++;
            quiz();
        }
    }

    public void quiz(){
        switch (numpregunta){
            case 1:
                pregunta.setText("¿Cómo se llama el punto más alto de los Pirineos?");
                resp_1.setText("Monte Perdido");
                resp_2.setText("Col du Galibier");
                resp_3.setText("Mont Montvieux");
                resp_4.setText("Pico Aneto");
                break;
            case 2:
                pregunta.setText("¿Cuál fue uno de los primeros animales en ser domesticados?");
                resp_1.setText("El camello");
                resp_2.setText("El elefante");
                resp_3.setText("El perro");
                resp_4.setText("La vaca");
                break;
            case 3:
                pregunta.setText("¿Qué equipo ganó el contrarreloj por equipos en el Tour de Francia 2008?");
                resp_1.setText("Garmin-Slipstream");
                resp_2.setText("Team Saxo Bank");
                resp_3.setText("Ninguno");
                resp_4.setText("Rabobank");
                break;
            case 4:
                pregunta.setText("¿Qué nacionalidad tenía Cristóbal Cólon?");
                resp_1.setText("Italiana");
                resp_2.setText("Alemana");
                resp_3.setText("Portuguesa");
                resp_4.setText("Española");
                break;
            case 5:
                pregunta.setText("¿Qué ciclista ganó la camiseta blanca para el ciclista joven mejor clasificado en el Tour de Francia 2002?");
                resp_1.setText("Jakob Phil");
                resp_2.setText("Carlos Sastre");
                resp_3.setText("Ivan Basso");
                resp_4.setText("Jan Ullrich");
                break;
            default:
                pregunta.setText("¿No hay más preguntas?");
                resp_1.setText("");
                resp_2.setText("");
                resp_3.setText("");
                resp_4.setText("");
                break;
        }
    }

    public void comprobar(View view) {
        if (resp_1.isChecked() || resp_2.isChecked() || resp_3.isChecked() || resp_4.isChecked()) {
            switch (numpregunta) {
                case 1:
                    if (resp_4.isChecked()) {
                        correcto = 1;
                        aciertos++;
                        puntos += 3;
                    } else {
                        correcto = 0;
                        fallos++;
                        puntos -= 2;
                    }
                    break;
                case 2:
                    if (resp_3.isChecked()) {
                        correcto = 1;
                        aciertos++;
                        puntos += 3;
                    } else {
                        correcto = 0;
                        fallos++;
                        puntos -= 2;
                    }
                    break;
                case 3:
                    if (resp_3.isChecked()) {
                        correcto = 1;
                        aciertos++;
                        puntos += 3;
                    } else {
                        correcto = 0;
                        fallos++;
                        puntos -= 2;
                    }
                    break;
                case 4:
                    if (resp_1.isChecked()) {
                        correcto = 1;
                        aciertos++;
                        puntos += 3;
                    } else {
                        correcto = 0;
                        fallos++;
                        puntos -= 2;
                    }
                    break;
                case 5:
                    if (resp_2.isChecked()) {
                        correcto = 1;
                        aciertos++;
                        puntos += 3;
                    } else {
                        correcto = 0;
                        fallos++;
                        puntos -= 2;
                    }
                    break;
                default:
                    correcto = 2;
                    break;
            }

            if (numVeces >= 5) {
                finish();
                Intent finalquiz = new Intent(this, TrivialActivity.class);
                finalquiz.putExtra("puntos", puntos);
                finalquiz.putExtra("aciertos", aciertos);
                finalquiz.putExtra("fallos", fallos);
                startActivity(finalquiz);
            }

            Intent respuesta = new Intent(QuizPregunta.this, QuizRespuesta.class);
            respuesta.putExtra("correcto", correcto);
            startActivity(respuesta);
        } else{
            Toast.makeText(this, "No has seleccionado ninguna opción", Toast.LENGTH_SHORT).show();
        }
    }

}
