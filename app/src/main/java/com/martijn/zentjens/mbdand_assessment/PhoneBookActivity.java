package com.martijn.zentjens.mbdand_assessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.martijn.zentjens.mbdand_assessment.models.Contact;

import java.util.ArrayList;

public class PhoneBookActivity extends AppCompatActivity {
    ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);

        RecyclerView phoneBookRecyclerView = (RecyclerView) findViewById(R.id.phone_book_list);

        // Initialize contacts
        contactList = new ArrayList<>();
        contactList.add(new Contact("Martijn Zentjens"));
        contactList.add(new Contact("Jesse Stupers"));

        // Create adapter passing in the sample user data
        PhoneBookRecycleViewAdapter adapter = new PhoneBookRecycleViewAdapter(contactList);
        // Attach the adapter to the recyclerview to populate items
        phoneBookRecyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        phoneBookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
