package com.example.mohamed_araby.newsappupgrade;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Mohamed_Araby on 4/6/2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {

    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsData> loadInBackground() {
        if (url == null)
            return null;
        List<NewsData> eventList = NewsUtils.fetchEarthquakeData(url);
        return eventList;
    }
}
