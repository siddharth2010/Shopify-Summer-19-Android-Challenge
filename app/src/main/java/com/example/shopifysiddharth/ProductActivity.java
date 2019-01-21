package com.example.shopifysiddharth;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.example.shopifysiddharth.adapter.ProductAdapter;

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

public class ProductActivity extends AppCompatActivity {
    public String test;
    public String ans;
    public String urr;
    public List<String> product = new ArrayList<>();
    public static List<String> pname = new ArrayList<>();
    public static  List<Integer> quanty = new ArrayList<>();

    private DrawerLayout mDrawerLayout;


    private RecyclerView recyclerView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_view_headline_24px);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        String collectionid = intent.getStringExtra("key");
        ans = collectionid;
        recyclerView = (RecyclerView) findViewById(R.id.rv_product);
        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }



    private class TsunamiAsyncTask extends AsyncTask<URL, Void, String> {

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                return null;
            }
            return url;
        }
        @Override
        protected String doInBackground(URL... urls) {
            URL url = createUrl("https://shopicruit.myshopify.com/admin/collects.json?collection_id=" +
                    ans + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6");

            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // Do Nothing
            }
            test = jsonResponse;
            extractFeatureFrom(jsonResponse);
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String e) {

            updateui();
        }

        private void updateui(){
            Tsunami task = new Tsunami();
            task.execute();
        }

        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // Do Nothing
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }


        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private ArrayList<String> extractFeatureFrom(String anss) {
            ArrayList<String> ans = new ArrayList<>();
            try {
                JSONObject Response = new JSONObject(anss);
                JSONArray Array = Response.getJSONArray("collects");
                int j = Array.length();
                JSONObject PP;
                for (int i = 0 ; i < j ; i++){
                    PP =Array.getJSONObject(i);
                    product.add(PP.getString("product_id"));
                }
            } catch (JSONException e) {
                // Do Nothing
            }
            return ans;
        }
    }

    private class Tsunami extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            String ans =  TextUtils.join(",", product);


            URL url = createUrl
                    ("https://shopicruit.myshopify.com/admin/products.json?ids=" + ans + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6");

            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                // Do Nothing
            }
            String sq = extractFeatureFrom(jsonResponse);
            return sq;
        }

        @Override
        protected void onPostExecute(String e) {
            updateui();
        }

        private void updateui(){
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            ProductAdapter Adapter = new ProductAdapter(getApplicationContext(),(ArrayList<String>) product, (ArrayList<String>) pname);

            recyclerView.setAdapter(Adapter);
            recyclerView.setHasFixedSize(true);
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                return null;
            }
            return url;
        }


        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // Do Nothing
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        private String extractFeatureFrom(String anss) {

            try {
                JSONObject Response = new JSONObject(anss);
                JSONArray Array = Response.getJSONArray("products");
                int j = Array.length();
                JSONObject PP;
                pname.clear();
                quanty.clear();
                for (int i = 0 ; i < j ; i++){
                    PP =Array.getJSONObject(i);
                    // Do Nothing
                    pname.add(PP.getString("title"));
                    JSONArray Array2 = PP.getJSONArray("variants");
                    int quantity = 0;
                    int y = Array2.length();
                    JSONObject QQ;
                    for (int p = 0 ; p < y ; p++){
                        QQ = Array2.getJSONObject(p);
                        quantity = quantity + QQ.getInt("inventory_quantity");
                    }
                    quanty.add(quantity);
                }
            } catch (JSONException e) {
                // Do Nothing
            }
            return ans;
        }
    }
}
