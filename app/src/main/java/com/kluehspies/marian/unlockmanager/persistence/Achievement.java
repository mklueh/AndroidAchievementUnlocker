package com.kluehspies.marian.unlockmanager.persistence;

/**
 * Created by Andreas Schattney on 05.01.2016.
 */
public interface Achievement {
    String getKey();
    String getState();
    void setKey(String key);
    void setState(String state);
    void setTriggeredFrom(String triggerName);
    String getTriggeredFrom();
    int hashCode();
    boolean equals(Object o);
}
