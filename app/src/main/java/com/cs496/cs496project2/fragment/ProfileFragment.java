package com.cs496.cs496project2.fragment;


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
import com.cs496.cs496project2.adapter.ProfileAdapter;
import com.cs496.cs496project2.model.Image;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private ArrayList<Image> images;
    private ProfileAdapter adapter;
    private RecyclerView recyclerView;


    public ProfileFragment() {
        // Required empty public constructor
        //TODO 내 프로필
    }

    public ProfileFragment(Bundle bundle) {
        //TODO 적절히 넘겨받은 친구의 프로필

    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.profile_recycler_view);

        images = new ArrayList<>();
        images.add(null);images.add(null);images.add(null);images.add(null);images.add(null);
        images.add(null);images.add(null);images.add(null);images.add(null);images.add(null);

        adapter = new ProfileAdapter(getActivity().getApplicationContext());
        adapter.setHeader(new Image(1));
        adapter.setItems(images);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new ProfileAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ProfileAdapter.ClickListener() {
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
