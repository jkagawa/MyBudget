package com.example.android.mybudget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.android.mybudget.MainActivity;
import com.example.android.mybudget.R;
import com.example.android.mybudget.R2;
import com.example.android.mybudget.data.BudgetContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Joshua on 10/27/2018.
 */

public class BudgetWidgetProvider extends AppWidgetProvider {

//    @BindView(R2.id.widget_budget) TextView mBudget;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_budget);
//        views.setOnClickPendingIntent(R.id.container_widget, pendingIntent);
//
//        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }

        final int count = appWidgetIds.length;

        Cursor mCursor = getBudget(context);

        List<String> expenseList = new ArrayList<>();
        List<String> expenseDateList = new ArrayList<>();

        Cursor mCursorExpense = getAllExpenses(context);
        double totalExpenseForMonth = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatToDigit = new SimpleDateFormat("MM");

        if(mCursorExpense != null) {
            mCursorExpense.moveToFirst();

            for(int i=0; i<mCursorExpense.getCount(); i++) {
                String expense = mCursorExpense.getString(mCursorExpense.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_EXPENSE));
                String expenseDate = mCursorExpense.getString(mCursorExpense.getColumnIndex(BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE));
                expenseList.add(expense);
                expenseDateList.add(expenseDate);
                try {
                    Date date = Calendar.getInstance().getTime();
                    Date date2 = format.parse(expenseDate);
                    String CurrentMonth = formatToDigit.format(date);
                    String formattedMonth = formatToDigit.format(date2);

                    if(Integer.parseInt(formattedMonth) == Integer.parseInt(CurrentMonth)) {
                        totalExpenseForMonth = totalExpenseForMonth + Double.parseDouble(expense);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mCursorExpense.moveToNext();
            }
            //mCursor.close();
        }


        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_budget);

            if(mCursor != null) {
                if(mCursor.moveToLast()) {

                    String budget = mCursor.getString(mCursor.getColumnIndex(BudgetContract.BudgetEntry.COLUMN_BUDGET));

                    budget = String.valueOf(Double.parseDouble(budget) - totalExpenseForMonth);

                    double remBudgetNumber = Double.parseDouble(budget);
                    String remBudgetString = String.format("%.2f", remBudgetNumber);

                    String value = context.getResources().getString(R.string.currency_value, remBudgetString);
//                    mBudget.setText(value);
                    remoteViews.setTextViewText(R.id.widget_budget, value);
                    //mCursor.close();
                }

            }


            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.container_widget, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private Cursor getBudget(Context context) {

        return context.getContentResolver().query(BudgetContract.BudgetEntry.CONTENT_URI_BUDGET,
                null,
                null,
                null,
                BudgetContract.BudgetEntry.COLUMN_TIMESTAMP_BUDGET);

    }

    private Cursor getAllExpenses(Context context) {

        return context.getContentResolver().query(BudgetContract.ExpenseEntry.CONTENT_URI_EXPENSE,
                null,
                null,
                null,
                BudgetContract.ExpenseEntry.COLUMN_TIMESTAMP_EXPENSE);
    }

}
