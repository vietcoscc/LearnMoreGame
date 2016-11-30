package com.example.vaio.learnmoregame;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vaio.adapter.GridViewAdapter;
import com.example.vaio.adapter.ListViewAdapter;
import com.example.vaio.adapter.ListViewDrawerLayoutAdapter;
import com.example.vaio.adapter.ListViewSearchAdapter;
import com.example.vaio.adapter.ViewPagerAdapter;
import com.example.vaio.database.MyDatabase;
import com.example.vaio.dialog.FeedbackDialog;
import com.example.vaio.dialog.IntroductionDialog;
import com.example.vaio.fragment.BaseFragment;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MenuItem.OnMenuItemClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    public static final String ACTION_GAME = "Action";
    public static final String FPS_GAME = "FPS";
    public static final String OPEN_WORLD_GAME = "Open world";
    public static final String SURVIVAL_GAME = "Survival";
    public static final String TPS_GAME = "TPS";
    private static final String TAG = "MainActivity";

    public static final int POPUP_MENU_IN_HOME = 0;
    public static final int POPUP_MENU_IN_LIST_LIKE_AND_LATER = 1;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private SearchView searchView;
    private LinearLayout linearLayoutListMain;
    private RelativeLayout linearLayoutListChosseFromDrawerLayout;
    private IntroductionDialog introductionDialog;
    private TextView titleToolbar;
    private ListViewDrawerLayoutAdapter listViewDrawerLayoutAdapter;
    private ArrayList<Integer> arrCountContentDrawerLayout;
    private RecyclerView listViewDrawerLayout;
    private ListView lvSearch;
    //thao tac khi thuc hien drawer layout
    private MyDatabase database;
    private ListView lvChosseFromDrawerLayout;
    private ListViewAdapter lvChosseAdapter;
    private ArrayList<ItemListView> arrItemListViews = new ArrayList<>();
    private ArrayList<ItemListView> arrAllItemListViews;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;
    //
    private FeedbackDialog feedbackDialog;
    private int chooseAdapter;
    //

    private boolean isOnHomePage = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new MyDatabase(this);
        //khoi tao
        arrCountContentDrawerLayout = new ArrayList<>();
        database = new MyDatabase(this);
        initCount();
        listViewDrawerLayoutAdapter = new ListViewDrawerLayoutAdapter(arrCountContentDrawerLayout);
        initToolbar();
        arrAllItemListViews = database.getDataFromGameList();
        //
        lvChosseAdapter = new ListViewAdapter(this, arrItemListViews, POPUP_MENU_IN_LIST_LIKE_AND_LATER);
        gridViewAdapter = new GridViewAdapter(this, arrItemListViews);

        initMainViews();

    }

    //khoi tao dem so tren drawerlayout
    private void initCount() {
        arrCountContentDrawerLayout.add(0);
        arrCountContentDrawerLayout.add(12);
        arrCountContentDrawerLayout.add(0);
    }

    private void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Trang chủ");
//        titleToolbar  = (TextView) findViewById(R.id.titleToolbar);
//        titleToolbar.setText("Trang chủ");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMainViews() {
        //set title action bar

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.black));

        linearLayoutListMain = (LinearLayout) findViewById(R.id.linear_list_main);
        linearLayoutListChosseFromDrawerLayout = (RelativeLayout) findViewById(R.id.fragment_list_drawerlayout);
        //drawerLayout
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                Toast.makeText(getBaseContext(),"open",Toast.LENGTH_SHORT).show();


            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                Toast.makeText(getBaseContext(),"close",Toast.LENGTH_SHORT).show();
//                window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // tabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setOffscreenPageLimit(5);

        //listview cua drawerlayout
        listViewDrawerLayout = (RecyclerView) findViewById(R.id.lv_drawerlayout);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        listViewDrawerLayout.setLayoutManager(horizontalLayoutManagaer);
        listViewDrawerLayout.setAdapter(listViewDrawerLayoutAdapter);
        listViewDrawerLayoutAdapter.setOnItemClickListener(clickDrawerLayout);

        //listview chon tu drawerlayout
        lvChosseFromDrawerLayout = (ListView) findViewById(R.id.lv_chosse_drawerlayout);
        lvChosseFromDrawerLayout.setAdapter(lvChosseAdapter);
        lvChosseFromDrawerLayout.setOnItemClickListener(this);
        //gridview
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(this);

        // list view cua thanh search

        lvSearch = (ListView) findViewById(R.id.lvSearch);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ContentGameActivity.class);
        boolean isLike = false;
        boolean isLater = false;
        ArrayList<ItemListView> arrItemListViewsLike = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LIKE);
        ArrayList<ItemListView> arrItemListViewsLater = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LATER);
        for (int count = 0; count < arrItemListViewsLike.size(); count++) {
            if (arrItemListViewsLike.get(count).getName().equals(arrItemListViews.get(i).getName())) {
                isLike = true;
                break;
            }
        }
        for (int count = 0; count < arrItemListViewsLater.size(); count++) {
            if (arrItemListViewsLater.get(count).getName().equals(arrItemListViews.get(i).getName())) {
                isLater = true;
            }
        }

        intent.putExtra(BaseFragment.KEY_INTENT_CHANGE, arrItemListViews.get(i));
        intent.putExtra(BaseFragment.KEY_ITEM_IS_LIKE, isLike);
        intent.putExtra(BaseFragment.KEY_ITEM_IS_LATER, isLater);
        startActivity(intent);
    }

    private ListViewDrawerLayoutAdapter.OnItemClickListener clickDrawerLayout = new ListViewDrawerLayoutAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    isOnHomePage = true;
                    searchView.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Trang chủ");
                    linearLayoutListMain.setVisibility(View.VISIBLE);
                    linearLayoutListChosseFromDrawerLayout.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    isOnHomePage = false;
                    getSupportActionBar().setTitle("Danh sách xem sau");
                    chooseAdapter = 1;
                    chooseDrawerLayout(1);
                    break;
                case 2:
                    isOnHomePage = false;
                    getSupportActionBar().setTitle("Danh sách yêu thích");
                    chooseAdapter = 2;
                    chooseDrawerLayout(2);
                    break;
                case 3:
                    break;
                case 4:
                    feedbackDialog = new FeedbackDialog();
                    feedbackDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    feedbackDialog.show(getFragmentManager(), "Feedback");
                    break;
                case 5:
                    introductionDialog = new IntroductionDialog();
                    introductionDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
                    introductionDialog.show(getFragmentManager(), "Introduction");
                    break;
                case 6:
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                    alertDialog.setTitle("Thoát ứng dụng");
                    alertDialog.setMessage("Bạn có chắc chắn muốn thoát ?");
                    alertDialog.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            System.exit(0);
                        }
                    });
                    alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                    break;
            }
            drawerLayout.closeDrawers();
        }

        @Override
        public void onClick(View view) {

        }
    };

    //cap nhat listview khi chon item trong drawerlayout
    //action hanh dong cua item tren drawerlayout
    //1:xem,2:thich
    private void chooseDrawerLayout(int action) {
        linearLayoutListMain.setVisibility(View.INVISIBLE);
        linearLayoutListChosseFromDrawerLayout.setVisibility(View.VISIBLE);

        lvChosseFromDrawerLayout.setVisibility(View.VISIBLE);
        gridView.setVisibility(View.INVISIBLE);
        lvChosseAdapter.setChooseListview(chooseAdapter);

        if (action == 1) {
            arrItemListViews.clear();
            arrItemListViews.addAll(database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LATER));
            lvChosseAdapter.notifyDataSetChanged();
            gridViewAdapter.notifyDataSetChanged();
        } else if (action == 2) {
            arrItemListViews.clear();
            arrItemListViews.addAll(database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LIKE));
            lvChosseAdapter.notifyDataSetChanged();
            gridViewAdapter.notifyDataSetChanged();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    } // Kiểm tra kết nối mạng trên máy

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //
        MenuItem item1 = menu.findItem(R.id.item1);
        item1.setOnMenuItemClickListener(this);
        MenuItem item2 = menu.findItem(R.id.item2);
        item2.setOnMenuItemClickListener(this);
        //
        MenuItem itemSearch = menu.findItem(R.id.searchView);
        this.searchView = (SearchView) itemSearch.getActionView();
        itemSearch.collapseActionView();
        searchView.setOnQueryTextListener(this);
        //

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        drawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                viewPagerAdapter.changListViewList();
                lvChosseFromDrawerLayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.INVISIBLE);
                break;
            case R.id.item2:
                viewPagerAdapter.changeGridViewList();
                lvChosseFromDrawerLayout.setVisibility(View.INVISIBLE);
                gridView.setVisibility(View.VISIBLE);
                break;
        }
        if (!isOnHomePage) {
            searchView.setVisibility(View.GONE);
        }
        return false;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            lvSearch.setVisibility(View.GONE);
        } else {
            lvSearch.setVisibility(View.VISIBLE);
        }
        if (chooseAdapter == 0) {
            arrAllItemListViews = database.getDataDistinctFromGameTable(MyDatabase.TB_NAME_LIST_MAIN);
        } else if (chooseAdapter == 1) {
            arrAllItemListViews = database.getDataDistinctFromGameTable(MyDatabase.TB_NAME_LIST_LATER);
        } else if (chooseAdapter == 2) {
            arrAllItemListViews = database.getDataDistinctFromGameTable(MyDatabase.TB_NAME_LIST_LIKE);
        }

        final ArrayList<ItemListView> arrSearchResult = new ArrayList<>();
        for (int i = 0; i < arrAllItemListViews.size(); i++) {
            if (arrAllItemListViews.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                arrSearchResult.add(arrAllItemListViews.get(i));
            }
        }
        ListViewSearchAdapter adapter = new ListViewSearchAdapter(this, arrSearchResult);
        lvSearch.setAdapter(adapter);
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ContentGameActivity.class);
                boolean isLike = false;
                boolean isLater = false;
                ArrayList<ItemListView> arrItemListViewsLike = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LIKE);
                ArrayList<ItemListView> arrItemListViewsLater = database.getDataFromGameTable(MyDatabase.TB_NAME_LIST_LATER);
                for (int count = 0; count < arrItemListViewsLike.size(); count++) {
                    if (arrItemListViewsLike.get(count).getName().equals(arrSearchResult.get(i).getName())) {
                        isLike = true;
                        break;
                    }
                }
                for (int count = 0; count < arrItemListViewsLater.size(); count++) {
                    if (arrItemListViewsLater.get(count).getName().equals(arrSearchResult.get(i).getName())) {
                        isLater = true;
                    }
                }

                intent.putExtra(BaseFragment.KEY_INTENT_CHANGE, arrSearchResult.get(i));
                intent.putExtra(BaseFragment.KEY_ITEM_IS_LIKE, isLike);
                intent.putExtra(BaseFragment.KEY_ITEM_IS_LATER, isLater);
                if (!searchView.isIconified()) {
                    searchView.onActionViewCollapsed();
                }
                startActivity(intent);
            }
        });
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }
}
