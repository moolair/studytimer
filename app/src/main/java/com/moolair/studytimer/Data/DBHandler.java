package com.moolair.studytimer.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.moolair.studytimer.Model.Timer;
import com.moolair.studytimer.Util.constants;

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

    }

    //Get a Timer
    private Timer getTimer(int id){
        return null;
    }

    //Get all Timer
    public List<Timer> getAllTimers(){
        return null;
    }

    //update Timer
    public int updateTimer(Timer timer){
        return 0;
    }

    //Delete Timer
    public void deleteTimer(int id){

    }

    //Get count
    public int getTimersCount(){
        return 0;
    }

}
