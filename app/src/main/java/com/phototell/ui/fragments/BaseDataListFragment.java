package com.phototell.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.phototell.R;
import com.phototell.common.ui.CustomViewHolder;
import com.phototell.common.ui.DataCustomRecyclerAdapter;
import com.phototell.data.Photo;

import java.util.Map;

/**
 *
 * extends {@see BaseDataFragment} using DataCustomRecyclerAdapter and a RecyclerView as a data wrapper.
 */

public abstract class BaseDataListFragment
        extends BaseDataFragment
        implements CustomViewHolder.ClickListener {

    protected DataCustomRecyclerAdapter adapter;
    protected Map<Class, Class> viewsHash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewsHash = getDataViewClasses();
        adapter = new DataCustomRecyclerAdapter(viewsHash, this);
    }

    @Override
    protected View inflateDataView(ViewStub dataWrapper) {
        dataWrapper.setLayoutResource(R.layout.photos_recycler);
        RecyclerView recyclerView = (RecyclerView) dataWrapper.inflate();
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setClipChildren(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);
        recyclerView.addItemDecoration(
                new SpaceItemDecoration(getActivity(), R.dimen.dimen_8,
                        true, true));
        registerForContextMenu(recyclerView);
        return recyclerView;
    }

    protected abstract Map<Class, Class> getDataViewClasses();

}