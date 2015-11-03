package com.kluehspies.marian.unlockmanager.trigger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian Klühspies on 21.01.2015.
 *
 * Default implementation of PersistenceHandler
 */

public class SharedPreferencesHandler<M> extends PersistenceHandler<M> {

    private String sharedPreferencesKey;
    private SharedPreferences preferences;

    public SharedPreferencesHandler(Class clazz,Context context, String sharedPreferencesKey) {
        super(clazz);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.sharedPreferencesKey = sharedPreferencesKey;
    }

    @Override
    public void unlockFailed(M item, Trigger<M> trigger) {
        setUnlockState(item, false);
    }

    public void unlockSucceeded(M item, Trigger<M> trigger) {
        setUnlockState(item, true);
    }


    private void setUnlockState(M item, boolean state) {
        preferences.edit().putBoolean(sharedPreferencesKey + "_" + item, state).apply();
    }

    /**
     * Let SharedPreferencesHandler handle the unlock for you if available
     *
     * @param item
     */
    @Override
    public void triggerUnlockIfAvailable(M item) {
        if (!isUnlocked(item))
            AndroidAchievementUnlocker.getDefault().unlockFailed(this);
        else
            AndroidAchievementUnlocker.getDefault().unlockSucceeded(this);
    }

    /**
     * Request unlock state
     *
     * @param item
     * @return
     */
    @Override
    public boolean isUnlocked(M item) {
        return preferences.getBoolean(sharedPreferencesKey + "_" + item.toString(), false);
    }

}
