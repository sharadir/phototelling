package com.phototell.ui.views;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.phototell.DetailScreen;
import com.phototell.PhotoTellApplication;
import com.phototell.R;
import com.phototell.common.ui.CustomView;
import com.phototell.data.Photo;
import com.phototell.ui.fragments.PhotoDetailsFragment;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoCard extends CardView
        implements CustomView<Photo> {

    public TextView description;
    public TextView createdAt;
    public ImageView imageView;

    public Button uploadDescription;

    public PhotoCard(Context context) {
        super(context);
        init();
    }

    public PhotoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.photo_card_layout, this, true);
        description = (TextView) view.findViewById(R.id.description);
        createdAt = (TextView) view.findViewById(R.id.created_at);
        imageView = (ImageView) view.findViewById(R.id.img_view);

        uploadDescription= (Button) view.findViewById(R.id.upload_description);
        setContentPadding(20, 20, 20, 20);
        setMaxCardElevation(5);
        setCardElevation(9);
        setClickable(false);
        setFocusable(false);
    }

    public void bind(@NonNull final Photo photo) {
        final Context context = PhotoTellApplication.getContext();

        description.setText(photo.getDescription());
        Date creationDate = photo.getCreationDate();
	    String newDateFormat = new SimpleDateFormat("dd-MM-yy").format(creationDate);
        if (newDateFormat != null) {
            createdAt.setText(newDateFormat);
        }
        uploadDescription.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailScreen.LOAD_DETAIL_BROADCAST_NAME);
                intent.putExtra(PhotoDetailsFragment.PHOTO_ID_KEY, photo.getId());
                context.sendBroadcast(intent);
            }
        });

        imageView.setImageBitmap(photo.getImageBitMap());
    }

    @Override
    public View getView() {
        return this;
    }
}