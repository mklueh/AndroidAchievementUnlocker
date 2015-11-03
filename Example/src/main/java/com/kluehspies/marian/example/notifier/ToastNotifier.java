package com.kluehspies.marian.example.notifier;

import android.content.Context;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class ToastNotifier<M> implements RewardListener<M> {

    private Context context;

    public ToastNotifier(Context context) {
        this.context = context;
    }

    @Override
    public void unlockSucceeded(M item, Trigger<M> trigger) {
        showToast("Unlock Succeeded: " + item + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockFailed(M item, Trigger<M> trigger) {
        showToast("Unlock Failed: " + item + " - " + trigger.getClass().getSimpleName());
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
