package com.jkagawa.mybudget.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joshua on 10/21/2018.
 */

public class BudgetDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mybudget.db";

    private static final int DATABASE_VERSION = 1;

    public BudgetDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_BUDGET_TABLE = "CREATE TABLE " +
                BudgetContract.BudgetEntry.TABLE_NAME_BUDGET + " (" +
                BudgetContract.BudgetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BudgetContract.BudgetEntry.COLUMN_BUDGET + " TEXT NOT NULL, " +
                BudgetContract.BudgetEntry.COLUMN_TIMESTAMP_BUDGET + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_BUDGET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BudgetContract.BudgetEntry.TABLE_NAME_BUDGET);
        onCreate(sqLiteDatabase);
    }
}
