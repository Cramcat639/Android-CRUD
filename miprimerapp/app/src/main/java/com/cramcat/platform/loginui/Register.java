package com.cramcat.platform.loginui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        Context context = this;
        Funciones pop = new Funciones();
        TextView tvNick = findViewById(R.id.nick);
        TextView tvPass = findViewById(R.id.pass);
        TextView tvemail = findViewById(R.id.email);
        TextView tvRePass = findViewById(R.id.rePass);


        Button home = findViewById(R.id.home);
        Button registro = findViewById(R.id.registro);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(tvemail.getText());
                String nick = String.valueOf(tvNick.getText());
                String pass = String.valueOf(tvPass.getText());
                String rePass = String.valueOf(tvRePass.getText());

                if (nick.matches("^[a-zA-Z0-9]{3,16}$")) {
                    if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                        if (pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,32}$")) {
                            // La contraseña cumple con los requisitos.
                            if (pass.equals(rePass)) {
                                DB db = new DB(context);
                                db.insert(nick, email, pass);
                                Integer idActual = db.encuentraID(email);
                                Intent intent = new Intent(Register.this, Crud.class);
                                intent.putExtra("id", String.valueOf(idActual));
                                startActivity(intent);
                            } else {

                                pop.showNewDialog(context, "Error", "Las contraseñas no coinciden");
                            }
                        } else {
                            // La contraseña no cumple con los requisitos.
                            pop.showNewDialog(context, "Error", "la contraseña debe tener entre 8 y 32 caracteres, contener almenos 1 letra minúscula, 1 letra mayuscula y 1 numero.");
                        }
                    } else {
                        pop.showNewDialog(context, "Error", "Este no es un mail valido.");
                    }
                }else{
                    pop.showNewDialog(context, "Error", "El nick debe contener entre 3 y 16 caracteres y no puede contener caracteres especiales.");
                }
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();
                Toast.makeText(context, "Home",Toast.LENGTH_LONG).show();}
        });
    }
}