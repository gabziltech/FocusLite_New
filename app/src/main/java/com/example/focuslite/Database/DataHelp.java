package com.example.focuslite.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelp {
    SQLiteDatabase db;
    private MyOpenHelper db1;
    Context context;

    public DataHelp(Context con) {
        this.context = con;
        SQLiteOpenHelper myHelper = new MyOpenHelper(this.context);
        this.db = myHelper.getWritableDatabase();
        this.db1 = new MyOpenHelper(this.context);
    }

    public void LoginSbmt(String Email, String Pass) {
        ContentValues conV = new ContentValues();
        conV.put("Email", Email);
        conV.put("Password", Pass);

        db.insert(MyOpenHelper.TABLE_NAME, null, conV);
    }

    public void InputsSubmit(String Speed, int UserId) {
        ContentValues conV = new ContentValues();
        conV.put("Speed", Speed);
        conV.put("UserId", UserId);

        db.insert(MyOpenHelper.TABLE_NAME2, null, conV);
    }

    public boolean DeleteInputs(int UserId) {
        boolean result = false;
        try {
            String whareClause = "UserId = " +UserId;
            db.delete(MyOpenHelper.TABLE_NAME2, whareClause, null);
            result = true;
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    public void DeleteSession() {
        db.delete(MyOpenHelper.TABLE_NAME1, null, null);
    }

    public void DeleteLogin() {
        db.delete(MyOpenHelper.TABLE_NAME, null, null);
    }

    public void UpdateSession(String Session, int LoginID) {
        ContentValues conV = new ContentValues();
        conV.put("SessionTake", Session);
        conV.put("LoginID", LoginID);

        Integer ID = db1.getID();
        if (ID == 0)
            db.insert(MyOpenHelper.TABLE_NAME1, null, conV);
        else {
            String where = "Id = " + 1;
            db.update(MyOpenHelper.TABLE_NAME1, conV, where, null);
        }
    }

    public void dbClose(SQLiteDatabase db) {
        db.close();
    }
}
