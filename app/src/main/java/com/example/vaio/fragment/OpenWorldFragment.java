package com.example.vaio.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.vaio.adapter.ListViewAdapter;
import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vaio on 11/22/2016.
 */

public class OpenWorldFragment extends BaseFragment {
    public static final int TYPE_ID = 2;
    public static final String LINK = "http://linkneverdie.com/OpenWorld-Games/?theloaiId=7&page=";

    public OpenWorldFragment(Context context) {
        super(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_open_world_game, container, false);

        initViews(v,LINK,TYPE_ID);
        listView.setOnScrollListener(this);
        return v;
    }

}
