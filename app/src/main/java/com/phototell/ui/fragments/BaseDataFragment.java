package com.phototell.ui.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phototell.R;

import static android.view.View.*;

/**
 * BaseDataFragment is a fragment for common loading/displaying items operations
 * abstract inflateDataView function: every fragment that uses BaseDataFragment has to specify
 * a view for displaying data,
 */
public abstract class BaseDataFragment<DataWrapper> extends Fragment {

    protected View dataView;
    protected View noResultView;
    protected ProgressBar loadingSpinner;
    protected long spinnerDuration = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.data_fragment, container, false);
        ViewStub dataWrapper = (ViewStub) fragmentView.findViewById(R.id.data_wrapper);
        dataView = inflateDataView(dataWrapper);
        dataView.setVisibility(GONE);
        ViewStub noResultWrapper = (ViewStub) fragmentView.findViewById(R.id.no_result_wrapper);
        noResultView = inflateNoResultView(noResultWrapper);
        noResultView.setVisibility(GONE);
        loadingSpinner = (ProgressBar) fragmentView.findViewById(R.id.loadingspinner_gn);
        return fragmentView;
    }

    protected View inflateNoResultView(ViewStub noResultWrapper) {
        noResultWrapper.setLayoutResource(R.layout.no_items_text);
        TextView noResultView = (TextView) noResultWrapper.inflate();
        noResultView.setText(getNoItemsMessage());
        return noResultView;
    }

    protected abstract View inflateDataView(ViewStub dataWrapper);

    protected abstract String getNoItemsMessage();

    protected void showNoItems(boolean show) {
        if (show) {
            noResultView.setVisibility(View.VISIBLE);
            dataView.setVisibility(View.GONE);
        } else {
            noResultView.setVisibility(GONE);
            dataView.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading(boolean show) {
        if (loadingSpinner == null) {
            return;
        }
        if (show) {
            showLoadingForDuration(spinnerDuration);
        } else {
            if (loadingSpinner.getVisibility() != GONE) { // Don't reset the spinner progress if it is not spinning
                loadingSpinner.setVisibility(GONE);
            }
        }
    }

    private void showLoadingForDuration(long duration) {
        if (loadingSpinner.getVisibility() != VISIBLE) { // Don't reset the spinner progress if it is spinning
            ObjectAnimator animation = ObjectAnimator.ofInt(loadingSpinner, "progress", 0, 100);
            animation.setDuration(duration);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    loadingSpinner.setVisibility(VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //do something when the countdown is complete
                    loadingSpinner.setVisibility(GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            animation.start();
        }
    }

    public void toast(String message){
       // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
       //         .setAction("Action", null).show();
    }

    public boolean isEmpty() {
        return true;
    }
}