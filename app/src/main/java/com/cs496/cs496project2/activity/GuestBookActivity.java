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
    private String phoneNumber;
    private boolean myGuestBook = true;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref = getSharedPreferences("registration", 0);

        String name = pref.getString("name", "");
        String profileImageURL = pref.getString("profile_image_url", "");
        phoneNumber = pref.getString("phone_number", "");

        setTitle(name);
        Glide.with(this).load(profileImageURL).centerCrop().into((ImageView)findViewById(R.id.AppBarBackground));

        adapter = new GuestBookAdapter();
        ListView listView = (ListView)findViewById(R.id.guestBookList);
        listView.setAdapter(adapter);

        context = this;

        update();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewGuestBookActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        update();
    }

    public void update() {
        adapter.clearItem();

        new AsyncTask<Void, Void, List<GuestBookItem>>() {
            @Override
            public List<GuestBookItem> doInBackground(Void... args) {
                List<GuestBookItem> items = (new ServerRequest(context)).getGuestBook(phoneNumber);

                return items;
            }

            @Override
            public void onPostExecute(List<GuestBookItem> results) {
                int i;

                for(i=results.size()-1; i>=0; i--) {
                    GuestBookItem item = results.get(i);
                    Log.i("index", String.valueOf(i));
                    if(!myGuestBook) {
                        if(item.isSecret()) continue;
                    }
                    Log.i("item", item.getContent());
                    adapter.addItem(item);
                }
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
