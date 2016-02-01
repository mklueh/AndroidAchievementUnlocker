package com.kluehspies.marian.unlockmanager.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreas Schattney on 02.10.2015.
 */
public class TableParams {

    private TableParams(String tableName, List<String> customColumns){
        this.tableName = tableName;
        buildTableParams(customColumns);
        customColumns.clear();
    }

    private final String tableName;

    /**
     * identifier for the unlock event
     */
    public final String columnKey = "unique_key";

    /**
     * State of the Event
     */
    public final String columnUnlockState = "unlock_state";

    public final String columnTriggeredFrom = "triggered_from";

    /**
     * Timestamp event added
     */
    public final String columnTimestamp = "created_at";

    public void buildTableParams(List<String> customColumns){

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName + "(")
                .append(columnKey + " TEXT NOT NULL, ")
                .append(columnUnlockState + " TEXT NOT NULL, ")
                .append(columnTriggeredFrom + " TEXT, ")
                .append(columnTimestamp + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, ");

        for (int i = 0; i < customColumns.size(); i++) {
            String columnName = customColumns.get(i);
            sb.append(columnName).append(" TEXT, ");
        }

        sb.append(String.format("PRIMARY KEY (%s)", columnKey));
        sb.append(");");

        TABLE_CREATE = sb.toString();
        TABLE_DROP = "DROP TABLE IF EXISTS " + tableName;
    }

    public String TABLE_CREATE = ""; /*"CREATE TABLE IF NOT EXISTS "
            + tableName + "("
            + columnKey + " TEXT NOT NULL, "
            + columnUnlockState + " TEXT NOT NULL, "
            + columnTriggeredFrom + " TEXT NOT NULL, "
            + columnTimestamp + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + String.format("PRIMARY KEY (%s)", columnKey)
            + ");";*/

    public String TABLE_DROP = "";

    public String getTableName() {
        return tableName;
    }

    public static class Builder {

        public static final String DEFAULT_TABLE_NAME = "resource";

        private String tableName = DEFAULT_TABLE_NAME;

        private final List<String> customColumns = new ArrayList<>();

        public Builder() { }

        public Builder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder addCustomColumn(String columnName) {
            customColumns.add(columnName);
            return this;
        }

        public TableParams build() {
            return new TableParams(tableName, customColumns);
        }

    }
}
