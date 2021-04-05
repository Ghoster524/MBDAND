package com.martijn.zentjens.mbdand_assessment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // Go to contacts
    public void gotToContacts(View view) {
        Intent intent = new Intent(this, PhoneBookActivity.class);
        startActivity(intent);
    }

    // Go to quotes
    public void gotToQuotes(View view) {
        Intent intent = new Intent(this, QuotesActivity.class);
        startActivity(intent);
    }
}
