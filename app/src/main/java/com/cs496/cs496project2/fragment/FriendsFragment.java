package com.cs496.cs496project2.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.cs496.cs496project2.R;
import com.cs496.cs496project2.adapter.FriendAdapter;
import com.cs496.cs496project2.model.Friend;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {

    EditText editText;
    ListView listView;
    public FriendAdapter adapter;
    ArrayList<Friend> friends = new ArrayList<>();

    //핸드폰 저장소에서 불러오기 위해.
    ContentResolver resolver;
    Cursor cursor;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        friends.clear();

        //TODO: 먼저 내부저장소에서 정보가져온다. 그 후 서버에도 정보가 있으면 덮어쓴다(프로필 사진을) 중복?

        //TODO 서버에 각 친구에 대한 요청
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        listView = (ListView) view.findViewById(R.id.friends);
/*
        (new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Friend> temp = new ServerRequest().getContacts();
                for (Friend friend : temp) {
                    friends.add(friend);
                }
                return null;
            }
        }).execute();*/


        adapter = new FriendAdapter(getActivity(), friends);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterVIew, View view, int position, long id){
                Friend friend = (Friend) adapter.getItem(position);

                //TODO: 태스트해봐야.
                Bundle bundle = new Bundle();
                bundle.putSerializable("friend", friend);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ProfileFragment profileFragment = ProfileFragment.newInstance();
                profileFragment.setArguments(bundle);
                ft.replace(R.id.friends_tap_content, profileFragment, "profile");
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        listView.setTextFilterEnabled(true);

        EditText editTextFilter = (EditText)view.findViewById(R.id.editTextFilter);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                String filterText = editable.toString();
                adapter.getFilter().filter(filterText);
            }
        });

        adapter.notifyDataSetChanged();
        return view;
    }

    public void update() {

    }


}

