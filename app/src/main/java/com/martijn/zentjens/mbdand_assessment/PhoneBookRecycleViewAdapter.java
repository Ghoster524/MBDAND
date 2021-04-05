package com.martijn.zentjens.mbdand_assessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.martijn.zentjens.mbdand_assessment.models.Contact;

import org.w3c.dom.Text;

import java.util.List;

public class PhoneBookRecycleViewAdapter extends RecyclerView.Adapter<PhoneBookRecycleViewAdapter.ViewHolder> {
    private final List<Contact> contactList;

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public Chip contactFirstLetter;
        public TextView nameTextView;
        public Button messageButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            contactFirstLetter = (Chip) itemView.findViewById(R.id.contact_name_first_letter_id);
            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }

    public PhoneBookRecycleViewAdapter(List<Contact> contacts) {
        contactList = contacts;
    }

    @NonNull
    @Override
    public PhoneBookRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.phone_book_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneBookRecycleViewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Contact contact = contactList.get(position);

        TextView textView = holder.nameTextView;
        textView.setText(contact.getName());

        Chip chip = holder.contactFirstLetter;
        chip.setText(contact.getFirstLetter());

        // TODO: Navigate with button to detail page
        Button button = holder.messageButton;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
