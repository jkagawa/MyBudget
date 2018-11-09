package com.example.android.mybudget;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.android.mybudget.data.BudgetContract;
import com.example.android.mybudget.data.ExpenseDBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 10/2/2018.
 */

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.HistoryAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

//    @BindView(R2.id.history_text) TextView mHistoryText;
    @BindView(R2.id.recyclerview_history_expense) RecyclerView mRecyclerView;
    private Cursor mCursor;

    public static List<String> mExpenseList = new ArrayList<>();
    public static List<String> mExpenseDateList = new ArrayList<>();
    public static List<String> mExpenseIDList = new ArrayList<>();

    private HistoryAdapter mHistoryAdapter;
//    private RecyclerView mRecyclerView;

    private static final int NUMBER_OF_ITEMS = 100;

    public static final String DB_EXPENSE_ID_COL = "_id";
    public static final String EXTRA_EXPENSE_ID_KEY = "EXPENSE_ID_KEY";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ButterKnife.bind(this);

        SQLiteDatabase mDatabase;
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

//        generateListOfExpenses();

        getSupportLoaderManager().initLoader(MainActivity.GET_EXPENSES_LOADER, null,this);

//        mCursor = getAllExpenses();
//        List<String> expenseList = new ArrayList<>();
//        List<String> expenseDateList = new ArrayList<>();
//        List<String> expenseIDList = new ArrayList<>();
//
//        if(mCursor != null) {
//            mCursor.moveToFirst();
//
//            for(int i=0; i<mCursor.getCount(); i++) {
//                String expense = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));
//                String expenseDate = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE));
//                String expenseID = mCursor.getString(mCursor.getColumnIndex(DB_EXPENSE_ID_COL));
//                expenseList.add(expense);
//                expenseDateList.add(expenseDate);
//                expenseIDList.add(expenseID);
//
//                mCursor.moveToNext();
//
//                //Log.i("Info", "voteAverageList size is " + voteAverageList.size());
//            }
//
//            mExpenseList = expenseList;
//            mExpenseDateList = expenseDateList;
//
//            Collections.reverse(expenseIDList);
//            mExpenseIDList = expenseIDList;
//
//            //mCursor.close();
//
//        }
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        mHistoryAdapter = new HistoryAdapter(NUMBER_OF_ITEMS, this);
//
//        mRecyclerView.setAdapter(mHistoryAdapter);
//
//        mHistoryAdapter.setData(mExpenseList, mExpenseDateList);

//        Toast.makeText(this, String.valueOf(mExpenseList.size()), Toast.LENGTH_LONG).show();

    }

//    private Cursor getAllExpenses() {
//
//        return getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
//                null,
//                null,
//                null,
//                BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE);
//
//
//
//        /*return mDatabase.query(
//                FavoriteContract.FavoriteEntry.TABLE_NAME_EXPENSE,
//                null,
//                null,
//                null,
//                null,
//                null,
//                FavoriteContract.FavoriteEntry.COLUMN_TIMESTAMP_EXPENSE
//        );
//        */
//
//
//    }

//    private void generateListOfExpenses() {
//        mCursor = getAllExpenses();
//        List<String> expenseList = new ArrayList<>();
//        List<String> expenseDateList = new ArrayList<>();
//        List<String> expenseIDList = new ArrayList<>();
//
//        if(mCursor != null) {
//            mCursor.moveToFirst();
//
//            for(int i=0; i<mCursor.getCount(); i++) {
//                String expense = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));
//                String expenseDate = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE));
//                String expenseID = mCursor.getString(mCursor.getColumnIndex(DB_EXPENSE_ID_COL));
//                expenseList.add(expense);
//                expenseDateList.add(expenseDate);
//                expenseIDList.add(expenseID);
//
//                mCursor.moveToNext();
//
//                //Log.i("Info", "voteAverageList size is " + voteAverageList.size());
//            }
//
//            mExpenseList = expenseList;
//            mExpenseDateList = expenseDateList;
//
//            Collections.reverse(expenseIDList);
//            mExpenseIDList = expenseIDList;
//
//            //mCursor.close();
//
//        }
//
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
//        mRecyclerView.setLayoutManager(layoutManager);
//
//        mHistoryAdapter = new HistoryAdapter(NUMBER_OF_ITEMS, this);
//
//        mRecyclerView.setAdapter(mHistoryAdapter);
//
//        mHistoryAdapter.setData(mExpenseList, mExpenseDateList);
//    }

    @Override
    public void onClick(int position) {
        Intent editExpenseIntent = new Intent(HistoryActivity.this, EditExpenseActivity.class);
        editExpenseIntent.putExtra(EXTRA_EXPENSE_ID_KEY, mExpenseIDList.get(position));
        startActivity(editExpenseIntent);

        HistoryActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        generateListOfExpenses();
        getSupportLoaderManager().initLoader(MainActivity.GET_EXPENSES_LOADER, null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                if (id == MainActivity.GET_EXPENSES_LOADER) {

                    return getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
                            null,
                            null,
                            null,
                            BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE);
                }

                return null;
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int id = loader.getId();
        if(id == MainActivity.GET_EXPENSES_LOADER) {
            mCursor = cursor;
            List<String> expenseList = new ArrayList<>();
            List<String> expenseDateList = new ArrayList<>();
            List<String> expenseIDList = new ArrayList<>();

            if(mCursor != null) {
                mCursor.moveToFirst();

                for(int i=0; i<mCursor.getCount(); i++) {
                    String expense = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));
                    String expenseDate = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE));
                    String expenseID = mCursor.getString(mCursor.getColumnIndex(DB_EXPENSE_ID_COL));
                    expenseList.add(expense);
                    expenseDateList.add(expenseDate);
                    expenseIDList.add(expenseID);

                    mCursor.moveToNext();

                    //Log.i("Info", "voteAverageList size is " + voteAverageList.size());
                }

                mExpenseList = expenseList;
                mExpenseDateList = expenseDateList;

                Collections.reverse(expenseIDList);
                mExpenseIDList = expenseIDList;

                //mCursor.close();

            }

            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            mRecyclerView.setLayoutManager(layoutManager);

            mHistoryAdapter = new HistoryAdapter(NUMBER_OF_ITEMS, this);

            mRecyclerView.setAdapter(mHistoryAdapter);

            mHistoryAdapter.setData(mExpenseList, mExpenseDateList);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
