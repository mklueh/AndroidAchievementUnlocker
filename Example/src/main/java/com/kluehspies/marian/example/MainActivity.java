package com.kluehspies.marian.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.kluehspies.marian.example.notifier.PushNotifier;
import com.kluehspies.marian.example.notifier.ToastNotifier;
import com.kluehspies.marian.example.trigger.Dialog;
import com.kluehspies.marian.example.trigger.RewardView;
import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

public class MainActivity extends AppCompatActivity implements RewardListener<Integer> {

    private RewardManager<Integer> rewardManager;
    private LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rewardManager = new RewardManager<>();

        //The listener will be called, if resources 1, 2, 3 or 4 are touched
        rewardManager.bindListener(this, 1, 2, 3, 4);

        Trigger trigger = new Trigger(rewardManager);

        //Dialog can unlock resource 1
        new Dialog(this,trigger).show();
        rewardManager.bindTrigger(trigger, 1);

        RewardView unlockView = new RewardView(this,trigger);
        rewardManager.bindTrigger(trigger, 1, 2);
        parent.addView(unlockView);

        rewardManager.bindListener(new PushNotifier(this), 4);
        rewardManager.bindListener(new ToastNotifier(this), 3);
    }

    @Override
    public void rewardNotAvailable(Integer resourceID, Trigger trigger) {

    }

    @Override
    public void rewardAvailable(Integer resourceID, Trigger trigger) {

    }

    @Override
    public void unlockSucceeded(Integer resourceID, Trigger trigger) {

    }

    @Override
    public void unlockFailed(Integer resourceID, Trigger trigger) {

    }
}
