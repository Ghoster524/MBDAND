package com.martijn.zentjens.mbdand_assessment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.HttpsURLConnection;

public class ContactInfo extends AppCompatActivity {

    public String contactName;
    public TextView nameTextView;
    public TextView quoteTextView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

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
}