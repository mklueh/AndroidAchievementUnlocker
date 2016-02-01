package com.kluehspies.marian.unlockmanager.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 03.11.2015.
 */
public abstract class UnlockDataSource<T extends Achievement> extends PersistenceHandler<T>{

    public static final String STATE_UNLOCKED = "UNLOCKED";
    public static final String STATE_LOCKED = "LOCKED";

    private final Database database;
    private final TableParams params;
    private SQLiteDatabase sqLiteDatabase;

    public UnlockDataSource(Class<T> clazz, Database database, TableParams params){
        super(clazz);
        this.database = database;
        this.params = params;
    }

    /**
     * opens the database
     */
    public void openDatabase(){
        sqLiteDatabase = database.getWritableDatabase();
    }

    /**
     * add an @code{item} to the database with STATE_LOCKED.
     * @param item item
     * @return true if the item was added to the database, false otherwise
     */
    public boolean add(T item){
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        boolean result = add(item, STATE_LOCKED);
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
        return result;
    }

    /**
     * add an @code{item} to the database with STATE_LOCKED or STATE_UNLOCKED
     * @param item item
     * @return true if the item was added to the database, false otherwise
     */
    public boolean add(T item,String state){
        ContentValues cv = new ContentValues();
        cv.put(params.columnKey,item.getKey());
        cv.put(params.columnUnlockState, (state == null) ? STATE_LOCKED : state);
        onBindValues(cv, item);
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        boolean amountRowsInserted = sqLiteDatabase.insert(params.getTableName(), null, cv) > 0;
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
        return amountRowsInserted;
    }

    /**
     * updates the item with the @code{state}
     * @param item item
     * @param state state
     * @return true if item was updated, false otherwise
     */
    public boolean update(T item, String triggerName, String state){
        ContentValues cv = new ContentValues();
        cv.put(params.columnUnlockState, state);
        cv.put(params.columnTriggeredFrom, triggerName);
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        long rowsModifiedOrInserted = 0;
        if (exists(item)) {
            rowsModifiedOrInserted = sqLiteDatabase.update(
                    params.getTableName(),
                    cv,
                    String.format("%s = ?", params.columnKey),
                    new String[]{item.getKey()}
            );
        }else{
            rowsModifiedOrInserted = sqLiteDatabase.insert(params.getTableName(), null, cv);
        }
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
        return rowsModifiedOrInserted > 0;
    }

    /**
     * checks for existence of the item in the database
     * @param item item
     * @return true if the item exists, false otherwise
     */
    private boolean exists(T item) {
        Cursor cursor = sqLiteDatabase.query(
                params.getTableName(),
                null,
                String.format("%s = ?", params.columnKey),
                new String[]{item.getKey()}, null, null, null
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    /**
     * deletes an item from the database
     * @param item item
     * @return true if the item was deleted, false otherwise
     */
    public boolean remove(T item){
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        int rowsRemoved = sqLiteDatabase.delete(
                params.getTableName(),
                String.format("%s = ?", params.columnKey),
                new String[]{item.getKey()}
        );
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
        return rowsRemoved > 0;
    }

    /**
     *
     * @param item item
     * @return true if the item is @code{STATE_LOCKED}, false otherwise
     */
    public boolean isLocked(T item){
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        boolean status = getStatus(item).equals(STATE_LOCKED);
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
        return status;
    }

    private String getStatus(T item){
        String status = null;
        Cursor cursor = sqLiteDatabase.query(
                params.getTableName(),
                new String[]{params.columnUnlockState},
                String.format("%s = ?", params.columnKey),
                new String[]{item.getKey()},
                null, null, null
        );
        if (cursor.moveToFirst()){
            status = cursor.getString(0);
        }
        cursor.close();
        return status;
    }

    public List<T> get(){
        return internalGet(null);
    }

    public T get(String key){
        return internalGetByKey(key);
    }

    private T internalGetByKey(String queryKey) {

        Cursor cursor = sqLiteDatabase.query(
                params.getTableName(),
                null,
                String.format("%s = ?", params.columnKey),
                new String[]{queryKey},
                null,null,null
        );

        T item = null;
        if (cursor.moveToFirst())
            item = buildItem(cursor);
        cursor.close();
        return item;

    }

    private T buildItem(Cursor cursor) {
        String key = cursor.getString(cursor.getColumnIndex(params.columnKey));
        String triggeredFrom = cursor.getString(cursor.getColumnIndex(params.columnTriggeredFrom));
        String state = cursor.getString(cursor.getColumnIndex(params.columnUnlockState));
        T item = createNewDataModelInstance();
        item.setKey(key);
        item.setTriggeredFrom(triggeredFrom);
        item.setState(state);
        onBindModel(cursor,item);
        return item;
    }

    public List<T> getLocked(){
        return internalGet(STATE_LOCKED);
    }

    public List<T> getUnlocked(){
        return internalGet(STATE_UNLOCKED);
    }

    protected abstract T createNewDataModelInstance();

    protected void onBindValues(ContentValues contentValues,T item){

    }

    protected void onBindModel(Cursor cursor, T item){

    }

    private List<T> internalGet(String withState){
        List<T> items = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(
                params.getTableName(),
                null,
                (withState != null) ? String.format("%s = ?", params.columnUnlockState) : null,
                (withState != null) ? new String[]{withState} : null,
                null,null,null
        );
        if (cursor.moveToFirst()){
            do{
                T item = buildItem(cursor);
                items.add(item);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public void closeDatabase(){
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

    @Override
    public void unlock(T item, String triggerName) {
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        update(item, triggerName, STATE_UNLOCKED);
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
    }

    @Override
    public void lock(T item, String triggerName){
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        update(item, triggerName, STATE_LOCKED);
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
    }

    private void ensureTemporarilyOpenedDbGetsClosed(boolean temporarilyOpen) {
        if (!temporarilyOpen) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

    private boolean ensureDatabaseIsOpen() {
        boolean isAlreadyOpen = (sqLiteDatabase != null) && (sqLiteDatabase.isOpen());
        if (!isAlreadyOpen)
            sqLiteDatabase = database.getWritableDatabase();
        return isAlreadyOpen;
    }

    @Override
    public String getItemTriggeredFrom(T item) {
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        String triggerName = getTriggeredFrom(item);
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
        return triggerName;
    }

    private String getTriggeredFrom(T item) {
        String triggeredFrom = null;
        Cursor cursor = sqLiteDatabase.query(
                params.getTableName(),
                new String[]{params.columnTriggeredFrom},
                String.format("%s = ?", params.columnKey),
                new String[]{item.getKey()},
                null, null, null
        );
        if (cursor.moveToFirst()){
            triggeredFrom = cursor.getString(0);
        }
        cursor.close();
        return triggeredFrom;
    }

    @Override
    public boolean isUnlocked(T item) {
        boolean temporarilyOpen = ensureDatabaseIsOpen();
        boolean status = getStatus(item).equals(STATE_UNLOCKED);
        ensureTemporarilyOpenedDbGetsClosed(temporarilyOpen);
        return status;
    }

}
