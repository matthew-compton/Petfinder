package com.ambergleam.petfinder.controller;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class FavoritesFragment extends PetListFragment {

    @Override
    public void onResume() {
        super.onResume();
        if (mPets != null && mPets.size() != 0) {
            if (!isCurrentPetFavorited()) {
                startPetLoading();
                mPets.remove(mPetIndex);
                if (mPetIndex != 0) {
                    mPetIndex--;
                }
                mPetSizeUnfiltered = mPets.size();
                mImageIndex = IMAGE_INDEX_INITIAL;
                if (mPets.size() == 0) {
                    showEmpty();
                } else {
                    updateUI();
                }
            }
        }
    }

    @Override
    protected void updatePetNavButtons() {
        if (mPetIndex + mPetOffset - 1 < 0) {
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

    private boolean isCurrentPetFavorited() {
        return mPetfinderServiceManager.getPetfinderPreference().isFavorite(mPets.get(mPetIndex).mId.toString());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favorites, menu);
        MenuItem itemDetail = menu.findItem(R.id.menu_main_details);
        itemDetail.setVisible((mPets != null && mPets.size() != 0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_details:
                startDetailActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void findPets() {
        startPetLoading();
        mCompositeSubscription = new CompositeSubscription();

        Action1<List<Pet>> successAction = petList -> {
            if (mPets == null) {
                mPets = new ArrayList<>();
            }
            if (petList.size() != 0) {
                mPets.addAll(filterPets(petList));
                Collections.sort(mPets);
            }
            mPetSizeUnfiltered = mPets.size();
            checkPetIndex();
            mImageIndex = IMAGE_INDEX_INITIAL;
            finishLoading();
            if (mPets == null || mPets.size() == 0) {
                showEmpty();
            } else {
                updateUI();
            }
        };

        Action1<Throwable> failureAction = throwable -> {
            Log.e(TAG, throwable.getMessage().toString());
            finishLoading();
            showError();
        };

        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(mPetfinderServiceManager.getPetfinderPreference().getFavorites());
        for (String id : ids) {
            Subscription subscription = mPetfinderServiceManager.performSearchById(id).subscribe(successAction, failureAction);
            mCompositeSubscription.add(subscription);
        }
    }

}