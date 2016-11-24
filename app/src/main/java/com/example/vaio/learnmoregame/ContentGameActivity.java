package com.example.vaio.learnmoregame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.vaio.pasresinforgame.ParsesInformationGame;

/**
 * Created by TungMai on 24/11/2016.
 */

public class ContentGameActivity extends AppCompatActivity {

    private ParsesInformationGame parsesInformationGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parsesInformationGame=new ParsesInformationGame();
        parsesInformationGame.execute("http://www.linkneverdie.com/GameDetail/?baivietId=2607&ten=Beholder-2016");
    }
}
