package com.example.vaio.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vaio.learnmoregame.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by TungMai on 26/11/2016.
 */

public class ImageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> arrImage;

    public ImageViewPagerAdapter(Context context, ArrayList<String> arrImage) {
        this.context = context;
        this.arrImage = arrImage;
    }

    @Override
    public int getCount() {
        return arrImage.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_view_pager_image, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_image);
        Picasso.with(context).load(arrImage.get(position)).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
