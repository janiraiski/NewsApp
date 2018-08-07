package com.example.janiraiski.newsapp;

import android.text.TextUtils;
import android.util.Log;

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

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<Article> fetchArticleData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, "Made HTTP request");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Article> articles = extractFeatureFromJson(jsonResponse);

        return articles;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
            Log.i(LOG_TAG, "Building the URL");
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            Log.i(LOG_TAG, "Connection opened");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            Log.i(LOG_TAG, "Requested \"GET\" method");
            urlConnection.connect();
            Log.i(LOG_TAG, "Connected");

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.i(LOG_TAG, "Response code: " + urlConnection.getResponseCode());
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
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

    private static String readFromStream(InputStream inputStream) throws IOException {
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


    public static List<Article> extractFeatureFromJson(String articleJSON) {

        //Log.i(LOG_TAG, "\n\narticleJSON: " + articleJSON + "\n");

        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();

        try {
            Log.i(LOG_TAG, "Extracting features");
            JSONObject rootJsonObject = new JSONObject(articleJSON);

            JSONObject responseJsonObject = rootJsonObject.getJSONObject("response");

            JSONArray resultsJsonArray = responseJsonObject.getJSONArray("results");

            for (int i = 0; i < resultsJsonArray.length(); i++) {
                JSONObject currentArticle = resultsJsonArray.getJSONObject(i);
                Log.i(LOG_TAG, "Created currentArticle JSONObject");

                String title = currentArticle.getString("webTitle");

                String section = currentArticle.getString("sectionName");

                String url = currentArticle.getString("webUrl");

                String date = currentArticle.getString("webPublicationDate");

                String author = "";

                JSONArray tagsJsonArray = currentArticle.getJSONArray("tags");
                for (int j = 0; j < tagsJsonArray.length(); j++) {
                    Log.i(LOG_TAG, "searching for author");
                    JSONObject currentTag = tagsJsonArray.getJSONObject(j);

                    author = currentTag.getString("webTitle");
                    Log.i(LOG_TAG, "author: " + author);
                }

                Article article = new Article(title, section, url, author, date);

                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return articles;
    }
}
