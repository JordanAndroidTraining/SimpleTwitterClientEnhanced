package com.codepath.apps.SimpleTwitterClient.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.SimpleTwitterClient.Activities.FullScreenImageViewActivity;
import com.codepath.apps.SimpleTwitterClient.Activities.ProfileViewActivity;
import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by jordanhsu on 8/12/15.
 */
public class TimelineAdapter extends ArrayAdapter<TweetModel> {
    public static final String HOME_TIMELINE_ADAPTER_DEV_TAG = "HomeTimelineAdapterDevTag";
    private Context mContext;
    private ArrayList<TweetModel> mHomeTimelineList;

    public TimelineAdapter(Context context, int resource, ArrayList<TweetModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mHomeTimelineList = objects;
    }

    private static class ViewHolder{
        ImageView profilePhotoIv;
        TextView userNameTv;
        TextView userScreenNameTv;
        TextView relativeTimestampTv;
        TextView captionTv;
        ImageView tweetImgIv;
        TextView retweetCountTv;
        TextView starCountTv;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tweet_list_item,parent,false);
            viewHolder = new ViewHolder();

            // get element reference
            viewHolder.profilePhotoIv = (ImageView) convertView.findViewById(R.id.profilePhotoIv);
            viewHolder.userNameTv = (TextView) convertView.findViewById(R.id.userNameTv);
            viewHolder.userScreenNameTv = (TextView) convertView.findViewById(R.id.userScreenNameTv);
            viewHolder.relativeTimestampTv = (TextView) convertView.findViewById(R.id.relativeTimestampTv);
            viewHolder.captionTv = (TextView) convertView.findViewById(R.id.captionTv);
            viewHolder.tweetImgIv = (ImageView) convertView.findViewById(R.id.tweetImgIv);
            viewHolder.retweetCountTv = (TextView) convertView.findViewById(R.id.retweetCountTv);
            viewHolder.starCountTv = (TextView) convertView.findViewById(R.id.starCountTv);

            // save to view tag
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // clear tweet img
        viewHolder.tweetImgIv.setVisibility(View.GONE);
        viewHolder.tweetImgIv.setImageResource(0);

        final TweetModel renderModel = mHomeTimelineList.get(position);

        // set view content
        if(renderModel.getUser() != null){
            Picasso.with(mContext).load(renderModel.getUser().getProfilePhotoUrl()).into(viewHolder.profilePhotoIv);
            viewHolder.profilePhotoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext,ProfileViewActivity.class);
                    String screenName = renderModel.getUser().getUserScreenName();
                    String userId = renderModel.getUser().getUserId();
                    i.putExtra("screen_name", screenName);
                    i.putExtra("user_id",userId);
                    mContext.startActivity(i);
                }
            });
            viewHolder.userNameTv.setText(renderModel.getUser().getUserName());
            viewHolder.userScreenNameTv.setText("@" + renderModel.getUser().getUserScreenName());
        }

        if(!renderModel.getTweetImgUrl().isEmpty()){
            viewHolder.tweetImgIv.setVisibility(View.VISIBLE);
            Log.d("JordanTestImageURl", String.valueOf(renderModel.getTweetImgUrl()));
            Picasso.with(mContext).load(renderModel.getTweetImgUrl()).into(viewHolder.tweetImgIv);
            viewHolder.tweetImgIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext,FullScreenImageViewActivity.class);
                    i.putExtra("imgUrl",   renderModel.getTweetImgUrl());
                    i.putExtra("retweetCount", String.valueOf(renderModel.getRetweetCount()));
                    i.putExtra("favoriteCount", String.valueOf(renderModel.getFavouritesCount()));
                    mContext.startActivity(i);
                }
            });
        }
        viewHolder.relativeTimestampTv.setText(renderModel.getRelativeTimestamp());
        viewHolder.captionTv.setText(renderModel.getCaption());
        viewHolder.retweetCountTv.setText(String.valueOf(renderModel.getRetweetCount()));
        viewHolder.starCountTv.setText(String.valueOf(renderModel.getFavouritesCount()));
        return convertView;
    }
}
