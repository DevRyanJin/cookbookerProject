package com.cookbooker.presentation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.cookbooker.R;


public class Message {
    public static void fatalError(final Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();
        alertDialog.setIcon(R.drawable.error);
        alertDialog.setTitle(owner.getString(R.string.fatalError));
        alertDialog.setMessage(message);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                owner.finish();
            }
        });

        alertDialog.show();
    }

    public static void warning(Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();
        alertDialog.setIcon(R.drawable.warning);
        alertDialog.setTitle(owner.getString(R.string.warning));
        alertDialog.setMessage(message);

        alertDialog.show();
    }

    public static void info(Activity owner, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(owner).create();
        alertDialog.setIcon(R.drawable.info);
        alertDialog.setTitle(owner.getString(R.string.info));
        alertDialog.setMessage(message);

        alertDialog.show();
    }
}
