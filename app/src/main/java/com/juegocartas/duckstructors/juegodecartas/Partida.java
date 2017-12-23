package com.juegocartas.duckstructors.juegodecartas;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by Eros on 18/11/2017.
 */

public class Partida {

    //Constantes para el final del juego
    //Se pueden crear nuevas constantes para distintos finales.
    public static int JUEGO_CONTINUA=0;

    public static int VICTORIA_JUGADOR=1;
    public static int SE_PASA_JUGADOR=2;
    public static int PIERDE_JUGADOR=3;
    public static int BLACKJACK_JUGADOR=5;
    public static int WHITEJACK_JUGADOR=6;

    public static int VICTORIA_BANCA=11;
    public static int SE_PASA_BANCA=12;
    public static int PIERDE_BANCA=13;
    public static int BLACKJACK_BANCA=15;
    public static int WHITEJACK_BANCA=16;

    public static int EMPATE=100;

    //HashMap que devuelve mensajes diferentes para cada condicion de final de juego
    //Se pueden crear nuevos mensajes para distintos finales.
    public static HashMap<Integer,String> mensajes=new HashMap<>();
    static {
        mensajes.put(VICTORIA_JUGADOR,"Enhorabuena, ¡Has ganado!");
        mensajes.put(SE_PASA_JUGADOR,"Te has pasado, pierdes...");
        mensajes.put(PIERDE_JUGADOR,"Has perdido");
        mensajes.put(BLACKJACK_JUGADOR,"Tienes BlackJack");
        mensajes.put(WHITEJACK_JUGADOR,"Tienes WhiteJack ¡GANAS!");

        mensajes.put(VICTORIA_BANCA,"¡Gana la banca!");
        mensajes.put(SE_PASA_BANCA,"¡Ganas! ¡la banca se ha pasado!");
        mensajes.put(PIERDE_BANCA,"¡Has ganado!");
        mensajes.put(BLACKJACK_BANCA,"La banca tiene BlackJack");
        mensajes.put(WHITEJACK_BANCA,"La banca tiene WhiteJack");
    }

    //Puntos y ases del BLACKJACK
    private float puntosJugador=0;
    private float puntosBanca=0;
    private int asesJugador=0;
    private int asesBanca=0;

    //La baraja en uso y un boolean para saber si la partida ha terminado
    public Baraja baraja;
    private boolean fin=false;

    /**
     * Constructor de la Partida
     * @param cont Espera el contexto para poder pasarselo a la baraja y que esta llame a los "R"
     * @param tipoBaraja Espera el tipo de baraja para iniciar el juego
     */
    public Partida(Context cont,int tipoBaraja){
        //Creamos la baraja
        baraja= new Baraja(cont,tipoBaraja);
    }

    //Evaluamos las diferentes posibilidades de la partida
    public int comprobarVictoria(){
        int sol=0;
        if(Baraja.BLACK_JACK==baraja.tipo&&puntosJugador==21)sol= BLACKJACK_JUGADOR;

        else if(Baraja.BLACK_JACK==baraja.tipo&&puntosBanca==21)sol= VICTORIA_BANCA;

        else if(Baraja.WHITE_JACK==baraja.tipo&&puntosJugador==31)sol= WHITEJACK_JUGADOR;

        else if(Baraja.WHITE_JACK==baraja.tipo&&puntosBanca==31)sol= WHITEJACK_BANCA;

        else if(puntosJugador>baraja.limite)sol= SE_PASA_JUGADOR;

        else if(puntosBanca>baraja.limite)sol= SE_PASA_BANCA;

        else if(puntosBanca>puntosJugador)sol= PIERDE_JUGADOR;

        if(sol!=JUEGO_CONTINUA)fin=true;
        return sol;
    }

    //Devuelve is ha terminado la partida
    public boolean isFin(){
        return fin;
    }

    //Suma una carta al jugador
    public Carta cogerCartaJugador(){
        Carta car=baraja.cogerCarta();

        //Si es blackjack tiene en cuenta el valor especial del As
        if ((baraja.tipo==Baraja.BLACK_JACK||baraja.tipo==baraja.WHITE_JACK)&&car.getValor()==1f){
            puntosJugador+=11f;
            asesJugador++;
        }else{
            puntosJugador+=car.getValor();
        }
        if((baraja.tipo==Baraja.BLACK_JACK&&puntosJugador>22&&asesJugador>0)||(baraja.tipo==baraja.WHITE_JACK&&puntosJugador>32&&asesJugador>0)){
            puntosJugador-=10;
            asesJugador--;
        }
        return car;
    }

    //Suma una carta a la banca
    public Carta cogerCartaBanca(){
            Carta car=baraja.cogerCarta();

            //Si es blackjack tiene en cuenta el valor especial del As
            if ((baraja.tipo==Baraja.BLACK_JACK||baraja.tipo==baraja.WHITE_JACK)&&car.getValor()==1f){
                puntosBanca+=11f;
                asesBanca++;
            }else{
                puntosBanca+=car.getValor();
            }
            if((baraja.tipo==Baraja.BLACK_JACK&&puntosBanca>22&&asesBanca>0)||(baraja.tipo==baraja.WHITE_JACK&&puntosBanca>32&&asesBanca>0)){
                puntosBanca-=10;
                asesBanca--;
            }
            return car;
    }

    //Estos metodos devuelven los puntos ya como String
    public String getPuntosJugador() {
        if (baraja.tipo==Baraja.SIETE_Y_MEDIA||baraja.tipo==Baraja.ONCE_Y_MEDIA){
            return Float.toString(puntosJugador);
        }else if ((baraja.tipo==Baraja.BLACK_JACK||baraja.tipo==baraja.WHITE_JACK)){
            return Integer.toString((int)puntosJugador);
        }else{
            return "";
        }
    }
    public String getPuntosBanca() {
        if (baraja.tipo==Baraja.SIETE_Y_MEDIA||baraja.tipo==Baraja.ONCE_Y_MEDIA){
            return Float.toString(puntosBanca);
        }else if ((baraja.tipo==Baraja.BLACK_JACK||baraja.tipo==baraja.WHITE_JACK)){
            return Integer.toString((int)puntosBanca);
        }else{
            return "";
        }
    }
}
