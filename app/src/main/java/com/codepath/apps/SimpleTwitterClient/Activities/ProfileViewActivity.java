package com.codepath.apps.SimpleTwitterClient.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.SimpleTwitterClient.Fragments.ProfileViewFragment;
import com.codepath.apps.SimpleTwitterClient.R;

public class ProfileViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        //set icon
        getSupportActionBar().setLogo(R.mipmap.ic_twitter_log_white);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //get intent data
        Intent intent = getIntent();
        String screenName = intent.getStringExtra("screen_name");
        String userId = intent.getStringExtra("user_id");
        Log.d("JordanTestXXX", "screenName: " + screenName);
        Log.d("JordanTestXXX", "userId: " + userId);
        //Start Fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ProfileViewFragment fg = ProfileViewFragment.newInstance(screenName,userId);
        ft.replace(R.id.profileViewFragmentContainer,fg);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_view, menu);
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
