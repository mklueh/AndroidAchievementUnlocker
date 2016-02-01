package com.kluehspies.marian.example.trigger;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.persistence.AchievementImpl;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class RewardView extends TextView implements View.OnLongClickListener {

    private final Trigger trigger;

    public RewardView(Context context,Trigger trigger) {
        super(context);
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        setPadding(10, 10, 10, 10);
        setBackgroundColor(Color.parseColor("#eeeeee"));
        setGravity(Gravity.CENTER);
        this.trigger = trigger;
        setOnLongClickListener(this);
        String text;
        if (trigger.forClass().equals(AchievementImpl.class))
            text = "Achievements";
        else
            text = trigger.forClass().equals(String.class) ? "Strings" : "Integers";

        setText("Long-Press to unlock " + text);
    }

    @Override
    public boolean onLongClick(View v) {
        trigger.unlockSucceeded();
        return false;
    }

}
