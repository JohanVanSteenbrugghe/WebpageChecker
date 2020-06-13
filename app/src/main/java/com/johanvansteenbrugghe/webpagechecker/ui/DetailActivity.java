package com.johanvansteenbrugghe.webpagechecker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.johanvansteenbrugghe.webpagechecker.R;
import com.johanvansteenbrugghe.webpagechecker.data.collection.DbHandlerKeyword;
import com.johanvansteenbrugghe.webpagechecker.data.collection.DbHandlerUrl;
import com.johanvansteenbrugghe.webpagechecker.data.model.UrlModel;
import com.johanvansteenbrugghe.webpagechecker.utils.KeywordRecycleViewAdapter;

public class DetailActivity extends AppCompatActivity {
    private int url_Id;
    private RecyclerView recyclerView;
    private KeywordRecycleViewAdapter keywordRecycleViewAdapter;
    private RecyclerView.LayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            this.url_Id = bundle.getInt("url_Id");
            loadUrl();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        keywordRecycleViewAdapter = new KeywordRecycleViewAdapter(this, url_Id);
        recyclerView.setAdapter(keywordRecycleViewAdapter);
    }

    private void loadUrl(){
        Activity activity = (Activity) this;

        DbHandlerUrl dbHandlerUrl = new DbHandlerUrl(this);
        UrlModel urlModel = dbHandlerUrl.getUrlModelFromUrlId(url_Id);

        EditText editTextDescription = (EditText) activity.findViewById(R.id.editTextDescription);
        EditText editTextUrl = (EditText) activity.findViewById(R.id.editTextUrl);

        editTextDescription.setText(urlModel.description);
        editTextUrl.setText(urlModel.url);
    }

    public void deleteUrl(View view){
        DbHandlerKeyword dbHandlerKeyword = new DbHandlerKeyword(this);
        dbHandlerKeyword.deleteAllKeywordModelsWithUrlId(url_Id);

        DbHandlerUrl dbHandlerUrl = new DbHandlerUrl(this);
        dbHandlerUrl.deleteUrlModelWithUrlId(url_Id);
        this.finish();
    }

    public void saveUrl(View view) {
        Activity activity = (Activity) this;

        EditText editTextDescription = (EditText) activity.findViewById(R.id.editTextDescription);
        EditText editTextUrl = (EditText) activity.findViewById(R.id.editTextUrl);

        UrlModel urlModel = new UrlModel();
        urlModel.url_Id = url_Id;
        urlModel.description = editTextDescription.getText().toString();
        urlModel.url = editTextUrl.getText().toString();

        DbHandlerUrl dbHandlerUrl = new DbHandlerUrl(this);
        if (url_Id == 0){
            url_Id = dbHandlerUrl.addUrlModel(urlModel, true);
            if (url_Id != 0){
                this.finish();
            }
        }
        else{
            boolean blnOk = dbHandlerUrl.updateUrlModel(urlModel, true);
            if (blnOk)
            {
                this.finish();
            }
        }
    }
}
