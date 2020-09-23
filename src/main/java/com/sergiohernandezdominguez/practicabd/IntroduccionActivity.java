package com.sergiohernandezdominguez.practicabd;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class IntroduccionActivity extends AppCompatActivity {
    private final int DURACION_INTRODUCCION=1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_introduccion);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent introduccion=new Intent(IntroduccionActivity.this,MainActivity.class);
                startActivity(introduccion);
                finish();
            };
        },DURACION_INTRODUCCION);
    }
}
