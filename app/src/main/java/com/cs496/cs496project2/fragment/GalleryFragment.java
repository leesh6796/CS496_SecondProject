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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by memorial on 2017. 7. 11..
 */

public class GalleryFragment extends Fragment {
    private GridViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        adapter = new GridViewAdapter();

        GridView grid = (GridView)rootView.findViewById(R.id.galleryGridView);
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

        return rootView;
    }

    // Glide로 사진을 GridView에 추가한다.
    public void update() {
        adapter.clearItem();

        Bundle args = getArguments();

        new AsyncTask<Void, Void, List<String>>() {
            @Override
            public List<String> doInBackground(Void... args) {
                List<String> urls;

                // 내 갤러리를 로드할 때
                if(args instanceof Void[]) {
                    String phoneNumber = MainActivity.myPhoneNumber;
                    Log.i("phoneNumber", phoneNumber);
                    urls = (new ServerRequest(getActivity())).getGallery(phoneNumber);

                }
                // 다른 사람 갤러리 로드할 때
                else {
                    urls = new ArrayList<String>();
                }

                return urls;
            }

            @Override
            public void onPostExecute(List<String> results) {
                for(String ele : results) {
                    Log.i("imgs", ele);
                    adapter.addItem(ele);
                }
            }
        }.execute();
    }
}
