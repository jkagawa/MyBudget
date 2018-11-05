package com.example.android.mybudget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Joshua on 10/14/2018.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    final private HistoryAdapterOnClickHandler mClickHandler;
    public final int mPosition;

    private List<String> mExpenseList;
    private List<String> mExpenseDateList;

    public interface HistoryAdapterOnClickHandler {
        void onClick(int position);
    }

    public HistoryAdapter(int position, HistoryAdapterOnClickHandler clickHandler) {
        mPosition = position;
        mClickHandler = clickHandler;

    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.history_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);

        return new HistoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewHolder holder, int position) {

        Context context = holder.mExpenseItemView.getContext();

        double expenseNumber = Double.parseDouble(mExpenseList.get(position));
        String expenseString = String.format("%.2f", expenseNumber);
        String value = context.getResources().getString(R.string.currency_value, expenseString);
        holder.mExpenseItemView.setText(value);

//        holder.mExpenseItemDateView.setText(mExpenseDateList.get(position));


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format3 = new SimpleDateFormat("MMMM yyyy");


        try {
            Date date = format.parse(mExpenseDateList.get(position));

            SimpleDateFormat format2 = new SimpleDateFormat("MMM d");
            String formattedDate = format2.format(date);
            String formattedHeaderDate = format3.format(date);

            holder.mExpenseItemDateView.setText(formattedDate);

            if(position > 0) {
                Date date2 = format.parse(mExpenseDateList.get(position-1));

                SimpleDateFormat formatToDigit = new SimpleDateFormat("MM");
                Date dateDigit1 = formatToDigit.parse(String.valueOf(date));
                Date dateDigit2 = formatToDigit.parse(String.valueOf(date2));
//                String dateDigit1 = formatToDigit.format(date);
//                String dateDigit2 = formatToDigit.format(date2);
                if(dateDigit1 != dateDigit2) {
                    holder.mExpenseHeaderView.setText(formattedHeaderDate);
                    holder.mExpenseHeaderView.setVisibility(View.VISIBLE);
                }
            }

            if(position == 0) {
                holder.mExpenseHeaderView.setText(formattedHeaderDate);
                holder.mExpenseHeaderView.setVisibility(View.VISIBLE);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        Toast.makeText(context, String.valueOf(mExpenseList.size()), Toast.LENGTH_LONG).show();

    }

    @Override
    public int getItemCount() {
        if(mExpenseList==null) {
            return 0;
        }
        else {
            return mExpenseList.size();
        }
    }


    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        final ImageView posterView;
//        final TextView ratingView;
        @BindView(R2.id.history_expense_item) TextView mExpenseItemView;
        @BindView(R2.id.history_expense_item_date) TextView mExpenseItemDateView;
        @BindView(R2.id.history_month_header) TextView mExpenseHeaderView;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

//            posterView = itemView.findViewById(R.id.history_expense_item);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mClickHandler.onClick(clickedPosition);
        }

    }

    public void setData(List<String> expenseList, List<String> expenseDateList) {
        mExpenseList = expenseList;
        Collections.reverse(mExpenseList);

        mExpenseDateList = expenseDateList;
        Collections.reverse(mExpenseDateList);
        notifyDataSetChanged();
    }

}
