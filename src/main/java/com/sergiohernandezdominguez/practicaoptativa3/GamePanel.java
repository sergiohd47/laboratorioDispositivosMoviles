package com.sergiohernandezdominguez.practicaoptativa3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;

//clase principal del juego donde se tiene todos los parametros del juego y el desarrollo de este
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<TopBorder> topborder;
    private ArrayList<BotBorder> botborder;
    private Random rand = new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;
    private int progressDenom = 20; //dificultad
    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000){
            counter++;
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.fondo));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 168, 80, 3);
        smoke = new ArrayList<>();
        missiles = new ArrayList<>();
        topborder = new ArrayList<>();
        botborder = new ArrayList<>();
        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!player.getPlaying() && newGameCreated && reset){
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying()){
                if(!started)started = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){
        if(player.getPlaying()) {
            if(botborder.isEmpty()){
                player.setPlaying(false);
                return;
            }
            if(topborder.isEmpty()){
                player.setPlaying(false);
                return;
            }
            bg.update();
            player.update();
            //Calculo del valor maximo de borde, para luego colocar texto puntuacion
            maxBorderHeight = 30+player.getScore()/progressDenom;
            //Debe quedarse entre los parametros maximos de la pantalla
            if(maxBorderHeight > HEIGHT/4)
                maxBorderHeight = HEIGHT/4;
            minBorderHeight = 5+player.getScore()/progressDenom;
            //compruebas colision
            for(int i = 0; i<botborder.size(); i++){
                if(collision(botborder.get(i), player))
                    player.setPlaying(false);
            }
            //compruebas colision
            for(int i = 0; i <topborder.size(); i++){
                if(collision(topborder.get(i),player))
                    player.setPlaying(false);
            }
            //actualizas bordes
            this.updateTopBorder();
            this.updateBottomBorder();
            //añades misiles al thread time
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            if(missileElapsed >(2000 - player.getScore()/4)){
                //primer misil siempre sale por debajo de la mitad
                if(missiles.size()==0){
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.
                            missile),WIDTH + 10, HEIGHT/2, 85, 27, player.getScore(), 13));
                }else{
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            WIDTH+10, (int)(rand.nextDouble()*(HEIGHT - (maxBorderHeight * 2))+maxBorderHeight),85,27, player.getScore(),13));
                }
                //reinicias timer
                missileStartTime = System.nanoTime();
            }
            //bucle para misiles aparezcan y desaparezcan
            for(int i = 0; i<missiles.size();i++){
                //actualizas misil
                missiles.get(i).update();
                if(collision(missiles.get(i),player)){
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //borras misil si sale de la pantalla
                if(missiles.get(i).getX()<-100){
                    missiles.remove(i);
                    break;
                }
            }
            //añades al timer el humo del helicoptero
            long elapsed = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed > 120){
                smoke.add(new Smokepuff(player.getX(), player.getY()+10));
                smokeStartTime = System.nanoTime();
            }
            for(int i = 0; i<smoke.size();i++){
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10){
                    smoke.remove(i);
                }
            }
        }else{
            player.resetDY();
            if(!reset){
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                        player.getY()-30, 100, 100, 25);
            }
            explosion.update();
            long resetElapsed = (System.nanoTime()-startReset)/1000000;
            if(resetElapsed > 2500 && !newGameCreated) {
                newGame();
            }
        }
    }

    public boolean collision(GameObject a, GameObject b){
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);
        if(canvas!=null){
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if(!dissapear){
                player.draw(canvas);
            }
            //dibujas humo del helicoptero
            for(Smokepuff sp: smoke){
                sp.draw(canvas);
            }
            //draw misiles
            for(Missile m: missiles){
                m.draw(canvas);
            }
            //dibujas el borde superior
            for(TopBorder tb: topborder){
                tb.draw(canvas);
            }
            //dibujas el borde inferior
            for(BotBorder bb: botborder){
                bb.draw(canvas);
            }
            //dibujas explosion
            if(started) {
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void updateTopBorder(){
        //cada 50 puntos, se insertan cortes y salientes en los bordes inferiores y superiores
        if(player.getScore()%50 ==0){
            topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.pared
            ),topborder.get(topborder.size()-1).getX()+20,0,(int)((rand.nextDouble()*(maxBorderHeight
            ))+1)));
        }
        for(int i = 0; i<topborder.size(); i++){
            topborder.get(i).update();
            if(topborder.get(i).getX()<-20){
                topborder.remove(i);
                if(topborder.get(topborder.size()-1).getHeight()>=maxBorderHeight){
                    topDown = false;
                }
                if(topborder.get(topborder.size()-1).getHeight()<=minBorderHeight){
                    topDown = true;
                }
                //añade nuevo borde con mayor alto (saliente en el borde)
                if(topDown){
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.pared),topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()+1));
                }
                //añade nuevo borde con menor alto (agujero en el borde)
                else{
                    topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),
                            R.drawable.pared),topborder.get(topborder.size()-1).getX()+20,
                            0, topborder.get(topborder.size()-1).getHeight()-1));
                }
            }
        }
    }

    public void updateBottomBorder()
    {
        //mismo caso que el metodo anterior pero esta vez cada 40 puntos
        if(player.getScore()%40 == 0){
            botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.pared),
                    botborder.get(botborder.size()-1).getX()+20,(int)((rand.nextDouble()
                    *maxBorderHeight)+(HEIGHT-maxBorderHeight))));
        }
        for(int i = 0; i<botborder.size(); i++){
            botborder.get(i).update();
            if(botborder.get(i).getX()<-20){
                botborder.remove(i);
                if (botborder.get(botborder.size() - 1).getY() <= HEIGHT-maxBorderHeight){
                    botDown = true;
                }
                if (botborder.get(botborder.size() - 1).getY() >= HEIGHT - minBorderHeight){
                    botDown = false;
                }
                if (botDown){
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.pared
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1
                    ).getY() + 1));
                }else{
                    botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.pared
                    ), botborder.get(botborder.size() - 1).getX() + 20, botborder.get(botborder.size() - 1
                    ).getY() - 1));
                }
            }
        }
    }

    public void newGame(){
        dissapear = false;
        botborder.clear();
        topborder.clear();
        missiles.clear();
        smoke.clear();
        minBorderHeight = 5;
        maxBorderHeight = 30;
        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT/2);
        if(player.getScore()>best){
            best = player.getScore();
        }
        // crean los bordes iniciales
        // borde superior
        for(int i = 0; i*20<WIDTH+40;i++){
            if(i==0) {
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.pared
                ),i*20,0, 10));
            }else{
                topborder.add(new TopBorder(BitmapFactory.decodeResource(getResources(),R.drawable.pared
                ),i*20,0, topborder.get(i-1).getHeight()+1));
            }
        }
        // borde inferior
        for(int i = 0; i*20<WIDTH+40; i++){
            if(i==0){
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.pared)
                        ,i*20,HEIGHT - minBorderHeight));
            }else{
                botborder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.pared),
                        i * 20, botborder.get(i - 1).getY() - 1));
            }
        }
        newGameCreated = true;
    }

    public void drawText(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCIA: " + (player.getScore()*3), 10, HEIGHT - 10, paint);
        if(!player.getPlaying()&&newGameCreated&&reset){
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PULSA PARA EMPEZAR", WIDTH/2-50, HEIGHT/2, paint1);
            paint1.setTextSize(20);
            canvas.drawText("PULSA Y MANTIENE PARA VOLAR", WIDTH/2-50, HEIGHT/2 + 20, paint1);
            canvas.drawText("SUELTA PARA DESCENDER", WIDTH/2-50, HEIGHT/2 + 40, paint1);
        }
    }
}
