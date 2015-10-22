package com.kluehspies.marian.unlockmanager.trigger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.kluehspies.marian.unlockmanager.manager.IRewardManager;
import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;
import com.kluehspies.marian.unlockmanager.trigger.AndroidAchievementUnlocker;
import com.kluehspies.marian.unlockmanager.trigger.Trigger;

/**
 * Created by Marian on 21.01.2015.
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
    public void rewardNotAvailable(M resourceID, Trigger<M> trigger) {

    }

    @Override
    public void rewardAvailable(M resourceID, Trigger<M> trigger) {

    }

    @Override
    public void unlockFailed(M resourceID, Trigger<M> trigger) {
        setUnlockState(resourceID, false);
    }

    public void unlockSucceeded(M resourceID, Trigger<M> trigger) {
        setUnlockState(resourceID, true);
    }


    private void setUnlockState(M resourceID, boolean state) {
        preferences.edit().putBoolean(sharedPreferencesKey + "_" + resourceID, state).apply();
    }

    /**
     * Let SharedPreferencesHandler handle the unlock for you if available
     *
     * @param resourceID
     */
    @Override
    public void triggerUnlockIfAvailable(M resourceID) {
        if (!isUnlocked(resourceID))
            AndroidAchievementUnlocker.unlockFailed(this);
        else
            AndroidAchievementUnlocker.unlockSucceeded(this);
    }

    /**
     * Request unlock state
     *
     * @param resourceID
     * @return
     */
    @Override
    public boolean isUnlocked(M resourceID) {
        return preferences.getBoolean(sharedPreferencesKey + "_" + resourceID.toString(), false);
    }

}
