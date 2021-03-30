package com.martijn.zentjens.mbdand_assessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Example button load new Activity
    public void goToAnActivity(View view) {
        /*Fragment anotherFragment = Fragment.instantiate(this, PhoneBookRecyclerViewAdapter.class.getName());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.add(R.id.realtabcontent, anotherFragment);
        ft.addToBackStack(null);
        ft.commit();*/
    }
}
