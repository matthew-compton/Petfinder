package com.ambergleam.petfinder.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import rx.subscriptions.CompositeSubscription;

public abstract class PetListFragment extends BaseFragment {

    public static final String TAG = PetListFragment.class.getSimpleName();

    public static final String STATE_PETS = TAG + "STATE_PETS";
    public static final String STATE_PETS_SIZE_UNFILTERED = TAG + "STATE_PETS_SIZE_UNFILTERED";
    public static final String STATE_PETS_INDEX = TAG + "STATE_PETS_INDEX";
    public static final String STATE_PETS_OFFSET = TAG + "STATE_PETS_OFFSET";
    public static final String STATE_IMAGE_INDEX = TAG + "STATE_IMAGE_INDEX";

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
    @InjectView(R.id.error) RelativeLayout mError;
    @InjectView(R.id.empty) TextView mEmpty;

    public ArrayList<Pet> mPets;
    public int mPetSizeUnfiltered;
    public int mPetIndex;
    public int mPetOffset;
    public int mImageIndex;

    protected abstract void findPets();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_pet, container, false);
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

        mError.setOnClickListener(v -> refresh());
        setHasOptionsMenu(true);
        return layout;
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

    public void refresh() {
        hideAll();
        mPetIndex = 0;
        mPetOffset = 0;
        findPets();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPets == null || mPets.size() == 0) {
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

    public ArrayList<Pet> filterPets(List<Pet> unfiltered) {
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

    @OnClick(R.id.pet_name)
    public void onClickPetName() {
        startDetailActivity();
    }

    public void startDetailActivity() {
        Pet pet = mPets.get(mPetIndex);
        Intent intentDetail = new Intent(getActivity(), DetailActivity.class);
        intentDetail.putExtra(DetailFragment.EXTRA_PET, pet);
        startActivity(intentDetail);
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

    public void checkPetIndex() {
        if (mPetIndex == -1) {
            mPetIndex = mPets.size() - 1;
        }
    }

    @OnClick(R.id.pet_previous)
    public void onClickPreviousPet() {
        startPetLoading();
        mImageIndex = IMAGE_INDEX_INITIAL;
        mPetIndex--;
        if (mPetIndex < 0) {
            mPetIndex = -1;
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

    public void updateUI() {
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

    public void startPetLoading() {
        hideAll();

        mProgressBar.setVisibility(View.VISIBLE);
        mNameTextView.setVisibility(View.INVISIBLE);

        mPreviousImageButton.setVisibility(View.INVISIBLE);
        mIndexTextView.setVisibility(View.INVISIBLE);
        mNextImageButton.setVisibility(View.INVISIBLE);
    }

    public void startImageLoading() {
        hideAll();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void finishLoading() {
        hideAll();
        mNameTextView.setVisibility(View.VISIBLE);
        mIndexTextView.setVisibility(View.VISIBLE);
        mImage.setVisibility(View.VISIBLE);
        getActivity().invalidateOptionsMenu();
    }

    public void showError() {
        hideAll();
        mError.setVisibility(View.VISIBLE);
        getActivity().invalidateOptionsMenu();
    }

    public void showEmpty() {
        hideAll();
        mEmpty.setVisibility(View.VISIBLE);
        mPreviousImageButton.setVisibility(View.INVISIBLE);
        mNextImageButton.setVisibility(View.INVISIBLE);
        getActivity().invalidateOptionsMenu();
    }

    private void hideAll() {
        mProgressBar.setVisibility(View.GONE);
        mImage.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
    }

}
