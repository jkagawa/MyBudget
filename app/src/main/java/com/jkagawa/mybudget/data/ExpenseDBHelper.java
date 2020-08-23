package com.jkagawa.mybudget.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joshua on 10/11/2018.
 */

public class ExpenseDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myexpense.db";

    private static final int DATABASE_VERSION = 1;

    public ExpenseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_EXPENSE_TABLE = "CREATE TABLE " +
                BudgetContract.ExpenseEntry.TABLE_NAME_EXPENSE + " (" +
                BudgetContract.ExpenseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BudgetContract.ExpenseEntry.COLUMN_EXPENSE + " TEXT NOT NULL, " +
                BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_EXPENSE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BudgetContract.ExpenseEntry.TABLE_NAME_EXPENSE);
        onCreate(sqLiteDatabase);
    }

}
