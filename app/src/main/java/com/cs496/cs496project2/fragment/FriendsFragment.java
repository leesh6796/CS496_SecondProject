package com.cs496.cs496project2.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.cs496.cs496project2.R;
import com.cs496.cs496project2.activity.VisitActivity;
import com.cs496.cs496project2.adapter.FriendAdapter;
import com.cs496.cs496project2.helper.ServerRequest;
import com.cs496.cs496project2.model.Friend;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {

    EditText editTextFilter;
    ListView listView;
    public FriendAdapter adapter;
    ArrayList<Friend> friends;


    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        listView = (ListView) view.findViewById(R.id.friends);
        editTextFilter = (EditText)view.findViewById(R.id.editTextFilter);
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
        update();
        return view;
    }

    public void update() {

        friends = new ArrayList<>();

        (new AsyncTask<Void, Void, List<Friend>>() {
            @Override
            protected List<Friend> doInBackground(Void... voids) {
                return (new ServerRequest(getActivity())).getContacts();
            }

            @Override
            protected void onPostExecute(List<Friend> temp) {
                friends = (ArrayList<Friend>) temp;
                adapter = new FriendAdapter(getActivity(), friends);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            }
        }).execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterVIew, View view, int position, long id){
                Friend friend = (Friend) adapter.getItem(position);
                if(friend.isRegistered(getActivity())) {
                    Intent visitIntent = new Intent(getActivity(), VisitActivity.class);
                    visitIntent.putExtra("friend", friend);
                    startActivity(visitIntent);
                } else {
                    Toast.makeText(getActivity(), friend.getName() + " has not registered", Toast.LENGTH_LONG).show();
                }
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("friend", friend);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                GalleryFragment galleryFragment = new GalleryFragment();
                galleryFragment.setArguments(bundle);
                ft.replace(R.id.friends_tap_content, galleryFragment, "gallery");
                ft.addToBackStack(null);
                ft.commit();*/
            }
        });


        listView.setTextFilterEnabled(true);





    }


}

