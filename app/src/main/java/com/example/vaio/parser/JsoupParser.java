package com.example.vaio.parser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.model_object.ItemListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by vaio on 11/23/2016.
 */

public class JsoupParser extends AsyncTask<String, Void, ArrayList<ItemListView>> {
    private static final String TAG = "JsoupParser";
    private Handler handler;
    private int typeId;
    private ProgressDialog progress;
    private Context context;
    public JsoupParser(Context context,Handler handler, int typeId) {
        this.handler = handler;
        this.typeId = typeId;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(context, null,
                "loading", true);
        progress.setCancelable(false);
    }

    @Override
    protected ArrayList<ItemListView> doInBackground(String... params) {
        ArrayList<ItemListView> arrItemListView = new ArrayList<>();
        String link = params[0];
        try {
            Document document = Jsoup.connect(link).get();
            Elements elementsI = document.select("div.portfolio-image");
            Elements elementsD = document.select("div.portfolio-desc");
            for (int i = 0; i < elementsI.size(); i++) {
                Elements elementsImage = elementsI.get(i).select("img");
                String path = elementsImage.attr("src");
                StringBuilder builder = new StringBuilder(path);
                String imageUrl;
                if(builder.toString().charAt(0)=='h'){
                    imageUrl = builder.toString();
                }else {
                    imageUrl = "http://linkneverdie.com" + builder.toString();
                }
                String name = elementsD.get(i).select("h3").text();
                String type = elementsD.get(i).select("span").text();
                String nameAndTypeAndDateAndViews = elementsD.get(i).text();
                String views = getViews(nameAndTypeAndDateAndViews);
                String date = getDate(nameAndTypeAndDateAndViews, name, type, views);
                String detailsUrl = "linkneverdie.com" + elementsD.get(i).select("a").attr("href");
//
//                Log.e(TAG,typeId+"");
//                Log.e(TAG, imageUrl);
//                Log.e(TAG, name);
//                Log.e(TAG, type);
//                Log.e(TAG, views);
//                Log.e(TAG, date);
//                Log.e(TAG, detailsUrl);

                ItemListView itemListView = new ItemListView(typeId, imageUrl, name, type, date, views, detailsUrl);
                arrItemListView.add(itemListView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return arrItemListView;
    }

    @Override
    protected void onPostExecute(ArrayList<ItemListView> itemListViews) {
        super.onPostExecute(itemListViews);
        Message msg = new Message();
        msg.what = 1;
        msg.obj = itemListViews;
        msg.arg1 = typeId;
        handler.sendMessage(msg);
        progress.dismiss();
    }

    private String getViews(String s) {
        StringBuilder builder = new StringBuilder(s);
        int start = builder.lastIndexOf(" ");
        return builder.substring(start + 1, builder.length());
    }

    @NonNull
    private String getDate(String s, String name, String type, String views) {
        StringBuilder builder = new StringBuilder(s);
        int startName = builder.indexOf(name);
        builder.delete(startName, startName + name.length());
        int startType = builder.indexOf(type);
        builder.delete(startType, startType + type.length());
        int startViews = builder.indexOf(views);
        builder.delete(startViews, startViews + views.length());
        return builder.toString();
    }
}
