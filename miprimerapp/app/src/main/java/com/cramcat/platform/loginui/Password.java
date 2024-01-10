package com.cramcat.platform.loginui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Password extends AppCompatActivity {
    ImageView imageView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_password);
        Context context = this;
        TextView tvemail = findViewById(R.id.email);
        Funciones pop = new Funciones();


        imageView = findViewById(R.id.imageView);
        Button enviar = findViewById(R.id.enviar);
        Button home = findViewById(R.id.home);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(tvemail.getText());
                DB db = new DB(context);
                if (db.checkMail(email) == true){
                    String pass = db.encuentraPass(email);
                    pop.showNewDialog(context, "Contraseña", pass);
                }else{
                    pop.showNewDialog(context, "Error", "Email no encontrado.");
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(context, "Home",Toast.LENGTH_LONG).show();
            }
        });
    }
}