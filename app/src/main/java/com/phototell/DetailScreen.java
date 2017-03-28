package com.phototell;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.EditText;

import com.phototell.data.Photo;
import com.phototell.model.PhotoManager;
import com.phototell.ui.fragments.PhotoDetailsFragment;


public class DetailScreen extends AppCompatActivity {

    public static final String LOAD_DETAIL_BROADCAST_NAME = "load_details";
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 2;

    private BroadcastReceiver detailsReceiver;
    private int photoId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Bundle bundle = intent.getExtras();
                photoId = bundle.getInt(PhotoDetailsFragment.PHOTO_ID_KEY, 0);
                handlePhotoDescription(photoId);
            }
        };

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }

        IntentFilter filter = new IntentFilter(LOAD_DETAIL_BROADCAST_NAME);
        this.registerReceiver(detailsReceiver, filter);
        if (savedInstanceState != null) {
            photoId = savedInstanceState.getInt(PhotoDetailsFragment.PHOTO_ID_KEY, 0);
            return;
        }

        // Create a new Fragment to be placed in the activity layout
        PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment();
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        photoDetailsFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, photoDetailsFragment, PhotoDetailsFragment.TAG)
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],  @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    handlePhotoDescription(photoId);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PhotoDetailsFragment.PHOTO_ID_KEY, photoId);
    }

    @Override
    public void onPause() {
        try {
            this.unregisterReceiver(detailsReceiver);
        } catch (IllegalArgumentException e) {
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter filter = new IntentFilter(LOAD_DETAIL_BROADCAST_NAME);
        this.registerReceiver(detailsReceiver, filter);
        super.onResume();
    }

    private void handlePhotoDescription(int photoId) {
        final Photo photo = PhotoManager.getInstance().getPhoto(photoId);
        final PhotoDetailsFragment photoDetailsFragment = getPhotoDetailsFragment();
        if (photo != null && photoDetailsFragment != null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Description:");
            final EditText input = new EditText(this);
            input.setText(photo.getDescription());
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Editable editable = input.getText();
                    photo.setDescription(editable.toString());
                    PhotoManager.getInstance().updatePhoto(photo);
                    photoDetailsFragment.setPhoto(photo);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }
    }

    public PhotoDetailsFragment getPhotoDetailsFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PhotoDetailsFragment.TAG);
        return fragment instanceof PhotoDetailsFragment ? (PhotoDetailsFragment) fragment : null;
    }
}