package com.ambergleam.petfinder.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.service.PetfinderServiceManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FavoritesFragment extends BaseFragment {

    private static final String TAG = FavoritesFragment.class.getSimpleName();

    @InjectView(R.id.favorites) TextView mFavorites;

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_favorites, container, false);
        ButterKnife.inject(this, layout);

        mFavorites.setText(mPetfinderServiceManager.getPetfinderPreference().getFavorites().toString());
        return layout;
    }

}