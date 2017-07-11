package com.cs496.cs496project2.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cs496.cs496project2.R;
import com.cs496.cs496project2.helper.GuestBookItem;
import com.cs496.cs496project2.helper.ServerRequest;

public class NewGuestBookActivity extends AppCompatActivity {

    private Context context;
    private CheckBox isSecret;
    private EditText tb_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_guest_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        isSecret = (CheckBox)findViewById(R.id.isSecret);
        tb_content = (EditText)findViewById(R.id.tb_content);

        Button bt_submit = (Button)findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final SharedPreferences pref = getSharedPreferences("registration", 0);

                final String phoneNumber = pref.getString("phone_number", "");

                final boolean secret = isSecret.isChecked();
                final String content = tb_content.getText().toString();

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    public Void doInBackground(Void... args) {
                        GuestBookItem item = new GuestBookItem();
                        item.setName(pref.getString("name", ""));
                        item.setContent(content);
                        item.setSecret(secret);
                        item.setProfilePictureURL(pref.getString("profile_image_url", ""));

                        (new ServerRequest(context)).addGuestBook(phoneNumber, item);

                        return null;
                    }

                    public void onPostExecute(Void results) {
                        finish();
                    }
                }.execute();
            }
        });
    }

}
