package com.ambergleam.petfinder.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;
import com.ambergleam.petfinder.model.Petfinder;
import com.ambergleam.petfinder.service.PetfinderServiceManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final String STATE_PET = TAG + "STATE_PET";
    private static final String STATE_INDEX = TAG + "STATE_INDEX";

    private static final int INDEX_INITIAL = 3;
    private static final int INDEX_DELTA = 5;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    @InjectView(R.id.image) ImageView mPetPictureImageView;
    @InjectView(R.id.loading) ProgressBar mLoadingProgressBar;

    @InjectView(R.id.previous) ImageButton mPreviousImageButton;
    @InjectView(R.id.next) ImageButton mNextImageButton;
    @InjectView(R.id.divider) View mDivider;

    @InjectView(R.id.name) TextView mNameTextView;

    private Pet mPet;
    private int mImageIndex;
    private int mImageIndexLength;

    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, layout);

        if (savedInstanceState != null) {
            mPet = (Pet) savedInstanceState.getSerializable(STATE_PET);
            mImageIndex = savedInstanceState.getInt(STATE_INDEX);
            mImageIndexLength = mPet.mMedia.mPhotos.mPhotos.length;
        }
        mPetfinderServiceManager.getPetfinderPreference().loadPreference(getActivity());

        setHasOptionsMenu(true);
        return layout;
    }

    private void findPet() {
        if (!isLoading) {
            isLoading = true;
            mCompositeSubscription = new CompositeSubscription();

            Action1<Petfinder> successAction = petfinder -> {
                updatePet(petfinder.mPet);
            };

            Action1<Throwable> failureAction = throwable -> {
                Log.e(TAG, throwable.getMessage().toString());
                isLoading = false;
            };

            mCompositeSubscription.add(mPetfinderServiceManager.performSearch().subscribe(successAction, failureAction));
        }
    }

    private void updatePet(Pet pet) {
        if (pet != null && pet.mMedia.mPhotos != null) {
            mPet = pet;
            mImageIndex = INDEX_INITIAL;
            mImageIndexLength = mPet.mMedia.mPhotos.mPhotos.length;
            updateUI();
        } else {
            Log.e(TAG, "Pet is invalid.");
            isLoading = false;
            findPet();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_PET, mPet);
        outState.getInt(STATE_INDEX, mImageIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPet == null) {
            findPet();
        } else {
            updateUI();
        }
    }

    @Override
    public void onPause() {
        super.onStop();
        mCompositeSubscription.clear();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                findPet();
                break;
            case R.id.info:
                Intent intentDetail = new Intent(getActivity(), DetailActivity.class);
                intentDetail.putExtra(DetailFragment.EXTRA_PET, mPet);
                startActivity(intentDetail);
                break;
            case R.id.settings:
                Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intentSettings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.previous)
    public void onClickPrevious() {
        getPreviousImage();
    }

    @OnClick(R.id.next)
    public void onClickNext() {
        getNextImage();
    }

    private void getPreviousImage() {
        mImageIndex -= INDEX_DELTA;
        if (mImageIndex < 0) {
            mImageIndex = mImageIndexLength + mImageIndex;
        }
        updateUI();
    }

    private void getNextImage() {
        mImageIndex += INDEX_DELTA;
        mImageIndex %= mImageIndexLength;
        updateUI();
    }

    private void updateUI() {
        mNameTextView.setText(mPet.mName.mString);
        mDivider.setVisibility(View.VISIBLE);
        updateImageButtons();
        updateImageView();
    }

    private void updateImageButtons() {
        if (mImageIndex - INDEX_DELTA < 0) {
            mPreviousImageButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousImageButton.setVisibility(View.VISIBLE);
        }
        if (mImageIndex + INDEX_DELTA > mImageIndexLength) {
            mNextImageButton.setVisibility(View.INVISIBLE);
        } else {
            mNextImageButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateImageView() {
        mPetPictureImageView.setVisibility(View.GONE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
        Picasso.with(getActivity())
                .load(mPet.mMedia.mPhotos.mPhotos[mImageIndex].mPhotoUrl)
                .into(mPetPictureImageView,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                onImageLoadFinish();
                            }

                            @Override
                            public void onError() {
                                onImageLoadFinish();
                            }
                        });
    }

    private void onImageLoadFinish() {
        mLoadingProgressBar.setVisibility(View.GONE);
        mPetPictureImageView.setVisibility(View.VISIBLE);
        isLoading = false;
    }

}
