package com.example.afinal.fingerPrint_Login.main_activity_fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);

    }

    @Override
    public Fragment getItem(int i) {

        return fragmentArrayList.get(i);
    }

    @Override
    public int getCount() {

        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment){

        fragmentArrayList.add(fragment);
    }

}
