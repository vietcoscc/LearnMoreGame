package com.example.vaio.iteminfor;

import java.util.ArrayList;

/**
 * Created by TungMai on 24/11/2016.
 */

public class ItemInforGame {

    private String introduce;
    private String linkYoutube;
    private String configuration;
    private ArrayList<String> arrUrlImage;

    public ItemInforGame(String introduce, String linkYoutube,String configuration, ArrayList<String> arrUrlImage) {
        this.introduce = introduce;
        this.linkYoutube = linkYoutube;
        this.configuration=configuration;
        this.arrUrlImage = arrUrlImage;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getLinkYoutube() {
        return linkYoutube;
    }

    public void setLinkYoutube(String linkYoutube) {
        this.linkYoutube = linkYoutube;
    }

    public ArrayList<String> getArrUrlImage() {
        return arrUrlImage;
    }

    public void setArrUrlImage(ArrayList<String> arrUrlImage) {
        this.arrUrlImage = arrUrlImage;
    }
}
