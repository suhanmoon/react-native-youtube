package com.inprogress.reactnativeyoutube;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YouTubeActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlayerStateChangeListener {

    private YouTubePlayer player;
    private int iNavColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        if (Build.VERSION.SDK_INT >= 21) {
            iNavColor = getWindow().getNavigationBarColor();
            getWindow().setNavigationBarColor(Color.BLACK);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);



        if (player == null) {
            YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.youtube_player_view);
            playerView.initialize(getIntent().getStringExtra("apiKey"), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(iNavColor);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        this.setResult(Activity.RESULT_OK, new Intent());
        this.finish();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        //use this to hide controls
        player = youTubePlayer;
        youTubePlayer.setFullscreenControlFlags(0);
        youTubePlayer.setPlayerStateChangeListener(this);
        youTubePlayer.cueVideo(getIntent().getStringExtra("videoId"), getIntent().getIntExtra("startTimeMillis", 0));

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {
        player.play();
    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {
    }

    @Override
    public void onVideoEnded() {
        this.setResult(Activity.RESULT_OK, new Intent());
        this.finish();
    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {
        Intent intent = new Intent();
        intent.putExtra("initialization_result", errorReason.toString());
        this.setResult(Activity.RESULT_CANCELED, intent);
    }
}