package com.ambergleam.petfinder.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailFragment extends BaseFragment {

    private static final String TAG = DetailFragment.class.getSimpleName();

    public static final String EXTRA_PET = TAG + "EXTRA_PET";

    @InjectView(R.id.layout_name) LinearLayout mNameLayout;
    @InjectView(R.id.pet_name) TextView mNameTextView;

    private Pet mPet;

    public static DetailFragment newInstance(Pet pet) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PET, pet);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPet = (Pet) getArguments().getSerializable(EXTRA_PET);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.inject(this, layout);
        setHasOptionsMenu(true);
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        updateName();
    }

    private void updateName() {
        if (mPet.mName != null && !mPet.mName.mString.equals("")) {
            mNameLayout.setVisibility(View.VISIBLE);
            mNameTextView.setText(mPet.mName.mString);
        } else {
            mNameLayout.setVisibility(View.INVISIBLE);
        }
    }

}
