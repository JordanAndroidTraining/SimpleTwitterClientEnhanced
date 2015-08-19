package com.codepath.apps.SimpleTwitterClient.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.codepath.apps.SimpleTwitterClient.R.color.black;
import static com.codepath.apps.SimpleTwitterClient.R.color.gray;

/**
 * Created by jordanhsu on 8/19/15.
 */
public class UserListAdapter  extends ArrayAdapter<UserModel> {

    private Context mContext;
    private ArrayList<UserModel> mUserList;

    public UserListAdapter(Context context, int resource, ArrayList<UserModel> objects) {
        super(context, resource, objects);
        mContext = context;
        mUserList = objects;
    }

    private static class ViewHolder{
        ImageView profilePhotoIv;
        TextView userNameTv;
        TextView userScreenNameTv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.profilePhotoIv = (ImageView) convertView.findViewById(R.id.userItemProfilePhotoIv);
            viewHolder.userNameTv = (TextView) convertView.findViewById(R.id.userItemUserNameTv);
            viewHolder.userScreenNameTv = (TextView) convertView.findViewById(R.id.userItemPageScreenNameTv);
            // save to view tag
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // clear img
        viewHolder.profilePhotoIv.setImageResource(0);

        UserModel renderRow = mUserList.get(position);

        Log.d("JordanGGGG", "username: " + renderRow.getUserName());
        Log.d("JordanGGGG", "screenname: " + renderRow.getUserScreenName());

        Picasso.with(mContext).load(renderRow.getProfilePhotoUrl()).into(viewHolder.profilePhotoIv);
        viewHolder.userNameTv.setText(renderRow.getUserName());
        viewHolder.userScreenNameTv.setText(renderRow.getUserScreenName());

        //hide loading theme
        viewHolder.userNameTv.setBackground(null);
        viewHolder.userNameTv.setTextColor(black);
        viewHolder.userScreenNameTv.setBackground(null);
        viewHolder.userScreenNameTv.setText(gray);

        return convertView;
    }
}
