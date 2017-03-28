package com.phototell.common.ui;

import android.view.View;
/**
 * For supporting {@link com.phototell.common.ui.DataCustomRecyclerAdapter}

 * */
public interface CustomView<D> {
    void bind(D data);
    View getView();
}