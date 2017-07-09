package com.cs496.cs496project2.fragment;

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
    FriendAdapter adapter;
    ArrayList<Friend> friends = new ArrayList<>();

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);

        //TODO: 서버에서 친구 정보 가져와라 가 아니라 내부저장소에 저장한 것을 불러오도록 하자

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        listView = (ListView) view.findViewById(R.id.friends);

        adapter = new FriendAdapter(getActivity(), friends);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterVIew, View view, int position, long id){
                Friend friend = (Friend) adapter.getItem(position);

                //TODO: ProfileFragment에 적절한 정보 넘긴다.
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


        return view;
    }


}

