package com.johanvansteenbrugghe.webpagechecker.ui;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.johanvansteenbrugghe.webpagechecker.R;
import com.johanvansteenbrugghe.webpagechecker.data.collection.DbHandlerBase;
import com.johanvansteenbrugghe.webpagechecker.service.CheckService;
import com.johanvansteenbrugghe.webpagechecker.utils.NotificationUtil;
import com.johanvansteenbrugghe.webpagechecker.utils.UrlRecycleViewAdapter;

import java.io.IOException;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private UrlRecycleViewAdapter urlRecycleViewAdapter;
    private RecyclerView.LayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        NotificationUtil.createNotificationChannel(this);

        //For debug pruposes only
        //DbHandlerBase dbHandlerBase = new DbHandlerBase(this);
        //dbHandlerBase.Seed();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        urlRecycleViewAdapter = new UrlRecycleViewAdapter(this);
        recyclerView.setAdapter(urlRecycleViewAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        urlRecycleViewAdapter.refresh();
    }

    public void check(View view) throws IOException {
        new CheckService().execute(this, urlRecycleViewAdapter);
    }

    public void add(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("url_Id", 0);
        this.startActivity(intent);
    }
}
