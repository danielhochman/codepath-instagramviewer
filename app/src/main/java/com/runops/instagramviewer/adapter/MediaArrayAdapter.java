package com.runops.instagramviewer.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.runops.instagramviewer.R;
import com.runops.instagramviewer.model.Comment;
import com.runops.instagramviewer.model.Media;
import com.runops.instagramviewer.transformation.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Math.min;

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
        LinearLayout llComments;
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
            viewHolder.llComments = (LinearLayout) convertView.findViewById(R.id.llComments);

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
            viewHolder.tvCaption.setText(formatHashtagsAndNames(media.getUser().getUsername(), media.getCaption().getText()));
        }
        else {
            viewHolder.tvCaption.setText("");
        }

        viewHolder.tvLikeCount.setText(
                formatNumber(media.getLikes().getCount()) + " likes");

        Picasso.with(getContext()).load(media.getImages().getStandardResolution().getUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(viewHolder.ivPrimaryImage);

        Picasso.with(getContext()).load(media.getUser().getProfilePicture()).transform(new RoundedTransformation())
                .into(viewHolder.ivProfilePicture);


        // Comments stuff
        viewHolder.llComments.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View commentSum = inflater.inflate(R.layout.item_inline_comment, null);
        TextView tvCommentSum = (TextView) commentSum.findViewById(R.id.tvComment);
        String numberOfComments = formatNumber(media.getComments().getCount());
        tvCommentSum.setText(Html.fromHtml(
                "<b><font color=\"#AAAAAA\">" + numberOfComments + " comments</font></b>"));
        viewHolder.llComments.addView(commentSum);

        List<Comment> commentsList = media.getComments().getCommentList();
        for (int i=0; i<min(6, commentsList.size()); i++) {
            Comment comment = commentsList.get(i);
            View llComment = inflater.inflate(R.layout.item_inline_comment, null);
            TextView tvComment = (TextView) llComment.findViewById(R.id.tvComment);
            tvComment.setText(formatHashtagsAndNames(comment.getFrom().getUsername(), comment.getText()));
            tvComment.setLinkTextColor(getContext().getResources().getColor(R.color.primary_blue));
            viewHolder.llComments.addView(llComment);
        }

        return convertView;
    }

    private String formatNumber(long input) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(input);
    }

    private Spannable formatHashtagsAndNames(String username, String text) {
        String htmlCommentText = text.replaceAll("([#@][A-Za-z0-9_]+)", "<a href=\"#\">$1</a>");

        // Prevent underlining of links
        Spannable s = (Spannable) Html.fromHtml(
                "<b><font color=\""+ getContext().getResources().getColor(R.color.primary_blue) + "\">" + username +"</font></b> " + htmlCommentText);
        for (URLSpan u: s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }

        return s;
    }
}
