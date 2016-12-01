package com.example.vaio.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
 * Adapter của drawerlayout
 */

public class ListViewDrawerLayoutAdapter extends RecyclerView.Adapter<ListViewDrawerLayoutAdapter.ItemView> {
    //danh sach icon
    private static final Integer[] arrIcon = {
            R.drawable.home,
            R.drawable.clock, R.drawable.like_drawerlayout, R.drawable.theme, R.drawable.feedback, R.drawable.help, R.drawable.logout
    };
    //danh sach noi dung
    private static final String[] arrContent = {
            "Trang chính", "Danh sách xem sau", "Danh sách yêu thích", "Chủ đề", "Phản hồi", "Giới thiệu", "Thoát"
    };
    //danh sach so dem
    private ArrayList<Integer> arrCountContent;

    private OnItemClickListener mItemClickListener;


    public ListViewDrawerLayoutAdapter(ArrayList<Integer> arrCountContent) {
        this.arrCountContent = arrCountContent;
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listview_drawerlayout, parent, false);

        return new ItemView(itemView);
    }

    @Override
    public void onBindViewHolder(ItemView holder, int position) {
        holder.ivIcon.setImageResource(arrIcon[position]);
        holder.tvContent.setText(arrContent[position]);
        holder.vPart.setVisibility(View.INVISIBLE);
        if (arrCountContent.size() - 1 < position || arrCountContent.get(position) == 0) {
            holder.tvCount.setText("");
        } else if (arrCountContent.get(position) > 0)
            holder.tvCount.setText(arrCountContent.get(position) + "");
        if (arrCountContent.size() - 1 == position) holder.vPart.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return arrContent.length;
    }

    public class ItemView extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivIcon;
        public TextView tvContent;
        public TextView tvCount;
        public View vPart;

        public ItemView(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon_listview_drawerlayout);
            tvContent = (TextView) view.findViewById(R.id.tv_content_listview_drawerlayout);
            tvCount = (TextView) view.findViewById(R.id.tv_count_listview_drawerlayout);
            vPart = view.findViewById(R.id.view_part);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getPosition());
            }
        }
    }


    public interface OnItemClickListener extends View.OnClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

}
