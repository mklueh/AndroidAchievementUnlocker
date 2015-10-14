package com.kluehspies.marian.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kluehspies.marian.example.notifier.PushNotifier;
import com.kluehspies.marian.example.notifier.ToastNotifier;
import com.kluehspies.marian.example.trigger.Dialog;
import com.kluehspies.marian.example.trigger.View;
import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.manager.RewardManager;
import com.kluehspies.marian.unlockmanager.trigger.ITrigger;

public class MainActivity extends AppCompatActivity implements RewardListener {

    private RewardManager unlockManager;
    private LinearLayout parent;
    private Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unlockManager = new RewardManager();

        //The listener will be called, if resources 1, 2, 3 or 4 are touched
        unlockManager.bindListener(this, 1, 2, 3, 4);

        //Dialog can unlock resource 1
        unlockManager.bindTrigger(new Dialog(this).show(), 1);

        View unlockView = new View(this);
        unlockManager.bindTrigger(unlockView, 1, 2);
        parent.addView(unlockView);

        unlockManager.bindListener(new PushNotifier(this), 4);

        unlockManager.bindListener(new ToastNotifier(this), 3);
    }


    @Override
    public void rewardNotAvailable(int resourceID, ITrigger trigger) {

    }

    @Override
    public void rewardAvailable(int resourceID, ITrigger trigger) {

    }

    @Override
    public void unlockSucceeded(int resourceID, ITrigger trigger) {

    }

    @Override
    public void unlockFailed(int resourceID, ITrigger trigger) {

    }
}
