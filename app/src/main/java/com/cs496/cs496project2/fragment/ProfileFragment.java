package com.cs496.cs496project2.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs496.cs496project2.MainActivity;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.adapter.ProfileAdapter;
import com.cs496.cs496project2.model.Friend;
import com.cs496.cs496project2.model.Image;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    private ArrayList<Image> images;
    private Image header;
    public ProfileAdapter adapter;
    private RecyclerView recyclerView;
    String phoneNumber;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
        // Required empty public constructor
        //TODO 내 프로필: Argument없으면 내 정보로 세팅한다
    }

//    public ProfileFragment(Bundle bundle) {
//        //TODO 적절히 넘겨받은 친구의 프로필 -> 내가 아니라 친구의 프로필이면 fab 나오게 해서 ***을 한다
//
//    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.profile_recycler_view);

        Bundle args = getArguments();
        if (args != null) {
            //전달받은 친구 프로필 <- 친구의 정보는 friendfragment에서 받아오므로 여기서는 데이터를 사용만 한다
            Friend friend = (Friend) args.getSerializable("friend");
            phoneNumber = friend.getPhoneNumber();
            header = new Image();
            header.setImageUrl(friend.getProfileImageUrl());
            header.setTitle(friend.getName());
            header.setDescription(friend.getEmail());
            //이미지 받아오기
            images = friend.fetchImages();

        } else {
            phoneNumber = MainActivity.myPhoneNumber;
            header = new Image();
            images = new ArrayList<>();
            SharedPreferences pref = getActivity().getSharedPreferences("registration", 0);
            String myProfileImageUrl = pref.getString("profile_image_url", "");
            header.setImageUrl(myProfileImageUrl);
        }


        adapter = new ProfileAdapter(getActivity().getApplicationContext(), phoneNumber);
        adapter.setHeader(header);
        adapter.setItems(images);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        //TODO: 이거 header에 간섭 받는지 확인
        recyclerView.addOnItemTouchListener(new ProfileAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ProfileAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", position);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SlideShowFragment slideShowFragment = SlideShowFragment.newInstance();
                slideShowFragment.setArguments(bundle);
                slideShowFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //fetchImages();
        return view;
    }

    public void update() {

    }

}
