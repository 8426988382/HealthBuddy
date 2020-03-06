package com.example.hackoverflow;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    MenuItem prevMenuItem;

    Profile_Fragment profile_fragment;
    chat_Fragment chat_fragment;
    questions_fragment questions_fragment;
    Mediate_Fragment mediate_fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.medium_pink));

        setContentView(R.layout.mainpage);

        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        viewPager = findViewById(R.id.viewpager);



        setupViewPager(viewPager);

        if (savedInstanceState == null) {
            viewPager.setCurrentItem(3);
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
            bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_questions:
                        viewPager.setCurrentItem(0);
                         bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_pink)));
                         window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.medium_pink));
                        break;
                    case R.id.nav_chat:
                        viewPager.setCurrentItem(1);
                        //   curvedBottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                        //   window.setStatusBarColor(ContextCompat.getColor(Home.this , R.color.blue));
                        break;
                    case R.id.nav_meditate:
                        viewPager.setCurrentItem(2);
                        //    curvedBottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ec87c0")))
                        //    window.setStatusBarColor(ContextCompat.getColor(Home.this , R.color.color3));

                        break;
                    case R.id.nav_profile:
                        viewPager.setCurrentItem(3);
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
                    bottomNavigationView.getMenu().getItem(3).setChecked(false);

                }

                if (position == 0) {
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.medium_pink)));
                      window.setStatusBarColor(ContextCompat.getColor(MainPage.this , R.color.medium_pink));
                }
//
//                } else if (position == 1) {
//                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor));
//                    //   window.setStatusBarColor(ContextCompat.getColor(Home.this , R.color.blue));
//
//                } else {
//                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#ec87c0")));
//                    //     window.setStatusBarColor(ContextCompat.getColor(Home.this , R.color.color3));
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
        viewPager.setOffscreenPageLimit(3);
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        questions_fragment = new questions_fragment();
        chat_fragment = new chat_Fragment();
        mediate_fragment = new Mediate_Fragment();
        profile_fragment = new Profile_Fragment();

        adapter.addFragment(questions_fragment);
        adapter.addFragment(chat_fragment);
        adapter.addFragment(mediate_fragment);
        adapter.addFragment(profile_fragment);

        viewPager.setAdapter(adapter);
    }
}
