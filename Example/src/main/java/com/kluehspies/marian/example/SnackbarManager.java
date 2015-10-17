package com.kluehspies.marian.example;


import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by Andy on 18.09.2015.
 */
public class SnackbarManager extends Snackbar.Callback implements Runnable {

    private static SnackbarManager snackbarManager;

    private LinkedList<Snackbar> snackbars = new LinkedList<>();
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isSnackbarShowing = false;

    public static synchronized SnackbarManager getInstance(){
        if (snackbarManager == null){
            snackbarManager = new SnackbarManager();
        }
        return snackbarManager;
    }

    public void makeTextOnly(View parent,String message){
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(Color.parseColor("#000000"));
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.parseColor("#eeeeee"));
        snackbar.setCallback(this);
        addSnackbar(snackbar);
    }

    private void addSnackbar(Snackbar snackbar) {
        snackbars.addLast(snackbar);
        if (!isSnackbarShowing) {
            isSnackbarShowing = true;
            snackbar.show();
        }
    }

    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        super.onDismissed(snackbar, event);
        nextSnackbar();
    }

    private void nextSnackbar() {
        if (snackbars.size() > 0)
            snackbars.pop().setCallback(null);
        if (snackbars.size() > 0) {
            handler.postDelayed(this, 1000);
        }else{
            isSnackbarShowing = false;
        }
    }

    @Override
    public void run() {
        snackbars.pop().show();
    }
}
