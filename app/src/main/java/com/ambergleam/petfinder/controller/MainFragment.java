package com.ambergleam.petfinder.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;
import com.ambergleam.petfinder.service.PetfinderServiceManager;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainFragment extends BaseFragment {

    private static final String TAG = MainFragment.class.getSimpleName();

    private static final String EXTRA_PET = TAG + ".mPet";

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    @InjectView(R.id.pet_image) ImageView mPetPictureImageView;
    @InjectView(R.id.pet_name) TextView mPetNameTextView;

    private Pet mPet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, layout);

        if (savedInstanceState != null) {
            mPet = (Pet) savedInstanceState.getSerializable(EXTRA_PET);
        }

//        DialogUtils.showLoadingDialog(getFragmentManager(), false);
        mPetfinderServiceManager.getPreference().loadPreference(getActivity());
//        DialogUtils.hideLoadingDialog(getFragmentManager());

        setHasOptionsMenu(true);
        return layout;
    }

    private void findPet() {
        mCompositeSubscription = new CompositeSubscription();

        Action1<Pet> successAction = pet -> {
            mPet = pet;
            updatePet();
        };

        mCompositeSubscription.add(mPetfinderServiceManager.performSearch().subscribe(successAction, throwable -> {
            mPet = null;
            updatePet();
        }));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_PET, mPet);
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePet();
    }

    @Override
    public void onPause() {
        super.onStop();
        mCompositeSubscription.clear();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                findPet();
                break;
            case R.id.settings:
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePet() {
        if (mPet != null) {
            mPetNameTextView.setText(mPet.mName.mString + " - " + mPet.mAnimal.mString);
            Picasso.with(getActivity()).load(mPet.mMedia.mPhotos.mPhotos[0].mPhotoUrl).into(mPetPictureImageView);
        } else {
            mPetNameTextView.setText(getString(R.string.no_results));
            mPetPictureImageView.setImageResource(R.drawable.paw);
        }
    }

}
