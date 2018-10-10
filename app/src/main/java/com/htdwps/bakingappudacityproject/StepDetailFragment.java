package com.htdwps.bakingappudacityproject;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.htdwps.bakingappudacityproject.models.Recipe;
import com.htdwps.bakingappudacityproject.models.Step;
import com.htdwps.bakingappudacityproject.util.StringConstantHelper;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Recipe recipe;
    private Step current_step;
    private int position_of_step = 0;
    private int whichStep = 0;
    private long mExoplayerPosition = 0;

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private TextView stepDescription;
    private LinearLayout.LayoutParams linearLayoutParams;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(int position) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putInt(StringConstantHelper.STEPS_POSITION_INT_KEY, position);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(StringConstantHelper.RECIPE_OBJECT_KEY)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);

        recipe = getArguments().getParcelable(StringConstantHelper.RECIPE_OBJECT_KEY);
        current_step = getArguments().getParcelable(StringConstantHelper.STEPS_OBJECT_KEY);
        position_of_step = getArguments().getInt(StringConstantHelper.STEPS_POSITION_INT_KEY);

        mSimpleExoPlayerView = rootView.findViewById(R.id.exoplayerview);
        stepDescription = rootView.findViewById(R.id.step_description_view);

        initializePlayer();

        updateVideoPlayerAndStepDescription(position_of_step);

        return rootView;
    }

    private void initializePlayer() {
        if (mSimpleExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);
            mSimpleExoPlayer.addListener(this);

            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                stepDescription.setVisibility(View.GONE);

                linearLayoutParams = (LinearLayout.LayoutParams) mSimpleExoPlayerView.getLayoutParams();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linearLayoutParams);
                params.setMargins(0, 0, 0, 0);
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mSimpleExoPlayerView.setLayoutParams(params);

//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mSimpleExoPlayerView.getLayoutParams();
//                params.width=params.MATCH_PARENT;
//                params.height=params.MATCH_PARENT;
//                mSimpleExoPlayerView.setLayoutParams(params);
//                mSimpleExoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);


            } else {

                stepDescription.setVisibility(View.VISIBLE);

            }

        }
    }

    public void updateVideoPlayerAndStepDescription(int position) {
        // Stop Player first
        mSimpleExoPlayer.stop();

        whichStep = position;

        current_step = recipe.getSteps().get(whichStep);

        stepDescription.setText(current_step.getDescription());

        if (current_step.getVideoURL().isEmpty()) {

            mSimpleExoPlayerView.setVisibility(View.GONE);

        } else {

            mSimpleExoPlayerView.setVisibility(View.VISIBLE);

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(current_step.getVideoURL()), new DefaultDataSourceFactory(getContext(), "Hello"), new DefaultExtractorsFactory(), null, null);

            mSimpleExoPlayer.prepare(mediaSource);

            mSimpleExoPlayer.setPlayWhenReady(true);

        }

    }

    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && mSimpleExoPlayer == null) {
            initializePlayer();
            updateVideoPlayerAndStepDescription(position_of_step);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 && mSimpleExoPlayer == null) {
            initializePlayer();
            updateVideoPlayerAndStepDescription(position_of_step);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) {
            mExoplayerPosition = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSimpleExoPlayer != null) {
            mExoplayerPosition = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSimpleExoPlayer != null) {
            mExoplayerPosition = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mSimpleExoPlayer != null) {
            outState.putLong(StringConstantHelper.EXOPLAYER_VIDEO_POSITION, mSimpleExoPlayer.getCurrentPosition());
        } else {
            outState.putLong(StringConstantHelper.EXOPLAYER_VIDEO_POSITION, mExoplayerPosition);
        }
        super.onSaveInstanceState(outState);
    }

}
