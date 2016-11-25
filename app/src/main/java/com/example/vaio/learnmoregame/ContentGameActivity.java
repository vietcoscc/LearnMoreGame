package com.example.vaio.learnmoregame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.vaio.parser.ParserInformationGame;


/**
 * Created by TungMai on 24/11/2016.
 */

public class ContentGameActivity extends AppCompatActivity {

    private ParserInformationGame parserInformationGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parserInformationGame=new ParserInformationGame();
        parserInformationGame.execute("http://www.linkneverdie.com/GameDetail/?baivietId=2607&ten=Beholder-2016");
    }
}
