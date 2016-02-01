package com.kluehspies.marian.example;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kluehspies.marian.example.notifier.PushNotifier;
import com.kluehspies.marian.example.notifier.ToastNotifier;
import com.kluehspies.marian.example.trigger.Dialog;
import com.kluehspies.marian.example.trigger.RewardView;
import com.kluehspies.marian.unlockmanager.listener.IntRewardListener;
import com.kluehspies.marian.unlockmanager.listener.RewardListener;
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

    private AchievementImpl createAchievement(String key){
        AchievementImpl achievement = new AchievementImpl();
        achievement.setKey(key);
        return achievement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TableParams tableParams = new TableParams.Builder().setTableName("example_resource_table").build();
        final Database database = DbFactory.create(getApplicationContext(), tableParams);

        UnlockDataSource<AchievementImpl> dataSource = new UnlockDataSource<AchievementImpl>(AchievementImpl.class, database, tableParams) {
            @Override
            protected AchievementImpl createNewDataModelInstance() {
                return new AchievementImpl();
            }
        };

        dataSource.openDatabase();
        dataSource.add(createAchievement("1"), UnlockDataSource.STATE_LOCKED);
        dataSource.add(createAchievement("2"), UnlockDataSource.STATE_LOCKED);
        dataSource.add(createAchievement("3"), UnlockDataSource.STATE_LOCKED);
        dataSource.add(createAchievement("4"), UnlockDataSource.STATE_LOCKED);
        dataSource.closeDatabase();

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


        unlocker.bindListener(new RewardListener<AchievementImpl>() {
            @Override
            public void unlockSucceeded(AchievementImpl item, Trigger<AchievementImpl> trigger) {
                SnackbarManager.getInstance().makeTextOnly(
                        findViewById(R.id.parent),
                        String.format("Unlock succeeded for: Achievement with key: %s and triggered by: %s", item.getKey(), trigger.getName()));
            }

            @Override
            public void unlockFailed(AchievementImpl item, Trigger<AchievementImpl> trigger) {
                SnackbarManager.getInstance().makeTextOnly(
                        findViewById(R.id.parent),
                        String.format("Unlock failed for: Achievement with key: %s", item.getKey()));
            }
        }, new AchievementImpl("1"), new AchievementImpl("2"));

        unlocker.bindListener(new RewardListener<AchievementImpl>() {
            @Override
            public void unlockSucceeded(AchievementImpl item, Trigger<AchievementImpl> trigger) {
                Toast.makeText(MainActivity.this,String.format("Unlock succeeded for: Achievement with key: %s and triggered by: %s", item.getKey(), trigger.getName()),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unlockFailed(AchievementImpl item, Trigger<AchievementImpl> trigger) {
                Toast.makeText(MainActivity.this,String.format("Unlock failed for: Achievement with key: %s and triggered by: %s", item.getKey(), trigger.getName()),Toast.LENGTH_SHORT).show();
            }
        }, new AchievementImpl("3"));

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


        Button clearPrefs = new Button(this);
        clearPrefs.setText("Clear Saved Data");
        clearPrefs.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();
                        database.dropTableIfExists(tableParams);
                        Toast.makeText(getApplicationContext(), "Data cleared", Toast.LENGTH_SHORT).show();
                    }
                });


        //RewardView gets notified if a,b or c are unlocked
        Trigger<String> rewardViewStringTrigger = new Trigger<>(String.class);
        unlocker.bindTrigger(rewardViewStringTrigger, "a", "b", "c", "d");
        RewardView stringUnlockView = new RewardView(this, rewardViewStringTrigger);

        Trigger<AchievementImpl> achievementTrigger = new Trigger<>(AchievementImpl.class, "Achievement-RewardView");
        unlocker.bindTrigger(achievementTrigger, new AchievementImpl("3"));
        RewardView achievementUnlockView = new RewardView(this, achievementTrigger);

        parent.addView(stringUnlockView);
        parent.addView(achievementUnlockView);
        parent.addView(clearPrefs);

        unlocker.triggerCurrentUnlockState(1);

        unlocker.triggerUnlock(new AchievementImpl("1"), new AchievementImpl("2"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unlocker.unbindAll(Integer.class, String.class);
    }

}
