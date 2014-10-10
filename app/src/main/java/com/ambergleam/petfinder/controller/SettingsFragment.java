package com.ambergleam.petfinder.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.AnimalType;
import com.ambergleam.petfinder.model.Preference;
import com.ambergleam.petfinder.service.PetfinderServiceManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsFragment extends BaseFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    @InjectView(R.id.type_all) RadioButton mTypeAllRadioButton;
    @InjectView(R.id.type_barnyard) RadioButton mTypeBarnyardRadioButton;
    @InjectView(R.id.type_bird) RadioButton mTypeBirdRadioButton;
    @InjectView(R.id.type_cat) RadioButton mTypeCatRadioButton;
    @InjectView(R.id.type_dog) RadioButton mTypeDogRadioButton;
    @InjectView(R.id.type_horse) RadioButton mTypeHorseRadioButton;
    @InjectView(R.id.type_pig) RadioButton mTypePigRadioButton;
    @InjectView(R.id.type_reptile) RadioButton mTypeReptileRadioButton;
    @InjectView(R.id.type_small_and_furry) RadioButton mTypeSmallAndFurryRadioButton;

    private Preference mPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, layout);
        setHasOptionsMenu(true);
        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:
                clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreference();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onStop();
        savePreference();
    }

    @OnClick({R.id.type_all, R.id.type_barnyard, R.id.type_bird, R.id.type_cat, R.id.type_dog, R.id.type_horse, R.id.type_pig, R.id.type_reptile, R.id.type_small_and_furry})
    public void activate(RadioButton radioButton) {
        radioButton.toggle();
        switch (radioButton.getId()) {
            case R.id.type_all:
                mPreference.setAnimalType(AnimalType.ALL);
                break;
            case R.id.type_barnyard:
                mPreference.setAnimalType(AnimalType.BARNYARD);
                break;
            case R.id.type_bird:
                mPreference.setAnimalType(AnimalType.BIRD);
                break;
            case R.id.type_cat:
                mPreference.setAnimalType(AnimalType.CAT);
                break;
            case R.id.type_dog:
                mPreference.setAnimalType(AnimalType.DOG);
                break;
            case R.id.type_horse:
                mPreference.setAnimalType(AnimalType.HORSE);
                break;
            case R.id.type_pig:
                mPreference.setAnimalType(AnimalType.PIG);
                break;
            case R.id.type_reptile:
                mPreference.setAnimalType(AnimalType.REPTILE);
                break;
            case R.id.type_small_and_furry:
                mPreference.setAnimalType(AnimalType.SMALLFURRY);
                break;
            default:
                mPreference.setAnimalType(AnimalType.ALL);
                break;
        }
    }

    private void clear() {
        mTypeAllRadioButton.setChecked(true);
        mPreference.setAnimalType(AnimalType.ALL);
    }

    private void updateUI() {
        updateAnimalType();
    }

    private void updateAnimalType() {
        switch (mPreference.getAnimalType()) {
            case ALL:
                mTypeAllRadioButton.setChecked(true);
                break;
            case BARNYARD:
                mTypeBarnyardRadioButton.setChecked(true);
                break;
            case BIRD:
                mTypeBirdRadioButton.setChecked(true);
                break;
            case CAT:
                mTypeCatRadioButton.setChecked(true);
                break;
            case DOG:
                mTypeDogRadioButton.setChecked(true);
                break;
            case HORSE:
                mTypeHorseRadioButton.setChecked(true);
                break;
            case PIG:
                mTypePigRadioButton.setChecked(true);
                break;
            case REPTILE:
                mTypeReptileRadioButton.setChecked(true);
                break;
            case SMALLFURRY:
                mTypeSmallAndFurryRadioButton.setChecked(true);
                break;
            default:
                break;
        }
    }

    private void loadPreference() {
        mPreference = mPetfinderServiceManager.getPreference();
    }

    private void savePreference() {
        mPreference.savePreference(getActivity());
    }

}