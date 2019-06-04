package com.example.tpdm_u5_practica2;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    public EditText ID,nombre,rfc,edad;
    public Button insertar, entrar, eliminar, actualizar;
    public DatabaseReference DB;
    public List<Sala> jugadores;
    String salaOcupada,sala_elgida;
    Sala jugador;
    LinearLayout layo;
    int i2;
    Button vectorBotones[];
    public ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layo = findViewById(R.id.layo);

        DB = FirebaseDatabase.getInstance().getReference();
        DB.child("Salas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(sala_elgida != null)
                    return;
                int i = 0;

                for (final DataSnapshot snap : dataSnapshot.getChildren()) {
                    i++;


                    DB.child("Salas").child(snap.getKey()).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(sala_elgida != null)
                                        return;
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            }
                    );
                }
                if(sala_elgida != null)
                    return;
                generarBotonesDinamicos(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void generarBotonesDinamicos(int c){
        eliminarBotonesDinamicos(c);
        int cantidad = c;
        vectorBotones = new Button[cantidad]; //construyendo las celdillas

        for(int i = 0; i<vectorBotones.length; i++){
            i2 = i;
            vectorBotones[i] = new Button(this);
            vectorBotones[i].setText("Sala"+(i+1));
            vectorBotones[i].setId(i);
            layo.addView(vectorBotones[i]);
            vectorBotones[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int id_sala= v.getId();
                    DB = FirebaseDatabase.getInstance().getReference();
                    DB.child("Salas").child(vectorBotones[id_sala].getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            jugadores = new ArrayList<>();
                            for (final DataSnapshot snap : dataSnapshot.getChildren()) {
                                DB.child("Salas").child(vectorBotones[id_sala].getText().toString()).child(snap.getKey()).addValueEventListener(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Sala e = dataSnapshot.getValue(Sala.class);
                                                if(salaOcupada != null)
                                                    return;
                                                if (e.ocupado.equals("0")) {
                                                    jugadores.add(e);
                                                    salaOcupada = e.ocupado = "1";
                                                    jugador = e;
                                                    sala_elgida=vectorBotones[id_sala].getText().toString();

                                                    Intent intent  = new Intent(MainActivity.this,Main2Activity.class);
                                                    intent.putExtra("jugador",jugador);
                                                    intent.putExtra("sala_elegida",vectorBotones[id_sala].getText().toString());
                                                    startActivity(intent);
                                                }else{
                                                    return;
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        }
                                );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

    }
    private void eliminarBotonesDinamicos(int w){
        if(vectorBotones==null) {

            if ( w == 0) {
                Toast.makeText(this, "Prrimero crea!", Toast.LENGTH_LONG).show();
            }
            return;
        }

        layo.removeViews(0, vectorBotones.length);
        vectorBotones = null;
    }
    /*public void actualizarSala(Sala e){
        final Sala jug = new Sala(e.id,e.lanzo, e.movimiento, e.ocupado);
        DB.child("Salas").child(sala_elgida).child(jug.id).setValue(jug)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "ENTRE A PRIMERA PANTA AZTUALIZAR", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR!!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        // La actividad está a punto de hacerse visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
        // La actividad se ha hecho visible (ahora es "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Otra de las actividades está tomando el enfoque (esta actividad está a punto de ser "una pausa").
    }
    @Override
    protected void onStop() {
        //jugador.ocupado = "0";
        super.onStop();
        // La actividad ya no es visible (en la actualidad esta "detenido")
    }
    @Override
    protected void onDestroy() {
        //jugador.ocupado = "0";
        super.onDestroy();
        // La actividad está a punto de ser destruido.
    }
}
