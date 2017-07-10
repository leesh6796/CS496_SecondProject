package com.cs496.cs496project2;


import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


import com.cs496.cs496project2.activity.LoginActivity;
import com.cs496.cs496project2.adapter.MainViewPagerAdapter;
import com.cs496.cs496project2.helper.ServerRequest;
import com.cs496.cs496project2.model.Friend;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] pageTitle = {"Friends", "Me", "Match"};
    private MainViewPagerAdapter pagerAdapter;

    private CallbackManager callbackManager;
    private AccessToken accessToken;

    ContentResolver resolver;
    Cursor cursor;

    public static String myPhoneNumber = "";
    public static String myEmail = "";
    public static String myName = "";
    public static String myProfileImageURL = "";

    static final int PICK_IMAGE_REQUEST = 2;
    static final int CAMERA_REQUEST = 3;
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

    //TODO: option에 로그아웃 만들기

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sync_friends) {
            syncFriends(); //TODO -> 자동으로 다시 로그인 하기?, Intent 전달?
        } else if (id == R.id.nav_camera) {
            camera();
        } else if (id == R.id.nav_import_local) {
            importFromLocalStorage();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRegistration() {
        Intent registrationIntent = new Intent(this, LoginActivity.class);
        startActivity(registrationIntent);
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
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        //viewpager and tab layout setup//////////////////////////////////////////////////////////////////
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for(int i = 0; i < 3; i++)
            tabLayout.addTab(tabLayout.newTab().setText(pageTitle[i]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////
    }


    public String getMyPhoneNumber() {
        return getSharedPreferences("registration", 0).getString("phone_number", "");
    }



    //TODO: 폰 연락처 받아와 서버로 올려보냄(일부), 내부저장소에 저장 -> 구현 방식?
    private void syncFriends() {

        final int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
        if (permissionCheck== PackageManager.PERMISSION_GRANTED){
            //makeText(this, "연락처 열람 권한 있음.", Toast.LENGTH_LONG).show();
        } else {
            makeText(this, "연락처 열람 권한 없음.", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_CONTACTS)){
                makeText(this, "연락처 권한 설명 필요함.", Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS},1);
            }
        }

        resolver = this.getContentResolver();
        cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC");


        //TODO 서버로 보내기: ServerRequest 형식에 맞춰 수정할것, 보낼정보: 번호와 이름 뿐. -> 꼭 여기서 처리해야하는지?
        JSONArray friends = new JSONArray();
        while (cursor.moveToNext()) {
            JSONObject friend = new JSONObject(); //그냥 array만 해도 괜찮을듯

            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

            //Log.i("MY INFO", id + " = " + name);
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //Log.i("MY INFO", id + " = " + phoneNumber);

                //TODO
            }

            friends.put(friend);
        }

    }


    private void importFromLocalStorage() { //갤러리에서 선택 후 onActivityResult로
        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: 서버로 이미지 올리기
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            String fileNameInDB = (new File(imageUri.toString())).getName();

            Log.d("     captured image", fileNameInDB);
        }
        else if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();


            String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            String fileNameInDB = myPhoneNumber + "_" + timeStamp + ".jpg";
            Log.d("        chosen image", fileNameInDB);
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

        }
    }
}



