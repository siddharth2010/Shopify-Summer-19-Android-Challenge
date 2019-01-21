package com.example.shopifysiddharth;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.shopifysiddharth.adapter.CollectionAdapter;
import com.example.shopifysiddharth.util.myAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    public String test;
    private RecyclerView recyclerView = null;
    public CollectionAdapter mCollectionAdapter;
    public static List<String> movieList = new ArrayList<>();
    public static List<String> coolid = new ArrayList<>();
    public static List<String> img = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_view_headline_24px);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.rv_collection);

        CollectionsAsyncTask task = new CollectionsAsyncTask("", "https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6");
        task.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        return super.onOptionsItemSelected(item);
    }


    private class CollectionsAsyncTask extends myAsyncTask {

        public CollectionsAsyncTask(String test, String baseUrl) {
            super(test, baseUrl);
        }

        @Override
        public void updateui(){
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mCollectionAdapter = new CollectionAdapter( getApplicationContext(),(ArrayList<String>) movieList);
            recyclerView.setAdapter(mCollectionAdapter);
            recyclerView.setHasFixedSize(true);
        }


        @Override
        public ArrayList<String> extractFeatureFrom(String anss) {
            ArrayList<String> ans = new ArrayList<>();
            try {
                JSONObject Response = new JSONObject(anss);
                JSONArray Array = Response.getJSONArray("custom_collections");
                int j = Array.length();
                JSONObject PP;
                for (int i = 0 ; i < j ; i++){
                    PP =Array.getJSONObject(i);
                    movieList.add(PP.getString("title"));
                    coolid.add(PP.getString("id"));
                    JSONObject image = PP.getJSONObject("image");
                    img.add(image.getString("src"));
                }
            } catch (JSONException e) {
                // Do Nothing
            }
            return ans;
        }
    }
}
