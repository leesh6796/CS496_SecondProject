package com.cs496.cs496project2.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cs496.cs496project2.R;
import com.cs496.cs496project2.adapter.FriendAdapter;
import com.cs496.cs496project2.model.Friend;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {

    EditText editText;
    ListView listView;
    FriendAdapter adapter;
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
        resolver = this.getActivity().getApplicationContext().getContentResolver();
        cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while(cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            try {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{ id.toString()  }, null);
            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

            Friend friend = new Friend(phoneNumber, name);
            friend.setProfileImageUri(photoUri);

            friends.add(friend);
            } catch (CursorIndexOutOfBoundsException e) {
                Log.w("cursor error", id.toString());
            }

        }
        cursor.close();


        //TODO 서버에 각 친구에 대한 요청
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


        return view;
    }


}

