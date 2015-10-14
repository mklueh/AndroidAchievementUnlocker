package com.kluehspies.marian.example.trigger;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.trigger.ITrigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class View extends TextView implements ITrigger, View.OnClickListener, View.OnLongClickListener {

    private IRewardManager manager;

    public View(Context context) {
        super(context);
        setOnClickListener(this);
        setText("Long-Press to unlock");
    }

    @Override
    public void setUnlockManager(IRewardManager unlockManager) {
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
