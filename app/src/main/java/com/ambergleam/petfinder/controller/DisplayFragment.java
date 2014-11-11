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

public abstract class DisplayFragment extends BaseFragment {

    public static final String TAG = DisplayFragment.class.getSimpleName();

    public static final String STATE_PETS = TAG + "STATE_PETS";
    public static final String STATE_PETS_SIZE_UNFILTERED = TAG + "STATE_PETS_SIZE_UNFILTERED";
    public static final String STATE_PETS_INDEX = TAG + "STATE_PETS_INDEX";
    public static final String STATE_PETS_OFFSET = TAG + "STATE_PETS_OFFSET";
    public static final String STATE_IMAGE_INDEX = TAG + "STATE_IMAGE_INDEX";

    public static final int IMAGE_INDEX_INITIAL = 2;
    public static final int IMAGE_INDEX_DELTA = 5;

    @InjectView(R.id.fragment_display_previous_pet_button) ImageButton mPreviousPetButton;
    @InjectView(R.id.fragment_display_name_text) TextView mNameTextView;
    @InjectView(R.id.fragment_display_next_pet_button) ImageButton mNextPetButton;

    @InjectView(R.id.fragment_display_previous_image_button) ImageButton mPreviousImageButton;
    @InjectView(R.id.fragment_display_index_text) TextView mImageIndexTextView;
    @InjectView(R.id.fragment_display_next_image_button) ImageButton mNextImageButton;

    @InjectView(R.id.fragment_display_image) ImageView mImageView;
    @InjectView(R.id.fragment_display_progress) ProgressBar mProgressBar;
    @InjectView(R.id.fragment_display_error) RelativeLayout mError;
    @InjectView(R.id.fragment_display_empty) TextView mEmpty;

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    public CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public ArrayList<Pet> mPetList;
    public int mPetListSizeUnfiltered;
    public int mPetIndex;
    public int mPetOffset;
    public int mImageIndex;

    public enum STATE {
        SEARCHING,
        LOADING,
        FINISHED,
        EMPTY,
        ERROR
    }

    public STATE mState = STATE.ERROR;

    protected abstract void search();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_display, container, false);
        ButterKnife.inject(this, layout);

        if (savedInstanceState != null) {
            mPetList = (ArrayList<Pet>) savedInstanceState.getSerializable(STATE_PETS);
            mPetListSizeUnfiltered = savedInstanceState.getInt(STATE_PETS_SIZE_UNFILTERED);
            mPetIndex = savedInstanceState.getInt(STATE_PETS_INDEX);
            mPetOffset = savedInstanceState.getInt(STATE_PETS_OFFSET);
            mImageIndex = savedInstanceState.getInt(STATE_IMAGE_INDEX);
        }
        mPetfinderServiceManager.getPetfinderPreference().loadPreference(getActivity());

        mError.setOnClickListener(v -> refresh());
        setHasOptionsMenu(true);
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_PETS, mPetList);
        outState.putInt(STATE_PETS_SIZE_UNFILTERED, mPetListSizeUnfiltered);
        outState.putInt(STATE_PETS_INDEX, mPetIndex);
        outState.putInt(STATE_PETS_OFFSET, mPetOffset);
        outState.putInt(STATE_IMAGE_INDEX, mImageIndex);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPetList == null || mPetList.size() == 0) {
            search();
        } else {
            mState = STATE.LOADING;
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

    private void refresh() {
        mPetIndex = 0;
        mPetOffset = 0;
        search();
    }

    public void startDetailActivity() {
        Pet pet = mPetList.get(mPetIndex);
        Intent intentDetail = new Intent(getActivity(), DetailsActivity.class);
        intentDetail.putExtra(DetailsFragment.EXTRA_PET, pet);
        startActivity(intentDetail);
    }

    @OnClick(R.id.fragment_display_name_text)
    public void onClickPetName() {
        startDetailActivity();
    }

    @OnClick(R.id.fragment_display_previous_image_button)
    public void onClickPreviousImage() {
        mState = STATE.LOADING;
        mImageIndex -= IMAGE_INDEX_DELTA;
        updateUI();
    }

    @OnClick(R.id.fragment_display_next_image_button)
    public void onClickNextImage() {
        mState = STATE.LOADING;
        mImageIndex += IMAGE_INDEX_DELTA;
        updateUI();
    }

    @OnClick(R.id.fragment_display_previous_pet_button)
    public void onClickPreviousPet() {
        mState = STATE.LOADING;
        mImageIndex = IMAGE_INDEX_INITIAL;
        mPetIndex--;
        if (mPetIndex < 0) {
            mPetIndex = -1;
            mPetOffset -= mPetfinderServiceManager.getCount();
            search();
        } else {
            updateUI();
        }
    }

    @OnClick(R.id.fragment_display_next_pet_button)
    public void onClickNextPet() {
        mState = STATE.LOADING;
        mImageIndex = IMAGE_INDEX_INITIAL;
        mPetIndex++;
        if (mPetIndex >= mPetList.size()) {
            mPetIndex %= mPetList.size();
            mPetOffset += mPetfinderServiceManager.getCount();
            search();
        } else {
            updateUI();
        }
    }

    public void updateUI() {
        hideUI();
        switch (mState) {
            case SEARCHING:
                showSearching();
                break;
            case LOADING:
                showLoading();
                break;
            case FINISHED:
                showFinished();
                break;
            case EMPTY:
                showEmpty();
                break;
            case ERROR:
            default:
                showError();
                break;
        }
        getActivity().invalidateOptionsMenu();
    }

    private void hideUI() {
        mPreviousPetButton.setVisibility(View.INVISIBLE);
        mNameTextView.setVisibility(View.INVISIBLE);
        mNextPetButton.setVisibility(View.INVISIBLE);
        mPreviousImageButton.setVisibility(View.INVISIBLE);
        mImageIndexTextView.setVisibility(View.INVISIBLE);
        mNextImageButton.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
    }

    private void showSearching() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        updatePetNameTextView();
        mNameTextView.setVisibility(View.VISIBLE);
        showPetNavButtons();
        updateImageIndexTextView();
        mImageIndexTextView.setVisibility(View.VISIBLE);
        showImageNavButtons();
        mProgressBar.setVisibility(View.VISIBLE);
        updateImageView();
    }

    private void showFinished() {
        mNameTextView.setVisibility(View.VISIBLE);
        showPetNavButtons();
        mImageIndexTextView.setVisibility(View.VISIBLE);
        showImageNavButtons();
        mImageView.setVisibility(View.VISIBLE);
    }

    private void showEmpty() {
        mEmpty.setVisibility(View.VISIBLE);
    }

    private void showError() {
        mError.setVisibility(View.VISIBLE);
    }

    private void updatePetNameTextView() {
        mNameTextView.setText(mPetList.get(mPetIndex).mName.mString);
    }

    private void showPetNavButtons() {
        if (mPetIndex + mPetOffset - 1 < 0 || (!mPetfinderServiceManager.getPetfinderPreference().isLocationSearch() && getActivity() instanceof MainActivity)) {
            mPreviousPetButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousPetButton.setVisibility(View.VISIBLE);
        }
        if (mPetIndex + 1 >= mPetList.size() && mPetListSizeUnfiltered < mPetfinderServiceManager.getCount()) {
            mNextPetButton.setVisibility(View.INVISIBLE);
        } else {
            mNextPetButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateImageIndexTextView() {
        int imageIndex = (mImageIndex / IMAGE_INDEX_DELTA) + 1;
        int imageIndexLength = mPetList.get(mPetIndex).mMedia.mPhotos.mPhotos.length / IMAGE_INDEX_DELTA;
        String index = new StringBuilder()
                .append("( ")
                .append(imageIndex)
                .append(" / ")
                .append(imageIndexLength)
                .append(" )").toString();
        mImageIndexTextView.setText(index);
    }

    private void showImageNavButtons() {
        if (mImageIndex - IMAGE_INDEX_DELTA < 0) {
            mPreviousImageButton.setVisibility(View.INVISIBLE);
        } else {
            mPreviousImageButton.setVisibility(View.VISIBLE);
        }
        if (mImageIndex + IMAGE_INDEX_DELTA > mPetList.get(mPetIndex).mMedia.mPhotos.mPhotos.length) {
            mNextImageButton.setVisibility(View.INVISIBLE);
        } else {
            mNextImageButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateImageView() {
        Picasso.with(getActivity())
                .load(mPetList.get(mPetIndex).mMedia.mPhotos.mPhotos[mImageIndex].mPhotoUrl)
                .into(mImageView,
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                mState = STATE.FINISHED;
                                updateUI();
                            }

                            @Override
                            public void onError() {
                                mState = STATE.FINISHED;
                                updateUI();
                            }
                        });
    }

}
