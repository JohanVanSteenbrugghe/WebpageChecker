package com.johanvansteenbrugghe.webpagechecker.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.johanvansteenbrugghe.webpagechecker.data.collection.DbHandlerKeyword;
import com.johanvansteenbrugghe.webpagechecker.data.collection.DbHandlerUrl;
import com.johanvansteenbrugghe.webpagechecker.data.model.KeywordModel;
import com.johanvansteenbrugghe.webpagechecker.data.model.UrlModel;
import com.johanvansteenbrugghe.webpagechecker.utils.NetworkUtil;
import com.johanvansteenbrugghe.webpagechecker.utils.NotificationUtil;
import com.johanvansteenbrugghe.webpagechecker.utils.UrlRecycleViewAdapter;

import java.util.LinkedList;

public class CheckService extends AsyncTask<Object, Void, Void> {

    @Override
    protected Void doInBackground(Object[] objectArray) {
        Context context = (Context)objectArray[0];
        final Activity activity = (Activity) context;
        final UrlRecycleViewAdapter urlRecycleViewAdapter = (UrlRecycleViewAdapter)objectArray[1];

        boolean connectionInternetOk = NetworkUtil.checkInternetAccess(context);
        if (!connectionInternetOk){
            return null;
        }

        DbHandlerUrl dbHandlerUrl = new DbHandlerUrl(context);
        DbHandlerKeyword dbHandlerKeyword = new DbHandlerKeyword(context);

        LinkedList<UrlModel> UrlModelList = dbHandlerUrl.getAllUrlModels();
        for (UrlModel urlModel : UrlModelList){
            String requestResult = NetworkUtil.getUrlRequestResult(urlModel.url);

            LinkedList<KeywordModel> keywordModelList = dbHandlerKeyword.getAllKeywordsFromUrlId(urlModel.url_Id);
            LinkedList<String> keywordFoundList = new LinkedList<String>();
            for (KeywordModel keywordModel : keywordModelList){
                if (requestResult.toLowerCase().contains(keywordModel.keyword.toLowerCase())){
                    keywordFoundList.add(keywordModel.keyword);
                }
            }

            for(int i = 1; i <= keywordFoundList.size(); i++){
                String keywordFound = keywordFoundList.get(i - 1);
                NotificationUtil.createNotification(context, "Webpage Checker", "Keyword: '" + keywordFound + "' found at '" + urlModel.description + "'.", i);
            }
        }

        if (urlRecycleViewAdapter != null){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(activity, "Check completed.", Toast.LENGTH_SHORT).show();
                    urlRecycleViewAdapter.refresh();
                }
            });
        }

        return null;
    }
}
