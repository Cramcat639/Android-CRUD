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
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText tvemail;
    EditText tvPass;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Context context = this;

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
                String email = tvemail.getText().toString();
                String pass = tvPass.getText().toString();

                Api api = new Api();

                api.loginAsync(email, pass, new Api.ApiCallback() {
                    @Override
                    public void onApiResult(String result) {
                        if (result != null) {
                            // Procesar la respuesta del servidor según sea necesario
                            handleApiResponse(result);
                        } else {
                            // Hubo un error en la operación de red
                            showErrorDialog("Error de red");
                        }
                    }
                });
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

    private void handleApiResponse(String result) {
        // Analizar y procesar la respuesta del servidor según el formato real de la respuesta
        // Supongamos que la respuesta es un JSON como {"id": "123"}
        // Puedes usar una biblioteca como Gson para un análisis JSON más robusto.

        String userId = result.trim().replace("{\"id\": \"", "").replace("\"}", "");

        if (!userId.isEmpty()) {
            // La autenticación fue exitosa, puedes realizar acciones adicionales aquí
            Intent intent = new Intent(MainActivity.this, Crud.class);
            intent.putExtra("id", userId);
            startActivity(intent);
        } else {
            showErrorDialog("Inicio de sesión fallido");
        }
    }

    private void showErrorDialog(String errorMessage) {
        // Muestra un cuadro de diálogo de error con el mensaje proporcionado
        Funciones pop = new Funciones();
        pop.showNewDialog(MainActivity.this, "Error", errorMessage);
    }
}
