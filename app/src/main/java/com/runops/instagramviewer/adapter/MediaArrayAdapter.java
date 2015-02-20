package com.runops.instagramviewer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.runops.instagramviewer.R;
import com.runops.instagramviewer.model.Media;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MediaArrayAdapter extends ArrayAdapter<Media>{

    public MediaArrayAdapter(Context context, ArrayList<Media> mediaList) {
        super(context, 0, mediaList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Media media = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_media, parent, false);
        }

        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);

        ImageView ivPrimaryImage = (ImageView) convertView.findViewById(R.id.ivPrimaryImage);

        Picasso.with(getContext()).load(media.getImages().getStandardResolution().getUrl())
                .into(ivPrimaryImage);

        tvUsername.setText(media.getUser().getUsername());
        tvCaption.setText(media.getCaption().getText());

        return convertView;
    }
}
