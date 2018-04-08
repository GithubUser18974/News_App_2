package com.example.mohamed_araby.newsappupgrade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mohamed_Araby on 4/6/2018.
 */

public class NewsAdapter extends ArrayAdapter<NewsData> {

    public NewsAdapter(Context context, ArrayList<NewsData> NewsDatas) {
        super(context, 0, NewsDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        NewsData currentNewsData = getItem(position);
        TextView title = listView.findViewById(R.id.title);
        title.setText(currentNewsData.getTitle());
        TextView date = listView.findViewById(R.id.date);
        date.setText(formatDate(currentNewsData.getDate()));
        TextView type = listView.findViewById(R.id.section);
        type.setText(currentNewsData.getType());
        TextView author = listView.findViewById(R.id.author);
        ImageView imageView = (ImageView) listView.findViewById(R.id.image_type);
        switch (currentNewsData.getType().toLowerCase()) {
            case "environment": {
                imageView.setImageResource(R.drawable.icon_environment);
                break;
            }
            case "politics": {
                imageView.setImageResource(R.drawable.icon_politic);
                break;
            }
            case "sport": {
                imageView.setImageResource(R.drawable.icon_sport);
                break;
            }
            case "film": {
                imageView.setImageResource(R.drawable.icon_film);
                break;
            }
            case "football": {
                imageView.setImageResource(R.drawable.icon_football);
                break;
            }
            case "media": {
                imageView.setImageResource(R.drawable.icon_media);
                break;
            }
            case "education": {
                imageView.setImageResource(R.drawable.icon_education);
                break;
            }
            case "fashion": {
                imageView.setImageResource(R.drawable.icon_fashion);
                break;
            }
            case "music": {
                imageView.setImageResource(R.drawable.icon_music);
                break;
            }
            case "money": {
                imageView.setImageResource(R.drawable.icon_money);
                break;
            }
            default:
                imageView.setImageResource(R.drawable.news);
        }

        if (currentNewsData.getauthor() != null && !currentNewsData.getauthor().isEmpty())
            author.setText(currentNewsData.getauthor());
        else
            author.setVisibility(View.GONE);
        return listView;
    }

    private String formatDate(String date) {
        String tokens[] = date.split("T");
        return tokens[0];
    }

}
