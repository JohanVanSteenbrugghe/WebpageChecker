package com.johanvansteenbrugghe.webpagechecker.data.collection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.johanvansteenbrugghe.webpagechecker.data.model.KeywordModel;

import java.util.LinkedList;

public class DbHandlerKeyword extends DbHandlerBase {
    private Context context;

    public DbHandlerKeyword(Context context) {
        super(context);
        this.context = context;
    }

    public int addKeywordModel(KeywordModel keywordModel, Boolean showToast){
        final Activity activity = (Activity) context;

        if (showToast &&keywordModel.url_Id == 0){
            Toast.makeText(activity, "Save failed. Url not found.", Toast.LENGTH_SHORT).show();
            return 0;
        }
        else if (showToast && keywordModel.keyword.equals("")){
            Toast.makeText(activity, "Save failed. Keyword is empty.", Toast.LENGTH_SHORT).show();
            return 0;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHandlerBase.KEYWORD_KOLOM_URL_ID, keywordModel.url_Id);
        contentValues.put(DbHandlerBase.KEYWORD_KOLOM_KEYWORD, keywordModel.keyword);
        long keyword_Id = sqLiteDatabase.insert(DbHandlerBase.KEYWORD_TABLE,null, contentValues);

        return (int)keyword_Id;
    }

    public boolean updateKeywordModel(KeywordModel keywordModel, Boolean showToast){
        final Activity activity = (Activity) context;

        if (showToast && keywordModel.url_Id == 0){
            Toast.makeText(activity, "Save failed. Url not found.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (showToast && keywordModel.keyword.equals("")){
            Toast.makeText(activity, "Save failed. Keyword is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHandlerBase.KEYWORD_KOLOM_URL_ID, keywordModel.url_Id);
        contentValues.put(DbHandlerBase.KEYWORD_KOLOM_KEYWORD, keywordModel.keyword);
        long keyword_Id = sqLiteDatabase.update(DbHandlerBase.KEYWORD_TABLE, contentValues, DbHandlerBase.KEYWORD_KOLOM_KEYWORD_ID + " = " + keywordModel.keyword_Id, null);

        if (showToast && keyword_Id != -1){
            Toast.makeText(activity, "Saved.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }
    }

    public LinkedList<KeywordModel> getAllKeywordsFromUrlId(int url_Id){
        LinkedList<KeywordModel> keywordModelList = new LinkedList<KeywordModel>();

        if (url_Id == 0){
            return keywordModelList;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " +
                DbHandlerBase.KEYWORD_KOLOM_KEYWORD_ID + ", " +
                DbHandlerBase.KEYWORD_KOLOM_URL_ID + ", " +
                DbHandlerBase.KEYWORD_KOLOM_KEYWORD + " " +
                "FROM " + DbHandlerBase.KEYWORD_TABLE + " WHERE " + DbHandlerBase.KEYWORD_KOLOM_URL_ID + " = " + url_Id;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext()){
            KeywordModel keywordModel = new KeywordModel();
            keywordModel.keyword_Id = cursor.getInt(cursor.getColumnIndex(DbHandlerBase.KEYWORD_KOLOM_KEYWORD_ID));
            keywordModel.url_Id = cursor.getInt(cursor.getColumnIndex(DbHandlerBase.KEYWORD_KOLOM_URL_ID));
            keywordModel.keyword = cursor.getString(cursor.getColumnIndex(DbHandlerBase.KEYWORD_KOLOM_KEYWORD));
            keywordModelList.add(keywordModel);
        }

        return keywordModelList;
    }

    public KeywordModel getKeywordFromKeywordId(int keyword_Id){
        KeywordModel keywordModel = new KeywordModel();

        if (keyword_Id == 0){
            return keywordModel;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT " +
                DbHandlerBase.KEYWORD_KOLOM_KEYWORD_ID + ", " +
                DbHandlerBase.KEYWORD_KOLOM_URL_ID + ", " +
                DbHandlerBase.KEYWORD_KOLOM_KEYWORD + " " +
                "FROM " + DbHandlerBase.KEYWORD_TABLE + " WHERE " + DbHandlerBase.KEYWORD_KOLOM_KEYWORD_ID + " = " + keyword_Id;
        Cursor cursor = sqLiteDatabase.rawQuery(query,null);
        while (cursor.moveToNext()){
            keywordModel.keyword_Id = cursor.getInt(cursor.getColumnIndex(DbHandlerBase.KEYWORD_KOLOM_KEYWORD_ID));
            keywordModel.url_Id = cursor.getInt(cursor.getColumnIndex(DbHandlerBase.KEYWORD_KOLOM_URL_ID));
            keywordModel.keyword = cursor.getString(cursor.getColumnIndex(DbHandlerBase.KEYWORD_KOLOM_KEYWORD));
        }

        return keywordModel;
    }

    public void deleteAllKeywordModels(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + DbHandlerBase.KEYWORD_TABLE);
    }

    public void deleteAllKeywordModelsWithUrlId(int url_ID){
        if (url_ID == 0){
            return;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String whereClause = DbHandlerBase.KEYWORD_KOLOM_URL_ID + " = ? ";
        String[] whereArgs = new String[] {String.valueOf(url_ID)};
        sqLiteDatabase.delete(DbHandlerBase.KEYWORD_TABLE, whereClause, whereArgs);
    }

    public void deleteKeywordModelWithKeywordId(int keyword_Id){
        if (keyword_Id == 0){
            return;
        }

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String whereClause = DbHandlerBase.KEYWORD_KOLOM_KEYWORD_ID + " = ? ";
        String[] whereArgs = new String[] {String.valueOf(keyword_Id)};
        sqLiteDatabase.delete(DbHandlerBase.KEYWORD_TABLE, whereClause, whereArgs);
    }
}
