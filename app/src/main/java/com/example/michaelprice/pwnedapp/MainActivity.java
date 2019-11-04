package com.example.michaelprice.pwnedapp;

//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.EditText;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;

/**
 * Main screen for our API testing app.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "PwnedApp:Main";

    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    /** The start of the url of the API */
    private static final String URLSTART = "https://haveibeenpwned.com/api/v2/breachedaccount/";

    /** The end of the url of the API. Used to trim the JSON response. */
    private static final String URLEND = "/?truncateResponse=true";

    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up a queue for our Volley requests
        requestQueue = Volley.newRequestQueue(this);

        // Load the main layout for our activity
        setContentView(R.layout.activity_main);

        // Attach the handler to our UI button
        final Button startAPICall = findViewById(R.id.startAPICall);
        startAPICall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Start API button clicked");
                startAPICall();
            }
        });
    }

    /**
     * Make an API call.
     */
    void startAPICall() {
        try {
            EditText editText = (EditText) findViewById(R.id.editText);
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URLSTART +  editText.getText().toString() + URLEND,
                    // "https://haveibeenpwned.com/api/v2/breachedaccount/ + {input (hello@yahoo.com)} + "/?truncateResponse=true"
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            Log.d(TAG, response.toString());
                            try {
                                Object a = response.get(0);
                                String json = new Gson().toJson(a);
                                JSONObject b = new JSONObject(json);
                                apiCallDone(b);
                                startActivity(new Intent(getApplicationContext(), com.example.michaelprice.pwnedapp.Response.class));
                            } catch (Exception e) {
                                //startActivity(new Intent(getApplicationContext(), Bad.class));
                                Log.w(TAG, e.toString());

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                    Log.w(TAG, "You are Safe!");
                    startActivity(new Intent(getApplicationContext(), Bad.class));
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            // Log.i(TAG, response.get("hostname").toString());
//            final TextView textDisplay = findViewById(R.id.resultText);
//            if (textDisplay == null) {
//                textDisplay.setText(response.toString());
//                Log.d(TAG, "null textbix");
//            } else {
//                textDisplay.setText(response.toString());
//            }
        } catch (JSONException ignored) { }
    }
}
