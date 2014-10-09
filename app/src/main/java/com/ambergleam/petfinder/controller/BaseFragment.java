package com.ambergleam.petfinder.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ambergleam.petfinder.PetfinderApplication;

public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetfinderApplication.get(getActivity()).inject(this);
    }

}
