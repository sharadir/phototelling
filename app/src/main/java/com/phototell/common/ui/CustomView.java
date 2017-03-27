package com.phototell.common.ui;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by user on 24/03/2017.
 */

public interface CustomView<D> {

    void bind(D data);

    View getView();
}