package com.example.mobileapp;

import java.util.Vector;

import datastructures.Favorite;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class HomeGridAdapter extends BaseAdapter {
    private Context mContext;
    private Vector<Favorite> m_favorites;
    TypedArray m_images;

    public HomeGridAdapter(Context c, TypedArray images) {
        mContext = c;
        m_favorites = UserSession.instance().getFavorites();
        m_images = images;
    }

    public int getCount() {
        return m_favorites.size();
    }

    public Favorite getItem(int position) {
    	return m_favorites.get(position);
    }

    public long getItemId(int position) {
    	return m_images.getResourceId(position, 0);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
        } else {
            imageView = (ImageView) convertView;
        }

        //get image from image index array via subcategory id (get image on with index 0 as default)
        Integer image_id = m_favorites.get(position).getId();      
        imageView.setImageResource(m_images.getResourceId(image_id, R.drawable.other_34));
        imageView.setBackgroundResource(R.drawable.iconlayout);
        return imageView;
    }
}
