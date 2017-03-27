package com.phototell;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.phototell.data.Photo;
import com.phototell.data.UriInfo;
import com.phototell.model.PhotoManager;
import com.phototell.ui.fragments.PhotoDetailsFragment;
import com.phototell.ui.fragments.PhotoListFragment;
import com.phototell.util.PhotoPicker;
import com.phototell.util.PhotoUtility;

import java.util.List;


public class DetailScreen extends AppCompatActivity {

    public static final String LOAD_DETAIL_BROADCAST_NAME = "load_details";

    private BroadcastReceiver detailsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        detailsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                Bundle bundle = intent.getExtras();
                int photoId = bundle.getInt(PhotoDetailsFragment.PHOTO_ID_KEY, 0);
                handlePhotoDescription(photoId);
            }
        };

        IntentFilter filter = new IntentFilter(LOAD_DETAIL_BROADCAST_NAME);
        this.registerReceiver(detailsReceiver, filter);
        if (savedInstanceState != null)
            return;

        // Create a new Fragment to be placed in the activity layout
        PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment();
        // In case this activity was started with special instructions from an
        // Intent, pass the Intent's extras to the fragment as arguments
        photoDetailsFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, photoDetailsFragment, photoDetailsFragment.TAG)
                .commit();
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