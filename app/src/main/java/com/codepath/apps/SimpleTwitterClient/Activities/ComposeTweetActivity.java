package com.codepath.apps.SimpleTwitterClient.Activities;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.SimpleTwitterClient.R;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterApplication;
import com.codepath.apps.SimpleTwitterClient.SimpleTwitterClient;
import com.codepath.apps.SimpleTwitterClient.models.UserModel;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ComposeTweetActivity extends ActionBarActivity implements View.OnKeyListener, View.OnClickListener {
    public static final String COMPOSE_TWEET_ACTIVITY_DEV_TAG = "ComposeTweetActivityDevTag";
    public int TWEET_AVAILABLE_WORD_COUNT;
    private SimpleTwitterClient mClient;
    private UserModel mUser;
    private ImageView mComposeProfilePhotoIv;
    private EditText mComposeInputEt;
    private TextView mComposeRemainWordCountTv;
    private Button mSubmitBtn;
    private ImageView mCloseBtnIv;
    private Context mSelfContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);
        // init
        mSelfContext = this;
        mClient = SimpleTwitterApplication.getRestClient();
        TWEET_AVAILABLE_WORD_COUNT  = getResources().getInteger(R.integer.twitter_compose_word_limit);

        // get view element reference
        mComposeProfilePhotoIv = (ImageView) findViewById(R.id.composeProfilePhotoIv);
        mComposeInputEt = (EditText) findViewById(R.id.composeContentEt);
        mComposeRemainWordCountTv = (TextView) findViewById(R.id.composeRemainWordCountTv);
        mSubmitBtn = (Button) findViewById(R.id.composeSubmitBtn);
        mCloseBtnIv = (ImageView) findViewById(R.id.composeCloseBtnIv);

        // Bind event
        mComposeInputEt.setOnKeyListener(this);
        mSubmitBtn.setOnClickListener(this);
        mCloseBtnIv.setOnClickListener(this);

        // render it!
        renderComposePage();
    }

    public void renderComposePage(){
        mClient.getCurrentUserInfo(new  JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(COMPOSE_TWEET_ACTIVITY_DEV_TAG, "renderComposePage|onSuccess()|response: " + response.toString());
                mUser = UserModel.parseFromJSONObject(response);
                Picasso.with(mSelfContext).load(mUser.getProfilePhotoUrl()).into(mComposeProfilePhotoIv);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(COMPOSE_TWEET_ACTIVITY_DEV_TAG, "renderComposePage|onFailure()|responseString: " + responseString);
            }
        });
    }

    public void updateRemainingWordCount(){
        int remainingWordCount = TWEET_AVAILABLE_WORD_COUNT - mComposeInputEt.length();
        mComposeRemainWordCountTv.setText(String.valueOf(remainingWordCount));
    }

    public void processPostNewTweet(String text){
        mClient.postNewTweet(text, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(COMPOSE_TWEET_ACTIVITY_DEV_TAG,"processPostNewTweet|onSuccess()|response: " + response);
                // exit compose activity
                setResult(RESULT_OK, getIntent());
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(COMPOSE_TWEET_ACTIVITY_DEV_TAG, "processPostNewTweet|onFailure()|responseString: " + responseString);
                Toast.makeText(mSelfContext,"Compose Tweet Error!",Toast.LENGTH_LONG).show();
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        // only handle keyup to prevent trigger event too quickly
        if(keyEvent.getAction() == KeyEvent.ACTION_UP){
            switch (view.getId()){
                case R.id.composeContentEt:
                    Log.d(COMPOSE_TWEET_ACTIVITY_DEV_TAG,"i: " + i + "|keyEvent: " + keyEvent.toString());
                    // update remaining word count
                    updateRemainingWordCount();
                    break;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        Log.d(COMPOSE_TWEET_ACTIVITY_DEV_TAG,"onClick()");
        switch (view.getId()){
            case R.id.composeSubmitBtn:
                processPostNewTweet(String.valueOf(mComposeInputEt.getText()));
                break;
            case R.id.composeCloseBtnIv:
                setResult(RESULT_CANCELED,getIntent());
                finish();
                break;
        }
    }
}
