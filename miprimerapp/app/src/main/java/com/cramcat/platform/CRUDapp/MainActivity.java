package com.cramcat.platform.CRUDapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    int count = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Context context = this;
        final boolean[] validacion = new boolean[1];
        Funciones pop = new Funciones();

        imageView = findViewById(R.id.imageView);
        TextView olvidadoTextView = findViewById(R.id.olvidado);
        TextView tvemail = findViewById(R.id.email);
        TextView tvPass = findViewById(R.id.pass);


        Button registrarse = findViewById(R.id.registrarse);
        Button iniciar = findViewById(R.id.iniciar);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(tvemail.getText());
                String pass = String.valueOf(tvPass.getText());
                DB db = new DB(context);

                // Validar credenciales
                if (db.searchUser(email, pass)) {
                    // Obtener ID
                    Integer idActual = db.encuentraID(email);

                    // Verificar si se encontró un ID
                    if (idActual != null) {
                        Intent intent = new Intent(MainActivity.this, Crud.class);
                        intent.putExtra("id", String.valueOf(idActual));
                        startActivity(intent);
                    } else {
                        pop.showNewDialog(context, "Error", "No se pudo encontrar el ID asociado al correo electrónico.");
                    }
                } else {
                    pop.showNewDialog(context, "Error", "Email o contraseña incorrectos.");
                }
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
        olvidadoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Password.class);
                startActivity(intent);
            }
        });
    }
}
