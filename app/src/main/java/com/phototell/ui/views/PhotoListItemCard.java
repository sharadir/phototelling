package com.phototell.ui.views;

import android.content.Context;
import android.util.AttributeSet;

public class PhotoListItemCard extends PhotoCard {
    public PhotoListItemCard(Context context) {
        super(context);
    }

    public PhotoListItemCard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoListItemCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void init() {
        super.init();
        setClickable(true);
        setFocusable(true);
        uploadDescription.setVisibility(GONE);
    }
}