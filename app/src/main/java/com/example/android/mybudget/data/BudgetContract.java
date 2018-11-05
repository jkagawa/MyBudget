package com.example.android.mybudget.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Joshua on 10/10/2018.
 */

public class BudgetContract {

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.android.mybudget";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY );
    public static final String PATH_MYEXPENSE = "expense";
    public static final String PATH_MYBUDGET = "budget";

    public static final class ExpenseEntry implements BaseColumns {

        public static final Uri CONTENT_URI_EXPENSE = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MYEXPENSE).build();

        public static final String TABLE_NAME_EXPENSE = "expense";

        public static final String COLUMN_EXPENSE = "expense_col";

        public static final String COLUMN_TIMESTAMP_EXPENSE = "timestamp";

    }

    public static final class BudgetEntry implements BaseColumns {

        public static final Uri CONTENT_URI_BUDGET = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MYBUDGET).build();

        public static final String TABLE_NAME_BUDGET = "budget";

        public static final String COLUMN_BUDGET = "budget_col";

        public static final String COLUMN_TIMESTAMP_BUDGET = "timestamp";

    }


}
