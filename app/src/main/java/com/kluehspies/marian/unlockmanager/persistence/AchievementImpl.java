package com.kluehspies.marian.unlockmanager.persistence;

/**
 * Created by Andreas Schattney on 16.10.2015.
 */
public class AchievementImpl implements Achievement {

    private String key;
    private String state;
    private String triggerName = null;

    public AchievementImpl(){}

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Achievement && key.equals(((AchievementImpl) o).key);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    @Override
    public void setTriggeredFrom(String triggerName) {
        this.triggerName = triggerName;
    }

    @Override
    public String getTriggeredFrom() {
        return triggerName;
    }

}
