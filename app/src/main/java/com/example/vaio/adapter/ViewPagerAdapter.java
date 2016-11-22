package com.example.vaio.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vaio.fragment.ActionGameFragment;
import com.example.vaio.fragment.FpsGameFragment;
import com.example.vaio.fragment.OpenWorldFragment;
import com.example.vaio.fragment.SurvivalGameFragment;
import com.example.vaio.fragment.TpsGameFragment;

/**
 * Created by vaio on 11/22/2016.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private String pageTitle []={"Action","FPS","Open world","Survival","TPS"};
    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ActionGameFragment actionGameFragment = new ActionGameFragment();
                return actionGameFragment;
            case 1:
                FpsGameFragment fpsGameFragment = new FpsGameFragment();
                return fpsGameFragment;
            case 2:
                OpenWorldFragment openWorldFragment = new OpenWorldFragment();
                return openWorldFragment;
            case 3:
                SurvivalGameFragment survivalGameFragment = new SurvivalGameFragment();
                return survivalGameFragment;
            case 4:
                TpsGameFragment tpsGameFragment = new TpsGameFragment();
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
