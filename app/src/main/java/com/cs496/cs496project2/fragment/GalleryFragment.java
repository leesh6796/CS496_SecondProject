package com.cs496.cs496project2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs496.cs496project2.R;
import com.cs496.cs496project2.adapter.GalleryAdapter;
import com.cs496.cs496project2.model.Image;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;

import java.util.ArrayList;


public class GalleryFragment extends Fragment {

    private ArrayList<Image> images;
    private GalleryAdapter adapter;
    private RecyclerView recyclerView;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_thumbnails);


        images = new ArrayList<>();

        images.add(null);images.add(null);images.add(null);images.add(null);images.add(null);
        images.add(null);images.add(null);images.add(null);images.add(null);images.add(null);

        adapter = new GalleryAdapter(getActivity().getApplicationContext(), images);

        adapter.setHeader(new Image(1));
        adapter.setItems(images);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                SlideShowFragment newFragment = SlideShowFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //fetchImages();
        return root;
    }



}
