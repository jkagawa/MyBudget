package com.jkagawa.mybudget.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.jkagawa.mybudget.data.BudgetContract.ExpenseEntry.TABLE_NAME_EXPENSE;
import static com.jkagawa.mybudget.data.BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE;
import static com.jkagawa.mybudget.data.BudgetContract.BudgetEntry.TABLE_NAME_BUDGET;
import static com.jkagawa.mybudget.data.BudgetContract.BudgetEntry.CONTENT_URI_BUDGET;

/**
 * Created by Joshua on 10/11/2018.
 */

public class BudgetContentProvider extends ContentProvider {

    public static final int MYEXPENSE = 100;
    public static final int MYEXPENSE_WITH_ID = 101;
    public static final int MYBUDGET = 200;
    public static final int MYBUDGET_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(BudgetContract.AUTHORITY, BudgetContract.PATH_MYEXPENSE, MYEXPENSE);

        uriMatcher.addURI(BudgetContract.AUTHORITY, BudgetContract.PATH_MYEXPENSE + "/#", MYEXPENSE_WITH_ID);

        uriMatcher.addURI(BudgetContract.AUTHORITY, BudgetContract.PATH_MYBUDGET, MYBUDGET);

        uriMatcher.addURI(BudgetContract.AUTHORITY, BudgetContract.PATH_MYBUDGET + "/#", MYBUDGET_WITH_ID);

        return uriMatcher;
    }

    private ExpenseDBHelper mExpenseDBHelper;
    private BudgetDBHelper mBudgetDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mExpenseDBHelper = new ExpenseDBHelper(context);
        mBudgetDBHelper = new BudgetDBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db_expense = mExpenseDBHelper.getWritableDatabase();
        final SQLiteDatabase db_budget = mBudgetDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch(match) {

            case MYEXPENSE:

                returnCursor = db_expense.query(TABLE_NAME_EXPENSE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;

            case MYBUDGET:

                returnCursor = db_budget.query(TABLE_NAME_BUDGET,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);


        }



        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db_expense = mExpenseDBHelper.getWritableDatabase();
        final SQLiteDatabase db_budget = mBudgetDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch(match) {
            case MYEXPENSE:


                long id = db_expense.insert(TABLE_NAME_EXPENSE, null, contentValues);
                if(id > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI_EXPENSE, id);
                } else {
                    throw new android.database.SQLException("Failes to insert row into " + uri);
                }

                break;
            case MYBUDGET:

                long id2 = db_budget.insert(TABLE_NAME_BUDGET, null, contentValues);
                if(id2 > 0) {
                    returnUri = ContentUris.withAppendedId(CONTENT_URI_BUDGET, id2);
                } else {
                    throw new android.database.SQLException("Failes to insert row into " + uri);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db_expense = mExpenseDBHelper.getWritableDatabase();
        final SQLiteDatabase db_budget = mBudgetDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int expenseDeleted;

        switch(match) {
            case MYEXPENSE:

                expenseDeleted = db_expense.delete(TABLE_NAME_EXPENSE,
                        selection,
                        selectionArgs);
                break;
            case MYBUDGET:

                expenseDeleted = db_budget.delete(TABLE_NAME_BUDGET,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (expenseDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return expenseDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db_expense = mExpenseDBHelper.getWritableDatabase();
        final SQLiteDatabase db_budget = mBudgetDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int expenseUpdated;

        switch(match) {
            case MYEXPENSE:

                expenseUpdated = db_expense.update(TABLE_NAME_EXPENSE,
                        contentValues,
                        s,
                        strings);
                break;
            case MYBUDGET:

                expenseUpdated = db_budget.update(TABLE_NAME_BUDGET,
                        contentValues,
                        s,
                        strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (expenseUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return expenseUpdated;
    }

}
