package com.ambergleam.petfinder.controller;

import android.support.v4.app.Fragment;

import com.ambergleam.petfinder.model.Pet;

public class DetailActivity extends BaseActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        Pet pet = (Pet) getIntent().getSerializableExtra(DetailFragment.EXTRA_PET);
        return DetailFragment.newInstance(pet);
    }

}
