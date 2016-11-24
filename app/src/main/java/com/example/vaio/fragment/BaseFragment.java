package com.example.vaio.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

import com.example.vaio.adapter.ListViewAdapter;
import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vaio on 11/24/2016.
 */

public class BaseFragment extends Fragment {
    public static final int WHAT = 1;
    protected int currentPage = 0;
    protected ListView listView;
    protected ListViewAdapter adapter;
    protected ArrayList<ItemListView> arrItemListView = new ArrayList<>();

    protected void initViews(View v) {
        listView = (ListView) v.findViewById(R.id.listView);
        adapter = new ListViewAdapter(getContext(), arrItemListView);
        listView.setAdapter(adapter);
    }

    public void getDataFromWeb(String link) {
        currentPage++;
        arrItemListView.clear();
        JsoupParser jsoupParser = new JsoupParser(handler);
        jsoupParser.execute(link + currentPage);
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT) {
                arrItemListView.addAll((Collection<? extends ItemListView>) msg.obj);
                adapter.notifyDataSetChanged();
            }
        }
    };
}
