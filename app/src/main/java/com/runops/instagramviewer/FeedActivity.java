package com.runops.instagramviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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

    private static final String CLIENT_ID = "db6e497b51c74edaab8939e9308904ef";

    private ArrayList<Media> items;
    private MediaArrayAdapter feedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        fetchPopularPhotos();

        ListView lvFeed = (ListView) findViewById(R.id.lvFeed);
        items = new ArrayList<Media>();
        feedAdapter = new MediaArrayAdapter(this, items);
        lvFeed.setAdapter(feedAdapter);
    }


    public void fetchPopularPhotos() {
        InstagramApi.getInstagramApiClient().getPopularMedia(CLIENT_ID, new Callback<Popular>() {
            @Override
            public void success(Popular popular, Response response) {
                List<Media> mediaList = popular.getMediaList();
                items.addAll(mediaList);
                feedAdapter.notifyDataSetChanged();

                Log.i("viewer","everything is great!");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("viewer", "problem occurred!", error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
