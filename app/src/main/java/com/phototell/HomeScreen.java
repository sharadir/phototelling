package com.phototell;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.phototell.data.Photo;
import com.phototell.model.PhotoManager;
import com.phototell.ui.fragments.PhotoDetailsFragment;
import com.phototell.ui.fragments.PhotoListFragment;
import com.phototell.util.MessageUtility;
import com.phototell.util.PhotoPicker;
import com.phototell.util.PhotoUtility;

import java.util.List;


public class HomeScreen extends AppCompatActivity
		implements PhotoListFragment.OnPhotoSelectedListener {

	private static final int RESULT_LOAD_IMAGE = 1;
	public static final String FILTER_KEY = "filter_id";

	private String filterText = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent chooseImageIntent = PhotoPicker.getPickImageIntent(HomeScreen.this);
				startActivityForResult(chooseImageIntent, RESULT_LOAD_IMAGE);
			}
		});

		if (savedInstanceState != null) {
			filterText = savedInstanceState.getString(FILTER_KEY, "");
			if(!filterText.equals("")) {
				filterPhotos(filterText);
			}
			return;
		}
		// Create a new Fragment to be placed in the activity layout
		PhotoListFragment photoListFragment = new PhotoListFragment();
		photoListFragment.setRetainInstance(true);
		// In case this activity was started with special instructions from an
		// Intent, pass the Intent's extras to the fragment as arguments
		photoListFragment.setArguments(getIntent().getExtras());

		// Add the fragment to the 'fragment_container' FrameLayout
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, photoListFragment, PhotoListFragment.TAG)
		      /*  .setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right)*/
				.commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(FILTER_KEY, filterText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		// Inflate menu to add items to action bar if it is present.
		inflater.inflate(R.menu.menu_main, menu);
		// Associate searchable configuration with the SearchView
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setOnQueryTextListener(new QueryTextListener());
		return true;
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			super.onBackPressed();
		} else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			moveTaskToBack(false);
		} else {
			getSupportFragmentManager().popBackStack();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case RESULT_LOAD_IMAGE:
				Uri uri = PhotoPicker.getUriFromResult(HomeScreen.this, resultCode, data);
				if (uri != null) {
					Photo photo = PhotoUtility.convertUri(HomeScreen.this, uri);
					if (photo != null) {
						PhotoManager.getInstance().addPhoto(photo);
						goToPhotoDetails(photo.getId());
					}
				}
				break;
			default:
				super.onActivityResult(requestCode, resultCode, data);
				break;
		}
	}

	@Override
	public void onPhotoSelected(Photo photo) {
		goToPhotoDetails(photo.getId());
	}

	private void goToPhotoDetails(int photoId) {
		Intent intent = new Intent(this, DetailScreen.class);
		intent.putExtra(PhotoDetailsFragment.PHOTO_ID_KEY, photoId);
		startActivity(intent);
	}

	public PhotoListFragment getPhotoListFragment() {
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(PhotoListFragment.TAG);
		return fragment instanceof PhotoListFragment ? (PhotoListFragment) fragment : null;
	}

	private class QueryTextListener implements SearchView.OnQueryTextListener {

		@Override
		public boolean onQueryTextSubmit(String query) {
			return true;
		}

		@Override
		public boolean onQueryTextChange(String filterText) {
			filterPhotos(filterText);
			return true;
		}
	}

	private void filterPhotos(String filterText){
		this.filterText = filterText;
		PhotoListFragment photoListFragment = getPhotoListFragment();
		if (photoListFragment != null) {
			photoListFragment.showLoading(true);//not significant since photo manager filer photo method is sync
			List<Photo> filteredPhoto = PhotoManager.getInstance().filterPhotos(filterText);
			photoListFragment.setItems(filteredPhoto);
			photoListFragment.showLoading(false);
			if (filteredPhoto == null || filteredPhoto.size() == 0) {
				MessageUtility.message(getResources().getString(R.string.no_pics_filter));
			}
		}
	}
}
