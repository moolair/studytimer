package com.moolair.studytimer.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.moolair.studytimer.Model.Timer;
import com.moolair.studytimer.Util.constants;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private Context ctx;

    public DBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, constants.DB_NAME, null, constants.DB_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMER_TABLE = "CREATE TABLE " + constants.TABLE_NAME + "("
                + constants.KEY_ID + " INTEGER PRIMARY KEY," + constants.KEY_TIMER_ITEM + " TEXT,"
                + constants.KEY_HOUR_ADDED + "TEXT,"
                + constants.KEY_MINUTE_ADDED + "TEXT);";
        db.execSQL(CREATE_TIMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + constants.TABLE_NAME);

        onCreate(db);
    }

    /*
    CRUD OPERATION
     */

    //Add Timer
    public void AddTimer(Timer timer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(constants.KEY_TIMER_ITEM, timer.getSubject());
        values.put(constants.KEY_HOUR_ADDED, timer.getHour());
        values.put(constants.KEY_MINUTE_ADDED, timer.getMinute());

        //Insert the row
        db.insert(constants.TABLE_NAME, null, values);
        Log.d("Saved!", "Saved to db");
    }

    //Get a Timer
    private Timer getTimer(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(constants.TABLE_NAME, new String[]{
                constants.KEY_ID, constants.KEY_TIMER_ITEM, constants.KEY_HOUR_ADDED,
                constants.KEY_MINUTE_ADDED},
                constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

            Timer timer = new Timer();
            timer.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(constants.KEY_ID))));
            timer.setSubject(cursor.getString(cursor.getColumnIndex(constants.KEY_TIMER_ITEM)));
            timer.setHour(cursor.getString(cursor.getColumnIndex(constants.KEY_HOUR_ADDED)));
            timer.setMinute(cursor.getString(cursor.getColumnIndex(constants.KEY_MINUTE_ADDED)));

            //convert timerstamp to something readable
//            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
//            String formatedDate =
//                    dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(constants.KEY_DATE_NAME))).getDate());
//
//            timer.setDateItemAdded(formatedDate);

        return timer;
    }

    //Get all Timer
    public List<Timer> getAllTimers(){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Timer> timerList = new ArrayList<>();

        Cursor cursor = db.query(constants.TABLE_NAME, new String[]{
                constants.KEY_ID, constants.KEY_TIMER_ITEM, constants.KEY_HOUR_ADDED,
                constants.KEY_MINUTE_ADDED}, null, null, null, null, "DESC");

        if(cursor.moveToFirst()){
            do {
                Timer timer = new Timer();
                timer.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(constants.KEY_ID))));
                timer.setSubject(cursor.getString(cursor.getColumnIndex(constants.KEY_TIMER_ITEM)));
                timer.setHour(cursor.getString(cursor.getColumnIndex(constants.KEY_HOUR_ADDED)));
                timer.setMinute(cursor.getString(cursor.getColumnIndex(constants.KEY_MINUTE_ADDED)));

                //convert timerstamp to something readable
//            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
//            String formatedDate =
//                    dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(constants.KEY_DATE_NAME))).getDate());
//
//            timer.setDateItemAdded(formatedDate);

                //Add to the timerList
                timerList.add(timer);
            }while (cursor.moveToNext());
        }

        return timerList;
    }

    //update Timer
    public int updateTimer(Timer timer){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(constants.KEY_TIMER_ITEM, timer.getSubject());
        values.put(constants.KEY_HOUR_ADDED, timer.getHour());
        values.put(constants.KEY_MINUTE_ADDED, timer.getMinute());

        return db.update(constants.TABLE_NAME, values, constants.KEY_ID + "=?",
                new String[]{String.valueOf(timer.getId())});
    }

    //Delete Timer
    public void deleteTimer(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(constants.TABLE_NAME, constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    //Get count
    public int getTimersCount(){
        String countQuery = "SELECT * FROM " + constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

}
