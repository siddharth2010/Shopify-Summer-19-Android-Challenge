package com.example.shopifysiddharth;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

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


    private RecyclerView recyclerView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Intent intent = getIntent();
        Log.d("pp","    -----------------     ");
        String collectionid = intent.getStringExtra("key");
        ans = collectionid;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();

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
                // TODO Handle the IOException
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
            // TextView prim = (TextView) findViewById(R.id.primary);
            //prim.setText(test);
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            //recyclerView.setLayoutManager(mLayoutManager);
            //recyclerView.setItemAnimator(new DefaultItemAnimator());

            Log.i("momo" , " -------------------------   ");

            // ProductAdapter Adapter = new ProductAdapter(getApplicationContext(),(ArrayList<String>) ProductActivity);

            //recyclerView.setAdapter(Adapter);

            //recyclerView.setHasFixedSize(true);
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
                // TODO: Handle the exception
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
                    //movieList.add(PP.getString("title"));
                    product.add(PP.getString("product_id"));
                    //coolid.add(PP.getString("id"));
                    // Log.d("tot" , " ---------------------   " + String.valueOf(PP.getString("id")));
                }
            } catch (JSONException e) {
            }
            // movieList = ans;
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
                // TODO Handle the IOException
            }
            String sq = extractFeatureFrom(jsonResponse);
            return sq;
        }

        @Override
        protected void onPostExecute(String e) {
            updateui();
        }

        private void updateui(){
            // TextView prim = (TextView) findViewById(R.id.primary);
            //prim.setText(test);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            Log.i("momo" , " -------------------------   ");

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
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } catch (IOException e) {
                // TODO: Handle the exception
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
                    //movieList.add(PP.getString("title"));
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
                    //coolid.add(PP.getString("id"));
                    Log.d("tot" , " ---------------------   " + PP.getString("title"));
                }
            } catch (JSONException e) {
            }
            // movieList = ans;
            return ans;
        }
    }
}
