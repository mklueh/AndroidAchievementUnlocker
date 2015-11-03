package com.kluehspies.marian.unlockmanager.persistence;

/**
 * Created by Andreas Schattney on 16.10.2015.
 */
public class Achievement {

    private String key;
    private String action;
    private String state;

    public Achievement(){}

    @Override
    public int hashCode() {
        return key.hashCode() + action.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof  Achievement)
            return key.equals(((Achievement) o).key) && action.equals(((Achievement) o).action);
        return false;
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

    public void setKey(String key) {
        this.key = key;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setState(String state) {
        this.state = state;
    }

}
