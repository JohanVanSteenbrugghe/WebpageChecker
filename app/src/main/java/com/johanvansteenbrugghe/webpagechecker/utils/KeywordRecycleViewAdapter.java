package com.johanvansteenbrugghe.webpagechecker.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.johanvansteenbrugghe.webpagechecker.R;
import com.johanvansteenbrugghe.webpagechecker.data.collection.DbHandlerKeyword;
import com.johanvansteenbrugghe.webpagechecker.data.model.KeywordModel;

import java.util.LinkedList;

public class KeywordRecycleViewAdapter extends RecyclerView.Adapter<KeywordRecycleViewAdapter.KeywordModelViewHolder> {
    private Context context;
    private Integer url_Id;
    private LinkedList<KeywordModel> keywordModelList;

    public KeywordRecycleViewAdapter(Context context, int url_Id){
        this.context = context;
        this.url_Id = url_Id;
        DbHandlerKeyword dbHandlerKeyword = new DbHandlerKeyword(context);
        keywordModelList = dbHandlerKeyword.getAllKeywordsFromUrlId(url_Id);
        KeywordModel keywordModel = new KeywordModel();
        keywordModel.keyword_Id = 0;
        keywordModel.url_Id = url_Id;
        keywordModel.keyword = "Add keyword ?";
        keywordModelList.add(keywordModel);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public KeywordModelViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.keywordmodelcardview, viewGroup, false);
        KeywordModelViewHolder keywordModelViewHolder = new KeywordModelViewHolder(view);
        return keywordModelViewHolder;
    }

    @Override
    public void onBindViewHolder(KeywordModelViewHolder keywordModelViewHolder, int position) {
        if (keywordModelList.get(position).keyword_Id == 0){
            keywordModelViewHolder.addKeywordFloatingActionButton.setVisibility(View.VISIBLE);
            keywordModelViewHolder.deleteKeywordFloatingActionButton.setVisibility(View.INVISIBLE);
            keywordModelViewHolder.cardView.setId(keywordModelList.get(position).keyword_Id);
            keywordModelViewHolder.addKeywordFloatingActionButton.setId(keywordModelList.get(position).url_Id);
            keywordModelViewHolder.deleteKeywordFloatingActionButton.setId(keywordModelList.get(position).keyword_Id);
            keywordModelViewHolder.keyword.setText(keywordModelList.get(position).keyword);
        }
        else{
            keywordModelViewHolder.addKeywordFloatingActionButton.setVisibility(View.INVISIBLE);
            keywordModelViewHolder.deleteKeywordFloatingActionButton.setVisibility(View.VISIBLE);
            keywordModelViewHolder.cardView.setId(keywordModelList.get(position).keyword_Id);
            keywordModelViewHolder.addKeywordFloatingActionButton.setId(keywordModelList.get(position).url_Id);
            keywordModelViewHolder.deleteKeywordFloatingActionButton.setId(keywordModelList.get(position).keyword_Id);
            keywordModelViewHolder.keyword.setText(keywordModelList.get(position).keyword);
        }
    }

    @Override
    public int getItemCount() {
        return keywordModelList.size();
    }

    public void refresh(){
        DbHandlerKeyword dbHandlerKeyword = new DbHandlerKeyword(context);
        keywordModelList = dbHandlerKeyword.getAllKeywordsFromUrlId(url_Id);
        KeywordModel keywordModel = new KeywordModel();
        keywordModel.keyword_Id = 0;
        keywordModel.url_Id = url_Id;
        keywordModel.keyword = "Add keyword ?";
        keywordModelList.add(keywordModel);
        this.notifyDataSetChanged();
    }

    public class KeywordModelViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        FloatingActionButton addKeywordFloatingActionButton;
        FloatingActionButton deleteKeywordFloatingActionButton;
        TextView keyword;

        KeywordModelViewHolder(View view) {
            super(view);
            cardView = (CardView)view.findViewById(R.id.keywordModelCardView);
            addKeywordFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.addKeywordFloatingActionButton);
            deleteKeywordFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.deleteKeywordFloatingActionButton);
            keyword = (TextView)view.findViewById(R.id.keywordTextView);

            addKeywordFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Context context = view.getContext();
                    Activity activity = (Activity) context;
                    int url_ID = view.getId();

                    if (url_ID != 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialogTheme);

                        builder.setTitle("Keyword:");
                        final EditText input = new EditText(context);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(layoutParams);
                        input.setPadding(65,50,0,0);
                        builder.setView(input);

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String keywordResponse = input.getText().toString();
                                DbHandlerKeyword dbHandlerKeyword = new DbHandlerKeyword(context);
                                KeywordModel keywordModel = new KeywordModel();
                                keywordModel.url_Id = url_Id;
                                keywordModel.keyword = keywordResponse;
                                dbHandlerKeyword.addKeywordModel(keywordModel, true);
                                refresh();
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                    else {
                        Toast.makeText(activity, "No url found.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            deleteKeywordFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    int keyword_Id = view.getId();
                    DbHandlerKeyword dbHandlerKeyword = new DbHandlerKeyword(context);
                    dbHandlerKeyword.deleteKeywordModelWithKeywordId(keyword_Id);
                    refresh();
                }
            });
        }
    }
}
