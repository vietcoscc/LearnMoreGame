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

import java.util.ArrayList;

/**
 * Created by TungMai on 25/11/2016.
 */

public class ListViewDrawerLayoutAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    //danh sach icon
    private static final Integer[] arrIcon = {
            R.drawable.clock, R.drawable.like_drawerlayout, R.drawable.like_drawerlayout
    };
    //danh sach noi dung
    private static final String[] arrContent = {
            "Danh sách xem sau", "Danh sách yêu thích", "fdsfas"
    };
    //danh sach so dem
    private ArrayList<Integer> arrCountContent;

    public ListViewDrawerLayoutAdapter(Context context, ArrayList<Integer> objects) {
        super(context, R.layout.item_listview_drawerlayout);
        inflater = LayoutInflater.from(context);
        arrCountContent = objects;
    }

    @Override
    public int getCount() {
        return arrContent.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_listview_drawerlayout, parent, false);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon_listview_drawerlayout);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content_listview_drawerlayout);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_count_listview_drawerlayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivIcon.setImageResource(arrIcon[position]);
        viewHolder.tvContent.setText(arrContent[position]);
        if (arrCountContent.size() - 1 < position || arrCountContent.get(position) == 0)
            viewHolder.tvCount.setText("");
        else if (arrCountContent.get(position) > 0)
            viewHolder.tvCount.setText(arrCountContent.get(position) + "");
        return convertView;

    }

    class ViewHolder {
        ImageView ivIcon;
        TextView tvContent;
        TextView tvCount;
    }
}
