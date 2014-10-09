package com.ambergleam.petfinder.controller;

import android.support.v4.app.Fragment;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new SettingsFragment();
    }

}
