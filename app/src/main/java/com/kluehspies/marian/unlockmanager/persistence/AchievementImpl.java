package com.kluehspies.marian.unlockmanager.persistence;

/**
 * Created by Andreas Schattney on 16.10.2015.
 */
public class AchievementImpl implements Achievement {

    private String key;
    private String action;
    private String state;

    public AchievementImpl(){}

    @Override
    public int hashCode() {
        return key.hashCode() + action.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Achievement && key.equals(((AchievementImpl) o).key) && action.equals(((AchievementImpl) o).action);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getAction() {
        return action;
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
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

}
