package com.example.shopifysiddharth;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.shopifysiddharth.adapter.CollectionAdapter;

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        TsunamiAsyncTask task = new TsunamiAsyncTask();
        task.execute();
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
            URL url = createUrl("https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6");

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
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            mCollectionAdapter = new CollectionAdapter( getApplicationContext(),(ArrayList<String>) movieList);
            if (movieList.size() == 0) {
                Log.d("sssss" , "-----------------" + " 000000");
            }

            recyclerView.setAdapter(mCollectionAdapter);
            recyclerView.setHasFixedSize(true);
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
                JSONArray Array = Response.getJSONArray("custom_collections");
                int j = Array.length();
                JSONObject PP;
                for (int i = 0 ; i < j ; i++){
                    PP =Array.getJSONObject(i);
                    movieList.add(PP.getString("title"));
                    coolid.add(PP.getString("id"));
                    JSONObject image = PP.getJSONObject("image");
                    img.add(image.getString("src"));
                    // Log.i("nono" , " -------------------------   " + PP.getString("handle"));

                    //img.add(PP.getString("src"));
                }
            } catch (JSONException e) {
            }
            // movieList = ans;
            return ans;
        }
    }
}
