package com.ambergleam.petfinder.controller;

import android.app.Activity;
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
import com.ambergleam.petfinder.service.PetfinderServiceManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final int REQUEST_CODE_SETTINGS = 0;

    private static final String STATE_PETS = TAG + "STATE_PETS";
    private static final String STATE_PETS_SIZE_UNFILTERED = TAG + "STATE_PETS_SIZE_UNFILTERED";
    private static final String STATE_PETS_INDEX = TAG + "STATE_PETS_INDEX";
    private static final String STATE_PETS_OFFSET = TAG + "STATE_PETS_OFFSET";
    private static final String STATE_IMAGE_INDEX = TAG + "STATE_IMAGE_INDEX";

    public static final int IMAGE_INDEX_INITIAL = 2;
    public static final int IMAGE_INDEX_DELTA = 5;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    @InjectView(R.id.pet_previous) ImageButton mPreviousPetButton;
    @InjectView(R.id.pet_name) TextView mNameTextView;
    @InjectView(R.id.pet_next) ImageButton mNextPetButton;

    @InjectView(R.id.image_previous) ImageButton mPreviousImageButton;
    @InjectView(R.id.image_index) TextView mIndexTextView;
    @InjectView(R.id.image_next) ImageButton mNextImageButton;

    @InjectView(R.id.image) ImageView mImage;
    @InjectView(R.id.progress) ProgressBar mProgressBar;
    @InjectView(R.id.error) TextView mError;
    private boolean isError = false;

    private ArrayList<Pet> mPets;
    private int mPetSizeUnfiltered;
    private int mPetIndex;
    private int mPetOffset;
    private int mImageIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, layout);

        if (savedInstanceState != null) {
            mPets = (ArrayList<Pet>) savedInstanceState.getSerializable(STATE_PETS);
            mPetSizeUnfiltered = savedInstanceState.getInt(STATE_PETS_SIZE_UNFILTERED);
            mPetIndex = savedInstanceState.getInt(STATE_PETS_INDEX);
            mPetOffset = savedInstanceState.getInt(STATE_PETS_OFFSET);
            mImageIndex = savedInstanceState.getInt(STATE_IMAGE_INDEX);
            updateUI();
        }
        mPetfinderServiceManager.getPetfinderPreference().loadPreference(getActivity());

        setHasOptionsMenu(true);
        return layout;
    }

    private void findPets() {
        startPetLoading();
        mCompositeSubscription = new CompositeSubscription();

        Action1<List<Pet>> successAction = petList -> {
            mPets = filterPets(petList);
            mPetSizeUnfiltered = petList.size();
            mImageIndex = IMAGE_INDEX_INITIAL;
            updateUI();
        };

        Action1<Throwable> failureAction = throwable -> {
            Log.e(TAG, throwable.getMessage().toString());
            finishLoading();
            showError();
        };

        Subscription subscription = mPetfinderServiceManager.getPetfinderPreference().isLocationSearch()
                ? mPetfinderServiceManager.performSearchWithLocation(mPetOffset).subscribe(successAction, failureAction)
                : mPetfinderServiceManager.performSearch().subscribe(successAction, failureAction);
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_PETS, mPets);
        outState.putInt(STATE_PETS_SIZE_UNFILTERED, mPetSizeUnfiltered);
        outState.putInt(STATE_PETS_INDEX, mPetIndex);
        outState.putInt(STATE_PETS_OFFSET, mPetOffset);
        outState.putInt(STATE_IMAGE_INDEX, mImageIndex);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPets == null) {
            findPets();
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
        MenuItem itemRefresh = menu.findItem(R.id.refresh);
        MenuItem itemDetails = menu.findItem(R.id.details);
        if (isError) {
            itemRefresh.setVisible(true);
            itemDetails.setVisible(false);
        } else {
            itemRefresh.setVisible(false);
            itemDetails.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                hideError();
                mPetIndex = 0;
                mPetOffset = 0;
                findPets();
                break;
            case R.id.details:
                Pet pet = mPets.get(mPetIndex);
                Intent intentDetail = new Intent(getActivity(), DetailActivity.class);
                intentDetail.putExtra(DetailFragment.EXTRA_PET, pet);
                startActivity(intentDetail);
                break;
            case R.id.settings:
                Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intentSettings, REQUEST_CODE_SETTINGS);
                break;
            case R.id.favorites:
                Intent intentFavorites = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intentFavorites);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTINGS && resultCode == Activity.RESULT_OK && data != null) {
            boolean hasChanged = data.getBooleanExtra(SettingsFragment.EXTRA_CHANGED, false);
            if (hasChanged) {
                mPetIndex = 0;
                mPetOffset = 0;
                findPets();
            }
        }
    }

    private ArrayList<Pet> filterPets(List<Pet> unfiltered) {
        ArrayList<Pet> filtered = new ArrayList<>();
        for (Pet pet : unfiltered) {
            if (isValidPet(pet)) {
                filtered.add(pet);
            }
        }
        return filtered;
    }

    private boolean isValidPet(Pet pet) {
        if (pet != null && pet.mMedia != null && pet.mMedia.mPhotos != null) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.image_previous)
    public void onClickPreviousImage() {
        startImageLoading();
        int imageIndexLength = mPets.get(mPetIndex).mMedia.mPhotos.mPhotos.length;
        mImageIndex -= IMAGE_INDEX_DELTA;
        if (mImageIndex < 0) {
            mImageIndex = imageIndexLength + mImageIndex;
        }
        updateUI();
    }

    @OnClick(R.id.image_next)
    public void onClickNextImage() {
        startImageLoading();
        int imageIndexLength = mPets.get(mPetIndex).mMedia.mPhotos.mPhotos.length;
        mImageIndex += IMAGE_INDEX_DELTA;
        mImageIndex %= imageIndexLength;
        updateUI();
    }

    @OnClick(R.id.pet_previous)
    public void onClickPreviousPet() {
        startPetLoading();
        mImageIndex = IMAGE_INDEX_INITIAL;
        mPetIndex--;
        if (mPetIndex < 0) {
            mPetIndex += mPets.size();
            mPetOffset -= mPetfinderServiceManager.getCount();
            findPets();
        } else {
            updateUI();
        }
    }

    @OnClick(R.id.pet_next)
    public void onClickNextPet() {
        startPetLoading();
        mImageIndex = IMAGE_INDEX_INITIAL;
        mPetIndex++;
        if (mPetIndex >= mPets.size()) {
            mPetIndex %= mPets.size();
            mPetOffset += mPetfinderServiceManager.getCount();
            findPets();
        } else {
            updateUI();
        }
    }

    private void updateUI() {
        updateToolbarTop();
        updateToolbarBottom();
        updateImageView();
    }

    private void updateToolbarTop() {
        updateNameView();
        updatePetNavButtons();
    }

    private void updateToolbarBottom() {
        updateImageIndexView();
        updateImageNavButtons();
    }

    private void updateNameView() {
        mNameTextView.setText(mPets.get(mPetIndex).mName.mString);
    }

    private void updatePetNavButtons() {
        if (mPetIndex + mPetOffset - 1 < 0 || !mPetfinderServiceManager.getPetfinderPreference().isLocationSearch()) {
            mPreviousPetButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousPetButton.setVisibility(View.VISIBLE);
        }
        if (mPetIndex + 1 >= mPets.size() && mPetSizeUnfiltered < mPetfinderServiceManager.getCount()) {
            mNextPetButton.setVisibility(View.INVISIBLE);
        } else {
            mNextPetButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateImageIndexView() {
        int imageIndex = (mImageIndex / IMAGE_INDEX_DELTA) + 1;
        int imageIndexLength = mPets.get(mPetIndex).mMedia.mPhotos.mPhotos.length / IMAGE_INDEX_DELTA;
        String index = new StringBuilder()
                .append("( ")
                .append(imageIndex)
                .append(" / ")
                .append(imageIndexLength)
                .append(" )").toString();
        mIndexTextView.setText(index);
    }

    private void updateImageNavButtons() {
        if (mImageIndex - IMAGE_INDEX_DELTA < 0) {
            mPreviousImageButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousImageButton.setVisibility(View.VISIBLE);
        }
        if (mImageIndex + IMAGE_INDEX_DELTA > mPets.get(mPetIndex).mMedia.mPhotos.mPhotos.length) {
            mNextImageButton.setVisibility(View.INVISIBLE);
        } else {
            mNextImageButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateImageView() {
        Picasso.with(getActivity())
                .load(mPets.get(mPetIndex).mMedia.mPhotos.mPhotos[mImageIndex].mPhotoUrl)
                .into(mImage,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                finishLoading();
                            }

                            @Override
                            public void onError() {
                                finishLoading();
                            }
                        });
    }

    private void startPetLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNameTextView.setVisibility(View.INVISIBLE);
        mIndexTextView.setVisibility(View.INVISIBLE);
        mImage.setVisibility(View.GONE);
    }

    private void startImageLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.GONE);
    }

    private void finishLoading() {
        mProgressBar.setVisibility(View.GONE);
        mNameTextView.setVisibility(View.VISIBLE);
        mIndexTextView.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mImage.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        isError = true;
        getActivity().invalidateOptionsMenu();
    }

    private void hideError() {
        mError.setVisibility(View.GONE);
        mImage.setVisibility(View.VISIBLE);
        isError = false;
        getActivity().invalidateOptionsMenu();
    }

}
