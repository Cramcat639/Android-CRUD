package com.cramcat.platform.CRUDapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Crud extends AppCompatActivity {
    private int id_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);
        Context context = this;
        Funciones pop = new Funciones();

        TextView tvNick = findViewById(R.id.nick);
        TextView tvPass = findViewById(R.id.pass);
        TextView tvEmail = findViewById(R.id.email);
        DB db = new DB(context);
        String id = getIntent().getStringExtra("id");
        id_1 = Integer.parseInt(id);

        if (id != null) {
            List<String> allUsers = db.databaseOnList(Integer.parseInt(id));
            tvNick.setText(allUsers.get(1));
            tvEmail.setText(allUsers.get(2));
            tvPass.setText(allUsers.get(3));
        } else {
            String mensaje = "ID Es Null";
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            }


        Button eliminar = findViewById(R.id.eliminar);
        Button cerrar = findViewById(R.id.cerrar);
        Button guardar = findViewById(R.id.guardar);


        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.showYesNoDialog(
                        context,
                        "Eliminar",
                        "Estas seguro que quieres elimir tu cuenta?",
                        (DialogInterface.OnClickListener) (dialog, which) -> {
                            DB db = new DB(context);
                            String id = getIntent().getStringExtra("id");
                            db.delete(Integer.parseInt(id));
                            Intent intent = new Intent(Crud.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(context, "Cuenta borrada",Toast.LENGTH_LONG).show();
                            Toast.makeText(context, "Home",Toast.LENGTH_LONG).show();
                        },
                        (DialogInterface.OnClickListener) (dialog, which) -> {
                            dialog.dismiss();
                        }
                );
            }
        });

        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Crud.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(context, "Home",Toast.LENGTH_LONG).show();
            }
        });
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(tvEmail.getText());
                String nick = String.valueOf(tvNick.getText());
                String pass = String.valueOf(tvPass.getText());

                if (nick.matches("^[a-zA-Z0-9]{3,16}$")) {
                    if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                        if (pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,32}$")) {
                            // La contraseña cumple con los requisitos.
                            DB db = new DB(context);
                            db.update(id_1, nick, email, pass);
                            pop.showNewDialog(context, "Exito", "Los cambios se han guardado correctamente.");
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
    }
}