package com.feiyueve.snsdemo.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class MyViewPageAdapter extends PagerAdapter {
    private List<View> mViews;
    private List fragments;

    public MyViewPageAdapter(List<View> views) {
        mViews = views;
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position,
                            final Object object) {
        container.removeView(mViews.get(position));
    }

    @Override
    public int getCount() {
        return mViews == null ? 0 : mViews.size();
    }
}