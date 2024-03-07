package com.riberadeltajo.pongexamen;

public class Pelota {
    static final int X = 0;
    static final int Y = 1;
    float[] posicionCir = new float[2];
    static final int segundos_cruzar_pelota = 5;
    float[] velocidadPelota = new float[2];
    int[] velocidad_random = new int[2];
    float maxX, maxY;
    int radio;

    public Pelota(float maxX, float maxY) {
        posicionCir[X] = maxX / 2;
        posicionCir[Y] = maxY / 2;
        velocidadPelota[Y] = maxY / segundos_cruzar_pelota / BucleJuego.MAX_FPS;
        velocidadPelota[X] = (float) (Math.random() * (maxX / segundos_cruzar_pelota / BucleJuego.MAX_FPS) + 1);
        velocidad_random[X] = (int) ((Math.random() * 2) + 1);
        velocidad_random[Y] = (int) ((Math.random() * 2) + 1);
        radio=100;
    }
}
