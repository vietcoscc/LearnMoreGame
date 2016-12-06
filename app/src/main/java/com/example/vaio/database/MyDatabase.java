package com.example.vaio.database;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.example.vaio.fragment.ActionGameFragment;
import com.example.vaio.fragment.FpsGameFragment;
import com.example.vaio.fragment.OpenWorldFragment;
import com.example.vaio.fragment.SurvivalGameFragment;
import com.example.vaio.fragment.TpsGameFragment;
import com.example.vaio.learnmoregame.MainActivity;
import com.example.vaio.model_object.ItemListView;
import com.example.vaio.parser.JsoupParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by vaio on 11/25/2016.
 */

public class MyDatabase {
    public static final int WHAT = 1;
    public static final String TB_NAME_LIST_MAIN = "gamelist";
    public static final String TB_NAME_LIST_LATER = "listlater";
    public static final String TB_NAME_LIST_LIKE = "listlike";

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
    // mở database
    public void openDatabase() {
        database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
    }
    // đóng database
    public void closeDatabase() {
        database.close();
    }
    // copy database vào bộ nhớ trong của thiết bị
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


    // 59 29 13 12 23

    public ArrayList<ItemListView> getDataFromGameList() {
        openDatabase();
        Cursor cursor = database.query(TB_NAME_LIST_MAIN, null, null, null, null, null, null, null);
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

    public ArrayList<ItemListView> getDataFromGameTable(String nameTable) {
        openDatabase();
        ArrayList<ItemListView> arrAllItemListViewTemp = new ArrayList<>();
        ArrayList<ItemListView> arrAllItemListView = new ArrayList<>();
        Cursor cursor = database.query(true, nameTable, null, null, null, null, null, null, null);
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
            arrAllItemListViewTemp.add(itemListView);
            cursor.moveToNext();
        }
        closeDatabase();
        for (int i = arrAllItemListViewTemp.size() - 1; i >= 0; i--) {
            arrAllItemListView.add(arrAllItemListViewTemp.get(i));
        }
        return arrAllItemListView;
    }

    public int getCountTable(String tableName) {//lay so luong phan tu trong bang
        openDatabase();
        int count = 0;
        Cursor cursor = database.rawQuery("SELECT COUNT(*) AS COUNT FROM " + tableName, null);
        cursor.moveToFirst();
        count = cursor.getInt(cursor.getColumnIndex("COUNT"));
        closeDatabase();
        return count;
    }
    // thêm vào database mảng dữ liệu trên listview ở viewpager trang chủ
    public void insertArrItemListView(ArrayList<ItemListView> arrItemListView, String
            nameTable) {
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
            database.insert(nameTable, null, contentValues);
        }
        closeDatabase();
    }
    // Lấy mảng không bị trùng game , loại bỏ type id chỉ quan tâm tên
    public ArrayList<ItemListView> getDataDistinctFromGameTable(String nameTable) {
        openDatabase();
        ArrayList<ItemListView> arrAllItemListView = new ArrayList<>();
        String columns[] = {IMAGE_URL, NAME, TYPE, DATE, VIEWS, DETAILS_URL};
        Cursor cursor = database.query(true, nameTable, columns, null, null, null, null, null, null);
        cursor.moveToFirst();
//        int idIndex = cursor.getColumnIndex(ID);
//        int typeIdIndex = cursor.getColumnIndex(TYPE_ID);
        int imageUrlIndex = cursor.getColumnIndex(IMAGE_URL);
        int nameIndex = cursor.getColumnIndex(NAME);
        int typeIndex = cursor.getColumnIndex(TYPE);
        int dateIndex = cursor.getColumnIndex(DATE);
        int viewsIndex = cursor.getColumnIndex(VIEWS);
        int detailsUrlIndex = cursor.getColumnIndex(DETAILS_URL);
        while (!cursor.isAfterLast()) {
//            int typeId = cursor.getInt(typeIdIndex);
            String imageUrl = cursor.getString(imageUrlIndex);
            String name = cursor.getString(nameIndex);
            String type = cursor.getString(typeIndex);
            String date = cursor.getString(dateIndex);
            String views = cursor.getString(viewsIndex);
            String detailsUrl = cursor.getString(detailsUrlIndex);
            int typeId = -1; // giá trị tạm để typeid không null
            ItemListView itemListView = new ItemListView(typeId, imageUrl, name, type, date, views, detailsUrl);
            arrAllItemListView.add(itemListView);
            cursor.moveToNext();
        }

        closeDatabase();
        return arrAllItemListView;
    }
    // clear tất cả dữ liệu trong bảng nameTable ở trong databsse
    public void deleteAllItemListView(int typeId, String nameTable) {
        openDatabase();
        String whereArgs[] = {typeId + ""};
        database.delete(nameTable, TYPE_ID + " = ?", whereArgs);
        closeDatabase();
    }
    // thêm vào database dữ liệu trên listview ở viewpager trang chủ
    public void insertItemListView(ItemListView itemListView, String nameTable) {
        openDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE_ID, itemListView.getTypeId());
        contentValues.put(IMAGE_URL, itemListView.getImageUrl());
        contentValues.put(NAME, itemListView.getName());
        contentValues.put(TYPE, itemListView.getType());
        contentValues.put(DATE, itemListView.getDate());
        contentValues.put(DETAILS_URL, itemListView.getDetailsUrl());
        contentValues.put(VIEWS, itemListView.getViews());
        database.insert(nameTable, null, contentValues);
        closeDatabase();
    }
    // xóa một dòng dữ liệu trong bảng nameTable ở trong database
    public void deleteItemListView(ItemListView itemListView, String nameTable) {
        openDatabase();
        String nameGame = itemListView.getName();
        String whereArgs[] = {nameGame};
        database.delete(nameTable, NAME + "= ? ", whereArgs);
        closeDatabase();
    }
}
