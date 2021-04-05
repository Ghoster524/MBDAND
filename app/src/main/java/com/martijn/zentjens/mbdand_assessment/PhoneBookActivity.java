package com.martijn.zentjens.mbdand_assessment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.martijn.zentjens.mbdand_assessment.models.Contact;

import java.util.ArrayList;

public class PhoneBookActivity extends AppCompatActivity {
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    ArrayList<Contact> contactList;
    int quoteAmount;
    PhoneBookRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_book);

        this.setToolbar();

        // Get RecycleView from the layout
        RecyclerView phoneBookRecyclerView = (RecyclerView) findViewById(R.id.phone_book_list);

        // Check if permission needs to be asked
        this.getPermissionToReadContacts();
        this.getUserContacts();

        // Attach the adapter to the recyclerview to populate items
        adapter = new PhoneBookRecycleViewAdapter(contactList);
        phoneBookRecyclerView.setAdapter(adapter);
        phoneBookRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setQuoteAmount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Intent intent = new Intent(this, SettingsActivity.class);

        startActivityForResult(intent, 1);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setQuoteAmount();
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

    // Read user contacts from phone
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
                if (contacts.moveToFirst()) {
                    String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));

                    //  Get all phone numbers.
                    Cursor phones = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    while (phones.moveToNext()) {
                        String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Contact contact = new Contact(contacts.getString(0), contacts.getString(1), number);
                        contactList.add(contact);
                    }
                    phones.close();
                }
            }
        }
    }

    // Get permission of the user to read contacts
    public void getPermissionToReadContacts() {
        if (!this.checkHasReadContactsPermission() && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }

    // Display simple message to the user
    public void createToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Check if has permission to read contacts
    public boolean checkHasReadContactsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    // On clicked of navigation back-button
    public void onBackButtonClicked(MenuItem item) {
        finish();
    }

    // Set the toolbar button
    public void setToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Contacten");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);
    }

    public void setQuoteAmount() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        quoteAmount = Integer.parseInt(preferences.getString("amount", "1"));
    }
}
