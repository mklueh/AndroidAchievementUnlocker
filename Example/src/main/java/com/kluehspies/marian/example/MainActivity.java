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
import com.kluehspies.marian.unlockmanager.persistence.Database;
import com.kluehspies.marian.unlockmanager.persistence.DbFactory;
import com.kluehspies.marian.unlockmanager.persistence.TableParams;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
import com.kluehspies.marian.unlockmanager.trigger.SharedPreferencesHandler;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

public class MainActivity extends AppCompatActivity {

    private AndroidAchievementUnlocker unlocker;

    private UserAchievement createUserAchievement(String key, String userName){
        UserAchievement achievement = new UserAchievement(key);
        achievement.setUser(userName);
        return achievement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TableParams tableParams = new TableParams.Builder()
                .setTableName("example_achievements")
                .addCustomColumn(UserAchievementDataSource.COLUMN_USER)
                .build();

        final Database database = DbFactory.create(getApplicationContext(), tableParams);

        UserAchievementDataSource dataSource = new UserAchievementDataSource(database, tableParams);

        dataSource.openDatabase();
        dataSource.add(createUserAchievement("1", "User-A"), UserAchievementDataSource.STATE_LOCKED);
        dataSource.add(createUserAchievement("2", "User-B"), UserAchievementDataSource.STATE_LOCKED);
        dataSource.add(createUserAchievement("3", "User-C"), UserAchievementDataSource.STATE_LOCKED);
        dataSource.add(createUserAchievement("4", "User-D"), UserAchievementDataSource.STATE_LOCKED);
        dataSource.closeDatabase();

        unlocker = AndroidAchievementUnlocker.getDefault();

        unlocker.addPersistenceHandler(new SharedPreferencesHandler<>(Integer.class, getApplicationContext(), "integer_key"));
        unlocker.addPersistenceHandler(new SharedPreferencesHandler<>(String.class, getApplicationContext(), "string_key"));
        unlocker.addPersistenceHandler(dataSource);

        ///////////////////////////////////Listeners///////////////////////////////////////

        //The listener will be called, if resources 1, 2, 3 or 4 are touched
        unlocker.bindListener(
                new IntRewardListener() {
                    @Override
                    public void unlockSucceeded(Integer item, Trigger<Integer> trigger) {
                        Toast.makeText(MainActivity.this, String.format("Unlock succeeded for: Integer %s and triggered by: %s", item, trigger.getName()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void unlockFailed(Integer item, Trigger<Integer> trigger) {
                        Toast.makeText(MainActivity.this, String.format("Unlock failed for: Integer %s and triggered by: %s", item, trigger.getName()), Toast.LENGTH_SHORT).show();
                    }
                }, 1, 2, 3, 4);

        unlocker.bindListener(
                new StringRewardListener() {
                    @Override
                    public void unlockSucceeded(String item, Trigger<String> trigger) {
                        Toast.makeText(MainActivity.this, String.format("Unlock succeeded for: String %s and triggered by: %s", item, trigger.getName()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void unlockFailed(String item, Trigger<String> trigger) {
                        Toast.makeText(MainActivity.this, String.format("Unlock failed for: String %s and triggered by: %s", item, trigger.getName()), Toast.LENGTH_SHORT).show();
                    }
                }, "a", "b", "c");


        unlocker.bindListener(new RewardListener<UserAchievement>() {
            @Override
            public void unlockSucceeded(UserAchievement item, Trigger<UserAchievement> trigger) {
                Toast.makeText(MainActivity.this, String.format("Unlock succeeded for: Achievement with key: %s and triggered by: %s and user is: %s", item.getKey(), trigger.getName(), item.getUser()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unlockFailed(UserAchievement item, Trigger<UserAchievement> trigger) {
                Toast.makeText(MainActivity.this, String.format("Unlock failed for: Achievement with key: %s and triggered by: %s and user is: %s", item.getKey(), trigger.getName(), item.getUser()), Toast.LENGTH_SHORT).show();
            }
        }, UserAchievement.with("1"), UserAchievement.with("2"), UserAchievement.with("3"));

        //Listeners get notified if an unlock succeeds/fails
        unlocker.bindListener(new PushNotifier<Integer>(this), 4);
        unlocker.bindListener(new ToastNotifier<Integer>(this), 3);
        unlocker.bindListener(new PushNotifier<String>(this), "b");
        unlocker.bindListener(new ToastNotifier<String>(this), "c");
        unlocker.bindListener(new PushNotifier<UserAchievement>(this), UserAchievement.with("4"));

        LinearLayout parent = (LinearLayout) findViewById(R.id.parent);


        ///////////////////////////////////Triggers///////////////////////////////////////

        //RewardView gets notified if 1,2,3 or 4 are unlocked
        Trigger<Integer> rewardViewIntegerTrigger = new Trigger<>(Integer.class, "Integer-RewardView-Trigger");
        unlocker.bindTrigger(rewardViewIntegerTrigger, 1, 2, 3, 4);
        RewardView integerUnlockView = new RewardView(this, rewardViewIntegerTrigger);
        parent.addView(integerUnlockView);

        //RewardView gets notified if a,b or c are unlocked
        Trigger<String> rewardViewStringTrigger = new Trigger<>(String.class, "String-RewardView-Trigger");
        unlocker.bindTrigger(rewardViewStringTrigger, "a", "b", "c", "d");
        RewardView stringUnlockView = new RewardView(this, rewardViewStringTrigger);

        Trigger<UserAchievement> achievementTrigger = new Trigger<>(UserAchievement.class, "Achievement-RewardView-Trigger");
        unlocker.bindTrigger(achievementTrigger, UserAchievement.with("3"), UserAchievement.with("4"));
        RewardView achievementUnlockView = new RewardView(this, achievementTrigger);

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

        parent.addView(stringUnlockView);
        parent.addView(achievementUnlockView);
        parent.addView(clearPrefs);

        //Dialog can unlock resource 1
        Trigger<Integer> trigger = new Trigger<>(Integer.class, "Integer-Trigger");
        unlocker.bindTrigger(trigger, 1);
        new Dialog(this, trigger).show();

        unlocker.triggerCurrentUnlockState(1);

        unlocker.triggerUnlock(UserAchievement.with("1"), UserAchievement.with("2"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unlocker.unbindAll(Integer.class, String.class);
    }

}
