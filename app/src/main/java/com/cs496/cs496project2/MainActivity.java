package com.cs496.cs496project2;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cs496.cs496project2.activity.GuestBookActivity;
import com.cs496.cs496project2.activity.LoginActivity;
import com.cs496.cs496project2.adapter.MainViewPagerAdapter;
import com.cs496.cs496project2.fragment.FriendsFragment;
import com.cs496.cs496project2.fragment.GalleryFragment;
import com.cs496.cs496project2.helper.ServerRequest;
import com.cs496.cs496project2.model.Friend;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ViewPager viewPager;
    private MainViewPagerAdapter pagerAdapter;
    private Activity thisActivity = this;

    private ImageView navigationProfileImage;
    private TextView navigationName, navigationPhoneNumber;

    private CallbackManager callbackManager;
    private AccessToken accessToken;

    ContentResolver resolver;
    Cursor cursor;

    public static String myPhoneNumber = "";
    public static String myEmail = "";
    public static String myName = "";
    public static String myProfileImageURL = "";

    final int READ_CONTACT_CODE = 987;
    final int WRITE_EXTERNAL_STORAGE_CODE = 745;
    static final int LOG_IN_REQUEST =1231;
    static final int PICK_IMAGE_REQUEST = 2324;
    static final int CAMERA_REQUEST = 35435;
    String mCurrentPhotoPath;

    private ArrayList<Friend> rv = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("registration", 0);
        if(!pref.getBoolean("isRegistered",false))
            initRegistration();

        initViews();

        myPhoneNumber = getMyPhoneNumber();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sync_friends) {
            trySyncFriends();
        } else if (id == R.id.nav_camera) {
            camera();
        } else if (id == R.id.nav_import_local) {
            tryImport();
        } else if (id == R.id.nav_logoff) {
            logoff();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRegistration() {

        Intent registrationIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(registrationIntent, LOG_IN_REQUEST);
    }

    private void initViews() {
        //toolbar setup///////////////////////////////////////////////////////////////////////////
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navigation drawer setup////////////////////////////////////////////////////////////////////
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);

        navigationProfileImage = (ImageView) navHeader.findViewById(R.id.nav_header_image);
        navigationName = (TextView) navHeader.findViewById(R.id.nav_header_name);
        navigationPhoneNumber = (TextView) navHeader.findViewById(R.id.nav_header_phone_number);

        SharedPreferences pref = getSharedPreferences("registration", 0);
        navigationPhoneNumber.setText(pref.getString("phone_number", ""));
        navigationName.setText(pref.getString("name", ""));
        myProfileImageURL = pref.getString("profile_image_url", "");

        Glide.with(this)
                .load(myProfileImageURL)
                .placeholder(R.drawable.person)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(navigationProfileImage);


        ////////////////////////////////////////////////////////////////////////////////////////////////////

        //viewpager and tab layout setup//////////////////////////////////////////////////////////////////
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

    }


    public String getMyPhoneNumber() {
        return getSharedPreferences("registration", 0).getString("phone_number", "");
    }

    //TODO: 폰 연락처 받아와 서버로 올려보냄(일부), 구현 방식?

    private void trySyncFriends() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            syncFriends();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACT_CODE);
        }
    }

    private void syncFriends() {
        final List<Friend> friends = new ArrayList<>();

        resolver = thisActivity.getContentResolver();
        cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[\\s\\-()]", "");;
                friends.add(new Friend(phoneNumber, name));
            }
        }
        cursor.close();

        (new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                new ServerRequest(thisActivity).setContacts(friends);
                return null;
            }
            @Override
            protected void onPostExecute(Void v) {
                update();
            }
        }).execute();
    }



    private void tryImport() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            importFromLocalStorage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    private void importFromLocalStorage() { //갤러리에서 선택 후 onActivityResult로
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
        pickImageIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(pickImageIntent, "Select picture to upload"), PICK_IMAGE_REQUEST);
    }

    private void camera() { // -> 카메라 실행 후 onActivityResult로
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cs496.cs496project2.fileprovider",
                        photoFile);
                Log.d("camera", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    //helper for camera()
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        String imageFileName = myPhoneNumber +"_" +  timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }


    private void logoff() {
        getSharedPreferences("registration",0).edit().clear().commit();
        initRegistration();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOG_IN_REQUEST && resultCode == RESULT_OK) {
            trySyncFriends();
        }

        else if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            final Uri imageUri = Uri.parse(mCurrentPhotoPath);

            final String fileNameInDB = (new File(imageUri.toString())).getName();
            Log.e("     captured image", imageUri.toString());
            (new AsyncTask<Void,Void,Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    (new ServerRequest(thisActivity)).uploadFile(fileNameInDB, new File(imageUri.getPath()));
                    return null;
                }
                @Override
                protected void onPostExecute(Void v) {
                    update();
                }
            }).execute();

        }

        else if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {

            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            final String fileNameInDB = myPhoneNumber + "_" + timeStamp + ".jpg";

            Uri uri = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, filePath, null, null, null);
            cursor.moveToFirst();
            final String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();
            Log.e("        chosen image", uri.getPath());

            (new AsyncTask<Void,Void,Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    (new ServerRequest(thisActivity)).uploadFile(fileNameInDB, new File(imagePath));
                    return null;
                }
                @Override
                protected void onPostExecute(Void v) {
                    update();
                }
            }).execute();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case READ_CONTACT_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    syncFriends();
                }
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    importFromLocalStorage();
                }
        }
    }

    public void update() {
        FriendsFragment ff = (FriendsFragment) pagerAdapter.getRegisteredFragment(0);
        GalleryFragment pf = (GalleryFragment) pagerAdapter.getRegisteredFragment(1);
        ff.update();
        pf.update();
    }

}



