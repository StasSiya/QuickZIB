package com.example.anastasiyaivanova.newjsonparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.anastasiyaivanova.newjsonparser.MyJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ZibAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reference to our RecyclerView

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //assign layoutmanager

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        requestJsonObject();

    }

    private void requestJsonObject() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api-tvthek.orf.at/api/v3/profiles?limit=1000";

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "MyJson" + response);
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                List<MyJson.EmbeddedBeanX.ItemsBean> orf = new ArrayList<MyJson.EmbeddedBeanX.ItemsBean>();
                orf = Arrays.asList(mGson.fromJson(response,MyJson.EmbeddedBeanX.ItemsBean[].class));

                adapter = new ZibAdapter(MainActivity.this, orf);
                //Type foundListType = new TypeToken<ArrayList<com.example.anastasiyaivanova.newjsonparser.MyJson.EmbeddedBeanX>> (){}.getType();

                //List<com.example.anastasiyaivanova.newjsonparser.MyJson.EmbeddedBeanX> orf = new Gson().fromJson(response, foundListType);


//create a new ZibAdapter
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error" + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                try {

                    Map<String, String> headers = new HashMap<>();
                    String credentials = "ps_android_v3:6a63d4da29c721d4a986fdd31edc9e41";
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                } catch (Exception e) {
                    Log.e(TAG, "Authentication Filure");
                }
                return super.getHeaders();
            }
        };

        queue.add(stringRequest);
        }


}


