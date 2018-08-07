package com.example.janiraiski.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private static final String LOCATION_SEPARATOR = "T";

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        String oldDate = currentArticle.getDate();
        String newDate = "";

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentArticle.getTitle());

        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(currentArticle.getSection());

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(currentArticle.getAuthor());

        if (oldDate.contains(LOCATION_SEPARATOR)) {
            String[] parts = oldDate.split(LOCATION_SEPARATOR);
            newDate = parts[0];
        }

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(newDate);

        return listItemView;

    }
}
