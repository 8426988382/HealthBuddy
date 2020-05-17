package com.example.hackoverflow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigationItem;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.common.util.CollectionUtils.listOf;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigation;
import com.tenclouds.fluidbottomnavigation.FluidBottomNavigationItem;
import com.tenclouds.fluidbottomnavigation.listener.OnTabSelectedListener;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class MainPage extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pagerAdapter;

    ImageView pfile;


    String key="";
    SharedPreferences sharedPreferences;
    CurvedBottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;

    chat_Fragment chat_fragment;
    questions_fragment questions_fragment;
    Mediate_Fragment mediate_fragment;

    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;

    String personName;
    String email;
    Uri personPhoto;
    ImageView pfileimg;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        pfileimg = findViewById(R.id.imageView10);




        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.login));

        setContentView(R.layout.mainpage);



        tabLayout = findViewById(R.id.tabLayout);
//        tabprofile = findViewById(R.id.profile_tab);
//        tabview = findViewById(R.id.view_tab);
//        tabinfo = findViewById(R.id.info_tab);
        viewPager = findViewById(R.id.view_pager);

        pfile = findViewById(R.id.imageView10);







        pagerAdapter = new PageAdapter(getSupportFragmentManager() , tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        if(savedInstanceState == null){
            viewPager.setCurrentItem(1);
        }

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
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));





    }
    public void gototprofile(View view) {

        sharedPreferences = this.getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this );
        key = account.getId();

        ApigetScores apigetScores = new ApigetScores(MainPage.this, sharedPreferences.getInt(key, 0));
        apigetScores.execute();

        asynchelper asynchelper = new asynchelper(MainPage.this , personName , email , personPhoto);
        asynchelper.execute();



    }

    private void profileInfo(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainPage.this);
        personName = account.getDisplayName();
        email = account.getEmail();
        personPhoto = account.getPhotoUrl();
    }
}
