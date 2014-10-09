package com.ambergleam.petfinder.controller;

import android.support.v4.app.Fragment;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
        return new MainFragment();
    }

}
