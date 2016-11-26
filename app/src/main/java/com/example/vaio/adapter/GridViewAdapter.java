package com.example.vaio.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by vaio on 11/26/2016.
 */

public class GridViewAdapter extends BaseAdapter {
    private static final String TAG = "GridViewAdapter";
    private ArrayList<ItemListView> arrItemListView;
    private LayoutInflater inflater;
    private int currentPosition;
    private Context context;

    public GridViewAdapter(Context context, ArrayList<ItemListView> arrItemListView) {
        this.arrItemListView = arrItemListView;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrItemListView.size();
    }

    @Override
    public Object getItem(int position) {
        return arrItemListView.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        currentPosition = position;
        ItemListView itemListView = arrItemListView.get(position);
        ViewHolder viewHolder;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_grid_view, parent, false);
            viewHolder.image = (ImageView) v.findViewById(R.id.image);
            viewHolder.name = (TextView) v.findViewById(R.id.name);
//            viewHolder.type = (TextView) v.findViewById(R.id.type);
//            viewHolder.date = (TextView) v.findViewById(R.id.date);
//            viewHolder.views = (TextView) v.findViewById(R.id.views);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
//        Log.e(TAG, itemListView.getImageUrl());

        Picasso.with(context)
                .load(itemListView.getImageUrl())
                .placeholder(R.drawable.loading)
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.warning)
                .into(viewHolder.image);
        viewHolder.name.setText(itemListView.getName());
//        viewHolder.type.setText(itemListView.getType());
//        viewHolder.date.setText(itemListView.getDate());
//        viewHolder.views.setText(itemListView.getViews());
        return v;
    }

    class ViewHolder {
        ImageView image;
        TextView name;
//        TextView type;
//        TextView date;
//        TextView views;
    }
}
