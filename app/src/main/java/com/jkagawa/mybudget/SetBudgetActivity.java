package com.jkagawa.mybudget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.jkagawa.mybudget.data.BudgetContract;
import com.jkagawa.mybudget.data.ExpenseDBHelper;
import com.jkagawa.mybudget.widget.BudgetWidgetProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 10/2/2018.
 */

public class SetBudgetActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R2.id.enter_budget) TextView mEnterBudget;

    public double mBudget;

    private Cursor mCursor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);

        ButterKnife.bind(this);

        SQLiteDatabase mDatabase;
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        mEnterBudget.setFilters(new InputFilter[] {new SetBudgetActivity.DecimalDigitsInputFilter(7,2)});

        getSupportLoaderManager().initLoader(MainActivity.GET_BUDGET_LOADER, null,this);

    }

    public void setBudget(View view) {

        ContentValues contentValues = new ContentValues();

        String enterBudget = mEnterBudget.getText().toString();

        if (!enterBudget.matches("")) {

            mBudget = Double.parseDouble(mEnterBudget.getText().toString());

        }

        if(mBudget > 0) {

            contentValues.put(BudgetContract.BudgetEntry.COLUMN_BUDGET, mBudget);

            Uri uri = getContentResolver().insert(BudgetContract.BudgetEntry.CONTENT_URI_BUDGET, contentValues);

            Cursor cursor = getContentResolver().query(BudgetContract.BudgetEntry.CONTENT_URI_BUDGET,
                    null,
                    null,
                    null,
                    null);

        }

        finish();

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

                if (id == MainActivity.GET_BUDGET_LOADER) {

                    return getContentResolver().query(BudgetContract.BudgetEntry.CONTENT_URI_BUDGET,
                            null,
                            null,
                            null,
                            BudgetContract.BudgetEntry.COLUMN_TIMESTAMP_BUDGET);
                }

                return null;
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        int id = loader.getId();
        if(id == MainActivity.GET_BUDGET_LOADER) {
            mCursor = cursor;

            if(mCursor != null) {
                if(mCursor.moveToLast()) {

                    String budget = mCursor.getString(mCursor.getColumnIndex(BudgetContract.BudgetEntry.COLUMN_BUDGET));

                    double budgetNumber = Double.parseDouble(budget);
                    String budgetString = String.format("%.2f", budgetNumber);

                    mEnterBudget.setText(budgetString);

                    //mCursor.close();

                }

            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
