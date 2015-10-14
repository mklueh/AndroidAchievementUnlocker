package com.kluehspies.marian.example.trigger;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.manager.IUnlockManager;
import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class UnlockView extends TextView implements IUnlockTrigger, View.OnClickListener, View.OnLongClickListener {

    private IUnlockManager manager;

    public UnlockView(Context context) {
        super(context);
        setOnClickListener(this);
        setText("Long-Press to unlock");
    }

    @Override
    public void setUnlockManager(IUnlockManager unlockManager) {
        this.manager = unlockManager;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "Long-Press to unlock", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        manager.unlockSucceeded(this);
        return false;
    }
}
