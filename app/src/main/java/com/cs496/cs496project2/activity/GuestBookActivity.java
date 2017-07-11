package com.cs496.cs496project2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.adapter.GuestBookAdapter;
import com.cs496.cs496project2.helper.GuestBookItem;
import com.cs496.cs496project2.helper.ServerRequest;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class GuestBookActivity extends AppCompatActivity {

    private GuestBookAdapter adapter;
    private boolean myGuestBook = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref = getSharedPreferences("registration", 0);

        String name = pref.getString("name", "");
        String profileImageURL = pref.getString("profile_image_url", "");
        final String phoneNumber = pref.getString("phone_number", "");

        Log.i("name", name);
        Log.i("URL", profileImageURL);
        Log.i("phoneNumber", phoneNumber);

        setTitle(name);
        Glide.with(this).load(profileImageURL).centerCrop().into((ImageView)findViewById(R.id.AppBarBackground));

        adapter = new GuestBookAdapter();
        ListView listView = (ListView)findViewById(R.id.guestBookList);
        listView.setAdapter(adapter);

        final Context context = this;

        new AsyncTask<Void, Void, List<GuestBookItem>>() {
            @Override
            public List<GuestBookItem> doInBackground(Void... args) {
                List<GuestBookItem> items = (new ServerRequest(context)).getGuestBook(phoneNumber, false);

                return items;
            }

            @Override
            public void onPostExecute(List<GuestBookItem> results) {
                for(GuestBookItem item : results) {
                    if(!myGuestBook) {
                        if(item.isSecret()) continue;
                    }
                    adapter.addItem(item);
                }
                adapter.notifyDataSetChanged();
            }
        }.execute();

        /*Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        PhotoView photoView = (PhotoView)findViewById(R.id.targetImg);
        Glide.with(this).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(photoView);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
}
