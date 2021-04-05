package com.martijn.zentjens.mbdand_assessment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.martijn.zentjens.mbdand_assessment.models.Quote;

import java.util.List;

public class QuotesRecycleViewAdapter extends RecyclerView.Adapter<QuotesRecycleViewAdapter.ViewHolder> {
    private final List<Quote> quoteList;
    public ViewHolder viewHolder;

    // Provide a direct reference to each of the views within a data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable
            super(itemView);

            // TODO: refactor
            messageTextView = (TextView) itemView.findViewById(R.id.contact_name);
        }
    }

    public QuotesRecycleViewAdapter(List<Quote> quotes) {
        quoteList = quotes;
    }

    @NonNull
    @Override
    public QuotesRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.phone_book_item, parent, false);

        // Return a new holder instance
        viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesRecycleViewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Quote quote = quoteList.get(position);

        TextView textView = holder.messageTextView;
        textView.setText(quote.getMessage());

        Log.d("TEST123", quote.getMessage());

        // TODO: Navigate with button to detail page
        //Button button = holder.messageButton;
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }
}
