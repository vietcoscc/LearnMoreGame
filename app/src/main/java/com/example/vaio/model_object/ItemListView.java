package com.example.vaio.model_object;

/**
 * Created by vaio on 11/23/2016.
 */

public class ItemListView {
    private String imageUrl;
    private String name;
    private String type;
    private String date;
    private String views;
    private String detailsUrl;

    public ItemListView(String imageUrl, String name, String type, String date, String views, String detailsUrl) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.type = type;
        this.date = date;
        this.views = views;
        this.detailsUrl = detailsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getViews() {
        return views;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }
}
