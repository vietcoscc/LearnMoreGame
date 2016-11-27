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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.example.vaio.adapter.ListViewDrawerLayoutAdapter;
import com.example.vaio.adapter.ViewPagerAdapter;
import com.example.vaio.dialog.IntroductionDialog;
import com.example.vaio.parser.JsoupParser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MenuItem.OnMenuItemClickListener {

    public static final String ACTION_GAME = "Action";
    public static final String FPS_GAME = "FPS";
    public static final String OPEN_WORLD_GAME = "Open world";
    public static final String SURVIVAL_GAME = "Survival";
    public static final String TPS_GAME = "TPS";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private SearchView searchView;
    private LinearLayout linearLayoutListMain;
    private IntroductionDialog introductionDialog;


    private ListViewDrawerLayoutAdapter listViewDrawerLayoutAdapter;
    private ArrayList<Integer> arrCountContentDrawerLayout;
    private RecyclerView listViewDrawerLayout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //khoi tao
        arrCountContentDrawerLayout = new ArrayList<>();
        initCount();
        listViewDrawerLayoutAdapter = new ListViewDrawerLayoutAdapter(arrCountContentDrawerLayout);

        initToolbar();
        initMainViews();

    }

    //khoi tao dem so tren drawerlayout
    private void initCount() {
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

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.black));

        linearLayoutListMain = (LinearLayout) findViewById(R.id.linear_list_main);
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
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager.setOffscreenPageLimit(5);

        //listview cua drawerlayout
        listViewDrawerLayout = (RecyclerView) findViewById(R.id.lv_drawerlayout);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        listViewDrawerLayout.setLayoutManager(horizontalLayoutManagaer);
        listViewDrawerLayout.setAdapter(listViewDrawerLayoutAdapter);
        listViewDrawerLayoutAdapter.setOnItemClickListener(clickDrawerLayout);
    }

    private ListViewDrawerLayoutAdapter.OnItemClickListener clickDrawerLayout = new ListViewDrawerLayoutAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    introductionDialog = new IntroductionDialog();
                    introductionDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
                    introductionDialog.show(getFragmentManager(), "Introduction");
                    break;
                case 5:
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
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

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        //
        MenuItem item = menu.findItem(R.id.item1);
        item.setOnMenuItemClickListener(this);
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
        return false;
    }
}
