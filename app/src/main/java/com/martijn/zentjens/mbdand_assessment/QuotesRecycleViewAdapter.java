package com.martijn.zentjens.mbdand_assessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.martijn.zentjens.mbdand_assessment.models.Quote;

import java.util.List;

public class QuotesRecycleViewAdapter extends RecyclerView.Adapter<QuotesRecycleViewAdapter.ViewHolder> {
    private final List<Quote> quoteList;
    public ViewHolder viewHolder;

    // Provide a direct reference to each of the views within a data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public Chip tagChip;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable
            super(itemView);

            messageTextView = (TextView) itemView.findViewById(R.id.message);
            tagChip = (Chip) itemView.findViewById(R.id.chip_tag);
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
        View contactView = inflater.inflate(R.layout.quote_list_item, parent, false);

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

        Chip chip = holder.tagChip;
        chip.setText(quote.getTag());
    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }
}
