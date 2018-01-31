package android.example.com.ratescurrencyapp;

import android.content.Context;
import android.content.Intent;
import android.example.com.ratescurrencyapp.RatesAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity implements RatesAdapter.RateClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout mRelativeLayout;
    private RecyclerView mRecyclerView;

    private RatesAdapter mAdapter;
    private Currency currency;

    Intent intent = getIntent();
    private String RATE_SELECT = intent.getExtras().toString();
    private String JSON_URL = "https://api.fixer.io/latest?base=" + RATE_SELECT; //base = selected rate


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRelativeLayout = findViewById(R.id.rl);
        mRecyclerView = findViewById(R.id.recycler_view);

        // Define a layout for RecyclerView
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        mAdapter = new RatesAdapter(this, this);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        loadCurrencyRates();
    }

    public void loadCurrencyRates(){

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject currency_obj = new JSONObject(response);
                            currency = new Currency(currency_obj.getString("base"), currency_obj.getString("date"));

                            JSONObject ratesJSON = currency_obj.getJSONObject("rates");
                            Iterator<String> keys = ratesJSON.keys();
                            while (keys.hasNext()){
                                String key = keys.next();
                                Rate rate = new Rate(key, ratesJSON.getDouble(key));
                                currency.addRate(rate);
                            }

                            ArrayList<Rate> rates = currency.getRates();
                            mAdapter.updateData(rates);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public void onRateItemClick(Rate rate){
        Log.d(TAG, "onRateItemClick: " + rate.toString());
    }
}