package com.martijn.zentjens.mbdand_assessment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.martijn.zentjens.mbdand_assessment.models.Quote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class QuotesActivity extends AppCompatActivity {
    ArrayList<Quote> quotesList = new ArrayList<>();
    QuotesRecycleViewAdapter adapter;
    QuotesActivity.LongOperation runningTask;
    Context self = this;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        this.setToolbar();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Get RecycleView from the layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.quotes_list);

        // Attach the adapter to the recyclerview to populate items
        adapter = new QuotesRecycleViewAdapter(quotesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runningTask != null) runningTask.cancel(true);
    }

    // Get all quotes
    public void getQuotes() {
        quotesList = new ArrayList<>();

        if (runningTask != null) runningTask.cancel(true);

        runningTask = new QuotesActivity.LongOperation(preferences.getString("amount", "1"));
        runningTask.execute();
    }

    // Display simple message to the user
    public void createToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // On clicked of navigation back-button
    public void onBackButtonClicked(MenuItem item) {
        finish();
    }

    // Set the toolbar button
    public void setToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Quotes overzicht");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);
    }

    private final class LongOperation extends AsyncTask<Void, Void, String> {

        public String amountOfQuotes;

        public LongOperation(String preference) {
            amountOfQuotes = preference;
        }

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;

            try {
                url = new URL("https://goquotes-api.herokuapp.com/api/v1/random?count=" + amountOfQuotes);
            } catch (MalformedURLException e) {
                createToast("Er kon geen verbinding worden gemaakt...");
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("quotes");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Add quote to list
                            quotesList.add(new Quote(jsonObject.optString("tag"), jsonObject.optString("text")));
                        }

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.quotes_list);
                        adapter = new QuotesRecycleViewAdapter(quotesList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(self));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    createToast("Er is iets misgegaan bij het ophalen van quites");
                }
            });

            RequestQueue queue = Volley.newRequestQueue(self);
            queue.add(jsonObjectRequest);

            return "succeed";
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 1);
        return true;
    }

    @Override
    protected void onResume() {
        this.getQuotes();
        super.onResume();
    }
}
