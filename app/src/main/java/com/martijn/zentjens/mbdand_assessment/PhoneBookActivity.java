package com.martijn.zentjens.mbdand_assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.martijn.zentjens.mbdand_assessment.models.Contact;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PhoneBookActivity extends AppCompatActivity {
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);

        // Set the toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Contacten");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);
        // TODO: getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get RecycleView from the layout
        RecyclerView phoneBookRecyclerView = (RecyclerView) findViewById(R.id.phone_book_list);

        // Get permission
        this.getPermissionToReadContacts();
        // Get all user-contacts
        this.getUserContacts();

        // Attach the adapter to the recyclerview to populate items
        PhoneBookRecycleViewAdapter adapter = new PhoneBookRecycleViewAdapter(contactList);
        phoneBookRecyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        phoneBookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Get permission of the user to read contacts
    public void getPermissionToReadContacts() {
        if (!this.checkHasReadContactsPermission() &&
                !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    // When permission has been given for the first time
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            boolean hasPermission = grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            this.createToast("Het uitlezen van contacten is " + (hasPermission ? "toegestaan" : "afgewezen"));

            if (hasPermission) this.getUserContacts();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // Check if has permission to read contacts
    public boolean checkHasReadContactsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    // Display simple message to the user
    public void createToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Read user contacts from phone
    // TODO: Are these users being loaded from Contact app?
    public void getUserContacts() {
        contactList = new ArrayList<>();

        // Check if there is permission
        if (this.checkHasReadContactsPermission()) {
            Uri contactUri = ContactsContract.Contacts.CONTENT_URI;

            String[] PROJECTION = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
            };

            // Filtering on PhoneNumber
            String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";

            try (Cursor contacts = managedQuery(contactUri, PROJECTION, SELECTION, null, null)) {
                while (contacts.moveToNext()) {
                    contactList.add(new Contact(contacts.getString(0), contacts.getString(1)));
                }
            }
        }
    }

    public void onBackButtonClicked(MenuItem item) {
        finish();
    }
}
