package com.kluehspies.marian.example.notifier;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 14.10.2015.
 */
public class PushNotifier<M> implements RewardListener<M> {

    private final Context context;

    public PushNotifier(Context context) {
        this.context = context;
    }

    @Override
    public void unlockSucceeded(M item, Trigger<M> trigger) {
        showNotification("Unlock succeeded " + trigger.getClass().getSimpleName());
    }

    @Override
    public void unlockFailed(M item, Trigger<M> trigger) {
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
