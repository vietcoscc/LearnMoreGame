package com.example.vaio.model_object;

import java.util.ArrayList;

/**
 * Created by TungMai on 24/11/2016.
 * Đối tượng thông tin chi tiết game
 */

public class ItemInforGame {
    private String urlBackgroup;
    private String introduce;
    private String linkYoutube;
    private String configuration;
    private ArrayList<String> arrUrlImage;

    public ItemInforGame(String urlBackgroup, String introduce, String linkYoutube, String configuration, ArrayList<String> arrUrlImage) {
        this.urlBackgroup = urlBackgroup;
        this.introduce = introduce;
        this.linkYoutube = linkYoutube;
        this.configuration = configuration;
        this.arrUrlImage = arrUrlImage;
    }

    public String getUrlBackgroup() {
        return urlBackgroup;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getLinkYoutube() {
        return linkYoutube;
    }

    public ArrayList<String> getArrUrlImage() {
        return arrUrlImage;
    }

}
