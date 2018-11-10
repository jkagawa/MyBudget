package com.example.android.mybudget;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.mybudget.data.BudgetContract;
import com.example.android.mybudget.data.ExpenseDBHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 10/2/2018.
 */

public class AddExpenseActivity extends AppCompatActivity {

    @BindView(R2.id.enter_expense) EditText mEnterExpense;

    public double mExpense;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        ButterKnife.bind(this);

        SQLiteDatabase mDatabase;
        ExpenseDBHelper dbHelper = new ExpenseDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        mEnterExpense.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
    }

    public void addExpense(View view) {

        ContentValues contentValues = new ContentValues();

        String enterExpense = mEnterExpense.getText().toString();

        if (!enterExpense.matches("")) {

            mExpense = Double.parseDouble(mEnterExpense.getText().toString());

        }

        if(mExpense > 0) {

            contentValues.put(BudgetContract.ExpenseEntry.COLUMN_EXPENSE, mExpense);

        Uri uri = getContentResolver().insert(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE, contentValues);

            Cursor cursor = getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
                    null,
                    null,
                    null,
                    null);

            String expenseString = String.format("%.2f", mExpense);

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

}
