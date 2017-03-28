

package com.phototell.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.phototell.PhotoTellApplication;
import com.phototell.R;
import com.phototell.common.ui.CustomView;
import com.phototell.data.Photo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoListItemCard extends LinearLayout implements CustomView<Photo> { // PhotoCard {

    public PhotoCard photoCard;

    public PhotoListItemCard(Context context) {
        super(context);
        init();
    }

    public PhotoListItemCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoListItemCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.photo_card, this, true);

        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        photoCard = (PhotoCard) view.findViewById(R.id.card_item);
        photoCard.setClickable(true);
        photoCard.setFocusable(true);
        photoCard.uploadDescription.setVisibility(GONE);

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        p.addRule(RelativeLayout.RIGHT_OF, R.id.img_view);
        p.addRule(RelativeLayout.LEFT_OF, R.id.created_at);

        photoCard.description.setLayoutParams(p);

        RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        p1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        photoCard.imageView.setLayoutParams(p1);
    }

    @Override
    public void bind(@NonNull final Photo photo) {
        final Context context = PhotoTellApplication.getContext();

        photoCard.description.setText(photo.getDescription());
        Date creationDate = photo.getCreationDate();
        String newDateFormat = new SimpleDateFormat("dd-MM-yy").format(creationDate);
        if (newDateFormat != null) {
            photoCard.createdAt.setText(newDateFormat);
        }
        photoCard.imageView.setImageBitmap(photo.getThumbnailBBitMap());
    }

    @Override
    public View getView() {
        return this;
    }
}
