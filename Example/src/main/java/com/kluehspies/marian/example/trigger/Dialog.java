package com.kluehspies.marian.example.trigger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class Dialog {

    private final Context context;
    private final Trigger trigger;

    public Dialog(Context context,Trigger trigger) {
        this.context = context;
        this.trigger = trigger;
    }

    public Dialog show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Dialog");
        builder.setMessage("Press unlock");
        builder.setPositiveButton(
                "Unlock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trigger.unlockSucceeded();
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });
        builder.setNegativeButton(
                "Don´t Unlock", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        trigger.unlockFailed();
                        if (dialog != null)
                            dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        return this;
    }

}
