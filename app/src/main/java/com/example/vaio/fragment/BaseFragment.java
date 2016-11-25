package com.example.vaio.fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.vaio.adapter.ListViewAdapter;
import com.example.vaio.database.MyDatabase;
import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;
import java.util.Collection;

import static android.content.ContentValues.TAG;

/**
 * Created by vaio on 11/24/2016.
 */

public class BaseFragment extends Fragment implements AbsListView.OnScrollListener {
    public static final int WHAT = 1;
    protected int currentPage = 0;
    protected ListView listView;
    protected ListViewAdapter adapter;
    protected ArrayList<ItemListView> arrItemListView = new ArrayList<>();
    private Context context;
    private MyDatabase database;
    private boolean isCleared = false;
    private String link;
    private int typeId;

    public BaseFragment(Context context) {
        this.context = context;
        database = new MyDatabase(context);

    }

    protected void initViews(View v, final String link, final int typeId) {
        this.link = link;
        this.typeId = typeId;
        listView = (ListView) v.findViewById(R.id.listView);
        adapter = new ListViewAdapter(getContext(), arrItemListView);
        listView.setAdapter(adapter);
        if (!MainActivity.isNetworkAvailable(context)) {
            getDataFromDatabase(typeId);
        }
    }

    public void getDataFromDatabase(int typeId) {
        arrItemListView.clear();
        ArrayList<ItemListView> arrTmp = database.getDataFromGameList();
        for (int i = 0; i < arrTmp.size(); i++) {
            if (arrTmp.get(i).getTypeId() == typeId) {
                arrItemListView.add(arrTmp.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void getDataFromWeb(String link, int typeId) {
        currentPage++;
        JsoupParser jsoupParser = new JsoupParser(handler, typeId);
        jsoupParser.execute(link + currentPage);
        adapter.notifyDataSetChanged();
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT) {
                if (MainActivity.isNetworkAvailable(context) && !isCleared) {
                    database.deleteAllItemListView(msg.arg1);
                    isCleared = true;
                }
                arrItemListView.addAll((Collection<? extends ItemListView>) msg.obj);
                database.insertArrItemListView((ArrayList<ItemListView>) msg.obj);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if((firstVisibleItem + visibleItemCount) ==  totalItemCount&& MainActivity.isNetworkAvailable(getContext())&& currentPage<20)
        {
            getDataFromWeb(link,typeId);
        }
    }
}
