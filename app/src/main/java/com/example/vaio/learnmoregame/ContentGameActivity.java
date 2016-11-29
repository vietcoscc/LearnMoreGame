package com.example.vaio.learnmoregame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vaio.adapter.ImageViewPagerAdapter;
import com.example.vaio.database.MyDatabase;
import com.example.vaio.fragment.BaseFragment;
import com.example.vaio.model_object.ItemInforGame;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.ParserInformationGame;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;


/**
 * Created by TungMai on 24/11/2016.
 */

public class ContentGameActivity extends YouTubeBaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private static final String YOUTUBE_KEY = "AIzaSyDSct6gyum4wKmQ_LjAa9shTbPBzfE41uU";
    private static final String TAG = "ContentGameActivity";
    private ParserInformationGame parserInformationGame;

    private ImageView ivBack;
    private ImageView ivMenu;
    private TextView tvNameGame;
    private TextView tvDate;
    private TextView tvView;
    private ImageView ivFirst;
    private TextView tvContent;
    private TextView tvConfiguration;
    private YouTubePlayerView videoView;
    private ViewPager viewPagerImage;
    private CirclePageIndicator circlePageIndicator;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private ImageViewPagerAdapter imageViewPagerAdapter;

    private ArrayList<String> arrImage;

    private ItemListView itemListView;


    private boolean isLike;
    private boolean isLater;
    private MyDatabase database;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ParserInformationGame.WHAT_PARSER_INFOR_GAME) {

                final ItemInforGame itemInforGame = (ItemInforGame) msg.obj;

                Log.e(TAG, itemInforGame.getUrlBackgroup());

                Picasso.with(getBaseContext()).load(itemInforGame.getUrlBackgroup()).into(ivFirst);

                arrImage.clear();
                if (itemInforGame.getArrUrlImage().size() > 0)
                    arrImage.addAll(itemInforGame.getArrUrlImage());
                imageViewPagerAdapter.notifyDataSetChanged();

                tvContent.setText(itemInforGame.getIntroduce());
                tvConfiguration.setText(itemInforGame.getConfiguration());
                videoView.initialize(YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(itemInforGame.getLinkYoutube());
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                        Toast.makeText(getBaseContext(),"Lỗi",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_game);
        database=new MyDatabase(this);
        arrImage = new ArrayList<>();
        imageViewPagerAdapter = new ImageViewPagerAdapter(this, arrImage);

        Intent intent = getIntent();
        itemListView = (ItemListView) intent.getSerializableExtra(BaseFragment.KEY_INTENT_CHANGE);
        isLike = intent.getBooleanExtra(BaseFragment.KEY_ITEM_IS_LIKE, false);
        isLater = intent.getBooleanExtra(BaseFragment.KEY_ITEM_IS_LATER, false);

        initViews();
//        Log.e(TAG,"https://"+itemListView.getDetailsUrl());
        parserInformationGame = new ParserInformationGame(this, handler);
        parserInformationGame.execute("https://" + itemListView.getDetailsUrl());
    }

    private void initViews() {
//        Intent intent=getIntent();
//        ItemListView itemListView= (ItemListView) intent.getSerializableExtra(BaseFragment.KEY_INTENT_CHANGE);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);

        ivMenu = (ImageView) findViewById(R.id.iv_menu);
        ivMenu.setOnClickListener(this);

        tvNameGame = (TextView) findViewById(R.id.tv_name_game);
        tvDate = (TextView) findViewById(R.id.date);
        tvView = (TextView) findViewById(R.id.views);

//        Log.e(TAG,itemListView.getName());

        tvNameGame.setText(itemListView.getName());
        tvDate.setText(itemListView.getDate());
        tvView.setText(itemListView.getViews());

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemListView.getName());
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        tvContent = (TextView) findViewById(R.id.tv_content_game);
        tvConfiguration = (TextView) findViewById(R.id.tv_configuration);
        ivFirst = (ImageView) findViewById(R.id.iv_image_fisrt);
        videoView = (YouTubePlayerView) findViewById(R.id.vv_video_game);

        viewPagerImage = (ViewPager) findViewById(R.id.iv_view_pager);

        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        viewPagerImage.setAdapter(imageViewPagerAdapter);
        circlePageIndicator.setViewPager(viewPagerImage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_menu:
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                if (isLike) popupMenu.getMenu().getItem(0).setEnabled(false);
                if (isLater) popupMenu.getMenu().getItem(1).setEnabled(false);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.favorite:
                database.insertItemListView(itemListView, MyDatabase.TB_NAME_LIST_LIKE);
                Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                break;
            case R.id.later:
                database.insertItemListView(itemListView, MyDatabase.TB_NAME_LIST_LATER);
                Toast.makeText(this, "Đã thêm vào danh sách xem sau", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, itemListView.getDetailsUrl());

                startActivity(Intent.createChooser(share, "Bạn muốn chia sẻ trên..."));
                break;
        }
        return false;
    }
}
