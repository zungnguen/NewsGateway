package com.example.newsgatewayfinal;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

public class SourceRunnable implements Runnable {

    String pass;
    private MainActivity mainActivity;
    private static final String TAG = "NewsSourceDownloader";
    private final String APIKEY = "https://newsapi.org/v1/sources?language=en&country=us&category=";
    private final String apiresourcekey ="&apiKey=50d1b28d27984532b5f0d2c5adaa9015";
    ArrayList<NewsSource> newsresourcelist = new ArrayList<>();
    ArrayList<String> newsresourcecategory = new ArrayList<>();
    ArrayList<String> newsresourcecategory1 = new ArrayList<>();
    HashMap<Integer, NewsSource> map = new HashMap<>();

    private String category;

    public SourceRunnable(MainActivity ma){
        mainActivity = ma;
    }
    public SourceRunnable(MainActivity ma, String item){
        mainActivity = ma;
        category = item;
    }
    @Override
    public void run() {
        String urlToUse;
        if (category != null) {
            if(category.equals("all")){
                Uri.Builder buildURL = Uri.parse(APIKEY+apiresourcekey).buildUpon();
                urlToUse = buildURL.build().toString();
            }
            else{
                Uri.Builder buildURL = Uri.parse(APIKEY+category+apiresourcekey).buildUpon();
                urlToUse = buildURL.build().toString();
            }
        }
        else{

            Uri.Builder buildURL = Uri.parse(APIKEY+apiresourcekey).buildUpon();
            urlToUse = buildURL.build().toString();

        }


        StringBuilder sb = new StringBuilder();


        try {
            URL url = new URL(urlToUse);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/88.0");
            connection.connect();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {

                handleResults(null);
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }



        } catch (Exception e) {

            handleResults(null);
            return;
        }
        handleResults(sb.toString());

    }

    public void handleResults(final String jsonString){
        if(jsonString==null){

            mainActivity.runOnUiThread(() -> mainActivity.error404());
            return;
        }
        final ArrayList<NewsSource> sourcelist = parseJSON(jsonString);

        mainActivity.runOnUiThread(()->{
            if(sourcelist!=null)
                Toast.makeText(mainActivity,"with size"+sourcelist.size(),Toast.LENGTH_LONG).show();
            mainActivity.setSources(sourcelist,newsresourcecategory1);
        });
    }

    private ArrayList<NewsSource> parseJSON(String s){
        String id = null, name = null, url = null, category = null;
        try
        {
            JSONObject jr1 = new JSONObject(s);
            JSONArray response1 = jr1.getJSONArray("sources");


            for(int i =0; i<response1.length(); i++)
            {
                {
                    JSONObject jb1 = response1.getJSONObject(i);
                    id = jb1.getString("id");

                    name = jb1.getString("name");

                    url = jb1.getString("url");

                    category = jb1.getString("category");

                }
                newsresourcelist.add(new NewsSource(id, name, url, category));
                newsresourcecategory.add(category);
            }

            Set<String> hashmap = new HashSet<>();
            hashmap.addAll(newsresourcecategory);
            newsresourcecategory.clear();
            newsresourcecategory1.addAll(hashmap);



            return newsresourcelist;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
