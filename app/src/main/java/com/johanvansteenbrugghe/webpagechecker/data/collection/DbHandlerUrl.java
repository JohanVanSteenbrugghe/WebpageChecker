package com.johanvansteenbrugghe.webpagechecker.data.collection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.johanvansteenbrugghe.webpagechecker.data.model.UrlModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class DbHandlerUrl extends DbHandlerBase {
    private Context context;

    public DbHandlerUrl(Context context) {
        super(context);
        this.context = context;
    }

    public int addUrlModel(UrlModel urlModel, Boolean showToast){
        final Activity activity = (Activity) context;

        if (showToast && urlModel.description.equals("")){
            Toast.makeText(activity, "Save failed. Description is empty.", Toast.LENGTH_SHORT).show();
            return 0;
        }
        else if (showToast && urlModel.url.equals("")){
            Toast.makeText(activity, "Save failed. Url is empty.", Toast.LENGTH_SHORT).show();
            return 0;
        }

        try{
            URL url = new URL(urlModel.url);
        }
        catch (MalformedURLException e) {
            if (showToast){
                Toast.makeText(activity, "Save failed. Wrong format url.", Toast.LENGTH_SHORT).show();
            }
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHandlerBase.URL_KOLOM_DESCRIPTION, urlModel.description);
        contentValues.put(DbHandlerBase.URL_KOLOM_URL, urlModel.url);
        long url_Id = sqLiteDatabase.insert(DbHandlerBase.URL_TABLE,null, contentValues);

        if (showToast && url_Id != -1){
            Toast.makeText(activity, "Saved.", Toast.LENGTH_SHORT).show();
        }

        return (int)url_Id;
    }

    public boolean updateUrlModel(UrlModel urlModel, Boolean showToast){
        final Activity activity = (Activity) context;

        if (showToast && urlModel.description.equals("")){
            Toast.makeText(activity, "Save failed. Description is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (showToast && urlModel.url.equals("")){
            Toast.makeText(activity, "Save failed. Url is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        try{
            URL url = new URL(urlModel.url);
        }
        catch (MalformedURLException e) {
            if (showToast){
                Toast.makeText(activity, "Save failed. Wrong format url.", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHandlerBase.URL_KOLOM_DESCRIPTION, urlModel.description);
        contentValues.put(DbHandlerBase.URL_KOLOM_URL, urlModel.url);
        long url_Id = sqLiteDatabase.update(DbHandlerBase.URL_TABLE, contentValues, DbHandlerBase.URL_KOLOM_URL_ID + " = " + urlModel.url_Id, null);

        if (showToast && url_Id != -1){
            Toast.makeText(activity, "Saved.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }
    }

    public LinkedList<UrlModel> getAllUrlModels() {
        LinkedList<UrlModel> urlModelList = new LinkedList<UrlModel>();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " +
                DbHandlerBase.URL_KOLOM_URL_ID + ", " +
                DbHandlerBase.URL_KOLOM_DESCRIPTION + ", " +
                DbHandlerBase.URL_KOLOM_URL + " " +
                "FROM " + DbHandlerBase.URL_TABLE;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext()){
            UrlModel urlModel = new UrlModel();
            urlModel.url_Id = cursor.getInt(cursor.getColumnIndex(DbHandlerBase.URL_KOLOM_URL_ID));
            urlModel.description = cursor.getString(cursor.getColumnIndex(DbHandlerBase.URL_KOLOM_DESCRIPTION));
            urlModel.url = cursor.getString(cursor.getColumnIndex(DbHandlerBase.URL_KOLOM_URL));
            urlModelList.add(urlModel);
        }

        return urlModelList;
    }

    public UrlModel getUrlModelFromUrlId(int url_Id){
        UrlModel urlModel = new UrlModel();

        if (url_Id == 0){
            return urlModel;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " +
                DbHandlerBase.URL_KOLOM_URL_ID + ", " +
                DbHandlerBase.URL_KOLOM_DESCRIPTION + ", " +
                DbHandlerBase.URL_KOLOM_URL + " " +
                "FROM " + DbHandlerBase.URL_TABLE + " WHERE " + DbHandlerBase.URL_KOLOM_URL_ID + " = " + url_Id;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext()){
            urlModel.url_Id = cursor.getInt(cursor.getColumnIndex(DbHandlerBase.URL_KOLOM_URL_ID));
            urlModel.description = cursor.getString(cursor.getColumnIndex(DbHandlerBase.URL_KOLOM_DESCRIPTION));
            urlModel.url = cursor.getString(cursor.getColumnIndex(DbHandlerBase.URL_KOLOM_URL));
        }

        return urlModel;
    }

    public void deleteAllUrlModels(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + DbHandlerBase.URL_TABLE);
    }

    public void deleteUrlModelWithUrlId(int url_Id){
        if (url_Id == 0){
            return;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String whereClause = DbHandlerBase.URL_KOLOM_URL_ID + " = ? ";
        String[] whereArgs = new String[] {String.valueOf(url_Id)};
        sqLiteDatabase.delete(DbHandlerBase.URL_TABLE, whereClause, whereArgs);
    }
}
