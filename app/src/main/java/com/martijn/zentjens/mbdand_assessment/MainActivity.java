package com.martijn.zentjens.mbdand_assessment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
}
