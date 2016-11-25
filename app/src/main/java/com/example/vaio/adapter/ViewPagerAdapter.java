package com.example.vaio.adapter;

import android.content.ClipData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vaio.database.MyDatabase;
import com.example.vaio.fragment.ActionGameFragment;
import com.example.vaio.fragment.FpsGameFragment;
import com.example.vaio.fragment.OpenWorldFragment;
import com.example.vaio.fragment.SurvivalGameFragment;
import com.example.vaio.fragment.TpsGameFragment;
import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.model_object.ItemListView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by vaio on 11/22/2016.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private String pageTitle[] = {"Action", "FPS", "Open world", "Survival", "TPS"};
    private MyDatabase database;
    private Context context;
    private ActionGameFragment actionGameFragment;
    private FpsGameFragment fpsGameFragment;
    private OpenWorldFragment openWorldFragment;
    private SurvivalGameFragment survivalGameFragment;
    private TpsGameFragment tpsGameFragment;

    public ViewPagerAdapter(Context context, FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        this.context = context;
        //
        actionGameFragment = new ActionGameFragment(context);
        fpsGameFragment = new FpsGameFragment(context);
        openWorldFragment = new OpenWorldFragment(context);
        survivalGameFragment = new SurvivalGameFragment(context);
        tpsGameFragment = new TpsGameFragment(context);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return actionGameFragment;
            case 1:
                return fpsGameFragment;
            case 2:
                return openWorldFragment;
            case 3:
                return survivalGameFragment;
            case 4:
                return tpsGameFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
