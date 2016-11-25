package com.example.vaio.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.vaio.model_object.ItemListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vaio on 11/25/2016.
 */

public class MyDatabase {

    public static final String TB_NAME = "gamelist";
    public static final String DB_NAME = "learnmoregame.sqlite";
    public static final String ID = "id";
    public static final String TYPE_ID = "typeId";
    public static final String IMAGE_URL = "imageUrl";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DATE = "date";
    public static final String VIEWS = "views";
    public static final String DETAILS_URL = "detailsUrl";
    public static final String PATH = Environment.getDataDirectory() + "/data/com.example.vaio.learnmoregame/databases/" + DB_NAME;
    private Context context;
    private SQLiteDatabase database;
    private ArrayList<ItemListView> arrAllItemListView = new ArrayList<>();

    public MyDatabase(Context context) {
        this.context = context;
        copyDatabase(context);
    }

    public void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }

    public void closeDatabase() {
        database.close();
    }

    private void copyDatabase(Context context) {
        File file = new File(PATH);
        if (file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();
        parentFile.mkdirs();

        byte[] b = new byte[1024];
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = context.getAssets().open(DB_NAME);
            int count = inputStream.read(b);
            while (count != -1) {
                outputStream.write(b, 0, count);
                count = inputStream.read(b);
            }
            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ItemListView> getDataFromGameList() {
        openDatabase();
        Cursor cursor = database.query(true,TB_NAME,null, null, null, null, null, null, null);
        cursor.moveToFirst();
//        int idIndex = cursor.getColumnIndex(ID);
        int typeIdIndex = cursor.getColumnIndex(TYPE_ID);
        int imageUrlIndex = cursor.getColumnIndex(IMAGE_URL);
        int nameIndex = cursor.getColumnIndex(NAME);
        int typeIndex = cursor.getColumnIndex(TYPE);
        int dateIndex = cursor.getColumnIndex(DATE);
        int viewsIndex = cursor.getColumnIndex(VIEWS);
        int detailsUrlIndex = cursor.getColumnIndex(DETAILS_URL);
        while (!cursor.isAfterLast()) {
            int typeId = cursor.getInt(typeIdIndex);
            String imageUrl = cursor.getString(imageUrlIndex);
            String name = cursor.getString(nameIndex);
            String type = cursor.getString(typeIndex);
            String date = cursor.getString(dateIndex);
            String views = cursor.getString(viewsIndex);
            String detailsUrl = cursor.getString(detailsUrlIndex);
            ItemListView itemListView = new ItemListView(typeId, imageUrl, name, type, date, views, detailsUrl);
            arrAllItemListView.add(itemListView);
            cursor.moveToNext();
        }

        closeDatabase();
        return arrAllItemListView;
    }

    public void insertArrItemListView(ArrayList<ItemListView> arrItemListView) {
        openDatabase();
        for (int i = 0; i < arrItemListView.size(); i++) {
            ItemListView itemListView = arrItemListView.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(TYPE_ID, itemListView.getTypeId());
            contentValues.put(IMAGE_URL, itemListView.getImageUrl());
            contentValues.put(NAME, itemListView.getName());
            contentValues.put(TYPE, itemListView.getType());
            contentValues.put(DATE, itemListView.getDate());
            contentValues.put(DETAILS_URL, itemListView.getDetailsUrl());
            contentValues.put(VIEWS, itemListView.getViews());
            database.insert(TB_NAME, null, contentValues);
        }
        closeDatabase();
    }

    public void deleteAllItemListView(int typeId) {
        openDatabase();
        String whereArgs[] = {typeId + ""};
        database.delete(TB_NAME, TYPE_ID + " = ?", whereArgs);
        closeDatabase();
    }
}
