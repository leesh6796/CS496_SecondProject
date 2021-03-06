package com.cs496.cs496project2.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import com.cs496.cs496project2.MainActivity;
import com.cs496.cs496project2.R;
import com.cs496.cs496project2.helper.ImageHelper;
import com.cs496.cs496project2.helper.ServerRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import static android.widget.Toast.makeText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private ImageView profileImageView;
    private TextView greeting;
    private EditText phoneNumberView;
    private View mProgressView;
    private View mLoginFormView;
    Button registerButton;
    private boolean postingEnabled = false;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    Profile profile;
    ProfileTracker profileTracker;

    String phoneNumber;
    String name;

    private Activity thisActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        //facebook login & get access token///////////////////////////////////////////////////////////////////////
        loginButton = (LoginButton) findViewById(R.id.btn_fb_login);
        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions(Arrays.asList("public_profile"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Connected to Facebook", Toast.LENGTH_SHORT).show();
                postingEnabled = true;
                phoneNumberView.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
                profile = Profile.getCurrentProfile();

                updateUI();
                SharedPreferences pref = getSharedPreferences("fb", 0);
                SharedPreferences.Editor edit = pref.edit();
                ///profile.getProfilePictureUri(500,500);
            }
            @Override
            public void onCancel() { }
            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.
            }
        };

        // Set up the login form.
        phoneNumberView = (EditText) findViewById(R.id.input_phone_number);
        profileImageView = (ImageView) findViewById(R.id.login_profile_picture);
        greeting = (TextView) findViewById(R.id.greeting);
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }


    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        phoneNumberView.setEnabled(enableButtons);
        registerButton.setEnabled(enableButtons);

        Profile profile = Profile.getCurrentProfile();
        if (enableButtons && profile != null) {
            new LoadProfileImage(profileImageView).execute(profile.getProfilePictureUri(200, 200).toString());
            greeting.setText("Hello, " + profile.getFirstName() + "!");
            postingEnabled = true;
            registerButton.setVisibility(View.VISIBLE);
            phoneNumberView.setVisibility(View.VISIBLE);

        } else {
            Bitmap icon = BitmapFactory.decodeResource(this.getResources(),R.drawable.placeholder);
            profileImageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getApplicationContext(), icon, 200, 200, 200, false, false, false, false));
            greeting.setText(null);
            postingEnabled = false;
            registerButton.setVisibility(View.GONE);
            phoneNumberView.setVisibility(View.GONE);
        }
    }


    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... uri) {
            String url = uri[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) {
                Bitmap resized = Bitmap.createScaledBitmap(result,200,200, true);
                bmImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(getApplicationContext(),resized,250,200,200, false, false, false, false));

            }
        }
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        phoneNumberView.setError(null);

        // Store values at the time of the login attempt.
        phoneNumber = phoneNumberView.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberView.setError(getString(R.string.error_field_required));
            focusView = phoneNumberView;
            cancel = true;
        }

        //TODO 페북 확인: 프로필 이미지 주소 저장
        if (AccessToken.getCurrentAccessToken() == null) {
            Toast.makeText(getApplicationContext(), "You need Facebook authentication to continue.", Toast.LENGTH_LONG).show();
            focusView = phoneNumberView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(phoneNumber);
            mAuthTask.execute((Void) null);
        }
    }





    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String phoneNumber;

        UserLoginTask(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            register();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            (new AsyncTask<Void,Void,Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    (new ServerRequest(thisActivity)).addAccount(name, phoneNumber, "", MainActivity.myProfileImageURL);

                    return null;
                }
                @Override
                protected void onPostExecute(Void v) {
                    Toast.makeText(thisActivity, "Registered", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).execute();

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private void register() {
            SharedPreferences pref = getSharedPreferences("registration", 0);
            SharedPreferences.Editor edit = pref.edit();
            profile = Profile.getCurrentProfile();
            Uri uri = profile.getProfilePictureUri(800,800);
            name = profile.getName();
            edit.putBoolean("isRegistered", true);
            edit.putString("phone_number", phoneNumber);
            edit.putString("name", name);
            try {
                MainActivity.myProfileImageURL = new URL(uri.toString()).toString();
                edit.putString("profile_image_url", MainActivity.myProfileImageURL);
            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "Malformed URL", Toast.LENGTH_SHORT).show();
            }
            edit.commit();
        }


    }




    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

