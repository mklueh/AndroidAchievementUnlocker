package com.kluehspies.marian.example.notifier;

import android.content.Context;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.ITrigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class ToastNotifier implements RewardListener {
    private Context context;

    public ToastNotifier(Context context) {
        this.context = context;
    }

    @Override
    public void rewardNotAvailable(int resourceID, ITrigger trigger) {
        showToast("Unlock Not Available: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void rewardAvailable(int resourceID, ITrigger trigger) {
        showToast("Unlock Available: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockSucceeded(int resourceID, ITrigger trigger) {
        showToast("Unlock Succeeded: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockFailed(int resourceID, ITrigger trigger) {
        showToast("Unlock Failed: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
