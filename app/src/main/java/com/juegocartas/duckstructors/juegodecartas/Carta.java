package com.juegocartas.duckstructors.juegodecartas;

import android.graphics.drawable.Drawable;

/**
 * Created by Eros on 18/11/2017.
 */

public class Carta {
    private int img;
    private float valor;
    public Carta(int ima,float val){
        this.img=ima;
        this.valor=val;
    }

    public int getImg() {
        return img;
    }
    public float getValor() { return valor; }
}
