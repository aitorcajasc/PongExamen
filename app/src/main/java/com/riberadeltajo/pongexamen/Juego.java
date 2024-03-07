package com.riberadeltajo.pongexamen;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, SurfaceView.OnTouchListener {

    private SurfaceHolder holder;
    private BucleJuego bucle;
    static final int X = 0;
    static final int Y = 1;

    //CANVAS
    float maxX, maxY;

    //CIRCULO
    Pelota pelota;
    int random;

    //RECTS
    float[] posicionRct1 = new float[2];
    float[] posicionRct2 = new float[2];
    static final int segundos_cruzar_rect = 3;
    float velocidadRect;

    //CONTROLES
    boolean arribaIzq;
    boolean arribaDer;
    boolean abajoIzq;
    boolean abajoDer;
    ArrayList<Toque> toques = new ArrayList<>();

    //COLISION
    boolean arriba;
    boolean abajo;
    boolean movimiento = true;
    boolean victroia;

    private static final String TAG = Juego.class.getSimpleName();

    public Juego(Activity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // se crea la superficie, creamos el game loop

        // Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);
        inicializar();

        // creamos el game loop
        bucle = new BucleJuego(getHolder(), this);
        setOnTouchListener(this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        //comenzar el bucle
        bucle.start();

    }

    private void inicializar() {
        //Canvas
        Canvas c = getHolder().lockCanvas();
        maxX = c.getWidth();
        maxY = c.getHeight();
        getHolder().unlockCanvasAndPost(c);

        //Circulo
        this.pelota = new Pelota(maxX, maxY);

        //Rects
        posicionRct1[X] = maxX / 2 - 250;
        posicionRct1[Y] = maxY / 6;
        posicionRct2[X] = maxX / 2 - 250;
        posicionRct2[Y] = maxY * 5 / 6;
        velocidadRect = maxX / segundos_cruzar_rect / BucleJuego.MAX_FPS;
    }

    /**
     * Este método actualiza el estado del juego. Contiene la lógica del videojuego
     * generando los nuevos estados y dejando listo el sistema para un repintado.
     */
    public void actualizar() {
        if (!victroia) {
            //Movimiento
            if (pelota.velocidad_random[Y] == 1) {
                pelota.posicionCir[Y] += pelota.velocidadPelota[Y];
                if (pelota.velocidad_random[X] == 1) {
                    pelota.posicionCir[X] += pelota.velocidadPelota[X];
                } else {
                    pelota.posicionCir[X] -= pelota.velocidadPelota[X];
                }
            } else {
                pelota.posicionCir[Y] -= pelota.velocidadPelota[Y];
                if (pelota.velocidad_random[X] == 1) {
                    pelota.posicionCir[X] += pelota.velocidadPelota[X];
                } else {
                    pelota.posicionCir[X] -= pelota.velocidadPelota[X];
                }
            }


            //Controles
            if (arribaIzq) {
                if (posicionRct1[X] > 0) {
                    posicionRct1[X] -= velocidadRect;
                }
            }
            if (arribaDer) {
                if (posicionRct1[X] + 500 < maxX) {
                    posicionRct1[X] += velocidadRect;
                }
            }
            if (abajoIzq) {
                if (posicionRct2[X] > 0) {
                    posicionRct2[X] -= velocidadRect;
                }
            }
            if (abajoDer) {
                if (posicionRct2[X] + 500 < maxX) {
                    posicionRct2[X] += velocidadRect;
                }
            }

            //Colision
            if (pelota.posicionCir[Y] - pelota.radio <= posicionRct1[Y] + 100 && pelota.posicionCir[X] > posicionRct1[X] && pelota.posicionCir[X] < posicionRct1[X] + 500 ||
                    pelota.posicionCir[Y] + pelota.radio >= posicionRct2[Y] && pelota.posicionCir[X] > posicionRct2[X] && pelota.posicionCir[X] < posicionRct2[X] + 500) {
                random = (int) ((Math.random() * 2) + 1);

                pelota.velocidadPelota[Y] = pelota.velocidadPelota[Y] * -1;
                if (random == 1) {
                    pelota.velocidadPelota[X] = pelota.velocidadPelota[X] * -1;
                }
            }
            if(pelota.posicionCir[X]-pelota.radio<0 || pelota.posicionCir[X]+pelota.radio>maxX){
                pelota.velocidadPelota[X] = pelota.velocidadPelota[X] * -1;
            }
            if(pelota.posicionCir[Y]-pelota.radio<=0 || pelota.posicionCir[Y]+pelota.radio>=maxY){
                victroia=true;
            }
        }
    }

    /**
     * Este método dibuja el siguiente paso de la animación correspondiente
     */
    public void renderizar(Canvas c) {

        c.drawColor(Color.WHITE);

        //pintar mensajes que nos ayudan
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(Color.RED);
        p.setTextSize(100);
        //c.drawText("Frame "+bucle.iteraciones+";"+"Tiempo "+bucle.tiempoTotal + " ["+bucle.maxX+","+bucle.maxY+"]",50,150,p);

        c.drawRect(posicionRct1[X], posicionRct1[Y], posicionRct1[X] + 500, posicionRct1[Y] + 100, p);
        c.drawRect(posicionRct2[X], posicionRct2[Y], posicionRct2[X] + 500, posicionRct2[Y] + 100, p);

        p.setColor(Color.BLACK);
        c.drawCircle(pelota.posicionCir[X], pelota.posicionCir[Y], pelota.radio, p);

        if (victroia) {
            if(pelota.posicionCir[Y]>maxY/2){
                c.drawText("HA GANADO EL DE ARRIBA", 0, maxY/2, p);
            }else{
                c.drawText("HA GANADO EL DE ABAJO", 0, maxY/2, p);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Juego destruido!");
        // cerrar el thread y esperar que acabe
        boolean retry = true;
        while (retry) {
            try {
                bucle.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index;
        int x, y;

        // Obtener el pointer asociado con la acción
        index = event.getActionIndex();

        x = (int) event.getX(index);
        y = (int) event.getY(index);


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                arribaIzq = true;

                synchronized (this) {
                    toques.add(index, new Toque(index, x, y));
                }

                //Arriba izquierda
                if (x < maxX / 2 && y < maxY / 2) {
                    abajoIzq = false;
                    abajoDer = false;
                    arribaDer = false;
                    arribaIzq = true;
                }
                //Arriba derecha
                if (x > maxX / 2 && y < maxY / 2) {
                    arribaIzq = false;
                    abajoIzq = false;
                    abajoDer = false;
                    arribaDer = true;
                }
                //Abajo izquierda
                if (x < maxX / 2 && y > maxY / 2) {
                    arribaIzq = false;
                    abajoDer = false;
                    arribaDer = false;
                    abajoIzq = true;
                }
                //Abajo derecha
                if (x > maxX / 2 && y > maxY / 2) {
                    arribaIzq = false;
                    abajoIzq = false;
                    abajoDer = true;
                    arribaDer = false;
                }

                break;

            case MotionEvent.ACTION_POINTER_UP:
                synchronized (this) {
                    toques.remove(index);
                }
                arribaIzq = false;
                abajoIzq = false;
                abajoDer = false;
                arribaDer = false;

                break;

            case MotionEvent.ACTION_UP:
                synchronized (this) {
                    toques.clear();
                }
                arribaIzq = false;
                abajoIzq = false;
                abajoDer = false;
                arribaDer = false;

                break;
        }

        return true;
    }
}
