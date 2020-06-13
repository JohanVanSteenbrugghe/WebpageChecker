package com.johanvansteenbrugghe.webpagechecker.data.collection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.johanvansteenbrugghe.webpagechecker.data.model.KeywordModel;
import com.johanvansteenbrugghe.webpagechecker.data.model.UrlModel;

public class DbHandlerBase extends SQLiteOpenHelper {
    private Context context;
    protected static final int DB_VERSION = 1;
    protected static final String DB_NAME = "WEBSITECHECKER";

    protected static final String URL_TABLE = "URL";
    protected static final String URL_KOLOM_URL_ID = "URL_ID";
    protected static final String URL_KOLOM_DESCRIPTION = "DESCRIPTION";
    protected static final String URL_KOLOM_URL = "URL";

    protected static final String KEYWORD_TABLE = "KEYWORD";
    protected static final String KEYWORD_KOLOM_KEYWORD_ID = "KEYWORD_ID";
    protected static final String KEYWORD_KOLOM_URL_ID = "URL_ID";
    protected static final String KEYWORD_KOLOM_KEYWORD = "KEYWORD";

    public DbHandlerBase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + URL_TABLE + " ( " +
                URL_KOLOM_URL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                URL_KOLOM_DESCRIPTION + " TEXT, " + URL_KOLOM_URL + " TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + KEYWORD_TABLE + " ( " +
                KEYWORD_KOLOM_KEYWORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEYWORD_KOLOM_URL_ID + " INTEGER, " +
                KEYWORD_KOLOM_KEYWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + URL_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + KEYWORD_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void Seed(){
        DbHandlerUrl dbHandlerUrl = new DbHandlerUrl(context);
        DbHandlerKeyword dbHandlerKeyword = new DbHandlerKeyword(context);

        dbHandlerUrl.deleteAllUrlModels();
        dbHandlerKeyword.deleteAllKeywordModels();

        UrlModel urlModel = new UrlModel();
        KeywordModel keywordModel = new KeywordModel();
        int url_Id = 0;
        int keyword_Id = 0;

        urlModel.description = "Reuters";
        urlModel.url = "https://www.reuters.com/news/world";

        url_Id = dbHandlerUrl.addUrlModel(urlModel, false);

        keywordModel.url_Id = url_Id;
        keywordModel.keyword = "climate";

        keyword_Id = dbHandlerKeyword.addKeywordModel(keywordModel, false);

        keywordModel = new KeywordModel();
        keywordModel.url_Id = url_Id;
        keywordModel.keyword = "corona";

        keyword_Id = dbHandlerKeyword.addKeywordModel(keywordModel, false);

        keywordModel = new KeywordModel();
        keywordModel.url_Id = url_Id;
        keywordModel.keyword = "belgium";

        keyword_Id = dbHandlerKeyword.addKeywordModel(keywordModel, false);

        urlModel = new UrlModel();
        urlModel.description = "New York Times";
        urlModel.url = "https://www.nytimes.com/";

        url_Id = dbHandlerUrl.addUrlModel(urlModel, false);

        keywordModel = new KeywordModel();
        keywordModel.url_Id = url_Id;
        keywordModel.keyword = "climate";

        keyword_Id = dbHandlerKeyword.addKeywordModel(keywordModel, false);
    }
}
