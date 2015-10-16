package com.kluehspies.marian.example.trigger;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class RewardView extends TextView implements View.OnClickListener, View.OnLongClickListener {

    private final Trigger trigger;

    public RewardView(Context context,Trigger trigger) {
        super(context);
        this.trigger = trigger;
        setOnClickListener(this);
        setText("Long-Press to unlock");
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "Long-Press to unlock", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        trigger.unlockSucceeded();
        return false;
    }

}
