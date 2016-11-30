package com.example.vaio.fragment;


import android.app.ProgressDialog;
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
    public static final String KEY_ITEM_IS_LIKE = "key_item_is_like";
    public static final String KEY_ITEM_IS_LATER = "key_item_is_later";
    protected int currentPage = 0;
    protected ListView listView;
    protected GridView gridView;
    protected ListViewAdapter listViewAdapter;
    protected GridViewAdapter gridViewAdapter;
    protected ArrayList<ItemListView> arrItemListView = new ArrayList<>();
    protected ArrayList<ItemListView> arrAllDataFromWeb = new ArrayList<>();
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
        listViewAdapter = new ListViewAdapter(getContext(), arrItemListView, MainActivity.POPUP_MENU_IN_HOME);
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

    public void getDataFromDatabase(final int typeId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();
    }

    public void getDataFromWeb(String link, int typeId) {
        currentPage++;
        JsoupParser jsoupParser = new JsoupParser(context,handler, typeId);
        jsoupParser.execute(link + currentPage);
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT) {
                if (MainActivity.isNetworkAvailable(context) && !isCleared) {
                    database.deleteAllItemListView(msg.arg1, MyDatabase.TB_NAME_LIST_MAIN);
                    isCleared = true;
                }

                arrItemListView.addAll((Collection<? extends ItemListView>) msg.obj);

                database.insertArrItemListView((ArrayList<ItemListView>) msg.obj, MyDatabase.TB_NAME_LIST_MAIN);
                database.insertArrItemListView((ArrayList<ItemListView>) msg.obj, MyDatabase.TB_NAME_LIST_MAIN);
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
        if(!MainActivity.isNetworkAvailable(getContext())){
            Toast.makeText(getContext(),"Vui lòng kết nối mạng để xem chi tiết",Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(getActivity().getBaseContext(), ContentGameActivity.class);
        boolean isLike = false;
        boolean isLater = false;
        ArrayList<ItemListView> arrItemListViewsLike = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LIKE);
        ArrayList<ItemListView> arrItemListViewsLater = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LATER);
        for (int count = 0; count < arrItemListViewsLike.size(); count++) {
            if (arrItemListViewsLike.get(count).getName().equals(arrItemListView.get(i).getName())) {
                isLike = true;
                break;
            }
        }
        for (int count = 0; count < arrItemListViewsLater.size(); count++) {
            if (arrItemListViewsLater.get(count).getName().equals(arrItemListView.get(i).getName())) {
                isLater = true;
            }
        }

        intent.putExtra(KEY_INTENT_CHANGE, arrItemListView.get(i));
        intent.putExtra(KEY_ITEM_IS_LIKE, isLike);
        intent.putExtra(KEY_ITEM_IS_LATER, isLater);
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
