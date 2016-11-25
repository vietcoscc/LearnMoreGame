package com.example.vaio.pasresinforgame;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by TungMai on 24/11/2016.
 */

public class ParsesInformationGame extends AsyncTask<String, Void, ItemInforGame> {
    private static final String TAG = "ParsesInformationGame";

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

            Element elementIntroduce = document.getElementById("gioithieudiv");
            introduce = elementIntroduce.text();

            Elements elementLinkYoutube = document.select("div.fluid-width-video-wrapper");
            linkYoutube = "https:" + elementLinkYoutube.get(0).select("iframe").attr("src");

            Element elementConfiguration = document.getElementById("cauhinhdiv");
            configuration = elementConfiguration.text();

            Elements elementsImage = document.select("div.slide");
            for (int i = 0; i < elementsImage.size(); i++) {
                arrUrlImage.add("http://www.linkneverdie.com/" + elementsImage.get(i).select("img").attr("src"));
            }
            itemInforGame = new ItemInforGame(introduce, linkYoutube, configuration, arrUrlImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemInforGame;
    }

    @Override
    protected void onPostExecute(ItemInforGame s) {
        super.onPostExecute(s);
    }
}
