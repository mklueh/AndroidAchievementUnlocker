package com.kluehspies.marian.unlockmanager.persistence;

/**
 * Created by Andreas Schattney on 05.01.2016.
 */
public interface Achievement {
    String getKey();
    String getAction();
    String getState();
    void setKey(String key);
    void setAction(String action);
    void setState(String state);
}
