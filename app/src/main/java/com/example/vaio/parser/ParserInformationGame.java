package com.example.vaio.parser;

/**
 * Created by TungMai on 25/11/2016.
 */

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.vaio.model_object.ItemInforGame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by TungMai on 24/11/2016.
 */

public class ParserInformationGame extends AsyncTask<String, Void, ItemInforGame> {
    private static final String TAG = "ParsesInformationGame";
    public static final int WHAT_PARSER_INFOR_GAME = 32;
    private Handler handler;

    public ParserInformationGame(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected ItemInforGame doInBackground(String... params) {
        String link = params[0];
        ItemInforGame itemInforGame = null;
        String introduce = "";//Thông tin game
        String linkYoutube = "";//link youtube
        String configuration = "";//cấu hình game
        ArrayList<String> arrUrlImage = new ArrayList<>();//list ảnh giới thiệu
        try {
            Document document = Jsoup.connect(link).get();

            Elements elementLinkYoutube = document.select("div.fluid-width-video-wrapper");
            linkYoutube = "https:" + elementLinkYoutube.get(0).select("iframe").attr("src");

            Elements elementsImage = document.select("div.slide");
            for (int i = 0; i < elementsImage.size(); i++) {
                arrUrlImage.add("http://www.linkneverdie.com/" + elementsImage.get(i).select("img").attr("src"));
            }

            document.select("br").append("br2n");
            Element elementIntroduce = document.getElementById("gioithieudiv");
            introduce = elementIntroduce.text().replace("br2n","\n\n");

            document.select("p").append("br2n");
            Element elementConfiguration = document.getElementById("cauhinhdiv");
            configuration = elementConfiguration.text().replace("br2n","\n");


            itemInforGame = new ItemInforGame(introduce, linkYoutube, configuration, arrUrlImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemInforGame;
    }

    @Override
    protected void onPostExecute(ItemInforGame s) {
        super.onPostExecute(s);
        Message message = new Message();
        message.what = WHAT_PARSER_INFOR_GAME;
        message.obj = s;
        handler.sendMessage(message);
    }
}

