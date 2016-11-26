package com.example.vaio.model_object;

import java.io.Serializable;

/**
 * Created by vaio on 11/23/2016.
 */

public class ItemListView implements Serializable{
    private int typeId; // Xác định item nằm trong listView nào
    private String imageUrl;
    private String name;
    private String type;
    private String date;
    private String views;
    private String detailsUrl;

    public ItemListView(int typeId, String imageUrl, String name, String type, String date, String views, String detailsUrl) {
        this.typeId = typeId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.type = type;
        this.date = date;
        this.views = views;
        this.detailsUrl = detailsUrl;
    }

    public int getTypeId() {
        return typeId;
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
