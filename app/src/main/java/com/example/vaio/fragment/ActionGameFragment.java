package com.example.vaio.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vaio on 11/22/2016.
 */

public class ActionGameFragment extends android.support.v4.app.Fragment {
    public static final int WHAT = 1;
    private ArrayList<ItemListView> arrActionGame = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_action_game,container,false);
        JsoupParser jsoupParser = new JsoupParser(handler);
        jsoupParser.execute("http://linkneverdie.com/Games-FPS/?theloaiId=1&page=1");
        return v;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == WHAT){
                arrActionGame.addAll((Collection<? extends ItemListView>) msg.obj);
            }
        }
    };
}
