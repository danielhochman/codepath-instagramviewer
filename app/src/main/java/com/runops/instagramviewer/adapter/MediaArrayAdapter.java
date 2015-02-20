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
import com.runops.instagramviewer.transformation.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

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
        TextView tvLikeCount = (TextView) convertView.findViewById(R.id.tvLikeCount);
        ImageView ivPrimaryImage = (ImageView) convertView.findViewById(R.id.ivPrimaryImage);
        ImageView ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);

        tvUsername.setText(media.getUser().getUsername());
        if (media.getCaption() != null) {
            tvCaption.setText(media.getCaption().getText());
        }
        tvLikeCount.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(media.getLikes().getCount()) + " likes");

        Picasso.with(getContext()).load(media.getImages().getStandardResolution().getUrl())
                .into(ivPrimaryImage);

        Picasso.with(getContext()).load(media.getUser().getProfilePicture()).transform(new RoundedTransformation())
                .into(ivProfilePicture);

        return convertView;
    }
}
