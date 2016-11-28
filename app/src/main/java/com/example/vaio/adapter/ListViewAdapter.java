package com.example.vaio.adapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.vaio.database.MyDatabase;
import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaio on 11/24/2016.
 */

public class ListViewAdapter extends ArrayAdapter {
    private static final String TAG = "ListViewAdapter";
    private Context context;
    private ArrayList<ItemListView> arrItemListView;
    private LayoutInflater inflater;
    private int currentPosition;

    private MyDatabase database;
    private ArrayList<ItemListView> arrItemListViewsLike;
    private ArrayList<ItemListView> arrItemListViewsLater;

    private int statePopupMenu;
    private int chooseListview;

    public ListViewAdapter(Context context, ArrayList<ItemListView> arrItemListView, int statePopupMenu) {
        super(context, android.R.layout.simple_list_item_1, arrItemListView);
        this.arrItemListView = arrItemListView;
        this.context = context;
        this.statePopupMenu = statePopupMenu;
        database = new MyDatabase(context);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrItemListView.size();
    }

    @NonNull
    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        currentPosition = position;
        final ItemListView itemListView = arrItemListView.get(position);
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
//            currentPosition = position;
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


        viewHolder.popupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition = position;
                PopupMenu popupMenu = new PopupMenu(context, view);
                if (statePopupMenu == MainActivity.POPUP_MENU_IN_HOME) {
                    String name = itemListView.getName();
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                    arrItemListViewsLike = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LIKE);
                    arrItemListViewsLater = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LATER);

                    for (int i = 0; i < arrItemListViewsLike.size(); i++) {
                        if (arrItemListViewsLike.get(i).getName().equals(name)) {
                            popupMenu.getMenu().getItem(0).setEnabled(false);
                        }
                    }
                    for (int i = 0; i < arrItemListViewsLater.size(); i++) {
                        if (arrItemListViewsLater.get(i).getName().equals(name)) {
                            popupMenu.getMenu().getItem(1).setEnabled(false);
                        }
                    }
                    popupMenu.setOnMenuItemClickListener(itemClickPopup);
                } else if (statePopupMenu == MainActivity.POPUP_MENU_IN_LIST_LIKE_AND_LATER) {
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu_list_drawerlayout, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(itemClickPopupFromDrawerLayout);
                }
                popupMenu.show();
            }
        });
        return v;
    }


    private PopupMenu.OnMenuItemClickListener itemClickPopup = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.favorite:
                    database.insertItemListView(arrItemListView.get(currentPosition), MyDatabase.TB_NAME_LIST_LIKE);
                    Toast.makeText(context, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.later:
                    database.insertItemListView(arrItemListView.get(currentPosition), MyDatabase.TB_NAME_LIST_LATER);
                    Toast.makeText(context, "Đã thêm vào danh sách xem sau", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.share:
                    shareItem();
                    break;
            }
            return false;
        }
    };

    private PopupMenu.OnMenuItemClickListener itemClickPopupFromDrawerLayout = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete:
                    if (chooseListview == 1) {
                        database.deleteItemListView(arrItemListView.get(currentPosition), MyDatabase.TB_NAME_LIST_LATER);
                        arrItemListView.clear();
                        arrItemListView.addAll(database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LATER));
                    } else if (chooseListview == 2) {
                        database.deleteItemListView(arrItemListView.get(currentPosition), MyDatabase.TB_NAME_LIST_LIKE);
                        arrItemListView.clear();
                        arrItemListView.addAll(database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LIKE));
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.share:
                    shareItem();
                    break;
            }
            return false;
        }
    };


//    public boolean reachedEndOfList() {
//        // can check if close or exactly at the end
//        return currentPosition == getCount() - 1;
//    }

    public void shareItem() {
        ItemListView itemListView = arrItemListView.get(currentPosition);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, itemListView.getDetailsUrl());

        getContext().startActivity(Intent.createChooser(share, "Bạn muốn chia sẻ trên..."));
    }


    public int getStatePopupMenu() {
        return statePopupMenu;
    }

    public void setStatePopupMenu(int statePopupMenu) {
        this.statePopupMenu = statePopupMenu;
    }

    public int getChooseListview() {
        return chooseListview;
    }

    public void setChooseListview(int chooseListview) {
        this.chooseListview = chooseListview;
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
