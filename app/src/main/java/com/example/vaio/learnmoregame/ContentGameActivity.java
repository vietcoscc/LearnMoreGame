package com.example.vaio.learnmoregame;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vaio.model_object.ItemInforGame;
import com.example.vaio.parser.ParserInformationGame;
import com.squareup.picasso.Picasso;


/**
 * Created by TungMai on 24/11/2016.
 */

public class ContentGameActivity extends AppCompatActivity {

    private ParserInformationGame parserInformationGame;

    private TextView tvNameGame;
    private TextView tvDate;
    private TextView tvView;
    private ImageView ivFirst;
    private TextView tvContent;
    private TextView tvConfiguration;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==ParserInformationGame.WHAT_PARSER_INFOR_GAME){
                ItemInforGame itemInforGame= (ItemInforGame) msg.obj;

                tvContent.setText(itemInforGame.getIntroduce());
                tvConfiguration.setText(itemInforGame.getConfiguration());
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_game);
        initViews();
        parserInformationGame=new ParserInformationGame(handler);
        parserInformationGame.execute("http://www.linkneverdie.com/GameDetail/?baivietId=2607&ten=Beholder-2016");
    }

    private void initViews() {
        ivFirst= (ImageView) findViewById(R.id.iv_backgroup_game);
        tvContent= (TextView) findViewById(R.id.tv_content_game);
        tvConfiguration= (TextView) findViewById(R.id.tv_configuration);
    }
}
