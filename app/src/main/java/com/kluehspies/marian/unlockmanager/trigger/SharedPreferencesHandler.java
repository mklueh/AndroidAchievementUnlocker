package com.kluehspies.marian.unlockmanager.trigger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kluehspies.marian.unlockmanager.persistence.PersistenceHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Marian Klühspies on 21.01.2015.
 *
 * Default implementation of PersistenceHandler
 */
public class SharedPreferencesHandler<M> extends PersistenceHandler<M> {

    private static final String CLASS_TYPE_ERROR = "Only reference types which have corresponding primitive type (String) are " +
            "allowed to use with this class, all other Object types have to be used with UnlockDataSource";

    private static List<Class> primitiveDataTypesAsReferenceType = Arrays.asList(new Class[]{
                    Byte.class,
                    Short.class,
                    Integer.class,
                    Float.class,
                    Double.class,
                    Long.class,
                    Character.class,
                    Boolean.class,
                    String.class
            }
    );

    private String sharedPreferencesKey;
    private SharedPreferences preferences;

    public SharedPreferencesHandler(Class<M> clazz, Context context, String sharedPreferencesKey) {
        super(clazz);
        if (!primitiveDataTypesAsReferenceType.contains(clazz))
            throw new RuntimeException(CLASS_TYPE_ERROR);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.sharedPreferencesKey = sharedPreferencesKey;
    }

    @Override
    public M unlock(M item, String triggerName) {
        setUnlockState(item, true);
        setTriggeredFrom(item, triggerName);
        return item;
    }

    @Override
    public M lock(M item, String triggerName) {
        setUnlockState(item, false);
        setTriggeredFrom(item, triggerName);
        return item;
    }

    @Override
    public String getItemTriggeredFrom(M item) {
        return getTriggeredFrom(item);
    }

    @Override
    public M get(M item) {
        return item;
    }

    private String getTriggeredFrom(M item) {
        return preferences.getString(sharedPreferencesKey + item.toString() +  "_trigger", null);
    }

    private void setTriggeredFrom(M item, String triggerName) {
        preferences.edit().putString(sharedPreferencesKey + item.toString() + "_trigger", triggerName).apply();
    }

    private void setUnlockState(M item, boolean state) {
        preferences.edit().putBoolean(sharedPreferencesKey + "_" + item, state).apply();
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
