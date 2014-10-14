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
    @InjectView(R.id.layout_animal) LinearLayout mAnimalLayout;
    @InjectView(R.id.pet_animal) TextView mAnimalTextView;
    @InjectView(R.id.layout_mix) LinearLayout mMixLayout;
    @InjectView(R.id.pet_mix) TextView mMixTextView;
    @InjectView(R.id.layout_age) LinearLayout mAgeLayout;
    @InjectView(R.id.pet_age) TextView mAgeTextView;
    @InjectView(R.id.layout_size) LinearLayout mSizeLayout;
    @InjectView(R.id.pet_size) TextView mSizeTextView;
    @InjectView(R.id.layout_description) LinearLayout mDescriptionLayout;
    @InjectView(R.id.pet_description) TextView mDescriptionTextView;

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
        updateAnimal();
        updateMix();
        updateAge();
        updateSize();
        updateDescription();
    }

    private void updateName() {
        if (mPet.mName.mString != null && !mPet.mName.mString.equals("")) {
            mNameLayout.setVisibility(View.VISIBLE);
            mNameTextView.setText(mPet.mName.mString);
        } else {
            mNameLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void updateAnimal() {
        if (mPet.mAnimal.mString != null && !mPet.mAnimal.mString.equals("")) {
            mAnimalLayout.setVisibility(View.VISIBLE);
            mAnimalTextView.setText(mPet.mAnimal.mString);
        } else {
            mAnimalLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void updateMix() {
        if (mPet.mMix.mString != null && !mPet.mMix.mString.equals("")) {
            mMixLayout.setVisibility(View.VISIBLE);
            mMixTextView.setText(mPet.mMix.mString);
        } else {
            mMixLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void updateAge() {
        if (mPet.mAge.mString != null && !mPet.mAge.mString.equals("")) {
            mAgeLayout.setVisibility(View.VISIBLE);
            mAgeTextView.setText(mPet.mAge.mString);
        } else {
            mAgeLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSize() {
        if (mPet.mSize.mString != null && !mPet.mSize.mString.equals("")) {
            mSizeLayout.setVisibility(View.VISIBLE);
            mSizeTextView.setText(mPet.mSize.mString);
        } else {
            mSizeLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void updateDescription() {
        if (mPet.mDescription.mString != null && !mPet.mDescription.mString.equals("")) {
            mDescriptionLayout.setVisibility(View.VISIBLE);
            mDescriptionTextView.setText(mPet.mDescription.mString);
        } else {
            mDescriptionLayout.setVisibility(View.INVISIBLE);
        }
    }

}
