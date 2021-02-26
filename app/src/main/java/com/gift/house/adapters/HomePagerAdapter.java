package com.gift.house.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.gift.house.R;

import java.util.ArrayList;

public class HomePagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Integer> layouts;

    public HomePagerAdapter(Context context, ArrayList<Integer> layouts) {
        this.context = context;
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.rv_products_home;
                break;
            case 1:
                resId = R.id.rv_my_orders;
                break;
            case 2:
                resId = R.id.cart_layout;
                break;
        }

        return ((Activity) context).findViewById(resId);
    }

    @Override
    public int getCount() {
        return layouts.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
