package com.ambergleam.petfinder.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ambergleam.petfinder.PetfinderPreference;
import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Animal;
import com.ambergleam.petfinder.model.Location;
import com.ambergleam.petfinder.model.Size;
import com.ambergleam.petfinder.model.State;
import com.ambergleam.petfinder.service.PetfinderServiceManager;
import com.ambergleam.petfinder.utils.LocationUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

public class SettingsFragment extends BaseFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    public static final String EXTRA_CHANGED = "EXTRA_CHANGED";

    @InjectView(R.id.fragment_settings_animal_spinner) Spinner mAnimalSpinner;
    private ArrayAdapter<Animal.AnimalEnum> mAnimalArrayAdapter;

    @InjectView(R.id.fragment_settings_size_spinner) Spinner mSizeSpinner;
    private ArrayAdapter<Size.SizeEnum> mSizeArrayAdapter;

    @InjectView(R.id.fragment_settings_location_spinner) Spinner mLocationSpinner;
    private ArrayAdapter<Location.LocationEnum> mLocationArrayAdapter;

    @InjectView(R.id.fragment_settings_state_layout) LinearLayout mStateLayout;
    @InjectView(R.id.fragment_settings_state_spinner) Spinner mStateSpinner;
    private ArrayAdapter<State.StateEnum> mStateArrayAdapter;

    @InjectView(R.id.fragment_settings_zip_layout) LinearLayout mZipLayout;
    @InjectView(R.id.fragment_settings_zip_edittext) EditText mZipEditText;

    @Inject PetfinderServiceManager mPetfinderServiceManager;
    private PetfinderPreference mPetfinderPreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, layout);
        setHasOptionsMenu(true);

        mAnimalArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, Animal.AnimalEnum.values());
        mAnimalArrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        mAnimalSpinner.setAdapter(mAnimalArrayAdapter);

        mSizeArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, Size.SizeEnum.values());
        mSizeArrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        mSizeSpinner.setAdapter(mSizeArrayAdapter);

        mLocationArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, Location.LocationEnum.values());
        mLocationArrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        mLocationSpinner.setAdapter(mLocationArrayAdapter);

        mStateArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, State.StateEnum.values());
        mStateArrayAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        mStateSpinner.setAdapter(mStateArrayAdapter);

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
            case R.id.menu_settings_clear:
                clearSettings();
                break;
            case R.id.menu_settings_location:
                useMyLocation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearSettings() {
        mAnimalSpinner.setSelection(0);
        mSizeSpinner.setSelection(0);
        mLocationSpinner.setSelection(0);
        mStateSpinner.setSelection(0);
        mZipEditText.setText("");
        updatePreference();
    }

    public void useMyLocation() {
        android.location.Location location = LocationUtils.getLocation(getActivity());
        String zip = LocationUtils.getZipCodeString(getActivity(), location);
        String state = LocationUtils.getStateString(getActivity(), location);
        int selection = State.StateEnum.fromUrlFormatString(state).ordinal();
        mZipEditText.setText(zip);
        mStateSpinner.setSelection(selection);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreference();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreference();
    }

    private void updateUI() {
        updateViewData();
        updateViewVisibility();
    }

    private void updateViewData() {
        mAnimalSpinner.setSelection(mAnimalArrayAdapter.getPosition(mPetfinderPreference.getAnimalEnum()));
        mSizeSpinner.setSelection(mSizeArrayAdapter.getPosition(mPetfinderPreference.getSizeEnum()));
        mLocationSpinner.setSelection(mLocationArrayAdapter.getPosition(mPetfinderPreference.getLocationEnum()));
        mStateSpinner.setSelection(mStateArrayAdapter.getPosition(mPetfinderPreference.getStateEnum()));
        mZipEditText.setText(mPetfinderPreference.getZipString());
    }

    private void updateViewVisibility() {
        switch (mPetfinderPreference.getLocationEnum()) {
            case STATE:
                mStateLayout.setVisibility(View.VISIBLE);
                mZipLayout.setVisibility(View.GONE);
                break;
            case ZIP:
                mStateLayout.setVisibility(View.GONE);
                mZipLayout.setVisibility(View.VISIBLE);
                break;
            case ANY:
            default:
                mStateLayout.setVisibility(View.GONE);
                mZipLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void loadPreference() {
        mPetfinderPreference = new PetfinderPreference();
        mPetfinderPreference.setAnimalEnum(mPetfinderServiceManager.getPetfinderPreference().getAnimalEnum());
        mPetfinderPreference.setSizeEnum(mPetfinderServiceManager.getPetfinderPreference().getSizeEnum());
        mPetfinderPreference.setLocationEnum(mPetfinderServiceManager.getPetfinderPreference().getLocationEnum());
        mPetfinderPreference.setStateEnum(mPetfinderServiceManager.getPetfinderPreference().getStateEnum());
        mPetfinderPreference.setZipString(mPetfinderServiceManager.getPetfinderPreference().getZipString());
    }

    private void savePreference() {
        boolean hasChanged = mPetfinderPreference.isDifferentFrom(mPetfinderServiceManager.getPetfinderPreference());
        if (hasChanged) {
            mPetfinderServiceManager.setPetfinderPreference(mPetfinderPreference);
            mPetfinderPreference.savePreference(getActivity());
        }
    }

    private void updatePreference() {
        boolean hasChanged = mPetfinderPreference.isDifferentFrom(mPetfinderServiceManager.getPetfinderPreference());
        if (hasChanged) {
            Intent i = new Intent();
            i.putExtra(EXTRA_CHANGED, hasChanged);
            getActivity().setResult(Activity.RESULT_OK, i);
        } else {
            getActivity().setResult(Activity.RESULT_CANCELED);
        }
    }

    @OnItemSelected(R.id.fragment_settings_animal_spinner)
    public void onItemSelectedAnimal() {
        Animal.AnimalEnum animal = (Animal.AnimalEnum) mAnimalSpinner.getSelectedItem();
        mPetfinderPreference.setAnimalEnum(animal);
        updatePreference();
    }

    @OnItemSelected(R.id.fragment_settings_size_spinner)
    public void onItemSelectedSize() {
        Size.SizeEnum animal = (Size.SizeEnum) mSizeSpinner.getSelectedItem();
        mPetfinderPreference.setSizeEnum(animal);
        updatePreference();
    }

    @OnItemSelected(R.id.fragment_settings_location_spinner)
    public void onItemSelectedLocation() {
        Location.LocationEnum locationEnum = (Location.LocationEnum) mLocationSpinner.getSelectedItem();
        mPetfinderPreference.setLocationEnum(locationEnum);
        updatePreference();
        updateViewVisibility();
    }

    @OnItemSelected(R.id.fragment_settings_state_spinner)
    public void onItemSelectedState() {
        State.StateEnum stateEnum = (State.StateEnum) mStateSpinner.getSelectedItem();
        mPetfinderPreference.setStateEnum(stateEnum);
        updatePreference();
    }

    @OnTextChanged(R.id.fragment_settings_zip_edittext)
    public void onTextChangedZip() {
        if (mPetfinderPreference != null) {
            String zipString = mZipEditText.getText().toString();
            mPetfinderPreference.setZipString(zipString);
            updatePreference();
        }
    }

}