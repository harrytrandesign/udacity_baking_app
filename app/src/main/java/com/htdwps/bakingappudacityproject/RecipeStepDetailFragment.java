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
import com.htdwps.bakingappudacityproject.dummy.DummyContent;
import com.htdwps.bakingappudacityproject.models.Step;
import com.htdwps.bakingappudacityproject.util.StringConstantHelper;

/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeStepListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private int current_step = 0;
    private long current_exo_video_position = 0;

    private LinearLayout.LayoutParams linearLayoutParams;

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;
    private Step mLocalStep;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private TextView stepDescriptionTextView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
//            mLocalStep = getArguments().getParcelable(ARG_ITEM_ID);
            mLocalStep = (Step) getArguments().get(ARG_ITEM_ID);

            Activity activity = this.getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipestep_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mLocalStep != null) {
            stepDescriptionTextView = rootView.findViewById(R.id.tv_recipestep_detail);
            stepDescriptionTextView.setText(mLocalStep.getDescription());
            mSimpleExoPlayerView = rootView.findViewById(R.id.simple_exo_player_view);
        }

        initializePlayer();

        updateVideoPlayerAndStepDescription(current_step);

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

                stepDescriptionTextView.setVisibility(View.GONE);

                // Expands the size of the Video player
                linearLayoutParams = (LinearLayout.LayoutParams) mSimpleExoPlayerView.getLayoutParams();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(linearLayoutParams);
                params.setMargins(0, 0, 0, 0);
                params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                mSimpleExoPlayerView.setLayoutParams(params);

            } else {

                stepDescriptionTextView.setVisibility(View.VISIBLE);

            }

        }
    }

    public void updateVideoPlayerAndStepDescription(int position) {
        // Stop Player first
        mSimpleExoPlayer.stop();

//        current_step = recipe.getSteps().get(whichStep);

        stepDescriptionTextView.setText(mLocalStep.getDescription());

        if (mLocalStep.getVideoURL().isEmpty()) {

            mSimpleExoPlayerView.setVisibility(View.GONE);

        } else {

            mSimpleExoPlayerView.setVisibility(View.VISIBLE);

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mLocalStep.getVideoURL()), new DefaultDataSourceFactory(getContext(), "Hello"), new DefaultExtractorsFactory(), null, null);

            mSimpleExoPlayer.prepare(mediaSource);

            mSimpleExoPlayer.setPlayWhenReady(true);

        }

    }

    private void releasePlayer() {
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

    // Override ExoPlayer.EventListeners
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
            updateVideoPlayerAndStepDescription(current_step);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 && mSimpleExoPlayer == null) {
            initializePlayer();
            updateVideoPlayerAndStepDescription(current_step);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSimpleExoPlayer != null) {
            current_exo_video_position = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSimpleExoPlayer != null) {
            current_exo_video_position = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSimpleExoPlayer != null) {
            current_exo_video_position = mSimpleExoPlayer.getCurrentPosition();
            releasePlayer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mSimpleExoPlayer != null) {
            outState.putLong(StringConstantHelper.EXOPLAYER_VIDEO_POSITION, mSimpleExoPlayer.getCurrentPosition());
        } else {
            outState.putLong(StringConstantHelper.EXOPLAYER_VIDEO_POSITION, current_exo_video_position);
        }
        super.onSaveInstanceState(outState);
    }

}
