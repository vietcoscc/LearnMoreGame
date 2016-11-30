package com.example.vaio.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vaio.learnmoregame.R;

import java.util.ArrayList;

/**
 * Created by vaio on 12/1/2016.
 */

public class ListViewThemeAdapter extends ArrayAdapter<Integer> {
    private LayoutInflater inflater;
    private ArrayList<Integer> arrItemColor;

    public ListViewThemeAdapter(Context context, ArrayList<Integer> arrItemColor) {
        super(context, android.R.layout.simple_list_item_1, arrItemColor);
        inflater = LayoutInflater.from(context);
        this.arrItemColor = arrItemColor;
    }

    @Override
    public int getCount() {
        return arrItemColor.size();
    }

    @NonNull
    @Override
    public View getView(int position, View v, ViewGroup parent) {
        ViewHolder viewHolder;
        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.item_list_view_theme, parent, false);
            viewHolder.tvColor = (TextView) v.findViewById(R.id.tvColor);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.tvColor.setBackgroundColor(arrItemColor.get(position));
        return v;
    }

    class ViewHolder {
        TextView tvColor;
    }
}
