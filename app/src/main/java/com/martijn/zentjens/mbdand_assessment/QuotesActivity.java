package com.martijn.zentjens.mbdand_assessment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
    ArrayList<Quote> quotesList;
    QuotesRecycleViewAdapter adapter;
    QuotesActivity.LongOperation runningTask;
    Context self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        this.setToolbar();

        // Get RecycleView from the layout
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.quotes_list);

        // Check if permission needs to be asked
        this.getQuotes();

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

        runningTask = new QuotesActivity.LongOperation();
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

    private final class LongOperation extends AsyncTask<Void, Void, ArrayList<Quote>> {

        @Override
        protected ArrayList<Quote> doInBackground(Void... params) {
            URL url = null;

            try {
                url = new URL("https://goquotes-api.herokuapp.com/api/v1/random?count=2");
            } catch (MalformedURLException e) {
                createToast("Er kon geen verbinding worden gemaakt...");
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("quotes");
                        ArrayList<Quote> quotes = new ArrayList<Quote>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Add quote to list
                            quotes.add(new Quote(jsonObject.optString("tag"), jsonObject.optString("text")));
                            Log.d("TEST123", "Quote is er aangemaakt");
                        }
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

            return new ArrayList<Quote>();
        }

        @Override
        protected void onPostExecute(ArrayList<Quote> quotes) {
            quotesList = quotes;
        }
    }
}
