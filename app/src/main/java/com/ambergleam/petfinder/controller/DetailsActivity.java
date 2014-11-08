package com.ambergleam.petfinder.controller;

import android.support.v4.app.Fragment;

import com.ambergleam.petfinder.model.Pet;

public class DetailsActivity extends BaseActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        Pet pet = (Pet) getIntent().getSerializableExtra(DetailsFragment.EXTRA_PET);
        return DetailsFragment.newInstance(pet);
    }

}