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



        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_budget);

            if(mCursor != null) {
                if(mCursor.moveToLast()) {

                    String budget = mCursor.getString(mCursor.getColumnIndex(BudgetContract.BudgetEntry.COLUMN_BUDGET));

                    String value = context.getResources().getString(R.string.currency_value, budget);
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

}
