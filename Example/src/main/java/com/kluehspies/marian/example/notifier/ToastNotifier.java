package com.kluehspies.marian.example.notifier;

import android.content.Context;
import android.widget.Toast;

import com.kluehspies.marian.unlockmanager.listener.UnlockListener;
import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class ToastNotifier implements UnlockListener {
    private Context context;

    public ToastNotifier(Context context) {
        this.context = context;
    }

    @Override
    public void unlockNotAvailable(int resourceID, IUnlockTrigger trigger) {
        showToast("Unlock Not Available: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockAvailable(int resourceID, IUnlockTrigger trigger) {
        showToast("Unlock Available: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockSucceeded(int resourceID, IUnlockTrigger trigger) {
        showToast("Unlock Succeeded: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockFailed(int resourceID, IUnlockTrigger trigger) {
        showToast("Unlock Failed: " + resourceID + " - " + trigger.getClass().getSimpleName());
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
