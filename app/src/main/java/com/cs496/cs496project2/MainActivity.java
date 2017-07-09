package com.cs496.cs496project2;


import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import okhttp3.*;


import com.cs496.cs496project2.adapter.MainViewPagerAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String[] pageTitle = {"Friends", "Me", "Match"};
    private MainViewPagerAdapter pagerAdapter;

    private void HTTPTest() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            public Void doInBackground(Void... args) {
                Log.i("작업", "시작");

                String dirPath = getFilesDir().getAbsolutePath();
                File file = new File(dirPath);

                // 일치하는 폴더가 없으면 생성
                if(!file.exists()) {
                    file.mkdirs();
                    Log.i("폴더 생성", "Success");
                }

                // txt 파일 생성
                String testStr = "ABCDE";
                File saveFile = new File(dirPath + "/test.txt");
                try {
                    if(!saveFile.exists()) {
                        FileOutputStream fos = new FileOutputStream(saveFile);
                        fos.write(testStr.getBytes());
                        fos.close();
                        Log.i("파일 생성", "Success");
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost("http://13.124.149.1/api/upload/picture/01082169122");

                MultipartEntity reqEntity = new MultipartEntity();
                reqEntity.addPart("attachment", new FileBody(new File(dirPath + "/test.txt")));
                post.setEntity(reqEntity);

                try {
                    HttpResponse response = client.execute(post);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void onPostExecute(final Void result) {
                Log.i("파일 전송", "Success");
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HTTPTest();

        //toolbar setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //navigation drawer setup
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //viewpager and tab layout setup
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

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_sync_contacts) {
            syncContacts();
        } else if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_import_local) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void syncContacts() {

    }




}
