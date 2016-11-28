package com.example.vaio.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by vaio on 11/28/2016.
 */

public class ListViewSearchAdapter extends ArrayAdapter<ItemListView> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ItemListView> arrSearchResult;

    public ListViewSearchAdapter(Context context, ArrayList<ItemListView> arrSearchResult) {
        super(context, android.R.layout.simple_list_item_1, arrSearchResult);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.arrSearchResult = arrSearchResult;
    }

    @Override
    public int getCount() {
        return arrSearchResult.size();
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder viewHolder;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_list_view_search, parent, false);
            viewHolder.image = (ImageView) v.findViewById(R.id.image);
            viewHolder.name = (TextView) v.findViewById(R.id.name);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        Picasso.with(context).load(arrSearchResult.get(position).getImageUrl())
                .placeholder(R.drawable.loading).error(R.drawable.offine).into(viewHolder.image);
        viewHolder.name.setText(arrSearchResult.get(position).getName());
        return v;
    }

    class ViewHolder {
        ImageView image;
        TextView name;
    }
}
