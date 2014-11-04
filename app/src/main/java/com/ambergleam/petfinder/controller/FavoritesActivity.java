package com.ambergleam.petfinder.controller;

import android.support.v4.app.Fragment;

public class FavoritesActivity extends BaseActivity {

    private static final String TAG = FavoritesActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new FavoritesFragment();
    }

}
