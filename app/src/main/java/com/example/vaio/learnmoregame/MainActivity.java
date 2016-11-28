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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.vaio.adapter.GridViewAdapter;
import com.example.vaio.adapter.ListViewAdapter;
import com.example.vaio.adapter.ListViewDrawerLayoutAdapter;
import com.example.vaio.adapter.ViewPagerAdapter;
import com.example.vaio.database.MyDatabase;
import com.example.vaio.dialog.IntroductionDialog;
import com.example.vaio.fragment.BaseFragment;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MenuItem.OnMenuItemClickListener, AdapterView.OnItemClickListener {

    public static final String ACTION_GAME = "Action";
    public static final String FPS_GAME = "FPS";
    public static final String OPEN_WORLD_GAME = "Open world";
    public static final String SURVIVAL_GAME = "Survival";
    public static final String TPS_GAME = "TPS";
    private static final String TAG = "MainActivity";
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

    private ListViewDrawerLayoutAdapter listViewDrawerLayoutAdapter;
    private ArrayList<Integer> arrCountContentDrawerLayout;
    private RecyclerView listViewDrawerLayout;

    //thao tac khi thuc hien drawer layout
    private MyDatabase database;
    private ListView lvChosseFromDrawerLayout;
    private ListViewAdapter lvChosseAdapter;
    private ArrayList<ItemListView> arrItemListViews;
    private GridView gridView;
    private GridViewAdapter gridViewAdapter;

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

        //
        arrItemListViews = new ArrayList<>();
        lvChosseAdapter = new ListViewAdapter(this, arrItemListViews);
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
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//    xx    actionBar.setTitle("");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ContentGameActivity.class);
        intent.putExtra(BaseFragment.KEY_INTENT_CHANGE, arrItemListViews.get(i));
        startActivity(intent);
    }

    private ListViewDrawerLayoutAdapter.OnItemClickListener clickDrawerLayout = new ListViewDrawerLayoutAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    linearLayoutListMain.setVisibility(View.VISIBLE);
                    linearLayoutListChosseFromDrawerLayout.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    chooseDrawerLayout(1);
                    break;
                case 2:
                    chooseDrawerLayout(2);
                    break;
                case 3:
                    break;
                case 4:
                    introductionDialog = new IntroductionDialog();
                    introductionDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
                    introductionDialog.show(getFragmentManager(), "Introduction");
                    break;
                case 5:
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
    }

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
        MenuItem searchView = menu.findItem(R.id.searchView);
        this.searchView = (SearchView) searchView.getActionView();
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
        return false;
    }


}
