package com.runops.instagramviewer;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.runops.instagramviewer.adapter.MediaArrayAdapter;
import com.runops.instagramviewer.api.InstagramApi;
import com.runops.instagramviewer.model.Caption;
import com.runops.instagramviewer.model.Media;
import com.runops.instagramviewer.model.Popular;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class FeedActivity extends ActionBarActivity {

    private ArrayList<Media> items;
    private MediaArrayAdapter feedAdapter;

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        getSupportActionBar();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });

        fetchPopularPhotos();

        ListView lvFeed = (ListView) findViewById(R.id.lvFeed);
        items = new ArrayList<Media>();
        feedAdapter = new MediaArrayAdapter(this, items);
        lvFeed.setAdapter(feedAdapter);
    }


    public void fetchPopularPhotos() {
        InstagramApi.getInstagramApiClient().getPopularMedia(
                getString(R.string.instagram_client_id), new Callback<Popular>() {

            @Override
            public void success(Popular popular, Response response) {
                List<Media> mediaList = popular.getMediaList();
                feedAdapter.clear();
                items.addAll(mediaList);
                feedAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("viewer", "problem occurred!", error);
                Toast.makeText(getApplicationContext(), "Problem fetching", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

}
