package com.kluehspies.marian.example.notifier;

import android.content.Context;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class ToastNotifier implements RewardListener<Integer> {

    private Context context;

    public ToastNotifier(Context context) {
        this.context = context;
    }

    @Override
    public void rewardNotAvailable(Integer resourceID, Trigger trigger) {
        showToast("Unlock Not Available: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void rewardAvailable(Integer resourceID, Trigger trigger) {
        showToast("Unlock Available: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockSucceeded(Integer resourceID, Trigger trigger) {
        showToast("Unlock Succeeded: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockFailed(Integer resourceID, Trigger trigger) {
        showToast("Unlock Failed: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
