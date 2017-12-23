package com.juegocartas.duckstructors.juegodecartas;

import android.content.Context;
import android.content.res.TypedArray;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Eros on 18/11/2017.
 */


/**
 * Baraja es la clase que representa la baraja, solo permite coger.
 * Se puede añadir una baraja nueva para crear un nuevo modo de juego,
 * siempre y cuando funcione igual que el blackjack/siete y media
 */

public class Baraja{

    //Array que contiene las cartas
    private ArrayList<Carta> cartas;

    //Contexto para coger los recursos
    private Context contexto;
    ArrayList<TypedArray> dibus;
    public ArrayList<Integer> icos=new ArrayList<>();

    //Constantes que representan o el mazo 7 y 1/2 o el Blackjack
    public static final int SIETE_Y_MEDIA=1;
    public static final int ONCE_Y_MEDIA=2;
    public static final int BLACK_JACK=11;
    public static final int WHITE_JACK=12;

    //Mapa que almacena los tipos de baraja para el Spinner partida.
    public static LinkedHashMap<String,Integer> barajas=new LinkedHashMap<>();
    static {
        barajas.put("Elije Juego",0);
        barajas.put("Siete y media",SIETE_Y_MEDIA);
        barajas.put("Once y media",ONCE_Y_MEDIA);
        barajas.put("Black Jack",BLACK_JACK);
        barajas.put("White Jack",WHITE_JACK);
    }

    //Variables de la baraja
    public int tipo;
    public float limite;

    public Baraja(Context contex,int tipoBaraja){
        this.contexto=contex;
        this.tipo=tipoBaraja;
        cartas = new ArrayList<>();
        dibus = new ArrayList<>();
        generarCartas(tipoBaraja);
    }

    //Metodo que coge una carta
    public Carta cogerCarta(){
        Collections.shuffle(cartas);
        return cartas.remove(0);
    }

    //Metodo que genera las distintas barajas.
    private void generarCartas(int tipoBaraja){
        int excluido = (int)(Math.random()*5);
        TypedArray iconos = contexto.getResources().obtainTypedArray(R.array.IconosMagia);
        for (int i = 0;i<iconos.length();i++)icos.add(iconos.getResourceId(i,0));
        icos.remove(excluido);
        if (tipoBaraja==SIETE_Y_MEDIA) {
            this.limite = 7.5f;
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoBlancoSiete));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoVerdeSiete));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoRojoSiete));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoNegroSiete));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoAzulSiete));
        }else if (tipoBaraja==ONCE_Y_MEDIA){
                this.limite=11.5f;
                dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoBlancoSiete));
                dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoVerdeSiete));
                dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoRojoSiete));
                dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoNegroSiete));
                dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoAzulSiete));
        }else if (tipoBaraja==BLACK_JACK){
            this.limite=21f;
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoBlancoJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoVerdeJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoRojoJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoNegroJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoAzulJack));

        }else if (tipoBaraja==WHITE_JACK){
            this.limite=31f;
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoBlancoJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoVerdeJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoRojoJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoNegroJack));
            dibus.add(contexto.getResources().obtainTypedArray(R.array.MazoAzulJack));
        }else{}
        dibus.remove(excluido);
        for (TypedArray dibujos:dibus) {
            for(int i=0;i<dibujos.length();i++){
                cartas.add(new Carta(dibujos.getResourceId(i,0),darValor(tipoBaraja,i)));
            }
        }
        Collections.shuffle(cartas);
    }
    private float darValor(int tipo,int val){
            if(tipo==WHITE_JACK)tipo=BLACK_JACK;
            else if(tipo==ONCE_Y_MEDIA)tipo=SIETE_Y_MEDIA;
        switch (tipo){
            case SIETE_Y_MEDIA: switch (val){
                case 0:return 1f;
                case 1:return 2f;
                case 2:return 3f;
                case 3:return 4f;
                case 4:return 5f;
                case 5:return 6f;
                case 6:return 7f;
                case 7:return 0.5f;
                case 8:return 0.5f;
                case 9:return 0.5f;
                default:return 0f;
            }
            case BLACK_JACK: switch (val){
                case 0:return 1f;
                case 1:return 2f;
                case 2:return 3f;
                case 3:return 4f;
                case 4:return 5f;
                case 5:return 6f;
                case 6:return 7f;
                case 7:return 8f;
                case 8:return 9f;
                case 9:return 10f;
                case 10:return 10f;
                case 11:return 10f;
                case 12:return 10f;
                default:return 0f;
            }
            default:return 0;
        }
    }
}
