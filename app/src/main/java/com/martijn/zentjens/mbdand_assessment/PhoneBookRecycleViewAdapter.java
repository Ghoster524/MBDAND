package com.martijn.zentjens.mbdand_assessment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.martijn.zentjens.mbdand_assessment.models.Contact;

import java.util.List;

public class PhoneBookRecycleViewAdapter extends RecyclerView.Adapter<PhoneBookRecycleViewAdapter.ViewHolder> {
    private static final int REQUEST_PHONE_CALL = 1;
    private final List<Contact> contactList;
    public ViewHolder viewHolder;
    public ImageButton callButton;
    private Context context;

    // Provide a direct reference to each of the views within a data item
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Chip firstLetterChip;
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable
            super(itemView);

            firstLetterChip = (Chip) itemView.findViewById(R.id.contact_name_first_letter_id);
            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            callButton = (ImageButton) itemView.findViewById(R.id.call_button);
        }
    }

    public PhoneBookRecycleViewAdapter(List<Contact> contacts/*, PhoneBookActivity activity*/) {
        contactList = contacts;
        //  PhoneActivity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public PhoneBookRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.phone_book_item, parent, false);

        // Return a new holder instance
        viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneBookRecycleViewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Contact contact = contactList.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(contact.getName());

        Chip chip = holder.firstLetterChip;
        chip.setText(contact.getFirstLetter());

        // On click button of phone
        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getPhonenumber()));
                context.startActivity(callIntent);

                // Close ActionDial
                ((PhoneBookActivity) context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
