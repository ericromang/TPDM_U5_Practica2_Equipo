package com.example.tpdm_u5_practica2;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Sala implements Serializable {
    public String id,lanzo,movimiento,ocupado;

    public Sala(String id,String lanzo, String movimiento, String ocupado) {
        this.id = id;
        this.lanzo = lanzo;
        this.movimiento = movimiento;
        this.ocupado = ocupado;
    }
    public Sala(){}
}
