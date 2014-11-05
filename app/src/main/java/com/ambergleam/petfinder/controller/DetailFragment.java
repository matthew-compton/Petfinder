package com.ambergleam.petfinder.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ambergleam.petfinder.R;
import com.ambergleam.petfinder.model.Pet;
import com.ambergleam.petfinder.service.PetfinderServiceManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DetailFragment extends BaseFragment {

    private static final String TAG = DetailFragment.class.getSimpleName();

    public static final String EXTRA_PET = TAG + "EXTRA_PET";

    @InjectView(R.id.layout_name) LinearLayout mNameLayout;
    @InjectView(R.id.pet_name) TextView mNameTextView;

    @InjectView(R.id.layout_animal) LinearLayout mAnimalLayout;
    @InjectView(R.id.pet_animal) TextView mAnimalTextView;

    @InjectView(R.id.layout_gender) LinearLayout mGenderLayout;
    @InjectView(R.id.pet_gender) TextView mGenderTextView;

    @InjectView(R.id.layout_status) LinearLayout mStatusLayout;
    @InjectView(R.id.pet_status) TextView mStatusTextView;

    @InjectView(R.id.layout_age) LinearLayout mAgeLayout;
    @InjectView(R.id.pet_age) TextView mAgeTextView;

    @InjectView(R.id.layout_size) LinearLayout mSizeLayout;
    @InjectView(R.id.pet_size) TextView mSizeTextView;

    @InjectView(R.id.layout_phone) LinearLayout mPhoneLayout;
    @InjectView(R.id.pet_phone) TextView mPhoneTextView;
    @InjectView(R.id.image_phone) ImageView mPhoneImageView;

    @InjectView(R.id.layout_email) LinearLayout mEmailLayout;
    @InjectView(R.id.pet_email) TextView mEmailTextView;
    @InjectView(R.id.image_email) ImageView mEmailImageView;

    @InjectView(R.id.layout_address) LinearLayout mAddressLayout;
    @InjectView(R.id.pet_address) TextView mAddressTextView;
    @InjectView(R.id.image_phone) ImageView mAddressImageView;

    @InjectView(R.id.layout_description) LinearLayout mDescriptionLayout;
    @InjectView(R.id.pet_description) TextView mDescriptionTextView;

    @InjectView(R.id.layout_last_updated) LinearLayout mLastUpdatedLayout;
    @InjectView(R.id.pet_last_updated) TextView mLastUpdatedTextView;

    @Inject PetfinderServiceManager mPetfinderServiceManager;

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
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem itemFavorite = menu.findItem(R.id.favorite);
        MenuItem itemUnfavorite = menu.findItem(R.id.unfavorite);
        if (isFavorite()) {
            itemFavorite.setVisible(false);
            itemUnfavorite.setVisible(true);
        } else {
            itemFavorite.setVisible(true);
            itemUnfavorite.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                share();
                break;
            case R.id.favorite:
                favorite();
                break;
            case R.id.unfavorite:
                unfavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Adopt a Homeless Pet!");
        intent.putExtra(Intent.EXTRA_TEXT, mPet.toHtml());
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_using)));
    }

    private boolean isFavorite() {
        return mPetfinderServiceManager.getPetfinderPreference().isFavorite(mPet.mId.toString());
    }

    private void favorite() {
        mPetfinderServiceManager.getPetfinderPreference().savePreference(getActivity());
        mPetfinderServiceManager.getPetfinderPreference().addFavorite(mPet.mId.toString());
        getActivity().invalidateOptionsMenu();
    }

    private void unfavorite() {
        mPetfinderServiceManager.getPetfinderPreference().savePreference(getActivity());
        mPetfinderServiceManager.getPetfinderPreference().removeFavorite(mPet.mId.toString());
        getActivity().invalidateOptionsMenu();
    }

    @OnClick(R.id.image_phone)
    public void onClickPhone() {
        launchPhone();
    }

    @OnClick(R.id.image_email)
    public void onClickEmail() {
        launchEmail();
    }

    @OnClick(R.id.image_address)
    public void onClickAddress() {
        launchMap();
    }

    private void launchPhone() {
        String number = mPhoneTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void launchEmail() {
        String recipient = mEmailTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{recipient});
        startActivity(intent);
    }

    private void launchMap() {
        String address = mAddressTextView.getText().toString();
        String uri = "https://maps.google.com/maps?f=d&daddr=" + address;
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(i);
    }

    private void updateUI() {
        updateName();
        updateAnimal();
        updateGender();
        updateStatus();
        updateAge();
        updateSize();
        updatePhone();
        updateEmail();
        updateAddress();
        updateDescription();
        updateLastUpdated();
    }

    private void updateName() {
        if (mPet.mName.mString != null) {
            mNameLayout.setVisibility(View.VISIBLE);
            mNameTextView.setText(mPet.mName.mString);
        } else {
            mNameLayout.setVisibility(View.GONE);
        }
    }

    private void updateAnimal() {
        if (mPet.mAnimal.mString != null && !mPet.mAnimal.toString().equals("Any")) {
            mAnimalLayout.setVisibility(View.VISIBLE);
            mAnimalTextView.setText(mPet.mAnimal.toString());
        } else {
            mAnimalLayout.setVisibility(View.GONE);
        }
    }

    private void updateGender() {
        if (mPet.mGender.mString != null && !mPet.mGender.toString().equals("Any")) {
            mGenderLayout.setVisibility(View.VISIBLE);
            mGenderTextView.setText(mPet.mGender.toString());
        } else {
            mGenderLayout.setVisibility(View.GONE);
        }
    }

    private void updateStatus() {
        if (mPet.mStatus.mString != null && !mPet.mStatus.toString().equals("Any")) {
            mStatusLayout.setVisibility(View.VISIBLE);
            mStatusTextView.setText(mPet.mStatus.toString());
        } else {
            mStatusLayout.setVisibility(View.GONE);
        }
    }

    private void updateAge() {
        if (mPet.mAge.mString != null && !mPet.mAge.toString().equals("Any")) {
            mAgeLayout.setVisibility(View.VISIBLE);
            mAgeTextView.setText(mPet.mAge.toString());
        } else {
            mAgeLayout.setVisibility(View.GONE);
        }
    }

    private void updateSize() {
        if (mPet.mSize.mString != null && !mPet.mSize.toString().equals("Any")) {
            mSizeLayout.setVisibility(View.VISIBLE);
            mSizeTextView.setText(mPet.mSize.toString());
        } else {
            mSizeLayout.setVisibility(View.GONE);
        }
    }

    private void updatePhone() {
        if (mPet.mContact.mPhone.mString != null && !mPet.mContact.mPhone.mString.equals("")) {
            mPhoneLayout.setVisibility(View.VISIBLE);
            mPhoneTextView.setText(mPet.mContact.mPhone.mString);
        } else {
            mPhoneLayout.setVisibility(View.GONE);
        }
    }

    private void updateEmail() {
        if (mPet.mContact.mEmail.mString != null && !mPet.mContact.mEmail.mString.equals("")) {
            mEmailLayout.setVisibility(View.VISIBLE);
            mEmailTextView.setText(mPet.mContact.mEmail.mString);
        } else {
            mEmailLayout.setVisibility(View.GONE);
        }
    }

    private void updateAddress() {
        if (mPet.mContact != null && mPet.mContact.isAddressValid()) {
            mAddressLayout.setVisibility(View.VISIBLE);
            mAddressTextView.setText(mPet.mContact.getAddressString());
        } else {
            mAddressLayout.setVisibility(View.GONE);
        }
    }

    private void updateDescription() {
        if (mPet.mDescription.mString != null && !mPet.mDescription.mString.equals("")) {
            mDescriptionLayout.setVisibility(View.VISIBLE);
            mDescriptionTextView.setText(mPet.mDescription.mString);
        } else {
            mDescriptionLayout.setVisibility(View.GONE);
        }
    }

    private void updateLastUpdated() {
        if (mPet.mLastUpdate.mString != null && !mPet.mLastUpdate.toString().equals("")) {
            mLastUpdatedLayout.setVisibility(View.VISIBLE);
            mLastUpdatedTextView.setText(mPet.mLastUpdate.toString());
        } else {
            mLastUpdatedLayout.setVisibility(View.GONE);
        }
    }

}
