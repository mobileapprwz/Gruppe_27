package com.example.mobileapp.categories;

import com.example.mobileapp.SessionData;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CategoryTabAdapter extends FragmentPagerAdapter {
	int count;
    public CategoryTabAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count=count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt("Category", SessionData.instance().getCategories().get(position).getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
    	return SessionData.instance().getCategories().size();
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
    	return SessionData.instance().getCategories().get(position).getName().toString().toUpperCase();
    }
}