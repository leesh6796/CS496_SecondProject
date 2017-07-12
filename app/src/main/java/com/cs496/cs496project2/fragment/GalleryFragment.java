package com.cs496.cs496project2.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cs496.cs496project2.MainActivity;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.activity.ImageViewerActivity;
import com.cs496.cs496project2.adapter.GridViewAdapter;
import com.cs496.cs496project2.helper.GridViewImgItem;
import com.cs496.cs496project2.helper.ServerRequest;
import com.cs496.cs496project2.model.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memorial on 2017. 7. 11..
 */

public class GalleryFragment extends Fragment {
    private GridViewAdapter adapter;
    GridView grid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new GridViewAdapter();

        grid = (GridView) getView().findViewById(R.id.galleryGridView);
        grid.setAdapter(adapter);

        // GridView Item Click EventListener
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int pos, long id) {
                GridViewImgItem item = (GridViewImgItem)parent.getItemAtPosition(pos);

                Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                intent.putExtra("url", adapter.getItem(pos).getUrl());
                startActivity(intent);
            }
        });

        update();
    }

    // Glide로 사진을 GridView에 추가한다.
    public void update() {
        adapter.clearItem();

        Bundle args = getArguments();
        if(args == null) { //내 갤러리
            new AsyncTask<Void, Void, List<String>>() {
                @Override
                public List<String> doInBackground(Void... args) {
                    String phoneNumber = MainActivity.myPhoneNumber;
                    Log.i("phoneNumber", phoneNumber);
                    return (new ServerRequest(getActivity())).getGallery(phoneNumber);

                }

                @Override
                public void onPostExecute(List<String> results) {
                    for(String ele : results) {
                        Log.i("imgs", ele);
                        adapter.addItem(ele);
                    }
                    adapter.notifyDataSetChanged();
                }
            }.execute();
        } else {
            final String phoneNumber = args.getString("phoneNumber");

            new AsyncTask<Void, Void, List<String>>() {
                @Override
                public List<String> doInBackground(Void... args) {
                    // 다른 사람 갤러리 로드할 때
                    return  (new ServerRequest(getActivity())).getGallery(phoneNumber);
                }
                @Override
                public void onPostExecute(List<String> results) {
                    for (String ele : results) {
                        Log.i("imgs", ele);
                        adapter.addItem(ele);
                    }
                    adapter.notifyDataSetChanged();
                }
            }.execute();
        }
    }
}
