package com.codepath.apps.SimpleTwitterClient.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.SimpleTwitterClient.Adapters.MainContainerPageAdapter;
import com.codepath.apps.SimpleTwitterClient.R;

public class MainTimelineContainerActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    public static final String HOME_TIMELINE_ACTIVITY_DEV_TAG = "TimelineContainerActivi";
    public static final int COMPOSE_REQUEST_CODE = 999;
    private FragmentManager mFm;
    private MainContainerPageAdapter mPageAdapter;
    private ViewPager mVp;
    private PagerSlidingTabStrip mPts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_timeline);

        //set icon
        getSupportActionBar().setLogo(R.mipmap.ic_twitter_log_white);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // get view pager
        mVp = (ViewPager) findViewById(R.id.mainContainerVp);

        // get fragmentManager && set to viewpager adapter
        mFm = getSupportFragmentManager();
        mPageAdapter = new MainContainerPageAdapter(mFm);
        mVp.setAdapter(mPageAdapter);

        // set tabstrip title
        mPts = (PagerSlidingTabStrip) findViewById(R.id.mainContainerVpHeaderPts);
        mPts.setViewPager(mVp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);
        //MenuItem composeBtn = menu.findItem(R.id.action_compose);
        //composeBtn.setOnMenuItemClickListener();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_compose) {
            Intent i = new Intent(this, ComposeTweetActivity.class);
            //startActivity(i);
            startActivityForResult(i, COMPOSE_REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COMPOSE_REQUEST_CODE) {
            Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,"onActivityResult()|resultCode: " + resultCode + "data" + data);
            switch (resultCode){
                case RESULT_OK:
                    Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG, "onActivityResult()|RESULT_OK");
//                    renderTimeline(true);

                    break;
                case RESULT_CANCELED:
                    break;
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//        Intent i = new Intent(this,DetailViewActivity.class);
//        startActivity(i);
//        Log.d(HOME_TIMELINE_ACTIVITY_DEV_TAG,mTweetList.get(position).getClass().toString());
    }

}
