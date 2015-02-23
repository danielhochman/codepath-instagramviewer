package com.runops.instagramviewer.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
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
import java.util.Date;
import java.util.Locale;

public class MediaArrayAdapter extends ArrayAdapter<Media>{

    public MediaArrayAdapter(Context context, ArrayList<Media> mediaList) {
        super(context, 0, mediaList);
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tvUsername;
        TextView tvCaption;
        TextView tvLikeCount;
        TextView tvRelativeTime;
        ImageView ivPrimaryImage;
        ImageView ivProfilePicture;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Media media = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_media, parent, false);

            viewHolder.tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.tvLikeCount = (TextView) convertView.findViewById(R.id.tvLikeCount);
            viewHolder.tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
            viewHolder.ivPrimaryImage = (ImageView) convertView.findViewById(R.id.ivPrimaryImage);
            viewHolder.ivProfilePicture = (ImageView) convertView.findViewById(R.id.ivProfilePicture);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUsername.setText(media.getUser().getUsername());

        Date now = new Date();
        Date then = new Date(media.getCreatedTime() * 1000);
        String relativeTime = DateUtils.getRelativeTimeSpanString(
                then.getTime(), now.getTime(), DateUtils.MINUTE_IN_MILLIS).toString();
        viewHolder.tvRelativeTime.setText(relativeTime.replaceAll("(\\d+)\\s(.).+", "$1$2"));


        if (media.getCaption() != null) {
            String htmlText = media.getCaption().getText().replaceAll("([#@][A-Za-z0-9_]+)", "<a href=\"#\">$1</a>");

            // Prevent underlining of links
            Spannable s = (Spannable) Html.fromHtml(htmlText);
            for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
                s.setSpan(new UnderlineSpan() {
                    public void updateDrawState(TextPaint tp) {
                        tp.setUnderlineText(false);
                    }
                }, s.getSpanStart(u), s.getSpanEnd(u), 0);
            }
            viewHolder.tvCaption.setText(s);
        }
        else {
            viewHolder.tvCaption.setText("");
        }

        viewHolder.tvLikeCount.setText(
                NumberFormat.getNumberInstance(Locale.getDefault()).format(media.getLikes().getCount()) + " likes");

        Picasso.with(getContext()).load(media.getImages().getStandardResolution().getUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(viewHolder.ivPrimaryImage);

        Picasso.with(getContext()).load(media.getUser().getProfilePicture()).transform(new RoundedTransformation())
                .into(viewHolder.ivProfilePicture);

        return convertView;
    }
}
