package com.phototell;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;

import com.phototell.data.Photo;
import com.phototell.model.PhotosLoader;
import com.phototell.data.UriInfo;
import com.phototell.util.PhotoPicker;
import com.phototell.util.PhotoUtility;

import java.util.List;


public class HomeScreen extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;

    private PhotosLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new PhotosLoader(this);
        //loader = new PhotosLoader(PhotoTellApplication.getInstance().getContext());

        //ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseImageIntent = PhotoPicker.getPickImageIntent(HomeScreen.this);
                startActivityForResult(chooseImageIntent, RESULT_LOAD_IMAGE);
            }
        });

        List<Photo> photos = loader.getPhotos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:

                UriInfo uriInfo = PhotoPicker.getUriInfoFromResult(HomeScreen.this, resultCode, data);
                if(uriInfo.getUri() != null) {

                    Photo photo = PhotoUtility.convertUri(HomeScreen.this, uriInfo.getUri(), uriInfo.isCamera());

                    ImageView imageView = (ImageView) findViewById(R.id.imgView);
                    imageView.setImageBitmap(photo.getImageBitMap());

                    loader.addPhoto(photo);
                    List<Photo> photos = loader.getPhotos();
                    int i = 0;
                    i++;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
