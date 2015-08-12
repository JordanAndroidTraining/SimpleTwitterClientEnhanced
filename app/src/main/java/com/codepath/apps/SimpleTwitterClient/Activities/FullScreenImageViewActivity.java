package com.codepath.apps.SimpleTwitterClient.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.models.TweetModel;
import com.squareup.picasso.Picasso;

public class FullScreenImageViewActivity extends ActionBarActivity {

    public static final String FULL_SCREEN_IMAGE_VIEW_ACTIVITY_DEV_TAG = "FullScreenImageViewActivityDevTag";
    private ImageView mImageContainerIv;
    private TextView mRetweetCountTv;
    private TextView mFavoriteCountTv;
    private ImageView mCloseBtn;
    private TweetModel mRenderData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_view);

        //get intent data
        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("imgUrl");
        String retweetCount = intent.getStringExtra("retweetCount");
        String favoriteCount = intent.getStringExtra("favoriteCount");

        Log.d(FULL_SCREEN_IMAGE_VIEW_ACTIVITY_DEV_TAG,"imgUrl: " + imgUrl + "|retweetCount: " + retweetCount + "|favoriteCount: "+ favoriteCount);

        // get view element reference
        mImageContainerIv = (ImageView) findViewById(R.id.fullScreenImgContainerIv);
        mRetweetCountTv = (TextView) findViewById(R.id.fullScreenRetweetCountTv);
        mFavoriteCountTv = (TextView) findViewById(R.id.fullScreenFavoriteCountTv);
        mCloseBtn = (ImageView) findViewById(R.id.fullScrennCloseBtnIv);

        // render it!
        Picasso.with(this).load(imgUrl).into(mImageContainerIv);
        mRetweetCountTv.setText(retweetCount);
        mFavoriteCountTv.setText(favoriteCount);

        // bind event!
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_image_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
