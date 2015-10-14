package com.kluehspies.marian.unlockmanager.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kluehspies.marian.unlockmanager.listener.SimpleUnlockListener;
import com.kluehspies.marian.unlockmanager.manager.IUnlockManager;
import com.kluehspies.marian.unlockmanager.trigger.IUnlockTrigger;

/**
 * Created by Marian on 21.01.2015.
 *
 * Default implementation of PersistenceHandler
 */
public class SharedPreferencesHandler extends SimpleUnlockListener implements IUnlockTrigger {

    private Context context;
    private IUnlockManager unlockManager;
    private String sharedPreferencesKey;
    private SharedPreferences preferences;

    public SharedPreferencesHandler(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferencesHandler(Context context, String sharedPreferencesKey) {
        this(context);
        this.sharedPreferencesKey = sharedPreferencesKey;
    }

    @Override
    public void unlockSucceeded(int resourceID, IUnlockTrigger trigger) {
        setUnlockState(resourceID, true);
    }


    private void setUnlockState(int resourceID, boolean state) {
        preferences.edit().putBoolean(sharedPreferencesKey + "_" + resourceID, state).apply();
    }

    /**
     * Let SharedPreferencesHandler handle the unlock for you if available
     *
     * @param resourceID
     */
    public void triggerUnlockIfAvailable(int resourceID) {
        if (isUnlocked(resourceID))
            unlockManager.unlockFailed(this);
        else unlockManager.unlockSucceeded(this);
    }

    /**
     * Request unlock state
     *
     * @param resourceID
     * @return
     */
    public boolean isUnlocked(int resourceID) {
        return preferences.getBoolean(sharedPreferencesKey + "_" + resourceID, false);
    }

    @Override
    public void setUnlockManager(IUnlockManager unlockManager) {
        this.unlockManager = unlockManager;
    }
}
