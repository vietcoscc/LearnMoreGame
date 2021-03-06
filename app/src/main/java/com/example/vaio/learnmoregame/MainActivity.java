package com.example.vaio.learnmoregame;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.example.vaio.adapter.ListViewThemeAdapter;
import com.example.vaio.adapter.ViewPagerAdapter;
import com.example.vaio.database.MyDatabase;
import com.example.vaio.dialog.FeedbackDialog;
import com.example.vaio.dialog.IntroductionDialog;
import com.example.vaio.fragment.BaseFragment;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, MenuItem.OnMenuItemClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    public static final int POPUP_MENU_IN_HOME = 0;
    public static final int POPUP_MENU_IN_LIST_LIKE_AND_LATER = 1;
    public static final String PREFERENCES = "preferences";
    public static final String TOOLBAR_THEME = "toolbar theme";
    public static final String TAB_LAYOUT_THEME = "tabLayout theme";
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
    private ListView lvTheme;
    private SharedPreferences sharedPreferences;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * khởi tạo đếm số trong danh sách xem sau,yêu thích
     */
    private void initCount() {
        arrCountContentDrawerLayout.clear();
        arrCountContentDrawerLayout.add(0);
        arrCountContentDrawerLayout.add(database.getCountTable(MyDatabase.TB_NAME_LIST_LATER));//ds xem sau
        arrCountContentDrawerLayout.add(database.getCountTable(MyDatabase.TB_NAME_LIST_LIKE));//sa yêu thích
    }

    /**
     * Toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Trang chủ");
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
                initCount();
                listViewDrawerLayoutAdapter.notifyDataSetChanged();
                super.onDrawerOpened(drawerView);
//                Toast.makeText(getBaseContext(),"open",Toast.LENGTH_SHORT).show();

            }

            public void onDrawerClosed(View drawerView) {
                clearColor(lastView);
                initCount();
                listViewDrawerLayoutAdapter.notifyDataSetChanged();
                super.onDrawerClosed(drawerView);
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
        loadSetting();
    }

    private void loadSetting() {
        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        if (sharedPreferences != null) {
            int toolBarTheme = sharedPreferences.getInt(TOOLBAR_THEME, R.color.Indigo600);
            int tabLayoutTheme = sharedPreferences.getInt(TAB_LAYOUT_THEME, R.color.colorPrimary);
            toolbar.setBackgroundColor(toolBarTheme);
            tabLayout.setBackgroundColor(tabLayoutTheme);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "Vui lòng kết nối mạng", Toast.LENGTH_SHORT).show();
            return;
        }
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

    /**
     * Action click của sự kiện click item trên drawerlayout
     * 0:trang chủ
     * 1:ds xem sau
     * 2:ds yêu thích
     * 3:theme
     * 4:phản hồi
     * 5:giới thiệu
     * 6:thoát
     */
    private View lastView;

    private void clearColor(View view) {
        if (view != null) {
            view.setBackgroundColor(0);
        }
    }

    private ListViewDrawerLayoutAdapter.OnItemClickListener clickDrawerLayout = new ListViewDrawerLayoutAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            lastView = view;
            view.setBackgroundColor(getResources().getColor(R.color.grey200));
            switch (position) {
                case 0:
                    searchView.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Trang chủ");
                    linearLayoutListMain.setVisibility(View.VISIBLE);
                    linearLayoutListChosseFromDrawerLayout.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    getSupportActionBar().setTitle("Danh sách xem sau");
                    chooseAdapter = 1;
                    chooseDrawerLayout(1);
                    break;
                case 2:
                    getSupportActionBar().setTitle("Danh sách yêu thích");
                    chooseAdapter = 2;
                    chooseDrawerLayout(2);
                    break;
                case 3:
                    final ArrayList<Integer> arrItemColor = new ArrayList<>();
                    int arrColor[] =
                            {getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.red200),
                                    getResources().getColor(R.color.red300), getResources().getColor(R.color.red400), getResources().getColor(R.color.pink200),
                                    getResources().getColor(R.color.pink300), getResources().getColor(R.color.pink400), getResources().getColor(R.color.purple200),
                                    getResources().getColor(R.color.purple300), getResources().getColor(R.color.purple400), getResources().getColor(R.color.deepPurple200),
                                    getResources().getColor(R.color.deepPurple300), getResources().getColor(R.color.deepPurple400), getResources().getColor(R.color.blue200),
                                    getResources().getColor(R.color.blue300), getResources().getColor(R.color.blue300), getResources().getColor(R.color.green200),
                                    getResources().getColor(R.color.green300), getResources().getColor(R.color.green400), getResources().getColor(R.color.orange200),
                                    getResources().getColor(R.color.orange300), getResources().getColor(R.color.orange400), getResources().getColor(R.color.brown200),
                                    getResources().getColor(R.color.brown300), getResources().getColor(R.color.brown400), getResources().getColor(R.color.grey200),
                                    getResources().getColor(R.color.grey300), getResources().getColor(R.color.grey400)};

                    for (int i = 0; i < arrColor.length; i++) {
                        arrItemColor.add(arrColor[i]);
                    }
                    final int lastColorToolbar = ((ColorDrawable) toolbar.getBackground()).getColor();
                    final int lastColorTablayout = ((ColorDrawable) tabLayout.getBackground()).getColor();
                    LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                    View v = inflater.inflate(R.layout.theme_changer_dialog, null);


                    lvTheme = (ListView) v.findViewById(R.id.lvThemeToolbar);
                    ListViewThemeAdapter listViewThemeAdapter = new ListViewThemeAdapter(MainActivity.this, arrItemColor);
                    lvTheme.setAdapter(listViewThemeAdapter);
                    lvTheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            int colorFrom = ((ColorDrawable) toolbar.getBackground()).getColor();
                            int colorTo = arrItemColor.get(i);
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(1000); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    toolbar.setBackgroundColor((int) animator.getAnimatedValue());
                                }

                            });
                            colorAnimation.start();
//                            toolbar.setBackgroundColor(arrItemColor.get(i));
                            sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(TOOLBAR_THEME, arrItemColor.get(i));
                            editor.apply();
                            editor.commit();

//
                        }
                    });


                    lvTheme = (ListView) v.findViewById(R.id.lvThemeTablayout);
                    ListViewThemeAdapter listViewThemeAdapter2 = new ListViewThemeAdapter(MainActivity.this, arrItemColor);
                    lvTheme.setAdapter(listViewThemeAdapter2);
                    lvTheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int colorFrom = ((ColorDrawable) tabLayout.getBackground()).getColor();
                            int colorTo = arrItemColor.get(i);
                            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                            colorAnimation.setDuration(1000); // milliseconds
                            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                @Override
                                public void onAnimationUpdate(ValueAnimator animator) {
                                    tabLayout.setBackgroundColor((int) animator.getAnimatedValue());


                                }

                            });
                            colorAnimation.start();

                            sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(TAB_LAYOUT_THEME, arrItemColor.get(i));
                            editor.apply();
                            editor.commit();
                        }
                    });

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
                    builder.setTitle("Lựa chọn chủ đề");
                    builder.setCancelable(false);
                    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            toolbar.setBackgroundColor(lastColorToolbar);
                            tabLayout.setBackgroundColor(lastColorTablayout);
                        }
                    });
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Đã thay đổi theme", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setView(v);
                    builder.create().show();
                    break;
                case 4:
                    feedbackDialog = new FeedbackDialog();
                    feedbackDialog.setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
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

    /**
     * @param action :vị trí của sự kiện click trên drawerlayout
     *               cap nhat listview khi chon item trong drawerlayout
     *               action hanh dong cua item tren drawerlayout
     *               1:xem,2:thich
     */
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

    /**
     * @param context:context hiện tại
     * @return true:có mạng,false :ko có mạng
     */
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

    /**
     * @param item item1:in theo dạng listview
     *             item2:in theo dạng gridview
     * @return click
     * sự kiện click vào menu trên thanh Toolbar
     */
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


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * seach trên thanh Toolbar
     *
     * @param newText:text để tìm kiếm game
     * @return listview tìm kiếm
     */
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

                if (!MainActivity.isNetworkAvailable(getBaseContext())) {
                    Toast.makeText(getBaseContext(), "Vui lòng kết nối mạng", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    /**
     * bắt sự kiện nút backpress
     */
    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
