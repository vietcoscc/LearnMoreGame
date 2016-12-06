package com.example.vaio.dialog;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaio.learnmoregame.R;

/**
 * Created by TungMai on 27/11/2016.
 */

public class IntroductionDialog extends DialogFragment {

    private View view;
    private ImageView ivBack;
    private TextView tvTeam;

    private static final String STR_TEAM = "Nguyễn Quốc Việt \n Mai Thanh Tùng\nTrương Văn Tý\nPhạm Văn Sơn\nNguyễn Việt Anh";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_introduction, container, false);


        getDialog().getWindow().getAttributes().windowAnimations = R.style.AnimationDialogIntroduction;
        initViews();
        return view;
    }

    private void initViews() {
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        tvTeam = (TextView) view.findViewById(R.id.tv_list_team);
        tvTeam.setText(STR_TEAM);

    }
}
