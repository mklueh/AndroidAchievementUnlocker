package com.kluehspies.marian.example.notifier;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.kluehspies.marian.example.R;
import com.kluehspies.marian.unlockmanager.listener.UnlockListener;
import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class PushNotifier implements UnlockListener {

    private Context context;

    public PushNotifier(Context context) {
        this.context = context;
    }

    @Override
    public void unlockNotAvailable(int resourceID, IUnlockTrigger trigger) {
        showNotification("Unlock not available " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockAvailable(int resourceID, IUnlockTrigger trigger) {
        showNotification("Unlock available " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockSucceeded(int resourceID, IUnlockTrigger trigger) {
        showNotification("Unlock succeeded " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockFailed(int resourceID, IUnlockTrigger trigger) {
        showNotification("Unlock failed " + trigger.getClass().getSimpleName());
    }

    private void showNotification(String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
        builder.setAutoCancel(true);
        builder.setContentTitle(text);
        builder.build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build());
    }
}
