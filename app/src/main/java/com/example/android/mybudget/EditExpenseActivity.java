package com.example.android.mybudget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.android.mybudget.data.BudgetContract;
import com.example.android.mybudget.data.ExpenseDBHelper;
import com.example.android.mybudget.widget.BudgetWidgetProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 10/2/2018.
 */

public class EditExpenseActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R2.id.edit_expense) EditText mEditExpense;
    @BindView(R2.id.button_confirm_expense) Button mConfirmExpense;
    @BindView(R2.id.button_delete_expense) FloatingActionButton mDeleteExpense;

    public double mExpense;

    public static String mExpenseID;

    private Cursor mCursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        ButterKnife.bind(this);

        SQLiteDatabase mDatabase;
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();



        Intent intentFromHistoryActivity = getIntent();
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            mExpenseID = intentFromHistoryActivity.getStringExtra(HistoryActivity.EXTRA_EXPENSE_ID_KEY);

            getSupportLoaderManager().initLoader(MainActivity.GET_EXPENSES_LOADER, null,this);
//            mCursor = getExpenseByID();
//
//            mCursor.moveToFirst();
//
//            String expense = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));
//
//            double expenseNumber = Double.parseDouble(expense);
//            String expenseString = String.format("%.2f", expenseNumber);
//
//            mEditExpense.setText(expenseString);
        }

//        mEditExpense.setFilters(new InputFilter[] {new EditExpenseActivity.DecimalDigitsInputFilter(7,2)});

        mDeleteExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteExpense();
                Toast.makeText(getBaseContext(), "Expense removed", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    public void ConfirmExpense(View view) {

        ContentValues contentValues = new ContentValues();

        String newExpense = mEditExpense.getText().toString();
        if(!newExpense.isEmpty()) {
            if (Double.parseDouble(newExpense) > 0) {
                contentValues.put(BudgetContract.ExpenseEntry.COLUMN_EXPENSE, newExpense);
                setExpense(contentValues);
                Toast.makeText(getBaseContext(), "Expense updated to $" + newExpense, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "Make sure that the expense is greater than $0", Toast.LENGTH_LONG).show();
            }
        }
//        if (!enterExpense.matches("")) {
////            mExpense = 0;
//            mExpense = Double.parseDouble(mEnterExpense.getText().toString());
//
//        }
//
//        if(mExpense > 0) {
//
////        if(cursor.getCount() <=0) {
//
//
//            contentValues.put(BudgetContract.ExpenseEntry.COLUMN_EXPENSE, mExpense);
////            contentValues.put(BudgetContract.ExpenseEntry.COLUMN_BUDGET, mExpense);
////        }
//
//        Uri uri = getContentResolver().insert(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE, contentValues);
//
//            Cursor cursor = getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
//                    null,
//                    null,
//                    null,
//                    null);
//
////            Toast.makeText(getBaseContext(), String.valueOf(cursor.getCount()), Toast.LENGTH_LONG).show();
//
//            Toast.makeText(getBaseContext(), "$" + mExpense + " added to your expenses", Toast.LENGTH_LONG).show();
//
//        }

        finish();

    }

//    private Cursor getExpenseByID() {
//
//        return getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
//                null,
//                HistoryActivity.DB_EXPENSE_ID_COL +" = "+ mExpenseID,
//                null,
//                null);
//    }

    private boolean deleteExpense() {

        return getContentResolver().delete(
                BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
                HistoryActivity.DB_EXPENSE_ID_COL +" = "+ mExpenseID,
                null) > 0;
    }

    private int setExpense(ContentValues contentValues) {

        return getContentResolver().update(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
                contentValues,
                HistoryActivity.DB_EXPENSE_ID_COL +" = "+ mExpenseID,
                null);
    }

    public class DecimalDigitsInputFilter implements InputFilter {
        private int mDigitsBeforeZero;
        private int mDigitsAfterZero;
        private Pattern mPattern;

        private static final int DIGITS_BEFORE_ZERO_DEFAULT = 100;
        private static final int DIGITS_AFTER_ZERO_DEFAULT = 100;

        public DecimalDigitsInputFilter(Integer digitsBeforeZero, Integer digitsAfterZero) {
            this.mDigitsBeforeZero = (digitsBeforeZero != null ? digitsBeforeZero : DIGITS_BEFORE_ZERO_DEFAULT);
            this.mDigitsAfterZero = (digitsAfterZero != null ? digitsAfterZero : DIGITS_AFTER_ZERO_DEFAULT);
            mPattern = Pattern.compile("-?[0-9]{0," + (mDigitsBeforeZero) + "}+((\\.[0-9]{0," + (mDigitsAfterZero)
                    + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String replacement = source.subSequence(start, end).toString();
            String newVal = dest.subSequence(0, dstart).toString() + replacement
                    + dest.subSequence(dend, dest.length()).toString();
            Matcher matcher = mPattern.matcher(newVal);
            if (matcher.matches())
                return null;

            if (TextUtils.isEmpty(source))
                return dest.subSequence(dstart, dend);
            else
                return "";
        }
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
                            HistoryActivity.DB_EXPENSE_ID_COL +" = "+ mExpenseID,
                            null,
                            null);
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

            mCursor.moveToFirst();

            String expense = mCursor.getString(mCursor.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));

            double expenseNumber = Double.parseDouble(expense);
            String expenseString = String.format("%.2f", expenseNumber);

            mEditExpense.setText(expenseString);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
