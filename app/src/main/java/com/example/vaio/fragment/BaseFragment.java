package com.example.vaio.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vaio.adapter.GridViewAdapter;
import com.example.vaio.adapter.ListViewAdapter;
import com.example.vaio.database.MyDatabase;
import com.example.vaio.learnmoregame.ContentGameActivity;
import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.learnmoregame.R;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vaio on 11/24/2016.
 */


public class BaseFragment extends Fragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final int WHAT = 1;
    public static final String KEY_INTENT_CHANGE = "key_intent_change";
    public static final String TAG = "BaseFragment";
    private static final int REQUEST_CODE_WATCH = 112;
    protected int currentPage = 0;
    protected ListView listView;
    protected GridView gridView;
    protected ListViewAdapter listViewAdapter;
    protected GridViewAdapter gridViewAdapter;
    protected ArrayList<ItemListView> arrItemListView = new ArrayList<>();
    private Context context;
    private MyDatabase database;
    private boolean isCleared = false;
    private int lastTotalItemCount = -1;
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
        listViewAdapter = new ListViewAdapter(getContext(), arrItemListView,MainActivity.POPUP_MENU_IN_HOME);
        listView.setAdapter(listViewAdapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        gridView = (GridView) v.findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(getContext(), arrItemListView);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnScrollListener(this);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);

        changeListViewList();

        if (!MainActivity.isNetworkAvailable(context)) {
            getDataFromDatabase(typeId);
        }
    }

    public void changeGridViewList() {  // Gọi phương thức để xoay giữa list view và grid view
        listView.setVisibility(View.INVISIBLE);
        gridView.setVisibility(View.VISIBLE);
    }

    public void changeListViewList() {  // Gọi phương thức để xoay giữa list view và grid view
        listView.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.INVISIBLE);
    }

    public void getDataFromDatabase(int typeId) {
        arrItemListView.clear();
        ArrayList<ItemListView> arrTmp = database.getDataFromGameList();
        for (int i = 0; i < arrTmp.size(); i++) {
            if (arrTmp.get(i).getTypeId() == typeId) {
                arrItemListView.add(arrTmp.get(i));
            }
        }
        listViewAdapter.notifyDataSetChanged();
        gridViewAdapter.notifyDataSetChanged();
    }

    public void getDataFromWeb(String link, int typeId) {
        currentPage++;
        JsoupParser jsoupParser = new JsoupParser(handler, typeId);
        jsoupParser.execute(link + currentPage);
//        listViewAdapter.notifyDataSetChanged();
//        gridViewAdapter.notifyDataSetChanged();
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT) {
                if (MainActivity.isNetworkAvailable(context) && !isCleared) {
                    database.deleteAllItemListView(msg.arg1,MyDatabase.TB_NAME_LIST_MAIN);
                    isCleared = true;
                }
                arrItemListView.addAll((Collection<? extends ItemListView>) msg.obj);
                database.insertArrItemListView((ArrayList<ItemListView>) msg.obj,MyDatabase.TB_NAME_LIST_MAIN);
                listViewAdapter.notifyDataSetChanged();
                gridViewAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if ((firstVisibleItem + visibleItemCount) == totalItemCount && MainActivity.isNetworkAvailable(getContext()) && currentPage < 20 && lastTotalItemCount < totalItemCount) {
            getDataFromWeb(link, typeId);
            lastTotalItemCount = totalItemCount;
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // Nhấn giữ vào item list view

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity().getBaseContext(), ContentGameActivity.class);
        intent.putExtra(KEY_INTENT_CHANGE, arrItemListView.get(i));
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}
