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
    public ArrayList<Contact> contactList;
    public int quoteAmount;
    public PhoneBookRecycleViewAdapter adapter;


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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
        finish();
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
            String[] PROJECTION = new String[] {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER,
            };
            String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
            Cursor contacts = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, SELECTION, null, null);


            if (contacts.getCount() > 0)
            {
                while(contacts.moveToNext()) {
                    int idFieldColumnIndex = 0;
                    int nameFieldColumnIndex = 0;
                    int numberFieldColumnIndex = 0;

                    String contactId = contacts.getString(contacts.getColumnIndex(ContactsContract.Contacts._ID));
                    Contact newContact = new Contact(contactId);

                    nameFieldColumnIndex = contacts.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    if (nameFieldColumnIndex > -1)
                    {
                        newContact.setName(contacts.getString(nameFieldColumnIndex));
                    }

                    PROJECTION = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
                    final Cursor phone = managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, ContactsContract.Data.CONTACT_ID + "=?", new String[]{String.valueOf(contactId)}, null);
                    if(phone.moveToFirst()) {
                        while(!phone.isAfterLast())
                        {
                            numberFieldColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            if (numberFieldColumnIndex > -1)
                            {
                                newContact.setPhoneNumber(phone.getString(numberFieldColumnIndex));
                                contactList.add(newContact);
                                phone.moveToNext();
                            }
                        }
                    }
                    phone.close();
                }

                contacts.close();
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
