package com.example.hackoverflow;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

public class MainPage extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private String personName, email;
    private Uri personPhoto;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        if(savedInstanceState == null){
            viewPager.setCurrentItem(1);
        }

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

    void initialise(){
//        ImageView pfileimg = findViewById(R.id.imageView10);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        PageAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
    }


    public void gototprofile(View view) {

        SharedPreferences sharedPreferences = this.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this );

        if(account != null) {

            String key = account.getId();

            ApigetScores apigetScores = new ApigetScores(MainPage.this, sharedPreferences.getInt(key, 0));
            apigetScores.execute();

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
}
