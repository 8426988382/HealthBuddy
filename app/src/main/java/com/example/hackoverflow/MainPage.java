package com.example.hackoverflow;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class MainPage extends AppCompatActivity {

    CurvedBottomNavigationView bottomNavigationView;
    ViewPager viewPager;
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

        pfileimg = findViewById(R.id.pfile_id);




        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.azure));

        setContentView(R.layout.mainpage);

        bottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        viewPager = findViewById(R.id.viewpager);



        setupViewPager(viewPager);

        if (savedInstanceState == null) {
            viewPager.setCurrentItem(1);
            bottomNavigationView.setSelectedItemId(R.id.nav_questions);
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.nav_chat:
                        viewPager.setCurrentItem(0);
                       // bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.azure));
                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                        window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.azure));
                        break;

                    case R.id.nav_questions:
                        viewPager.setCurrentItem(1);
                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                        window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.azure));
                        break;

                    case R.id.nav_meditate:
                        viewPager.setCurrentItem(2);
                        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBackground)));
                        bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorBackground)));
                        window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.colorBackground));
                        break;

                    default:

                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(1).setChecked(false);

                }

                if (position == 0) {
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                    bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                    window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.azure));
                }

                else if(position == 1){
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                    bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.azure)));
                    window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.azure));
                }
//
//                else if(position == 2){
//                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBackground)));
//                    bottomNavigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorBackground)));
//                    window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.colorBackground));
//                }
//                else {
//                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#4F68F1")));
//                    bottomNavigationView.setItemTextColor(ColorStateList.valueOf(Color.parseColor("#4F68F1")));
//                    window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.color3));
//
//                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.removeAllViews();
        viewPager.setOffscreenPageLimit(2);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        questions_fragment = new questions_fragment();
        chat_fragment = new chat_Fragment();
        mediate_fragment = new Mediate_Fragment();

        adapter.addFragment(chat_fragment);
        adapter.addFragment(questions_fragment);
        adapter.addFragment(mediate_fragment);


        viewPager.setAdapter(adapter);
    }


    public void gototprofile(View view) {
                Intent intent = new Intent(MainPage.this, Profile_Fragment.class);

//                Log.e(TAG, "onClick: " + "CLICKED!" );
//                intent.putExtra("userName" , personName);
//                intent.putExtra("userEmail" , email);
//                intent.putExtra("profile_pic" , personPhoto);
//                startActivity(intent);

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
