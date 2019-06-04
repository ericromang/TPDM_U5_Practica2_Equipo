package com.example.tpdm_u5_practica2;

public class Salas {
    public Jugador jugador1,jugador2;

    public Salas(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador1.id="1";
        this.jugador1.movimiento="0";
        this.jugador1.ocupado="0";
        this.jugador2 = jugador2;
        this.jugador2.id="1";
        this.jugador2.movimiento="0";
        this.jugador2.ocupado="0";
    }
    public Salas(){}
}
