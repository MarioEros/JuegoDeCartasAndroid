package com.juegocartas.duckstructors.juegodecartas;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Tapete extends AppCompatActivity {

    //Tapetes y cartas
    LinearLayout tapeteJugador;
    RelativeLayout tapeteCartasJugador;
    ArrayList<View> cartasJugador=new ArrayList<>();
    LinearLayout tapeteBanca;
    RelativeLayout tapeteCartasBanca;
    ArrayList<View> cartasBanca=new ArrayList<>();
    LinearLayout tapeteMazo;

    //Partida
    Partida partida;
    TextView puntosJugador;
    TextView puntosBanca;
    Spinner espinete;

    //Otras Ids
    CheckBox siete;
    CheckBox black;
    ImageButton pedir;
    ImageButton pasar;
    ImageButton nueva;

    //Toast, lo declaro aqui para poder detenerlo en caso de empezar otra partida
    Toast tostada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tapete);

        //Damos valores a las Views
        this.tapeteJugador = (LinearLayout) findViewById(R.id.tapeteJugador);
        this.tapeteCartasJugador=(RelativeLayout) findViewById(R.id.tapeteCartasJugador);
        this.tapeteBanca = (LinearLayout) findViewById(R.id.tapeteBanca);
        this.tapeteCartasBanca =(RelativeLayout) findViewById(R.id.tapeteCartasBanca);
        this.puntosJugador = (TextView) findViewById(R.id.puntosJugador);
        this.puntosBanca = (TextView) findViewById(R.id.puntosBanca);
        /*this.siete = (CheckBox) findViewById(R.id.CheckSiete);
        this.black = (CheckBox) findViewById(R.id.CheckBlack);*/
        this.pedir = (ImageButton) findViewById(R.id.BotonPedir);
        this.pasar = (ImageButton) findViewById(R.id.BotonPasar);
        this.nueva = (ImageButton) findViewById(R.id.BotonNuevaPartida);
        this.tapeteMazo = (LinearLayout) findViewById(R.id.tapeteMazo);
        espinete = (Spinner) findViewById(R.id.JuegoSpinner);
        List<String> paraSpin= new ArrayList<>();
        for(Map.Entry entrada:Baraja.barajas.entrySet()){
            paraSpin.add(entrada.getKey().toString());
        }
        espinete.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,paraSpin));
        espinete.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nuevaPartida(Baraja.barajas.get(espinete.getSelectedItem().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                nuevaPartida(0);
            }
        });

        //Dejamos el tablero listo
        nuevaPartida(0);
    }

    //Metodo que crea una partida nueva
    public void nuevaPartida(int i){
        tapeteCartasJugador.removeAllViews();
        tapeteCartasBanca.removeAllViews();
        cartasJugador = new ArrayList<>();
        cartasBanca = new ArrayList<>();
        if (tostada!=null)tostada.cancel();
        //Si la partida es Siete y media
        if (i!=0){
            partida = new Partida(this,i);
            estadoBotones(true);
            nueva.setEnabled(true);
            fondoBoton(nueva,true);
            puntosJugador.setText(partida.getPuntosJugador());
            puntosBanca.setText(partida.getPuntosBanca());

            //Metemos los iconos
            if(tapeteMazo.getChildCount()>2)tapeteMazo.removeViewAt(0);
            LayoutInflater infli= getLayoutInflater();
            View setMagia=infli.inflate(R.layout.layout_iconos,null);
            GridLayout iconitos=(GridLayout) setMagia.findViewById(R.id.IconosGrid);
            ImageView ico0=(ImageView) setMagia.findViewById(R.id.ico0);
            ico0.setImageResource(partida.baraja.icos.get(0));
            ImageView ico1=(ImageView) setMagia.findViewById(R.id.ico1);
            ico1.setImageResource(partida.baraja.icos.get(1));
            ImageView ico2=(ImageView) setMagia.findViewById(R.id.ico2);
            ico2.setImageResource(partida.baraja.icos.get(2));
            ImageView ico3=(ImageView) setMagia.findViewById(R.id.ico3);
            ico3.setImageResource(partida.baraja.icos.get(3));
            tapeteMazo.addView(setMagia,0);
        }
        //Si no hay partida seleccionada
        else{
            if(tapeteMazo.getChildCount()>2)tapeteMazo.removeViewAt(0);
            estadoBotones(false);
            nueva.setEnabled(false);
            fondoBoton(nueva,false);
            puntosJugador.setText("");
            puntosBanca.setText("");
        }
    }

    //Este metodo activa o desactiva los 3 botones de jugar
    private void estadoBotones(boolean act){
        pedir.setEnabled(act);
        fondoBoton(pedir,act);
        pasar.setEnabled(act);
        fondoBoton(pasar,act);
    }
    //Metodo que cambia el color de los botones para ver si estan habilitados
    private void fondoBoton(ImageButton bot,boolean act){
        if(act)bot.setBackgroundResource(android.R.drawable.btn_default);
        else bot.setBackgroundColor(getResources().getColor(R.color.tapete));
    }

    //Jugador pide una carta
    public void PedirCarta(View view) {
        if(pedir.isEnabled())sacarCarta(tapeteCartasJugador,cartasJugador,tapeteJugador,puntosJugador,true);
    }

    //El jugador pasa y comienza a jugar la banca
    public void Pasar(View view) {
        estadoBotones(false);
            sacarCarta(tapeteCartasBanca,cartasBanca,tapeteBanca,puntosBanca,false);
    }

    //Pide empezar una nueva partida
    public void PartidaNueva(View view) {
        nuevaPartida(Baraja.barajas.get(espinete.getSelectedItem().toString()));
    }

    /**
     * //Saca una carta de la baraja y la dibuja en el tablero correspondiente
     * @param tapete        El Layout en el que se van a meter las cartas
     * @param cartas        El array de cartas que ya están en la mesa
     * @param mesa          El Layout que contiene el tapete
     * @param puntos        El Textview de quien está sacando la carta
     * @param isJugador     Boolean que identifica si juega el jugador o la banca
     */
    public void sacarCarta(RelativeLayout tapete,ArrayList<View> cartas,LinearLayout mesa,TextView puntos,boolean isJugador){
        //Vaciamos el tapete porque vamos a añadir todas las cartas de nuevo, con espaciado dinamico
        tapete.removeAllViews();
        //Inflamos la carta, le ponemos la imagen y la añadimos al array de cartas
        final LayoutInflater inflador=getLayoutInflater();
        final View carta=inflador.inflate(R.layout.layout_carta,null);
        ImageView img=(ImageView) carta.findViewById(R.id.ImagenCarta);

        //Indica quien saca la carta y la añadimos a su array
        if(isJugador)img.setImageResource(partida.cogerCartaJugador().getImg());
        else img.setImageResource(partida.cogerCartaBanca().getImg());
        cartas.add(carta);

        //Cremos el array de parametros
        ArrayList<RelativeLayout.LayoutParams> parametritos= new ArrayList<>();

        //Recuperamos el tamaño de las cartas de la banca
        int tam = cartas.size();

        //Creamos una variable que es el 85% del ancho del tapete del jugador para espaciar dinamicamente
        int tamtab = (int) (mesa.getWidth()-(mesa.getWidth()*0.15));

        //Hacemos un bucle para meter las cartas
        for(int i=0;i<tam;i++){

            //Creamos LayoutParameters personalizados para cada una de las cartas
            parametritos.add(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            parametritos.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
            parametritos.get(i).leftMargin=((int)((tamtab/tam)*i));

            //Añade un listener para la carta
            cartas.get(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    //Lanza la toast, porque no he sido capaz de hacer una animación
                    if(tostada!=null)tostada.cancel();
                    View tosta = inflador.inflate(R.layout.tostada_carta,null);
                    ((ImageView)tosta.findViewById(R.id.ImagenTostada)).setImageDrawable(((ImageView)v.findViewById(R.id.ImagenCarta)).getDrawable());
                    tostada = new Toast(getApplicationContext());
                    tostada.setView(tosta);
                    tostada.setDuration(Toast.LENGTH_SHORT);
                    tostada.show();
                    return false;
                }
            });
            //Añadimos la carta
            tapete.addView(cartas.get(i),parametritos.get(i));
        }

        //ponemos los puntos y comprobamos si alguien ha ganado
        if(isJugador)puntos.setText(partida.getPuntosJugador());
        else puntos.setText(partida.getPuntosBanca());
        finalDeJuego(partida.comprobarVictoria());

        //Si es la banca y no ha terminado la partida, saca otra carta
        if(!isJugador&&!partida.isFin()){
            Handler retardo = new Handler();
            retardo.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sacarCarta(tapeteCartasBanca, cartasBanca, tapeteBanca, puntosBanca, false);
                }

            },1000);}
        }


    //lanza un toast en caso de haber habido un final
    public void finalDeJuego(int juego){
        if(juego!=Partida.JUEGO_CONTINUA){
            if(tostada!=null)tostada.cancel();
            tostada = Toast.makeText(this,Partida.mensajes.get(juego),Toast.LENGTH_LONG);
            tostada.show();
            estadoBotones(false);
        }
    }
}
