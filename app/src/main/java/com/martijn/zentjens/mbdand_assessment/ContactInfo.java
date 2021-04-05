package com.martijn.zentjens.mbdand_assessment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class ContactInfo extends AppCompatActivity {

    public String contactName;
    public TextView nameTextView;
    public TextView quoteTextView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        this.setToolbar();

        contactName = getIntent().getStringExtra("contactName");
        setContactInfo();

        quoteTextView = new TextView(this);
        quoteTextView = (TextView) this.findViewById(R.id.quoteText);
        quoteTextView.setText("QUOTE TEXT");

        try {
            URL url = new URL("https://goquotes-api.herokuapp.com/api/v1/random?count=1");

            setApiQuote(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public ContactInfo() { }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setContactInfo() {
        nameTextView = new TextView(this);
        nameTextView = (TextView) this.findViewById(R.id.textView);
        nameTextView.setText(contactName);
    }

    private void setApiQuote(URL url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("quotes");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String quote = jsonObject.optString("text");

                    quoteTextView.setText(quote);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                quoteTextView.setText("Hmm, hier is iets misgegaan. Maar dit is ook een citaat!");
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(jsonObjectRequest);
    }

    // On clicked of navigation back-button
    public void onBackButtonClicked(MenuItem item) {
        finish();
    }

    // Set the toolbar button
    public void setToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("Quote");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFFFF"));
        setSupportActionBar(toolbar);
    }
}
