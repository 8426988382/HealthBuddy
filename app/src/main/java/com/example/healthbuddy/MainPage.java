package com.example.healthbuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity implements UriResponse{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private String personName, email;
    private Uri personPhoto;

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF= "shared_prefs";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    //Toast.makeText(MainPage.this, "Please Login", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };

        profileInfo();

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.login));

        setContentView(R.layout.mainpage);

        initialise();

//        viewPager.setCurrentItem(1, true);


        viewPager.setCurrentItem(1, true);
        //    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                tabLayout.setupWithViewPager(viewPager);
//            }
//        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
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
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    void initialise(){
//        ImageView pfileimg = findViewById(R.id.imageView10);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        PageAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager(), 3);
        viewPager.setAdapter(pagerAdapter);
    }


    public void gototprofile(View view) {

        sharedPreferences = this.getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this );

        if(account != null) {

            String key = account.getId();

//            ApigetScores apigetScores = new ApigetScores(MainPage.this, sharedPreferences.getInt(key, 0));
//            apigetScores.execute();


            asynchelper asynchelper = new asynchelper(MainPage.this, personName, email, personPhoto);
            asynchelper.execute();
        }
    }

    private void profileInfo(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainPage.this);
        if(account == null){
            finish();
        }else {
            personName = account.getDisplayName();
            email = account.getEmail();
            personPhoto = account.getPhotoUrl();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getURI(final Bitmap image) {


        new ApiGetSuggestions(this){
            @Override
            protected void onPostExecute(ArrayList<SuggestionsData> aVoid) {
                super.onPostExecute(aVoid);
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                PerformAction(aVoid, image);
            }
        }.execute();
    }

    private void PerformAction(ArrayList<SuggestionsData> list, Bitmap image){



        int scores= sharedPreferences.getInt("scores", Integer.parseInt("0"));

        String average= String.valueOf( (int) ((double)scores/5 * 10));

        Log.e("Average", average);




        Intent intent = new Intent(MainPage.this , Profile_Activity.class);
        intent.putExtra("userName", personName);
        intent.putExtra("userEmail" , email);
        intent.putExtra("profilepic" , image);
        intent.putExtra("Score", average);
        intent.putExtra("data", list);

        startActivity(intent);

    }


}
