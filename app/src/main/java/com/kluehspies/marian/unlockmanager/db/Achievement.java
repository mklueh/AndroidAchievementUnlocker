package com.kluehspies.marian.unlockmanager.db;

/**
 * Created by Andy on 16.10.2015.
 */
public class Achievement {

    private final String key;
    private final String action;
    private final String state;

    public Achievement(String key, String action){
        this(key,action,null);
    }

    public Achievement(String key,String action,String state){
        this.key = key;
        this.action = action;
        this.state = state;
    }

    public String getKey() {
        return key;
    }

    public String getAction() {
        return action;
    }

    public String getState() {
        return state;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Achievement)
            return key.equals(((Achievement) o).key);
        return false;
    }
}
