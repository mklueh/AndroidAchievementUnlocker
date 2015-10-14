package com.kluehspies.marian.example.trigger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class Dialog extends Trigger {

    private final AlertDialog dialog;

    public Dialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Dialog");
        builder.setMessage("Press unlock");
        builder.setPositiveButton(
                "Unlock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unlockSucceeded();
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });
        builder.setNegativeButton(
                "Don´t Unlock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unlockFailed();
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });
        dialog = builder.create();
    }

    public Dialog show() {
        dialog.show();
        return this;
    }

}
