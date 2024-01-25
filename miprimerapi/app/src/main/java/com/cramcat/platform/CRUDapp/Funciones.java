package com.cramcat.platform.CRUDapp;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Funciones {
    public void showNewDialog(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showYesNoDialog(Context context, String title, String message, final DialogInterface.OnClickListener yesClickListener, final DialogInterface.OnClickListener noClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (yesClickListener != null) {
                            yesClickListener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (noClickListener != null) {
                            noClickListener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
