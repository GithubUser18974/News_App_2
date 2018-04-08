package com.example.mohamed_araby.newsappupgrade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed_Araby on 4/8/2018.
 */

public class NewsEnvironmentFragmant extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsData>> {


    private final String URL = "http://content.guardianapis.com/search";
    private final int LOADER_ID = 4;
    private final String NewsType = "environment";
    private NewsAdapter newsAdapter;
    private TextView networkState;
    private boolean isConnected;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // check if there is internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        LoaderManager loaderManager = getLoaderManager();
        ListView listView = view.findViewById(R.id.news_list);
        networkState = (TextView) view.findViewById(R.id.network_state);
        progressBar = view.findViewById(R.id.progress_bar);
        newsAdapter = new NewsAdapter(getActivity(), new ArrayList<NewsData>());
        listView.setAdapter(newsAdapter);
        listView.setEmptyView(networkState);

        if (isConnected) {
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            networkState.setText(R.string.no_internet);
            progressBar.setVisibility(View.GONE);

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsData currentEvent = newsAdapter.getItem(i);
                openWebPage(currentEvent.getWebUrl());
            }
        });
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String minEventNumber = sharedPrefs.getString(
                getString(R.string.settings_min_page_number_key),
                getString(R.string.settings_min_page_number_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", NewsType);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", minEventNumber);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("api-key", getResources().getString(R.string.api_key));
        return new NewsLoader(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> events) {
        progressBar.setVisibility(View.GONE);
        networkState.setText(R.string.no_news);
        newsAdapter.clear();
        if (events != null && !events.isEmpty())
            newsAdapter.addAll(events);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        newsAdapter.clear();
    }
}
