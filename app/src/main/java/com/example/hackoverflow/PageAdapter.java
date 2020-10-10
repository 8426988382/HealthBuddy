package com.example.hackoverflow;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class PageAdapter extends FragmentPagerAdapter {

    private int noOftabs;

    PageAdapter(FragmentManager fm , int numOftabs)
    {
        super(fm);
        this.noOftabs = numOftabs;
    }

    @Override
    public Fragment getItem(int position){

        switch (position)
        {
            case 0:
                return new Chat_Fragment();
            case 1:

                return new Question_Fragment();
            case 2:
                return new Meditate_Fragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount(){return noOftabs;}
}
