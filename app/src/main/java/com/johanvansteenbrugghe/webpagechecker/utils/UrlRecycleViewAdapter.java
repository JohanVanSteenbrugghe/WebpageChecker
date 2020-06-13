package com.johanvansteenbrugghe.webpagechecker.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johanvansteenbrugghe.webpagechecker.R;
import com.johanvansteenbrugghe.webpagechecker.data.collection.DbHandlerUrl;
import com.johanvansteenbrugghe.webpagechecker.data.model.UrlModel;
import com.johanvansteenbrugghe.webpagechecker.ui.DetailActivity;

import java.util.LinkedList;

public class UrlRecycleViewAdapter extends RecyclerView.Adapter<UrlRecycleViewAdapter.UrlModelViewHolder> {
    private Context context;
    private LinkedList<UrlModel> urlModelList;

    public UrlRecycleViewAdapter(Context context){
        this.context = context;
        DbHandlerUrl dbHandlerURL = new DbHandlerUrl(context);
        urlModelList = dbHandlerURL.getAllUrlModels();

        UrlModel urlModel = new UrlModel();
        urlModel.url_Id = 0;
        urlModelList.add(urlModel);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UrlModelViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.urlmodelcardview, viewGroup, false);
        UrlModelViewHolder urlModelViewHolder = new UrlModelViewHolder(view);
        return urlModelViewHolder;
    }

    @Override
    public void onBindViewHolder(UrlModelViewHolder urlModelViewHolder, int position) {
        if (urlModelList.get(position).url_Id == 0){
            urlModelViewHolder.addUrlFloatingActionButton.setVisibility(View.VISIBLE);
            urlModelViewHolder.cardView.setId(urlModelList.get(position).url_Id);
            urlModelViewHolder.description.setText("");
            urlModelViewHolder.url.setText("");
        }
        else{
            urlModelViewHolder.addUrlFloatingActionButton.setVisibility(View.INVISIBLE);
            urlModelViewHolder.cardView.setId(urlModelList.get(position).url_Id);
            urlModelViewHolder.description.setText(urlModelList.get(position).description);
            urlModelViewHolder.url.setText(urlModelList.get(position).url);
        }
    }

    @Override
    public int getItemCount() {
        return urlModelList.size();
    }

    public void refresh(){
        DbHandlerUrl dbHandlerURL = new DbHandlerUrl(context);
        urlModelList = dbHandlerURL.getAllUrlModels();
        UrlModel urlModel = new UrlModel();
        urlModel.url_Id = 0;
        urlModelList.add(urlModel);
        this.notifyDataSetChanged();
    }

    public static class UrlModelViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        FloatingActionButton addUrlFloatingActionButton;
        TextView description;
        TextView url;

        UrlModelViewHolder(View view) {
            super(view);
            cardView = (CardView)view.findViewById(R.id.urlModelCardView);
            addUrlFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.addUrlFloatingActionButton);
            description = (TextView)view.findViewById(R.id.description);
            url = (TextView)view.findViewById(R.id.url);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int url_Id = view.getId();

                    if (url_Id != 0){
                        Intent intent = new Intent(view.getContext(), DetailActivity.class);
                        intent.putExtra("url_Id", url_Id);
                        view.getContext().startActivity(intent);
                    }
                }
            });

            addUrlFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int url_Id = view.getId();
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("url_Id", 0);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
