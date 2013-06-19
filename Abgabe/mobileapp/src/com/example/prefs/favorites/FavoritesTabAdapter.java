package com.example.prefs.favorites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.mobileapp.SessionData;
import com.example.mobileapp.categories.CategoryListFragment;

public class FavoritesTabAdapter extends FragmentPagerAdapter {

	public FavoritesTabAdapter(FragmentManager fm) {
		super(fm);
	}

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FavoriteSettingsListFragment();
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