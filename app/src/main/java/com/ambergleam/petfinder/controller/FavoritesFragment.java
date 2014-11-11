package com.ambergleam.petfinder.controller;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class FavoritesFragment extends DisplayFragment {

    @Override
    public void onResume() {
        super.onResume();
        if (mPetList != null && mPetList.size() > 0) {
            if (!isCurrentPetFavorited()) {
                mState = STATE.LOADING;
                removeCurrentPet();
                if (mPetList.size() == 0) {
                    mState = STATE.EMPTY;
                }
                updateUI();
            }
        }
    }

    @Override
    protected void search() {
        mState = STATE.SEARCHING;
        mCompositeSubscription = new CompositeSubscription();

        Action1<List<Pet>> successAction = petList -> {
            mState = STATE.LOADING;
            if (mPetList == null) {
                mPetList = new ArrayList<>();
            }
            if (petList.size() > 0) {
                mPetList.addAll(filterPets(petList));
                Collections.sort(mPetList);
            }
            mPetListSizeUnfiltered = petList.size();
            mPetIndex = mPetIndex < 0 ? mPetList.size() - 1 : mPetIndex;
            mImageIndex = IMAGE_INDEX_INITIAL;
            if (mPetList == null || mPetList.size() == 0) {
                mState = STATE.EMPTY;
            }
            updateUI();
        };

        Action1<Throwable> failureAction = throwable -> {
            Log.e(TAG, throwable.getMessage().toString());
            mState = STATE.ERROR;
            updateUI();
        };

        ArrayList<String> ids = new ArrayList<>();
        ids.addAll(mPetfinderServiceManager.getPetfinderPreference().getFavorites());
        if (ids.size() > 0) {
            for (String id : ids) {
                Subscription subscription = mPetfinderServiceManager.performSearchById(id).subscribe(successAction, failureAction);
                mCompositeSubscription.add(subscription);
            }
        } else {
            mState = STATE.EMPTY;
        }

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favorites, menu);
        MenuItem itemDetail = menu.findItem(R.id.menu_favorites_details);
        itemDetail.setVisible((mPetList != null && mPetList.size() != 0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorites_details:
                startDetailActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCurrentPetFavorited() {
        return mPetfinderServiceManager.getPetfinderPreference().isFavorite(mPetList.get(mPetIndex).mId.toString());
    }

    private void removeCurrentPet() {
        mPetList.remove(mPetIndex);
        if (mPetIndex != 0) {
            mPetIndex--;
        }
        mPetListSizeUnfiltered = mPetList.size();
        mImageIndex = IMAGE_INDEX_INITIAL;
    }

}