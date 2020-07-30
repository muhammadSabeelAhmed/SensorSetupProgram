package com.discoveritech.sensorsetupprogram.GeneralClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class myDbAdapter {
    myDbHelper myhelper;

    public myDbAdapter(Context context) {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String sending, String receving) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.SENDING_PORT, sending);
        contentValues.put(myDbHelper.RECEVING_PORT, receving);
        long id = dbb.insert(myDbHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    public ArrayList<String> getData() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myDbHelper.SNO, myDbHelper.SENDING_PORT, myDbHelper.RECEVING_PORT};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME, columns, null, null, null, null, null);
        //StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            Global.myPorts.clear();
            int cid = cursor.getInt(cursor.getColumnIndex(myDbHelper.SNO));
            String sending_port = cursor.getString(cursor.getColumnIndex(myDbHelper.SENDING_PORT));
            String receving_port = cursor.getString(cursor.getColumnIndex(myDbHelper.RECEVING_PORT));
            Global.myPorts.add(sending_port);
            Global.myPorts.add(receving_port);
            //  buffer.append(cid + "   " + sending_port + "   " + receving_port + " \n");
        }
        cursor.close();
        db.close();
        return Global.myPorts;
    }


    public int deletePortTable() {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        int count = db.delete("portSettingsTable", "1", null);
        db.close();
        return count;
    }


    static class myDbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "senso_setup_program";    // Database Name
        private static final String TABLE_NAME = "portSettingsTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String SNO = "_id";     // Column I (Primary Key)
        private static final String SENDING_PORT = "Sending_Port";    //Column II
        private static final String RECEVING_PORT = "Receving_Port";    // Column III
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + SNO + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SENDING_PORT + " VARCHAR(255) ," + RECEVING_PORT + " VARCHAR(225));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
            }
        }
    }
}


