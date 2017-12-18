package com.example.anastasiyaivanova.newjsonparser;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.anastasiyaivanova.newjsonparser.MyJson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements ZibAdapter.ListItemClickListener {
    private final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ZibAdapter adapter;
    List<OrfSendung> sendungen;
    private static final String NAME_SEPARATOR = "tel";


    @Override
    public void onClick(OrfSendung aktuelleSendung) {

        requestUrlLink(aktuelleSendung.getUrl());

        //We don't start the intent because we wait for the requestUrlLink to finish
    }

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


    private void requestUrlLink(String episodesJsonUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);

        Log.d(TAG, "requesting url:" + episodesJsonUrl);

        final StringRequest stringRequest2 = new StringRequest(Request.Method.GET, episodesJsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "received response, parsing json");
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject embedded = root.getJSONObject("_embedded");
                    JSONArray items2 = embedded.getJSONArray("items");
                    JSONObject item0 = items2.getJSONObject(0);
                    JSONObject sources = item0.getJSONObject("sources");
                    JSONArray hls = sources.getJSONArray("hls");
                    JSONObject item3 = hls.getJSONObject(6);
                    String link = item3.getString("src");
                    // http://apasfiis.apa.at/ipad/cms-worldwide_episodes/13957727_0007_QXB.mp4/playlist.m3u8
                    Uri playListUrl = Uri.parse(link);
                    Log.d(TAG, "Video URL:" + playListUrl);

                    Intent i = new Intent(Intent.ACTION_VIEW, playListUrl);
                    startActivity(i);

                } catch (JSONException e) {
                    Log.e(TAG, "json ich liebe Stefan", e);
                }
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

        queue.add(stringRequest2);
    }


    private void requestJsonObject() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api-tvthek.orf.at/api/v3/profiles?limit=1000";

        Log.d(TAG, "building request for sendungen");

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "received sendungen response, parsing json‚" );

                List<OrfSendung> sendungen = new ArrayList<>();
                try {
                    JSONObject root = new JSONObject(response);
                    JSONObject embedded = root.getJSONObject("_embedded");
                    JSONArray items = embedded.getJSONArray("items");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);

                        String name = item.getString("title");

                        if (name.contains("ZIB")) {

                            JSONObject links = item.getJSONObject("_links");
                            JSONObject episodes = links.getJSONObject("episodes");
                            String episodesJsonUrl = episodes.getString("href");

                            Log.d(TAG, "found zib sendung: " + name);
                            OrfSendung sendung = new OrfSendung(name, episodesJsonUrl);

                            sendungen.add(sendung);
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "json problem", e);
                    //TODO: Handle exception
                }

                Log.d(TAG, "creating adapter");
                adapter = new ZibAdapter(MainActivity.this, sendungen, MainActivity.this);
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


