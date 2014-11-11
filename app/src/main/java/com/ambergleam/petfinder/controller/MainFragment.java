package com.ambergleam.petfinder.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ambergleam.petfinder.BuildConfig;
import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;

import org.codechimp.apprater.AppRater;

import java.util.List;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainFragment extends DisplayFragment {

    private static final int REQUEST_CODE_SETTINGS = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.ENABLE_RATING) {
            AppRater.app_launched(getActivity());
        }
    }

    @Override
    protected void search() {
        mState = STATE.SEARCHING;
        mCompositeSubscription = new CompositeSubscription();

        Action1<List<Pet>> successAction = petList -> {
            mState = STATE.LOADING;
            mPetList = filterPets(petList);
            mPetListSizeUnfiltered = petList.size();
            mPetIndex = mPetIndex < 0 ? mPetList.size() - 1 : mPetIndex;
            mImageIndex = IMAGE_INDEX_INITIAL;
            updateUI();
        };

        Action1<Throwable> failureAction = throwable -> {
            mState = STATE.ERROR;
            Log.e(TAG, throwable.getMessage().toString());
            updateUI();
        };

        Subscription subscription = mPetfinderServiceManager.getPetfinderPreference().isLocationSearch()
                ? mPetfinderServiceManager.performSearchWithLocation(mPetOffset).subscribe(successAction, failureAction)
                : mPetfinderServiceManager.performSearch().subscribe(successAction, failureAction);
        mCompositeSubscription.add(subscription);

        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuDetail = menu.findItem(R.id.menu_main_details);
        menuDetail.setVisible((mPetList == null || mPetList.size() == 0) ? false : true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_details:
                startDetailActivity();
                break;
            case R.id.menu_main_favorites:
                Intent intentFavorites = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intentFavorites);
                break;
            case R.id.menu_main_settings:
                Intent intentSettings = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intentSettings, REQUEST_CODE_SETTINGS);
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
                search();
            }
        }
    }

}
