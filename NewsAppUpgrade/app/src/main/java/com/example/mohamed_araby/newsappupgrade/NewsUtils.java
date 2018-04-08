package com.example.mohamed_araby.newsappupgrade;

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

/**
 * Created by Mohamed_Araby on 4/6/2018.
 */
public class NewsUtils {

    public NewsUtils() {
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponce = "";
        if (url == null)
            return jsonResponce;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponce = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return jsonResponce;
    }

    /**
     * takes the json response to extract the events features from it
     *
     * @param jsonResponce the jsonResponse to be parsed
     * @return list of the events
     */
    private static List<NewsData> extractFeatureFromJson(String jsonResponce) {
        List<NewsData> eventList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonResponce);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject eventJsonObject = results.getJSONObject(i);
                String type = eventJsonObject.getString("sectionName");
                String date = eventJsonObject.getString("webPublicationDate");
                String title = eventJsonObject.getString("webTitle");
                String url = eventJsonObject.getString("webUrl");
                JSONArray tags = eventJsonObject.getJSONArray("tags");
                String name = "";
                if (tags.length() > 0) {
                    // get the first author name
                    JSONObject tagObject = tags.getJSONObject(0);
                    name = tagObject.getString("webTitle");
                    // get the names of the rest of authors if exist
                    for (int j = 1; j < tags.length(); j++) {
                        JSONObject tagObjectx = tags.getJSONObject(j);
                        name += " - " + tagObjectx.getString("webTitle");
                    }
                }
                NewsData event = new NewsData(title, type, date, name, url);
                eventList.add(event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public static List<NewsData> fetchEarthquakeData(String jsonString) {
        URL url = createUrl(jsonString);
        String jsonResbonce = null;
        try {
            jsonResbonce = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<NewsData> eventList = extractFeatureFromJson(jsonResbonce);
        return eventList;
    }
}
