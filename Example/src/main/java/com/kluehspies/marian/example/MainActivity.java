package com.kluehspies.marian.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.kluehspies.marian.example.notifier.PushNotifier;
import com.kluehspies.marian.example.notifier.ToastNotifier;
import com.kluehspies.marian.example.trigger.Dialog;
import com.kluehspies.marian.example.trigger.RewardView;
import com.kluehspies.marian.unlockmanager.listener.IntRewardListener;
import com.kluehspies.marian.unlockmanager.listener.StringRewardListener;
import com.kluehspies.marian.unlockmanager.persistence.Achievement;
import com.kluehspies.marian.unlockmanager.persistence.AchievementImpl;
import com.kluehspies.marian.unlockmanager.persistence.Database;
import com.kluehspies.marian.unlockmanager.persistence.DbFactory;
import com.kluehspies.marian.unlockmanager.persistence.TableParams;
import com.kluehspies.marian.unlockmanager.persistence.UnlockDataSource;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
import com.kluehspies.marian.unlockmanager.trigger.SharedPreferencesHandler;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

public class MainActivity extends AppCompatActivity {

    private AndroidAchievementUnlocker unlocker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableParams tableParams = new TableParams.Builder().setTableName("example_resource_table").build();
        Database database = DbFactory.create(getApplicationContext(), tableParams);

        UnlockDataSource<Achievement> dataSource = new UnlockDataSource<Achievement>(Achievement.class, database, tableParams) {
            @Override
            protected Achievement createNewDataModelInstance() {
                return new AchievementImpl();
            }
        };

        unlocker = AndroidAchievementUnlocker.getDefault();

        unlocker.addPersistenceHandler(new SharedPreferencesHandler<>(Integer.class, getApplicationContext(), "integer_key"));
        unlocker.addPersistenceHandler(new SharedPreferencesHandler<>(String.class, getApplicationContext(), "string_key"));
        unlocker.addPersistenceHandler(dataSource);

        SnackbarManager.getInstance().resume();

        ///////////////////////////////////Listeners///////////////////////////////////////

        //The listener will be called, if resources 1, 2, 3 or 4 are touched
        unlocker.bindListener(
                new IntRewardListener() {
                    @Override
                    public void unlockSucceeded(Integer item, Trigger<Integer> trigger) {
                        SnackbarManager.getInstance().makeTextOnly(
                                findViewById(R.id.parent),
                                String.format("Unlock succeeded for: %s", item));
                    }

                    @Override
                    public void unlockFailed(Integer item, Trigger<Integer> trigger) {

                    }
                }, 1, 2, 3, 4);

        unlocker.bindListener(
                new StringRewardListener() {
                    @Override
                    public void unlockSucceeded(String item, Trigger<String> trigger) {
                        SnackbarManager.getInstance().makeTextOnly(
                                findViewById(R.id.parent),
                                String.format("Unlock succeeded for: %s", item));
                    }

                    @Override
                    public void unlockFailed(String item, Trigger<String> trigger) {

                    }
                }, "a", "b", "c");


        //Listeners get notified if an unlock succeeds/fails
        unlocker.bindListener(new PushNotifier(this), 4);
        unlocker.bindListener(new ToastNotifier(this), 3);
        unlocker.bindListener(new PushNotifier(this), "b");
        unlocker.bindListener(new ToastNotifier(this), "c");


        ///////////////////////////////////Triggers///////////////////////////////////////
        //Dialog can unlock resource 1
        Trigger<Integer> trigger = new Trigger<>(Integer.class);
        unlocker.bindTrigger(trigger, 1);

        new Dialog(this, trigger).show();

        LinearLayout parent = (LinearLayout) findViewById(R.id.parent);

        //RewardView gets notified if 1,2,3 or 4 are unlocked
        Trigger<Integer> rewardViewIntegerTrigger = new Trigger<>(Integer.class);
        unlocker.bindTrigger(rewardViewIntegerTrigger, 1, 2, 3, 4);
        RewardView integerUnlockView = new RewardView(this, rewardViewIntegerTrigger);
        parent.addView(integerUnlockView);

        //RewardView gets notified if a,b or c are unlocked
        Trigger<String> rewardViewStringTrigger = new Trigger<>(String.class);
        unlocker.bindTrigger(rewardViewStringTrigger, "a", "b", "c", "d");
        RewardView stringUnlockView = new RewardView(this, rewardViewIntegerTrigger);
        parent.addView(stringUnlockView);

        unlocker.triggerCurrentUnlockState(1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unlocker.unbindAll(Integer.class, String.class);
    }

}
