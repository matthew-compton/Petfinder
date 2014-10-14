package com.ambergleam.petfinder.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Animal;
import com.ambergleam.petfinder.PetfinderPreference;
import com.ambergleam.petfinder.model.Gender;
import com.ambergleam.petfinder.model.Size;
import com.ambergleam.petfinder.service.PetfinderServiceManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;

public class SettingsFragment extends BaseFragment {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @Inject PetfinderServiceManager mPetfinderServiceManager;
    private PetfinderPreference mPetfinderPreference;

    @InjectView(R.id.spinner_animal) Spinner mAnimalSpinner;
    private ArrayAdapter<Animal.AnimalEnum> mAnimalArrayAdapter;

    @InjectView(R.id.spinner_gender) Spinner mGenderSpinner;
    private ArrayAdapter<Gender.GenderEnum> mGenderArrayAdapter;

    @InjectView(R.id.spinner_size) Spinner mSizeSpinner;
    private ArrayAdapter<Size.SizeEnum> mSizeArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, layout);
        setHasOptionsMenu(true);

        mAnimalArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Animal.AnimalEnum.values());
        mAnimalArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAnimalSpinner.setAdapter(mAnimalArrayAdapter);

        mGenderArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Gender.GenderEnum.values());
        mGenderArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenderSpinner.setAdapter(mGenderArrayAdapter);

        mSizeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Size.SizeEnum.values());
        mSizeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSizeSpinner.setAdapter(mSizeArrayAdapter);

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

    private void updateUI() {
        mAnimalSpinner.setSelection(mAnimalArrayAdapter.getPosition(mPetfinderPreference.getAnimalEnum()));
        mGenderSpinner.setSelection(mGenderArrayAdapter.getPosition(mPetfinderPreference.getGenderEnum()));
        mSizeSpinner.setSelection(mSizeArrayAdapter.getPosition(mPetfinderPreference.getSizeEnum()));
    }

    private void clear() {
        mAnimalSpinner.setSelection(0);
        mGenderSpinner.setSelection(0);
        mSizeSpinner.setSelection(0);
    }

    private void loadPreference() {
        mPetfinderPreference = mPetfinderServiceManager.getPetfinderPreference();
    }

    private void savePreference() {
        mPetfinderPreference.savePreference(getActivity());
    }

    @OnItemSelected(R.id.spinner_animal)
    public void onItemSelectedAnimal() {
        Animal.AnimalEnum animal = (Animal.AnimalEnum) mAnimalSpinner.getSelectedItem();
        mPetfinderPreference.setAnimalEnum(animal);
    }

    @OnItemSelected(R.id.spinner_gender)
    public void onItemSelectedGender() {
        Gender.GenderEnum gender = (Gender.GenderEnum) mGenderSpinner.getSelectedItem();
        mPetfinderPreference.setGenderEnum(gender);
    }

    @OnItemSelected(R.id.spinner_size)
    public void onItemSelectedSize() {
        Size.SizeEnum animal = (Size.SizeEnum) mSizeSpinner.getSelectedItem();
        mPetfinderPreference.setSizeEnum(animal);
    }

}