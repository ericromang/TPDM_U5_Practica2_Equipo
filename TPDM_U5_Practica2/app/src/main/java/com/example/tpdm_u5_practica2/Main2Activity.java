package com.example.tpdm_u5_practica2;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Main2Activity extends AppCompatActivity {
    Sala jugador = new Sala();
    int verificaLanzamiento;
    public List<Sala> jugadores;
    public DatabaseReference DB;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    Thread hilo;
    Button tirar;
    TextView accion,contraria,ganad;
    ImageView imgTuya,imgContraria;
    Boolean animacion = false,tiro=false;
    String ganador="",sala_elegida;
    String [] movi = {"nada","piedra","papel","tijera"};
    int [] imagenes = {10,R.drawable.piedra,R.drawable.papel,R.drawable.tijera};
    CountDownTimer timer;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        jugador = (Sala) getIntent().getExtras().getSerializable("jugador");
        sala_elegida = getIntent().getExtras().getString("sala_elegida");
        DB = FirebaseDatabase.getInstance().getReference();
        actualizarMovimiento(jugador);
        imgTuya = findViewById(R.id.imgTuya);
        imgContraria = findViewById(R.id.imgContra);

        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                if(animacion || tiro)
                    return;
                tiro = true;
                int random = (int)(Math.random()*3+1);
                jugador.movimiento = random+"";
                jugador.lanzo = 1+"";
                //accion.setText(movi[Integer.parseInt(jugador.movimiento)]+" ");
                imgTuya.setImageResource(imagenes[Integer.parseInt(jugador.movimiento)]);
                actualizarMovimiento(jugador);

                Toast.makeText(Main2Activity.this, "Shaked!!!", Toast.LENGTH_SHORT).show();
            }
        });


        DB.child("Salas").child(sala_elegida).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(jugadores!=null)
                    jugadores.clear();
                jugadores = new ArrayList<>();
                for (final DataSnapshot snap : dataSnapshot.getChildren()) {
                    final int j = 0;
                    DB.child("Salas").child(sala_elegida).child(snap.getKey()).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Sala e = dataSnapshot.getValue(Sala.class);
                                    jugadores.add(e);

                                    if(e.id == jugador.id){
                                        jugador = e;
                                    }
                                    Toast.makeText(Main2Activity.this, e.id+" DEL JUGADOR "+jugador.id, Toast.LENGTH_SHORT).show();
                                    if(e.id.equals("2") && jugadores.size() == 2 && Integer.parseInt(jugadores.get(0).lanzo) == 1 && Integer.parseInt(jugadores.get(1).lanzo) ==1 && !animacion) {
                                        Toast.makeText(Main2Activity.this, "entre", Toast.LENGTH_SHORT).show();
                                        animacion = true;

                                        if (Integer.parseInt(jugadores.get(0).movimiento) == Integer.parseInt(jugadores.get(1).movimiento)) {
                                            ganador = "Empate";
                                        }
                                        if (Integer.parseInt(jugadores.get(0).movimiento) == 1 && Integer.parseInt(jugadores.get(1).movimiento) == 3) {
                                            ganador = "Jugador 1";
                                        }
                                        if (Integer.parseInt(jugadores.get(0).movimiento) == 3 && Integer.parseInt(jugadores.get(1).movimiento) == 2) {
                                            ganador = "Jugador 1";
                                        }
                                        if (Integer.parseInt(jugadores.get(0).movimiento) == 2 && Integer.parseInt(jugadores.get(1).movimiento) == 1) {
                                            ganador = "Jugador 1";
                                        }
                                        if (Integer.parseInt(jugadores.get(1).movimiento) == 1 && Integer.parseInt(jugadores.get(0).movimiento) == 3) {
                                            ganador = "Jugador 2";
                                        }
                                        if (Integer.parseInt(jugadores.get(1).movimiento) == 3 && Integer.parseInt(jugadores.get(0).movimiento) == 2) {
                                            ganador = "Jugador 2";
                                        }
                                        if (Integer.parseInt(jugadores.get(1).movimiento) == 2 && Integer.parseInt(jugadores.get(0).movimiento) == 1) {
                                            ganador = "Jugador 2";
                                        }

                                        timer = new CountDownTimer(8000, 200) {
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                i++;
                                                if(i < 20)
                                                    imgContraria.setImageResource(imagenes[(int)(Math.random()*3+1)]);
                                                if (i == 21) {
                                                    jugador.lanzo = jugador.movimiento = "0";
                                                    verificaLanzamiento = 0;

                                                    if (Integer.parseInt(jugador.id) == 1) {
                                                        //contraria.setText(movi[Integer.parseInt(jugadores.get(1).movimiento)] + "");
                                                        imgContraria.setImageResource(imagenes[Integer.parseInt(jugadores.get(1).movimiento)]);
                                                    }
                                                    else {
                                                        //contraria.setText(movi[Integer.parseInt(jugadores.get(0).movimiento)] + "");
                                                        imgContraria.setImageResource(imagenes[Integer.parseInt(jugadores.get(0).movimiento)]);
                                                    }
                                                    ganad.setText(ganador);
                                                }
                                            }

                                            @Override
                                            public void onFinish() {
                                                if(jugador.id.equals("1")) {
                                                    actualizarMovimiento(jugador);
                                                    actualizarMovimiento(new Sala("2","0","0","1"));
                                                }
                                                i=0;
                                                tiro = false;
                                                animacion = false;
                                            }
                                        };
                                        timer.start();
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

        tirar = findViewById(R.id.button);
        //accion = findViewById(R.id.textView);
        //contraria = findViewById(R.id.contraria);
        ganad = findViewById(R.id.ganador);
        tirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(animacion || tiro)
                    return;
                tiro = true;
                int random = (int)(Math.random()*3+1);
                jugador.movimiento = random+"";
                jugador.lanzo = 1+"";
                //accion.setText(movi[Integer.parseInt(jugador.movimiento)]+" ");
                imgTuya.setImageResource(imagenes[Integer.parseInt(jugador.movimiento)]);
                actualizarMovimiento(jugador);
            }
        });

    }


    public void actualizarMovimiento(Sala e){
        final Sala jug = new Sala(e.id,e.lanzo,e.movimiento, e.ocupado);
        DB.child("Salas").child(sala_elegida).child(jug.id).setValue(jug)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Toast.makeText(Main2Activity.this, "ENTRE A SEGUNDA PANTALLA AZTUALIZAR", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main2Activity.this, "ERROR!!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onStop() {
        jugador.ocupado = "0";
        jugador.movimiento="0";
        jugador.lanzo="0";
        actualizarMovimiento(jugador);
        super.onStop();
        // La actividad ya no es visible (en la actualidad esta "detenido")
    }
    @Override
    protected void onDestroy() {
        jugador.ocupado = "0";
        jugador.movimiento="0";
        jugador.lanzo="0";
        actualizarMovimiento(jugador);
        super.onDestroy();
        // La actividad está a punto de ser destruido.
    }
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
