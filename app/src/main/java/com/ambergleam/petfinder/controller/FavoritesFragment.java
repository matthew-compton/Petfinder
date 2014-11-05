package com.ambergleam.petfinder.controller;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class FavoritesFragment extends PetListFragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favorites, menu);

        if (mPets == null || mPets.size() == 0) {
            return;
        }

        MenuItem itemDetail = menu.findItem(R.id.details);
        itemDetail.setVisible(true);

        boolean isFavorite = isFavorite();
        MenuItem itemFavorite = menu.findItem(R.id.favorite);
        itemFavorite.setVisible(!isFavorite);
        MenuItem itemUnfavorite = menu.findItem(R.id.unfavorite);
        itemUnfavorite.setVisible(isFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details:
                startDetailActivity();
                break;
            case R.id.favorite:
                favorite();
                break;
            case R.id.unfavorite:
                unfavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFavorite() {
        return mPetfinderServiceManager.getPetfinderPreference().isFavorite(mPets.get(mPetIndex).mId.toString());
    }

    private void favorite() {
        mPetfinderServiceManager.getPetfinderPreference().savePreference(getActivity());
        mPetfinderServiceManager.getPetfinderPreference().addFavorite(mPets.get(mPetIndex).mId.toString());
    }

    private void unfavorite() {
        mPetfinderServiceManager.getPetfinderPreference().savePreference(getActivity());
        mPetfinderServiceManager.getPetfinderPreference().removeFavorite(mPets.get(mPetIndex).mId.toString());
    }

    @Override
    protected void findPets() {
        startPetLoading();
        mCompositeSubscription = new CompositeSubscription();

        Action1<List<Pet>> successAction = petList -> {
            if (mPets == null) {
                mPets = new ArrayList<>();
            }
            ArrayList<Pet> tmp = filterPets(petList);
            mPets.addAll(tmp);
            mPetSizeUnfiltered = mPets.size();
            mImageIndex = IMAGE_INDEX_INITIAL;
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
        if (ids.size() == 0) {
            finishLoading();
            showEmpty();
        }
    }

}