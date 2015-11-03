package com.kluehspies.marian.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.kluehspies.marian.example.notifier.PushNotifier;
import com.kluehspies.marian.example.notifier.ToastNotifier;
import com.kluehspies.marian.example.trigger.Dialog;
import com.kluehspies.marian.example.trigger.RewardView;
import com.kluehspies.marian.unlockmanager.listener.RewardListener;
import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
import com.kluehspies.marian.unlockmanager.trigger.SharedPreferencesHandler;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

public class MainActivity extends AppCompatActivity {

    private static class IntegerRewardListener implements RewardListener<Integer>{

        private final View parentView;

        public IntegerRewardListener(View parentView){
            this.parentView = parentView;
        }

        @Override
        public void unlockSucceeded(Integer resourceID, Trigger trigger) {
            SnackbarManager.getInstance().makeTextOnly(parentView, String.format("Unlock succeeded for: %s", resourceID));
        }

        @Override
        public void unlockFailed(Integer resourceID, Trigger trigger) {

        }

    }
    private static class StringRewardListener implements RewardListener<String>{

        private final View parentView;

        public StringRewardListener(View parentView){
            this.parentView = parentView;
        }

        @Override
        public void unlockSucceeded(String resourceID, Trigger<String> trigger) {
            SnackbarManager.getInstance().makeTextOnly(parentView, String.format("Unlock succeeded for: %s", resourceID));
        }

        @Override
        public void unlockFailed(String resourceID, Trigger<String> trigger) {

        }

    }

    private AndroidAchievementUnlocker unlocker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unlocker = AndroidAchievementUnlocker.getDefault();

        PersistenceHandler<Integer> persistenceHandler = new SharedPreferencesHandler<>(Integer.class,getApplicationContext(),"integer_key");
        unlocker.bindPersistenceHandler(persistenceHandler);

        SnackbarManager.getInstance().resume();

        //The listener will be called, if resources 1, 2, 3 or 4 are touched
        unlocker.bindListener(new IntegerRewardListener(findViewById(R.id.parent)), 1, 2, 3, 4);
        unlocker.bindListener(new StringRewardListener(findViewById(R.id.parent)), "a", "b", "c");

        RewardView integerUnlockView = new RewardView(this,new Trigger<>(Integer.class,1,2,3,4));
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent);
        parent.addView(integerUnlockView);

        RewardView stringUnlockView = new RewardView(this,new Trigger<>(String.class,"a","b","c"));
        parent.addView(stringUnlockView);

        unlocker.bindListener(new PushNotifier(this), 4);
        unlocker.bindListener(new ToastNotifier(this), 3);
        unlocker.bindListener(new PushNotifier(this), "b");
        unlocker.bindListener(new ToastNotifier(this), "c");

        //Dialog can unlock resource 1
        new Dialog(this,new Trigger<>(Integer.class,1)).show();

        unlocker.triggerUnlockIfAvailable(1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unlocker.unbindAll(Integer.class, String.class);
    }

}
