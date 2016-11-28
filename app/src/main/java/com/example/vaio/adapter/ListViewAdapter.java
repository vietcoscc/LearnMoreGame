package com.example.vaio.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaio on 11/24/2016.
 */

public class ListViewAdapter extends ArrayAdapter implements View.OnClickListener{
    private static final String TAG = "ListViewAdapter";
    private Context context;
    private ArrayList<ItemListView> arrItemListView;
    private LayoutInflater inflater;
    private int currentPosition;

    public ListViewAdapter(Context context, ArrayList<ItemListView> arrItemListView) {
        super(context, android.R.layout.simple_list_item_1, arrItemListView);
        this.arrItemListView = arrItemListView;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrItemListView.size();
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        currentPosition = position;
        ItemListView itemListView = arrItemListView.get(position);
        ViewHolder viewHolder;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_list_view, parent, false);
            viewHolder.image = (ImageView) v.findViewById(R.id.image);
            viewHolder.name = (TextView) v.findViewById(R.id.name);
            viewHolder.type = (TextView) v.findViewById(R.id.type);
            viewHolder.date = (TextView) v.findViewById(R.id.date);
            viewHolder.views = (TextView) v.findViewById(R.id.views);
            viewHolder.popupMenu = (ImageView) v.findViewById(R.id.popupMenu);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
//        Log.e(TAG, itemListView.getImageUrl());

        Picasso.with(context)
                .load(itemListView.getImageUrl())
                .placeholder(R.drawable.loading)
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.offine)
                .into(viewHolder.image);
        viewHolder.name.setText(itemListView.getName());
        viewHolder.type.setText(itemListView.getType());
        viewHolder.date.setText(itemListView.getDate());
        viewHolder.views.setText(itemListView.getViews());
        viewHolder.popupMenu.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Click vào item của popUp menu yêu thích , xem sau , chia sẻ ....
                return false;
            }
        });
        popupMenu.show();
    }


    class ViewHolder {
        ImageView image;
        TextView name;
        TextView type;
        TextView date;
        TextView views;
        ImageView popupMenu;
    }
}
