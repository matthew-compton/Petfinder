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
import android.widget.TextView;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;
import com.ambergleam.petfinder.model.Petfinder;
import com.ambergleam.petfinder.service.PetfinderServiceManager;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final String EXTRA_PET = TAG + ".mPet";
    private static final String EXTRA_INDEX = TAG + ".mImageIndex";

    private static final int INDEX_INITIAL = 3;
    private static final int INDEX_DELTA = 5;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    @InjectView(R.id.image) ImageView mPetPictureImageView;
    @InjectView(R.id.previous) ImageButton mPreviousImageButton;
    @InjectView(R.id.next) ImageButton mNextImageButton;

    @InjectView(R.id.pet_name) TextView mPetNameTextView;

    private Pet mPet;
    private int mImageIndex;
    private int mImageIndexLength;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, layout);

        if (savedInstanceState != null) {
            mPet = (Pet) savedInstanceState.getSerializable(EXTRA_PET);
            mImageIndex = savedInstanceState.getInt(EXTRA_INDEX);
            mImageIndexLength = mPet.mMedia.mPhotos.mPhotos.length;
        }
        mPetfinderServiceManager.getPreference().loadPreference(getActivity());

        setHasOptionsMenu(true);
        return layout;
    }

    private void findPet() {
        mCompositeSubscription = new CompositeSubscription();

        Action1<Petfinder> successAction = petfinder -> {
            updatePet(petfinder.mPet);
        };

        Action1<Throwable> failureAction = throwable -> {
            Log.e(TAG, throwable.getMessage().toString());
        };

        mCompositeSubscription.add(mPetfinderServiceManager.performSearch().subscribe(successAction, failureAction));
    }

    private void updatePet(Pet pet) {
        if (pet != null && pet.mMedia.mPhotos != null) {
            mPet = pet;
            mImageIndex = INDEX_INITIAL;
            mImageIndexLength = mPet.mMedia.mPhotos.mPhotos.length;
            updateUI();
        } else {
            Log.e(TAG, "Pet is invalid: " + pet.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_PET, mPet);
        outState.getInt(EXTRA_INDEX, mImageIndex);
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
            case R.id.settings:
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
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
        mPetNameTextView.setText(mPet.mName.mString);
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
        Picasso.with(getActivity())
                .load(mPet.mMedia.mPhotos.mPhotos[mImageIndex].mPhotoUrl)
                .into(mPetPictureImageView);
    }

}
