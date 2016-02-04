package com.kluehspies.marian.unlockmanager.persistence;

/**
 * Created by Andreas Schattney on 05.01.2016.
 */
public abstract class Achievement {

    private String key;
    private String state;
    private String triggeredFrom;

    public Achievement(){}

    protected Achievement(String key){ this.key = key;}

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Achievement && key.equals(((Achievement) o).key);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTriggeredFrom() {
        return triggeredFrom;
    }

    public void setTriggeredFrom(String triggeredFrom) {
        this.triggeredFrom = triggeredFrom;
    }

    @Override
    public String toString() {
        return key;
    }
}
