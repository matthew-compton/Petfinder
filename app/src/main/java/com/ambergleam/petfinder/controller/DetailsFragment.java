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

public class DetailsFragment extends BaseFragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();

    public static final String EXTRA_PET = TAG + "EXTRA_PET";

    @InjectView(R.id.fragment_details_name_layout) LinearLayout mNameLayout;
    @InjectView(R.id.fragment_details_name_text) TextView mNameTextView;

    @InjectView(R.id.fragment_details_animal_layout) LinearLayout mAnimalLayout;
    @InjectView(R.id.fragment_details_animal_text) TextView mAnimalTextView;

    @InjectView(R.id.fragment_details_gender_layout) LinearLayout mGenderLayout;
    @InjectView(R.id.fragment_details_gender_text) TextView mGenderTextView;

    @InjectView(R.id.fragment_details_status_layout) LinearLayout mStatusLayout;
    @InjectView(R.id.fragment_details_status_text) TextView mStatusTextView;

    @InjectView(R.id.fragment_details_age_layout) LinearLayout mAgeLayout;
    @InjectView(R.id.fragment_details_age_text) TextView mAgeTextView;

    @InjectView(R.id.fragment_details_size_layout) LinearLayout mSizeLayout;
    @InjectView(R.id.fragment_details_size_text) TextView mSizeTextView;

    @InjectView(R.id.fragment_details_phone_layout) LinearLayout mPhoneLayout;
    @InjectView(R.id.fragment_details_phone_text) TextView mPhoneTextView;
    @InjectView(R.id.fragment_details_phone_image) ImageView mPhoneImageView;

    @InjectView(R.id.fragment_details_email_layout) LinearLayout mEmailLayout;
    @InjectView(R.id.fragment_details_email_text) TextView mEmailTextView;
    @InjectView(R.id.fragment_details_email_image) ImageView mEmailImageView;

    @InjectView(R.id.fragment_details_address_layout) LinearLayout mAddressLayout;
    @InjectView(R.id.fragment_details_address_text) TextView mAddressTextView;
    @InjectView(R.id.fragment_details_address_image) ImageView mAddressImageView;

    @InjectView(R.id.fragment_details_description_layout) LinearLayout mDescriptionLayout;
    @InjectView(R.id.fragment_details_description_text) TextView mDescriptionTextView;

    @InjectView(R.id.fragment_details_updated_layout) LinearLayout mUpdatedLayout;
    @InjectView(R.id.fragment_details_updated_text) TextView mUpdatedTextView;

    @Inject PetfinderServiceManager mPetfinderServiceManager;

    private Pet mPet;

    public static DetailsFragment newInstance(Pet pet) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PET, pet);

        DetailsFragment fragment = new DetailsFragment();
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
        View layout = inflater.inflate(R.layout.fragment_details, container, false);
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
        boolean isPetLiked = isPetLiked();
        MenuItem itemLike = menu.findItem(R.id.menu_details_like);
        itemLike.setVisible(!isPetLiked);
        MenuItem itemDislike = menu.findItem(R.id.menu_details_dislike);
        itemDislike.setVisible(isPetLiked);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_details_share:
                sharePet();
                break;
            case R.id.menu_details_like:
                likePet();
                break;
            case R.id.menu_details_dislike:
                dislikePet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sharePet() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.fragment_details_share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, mPet.toHtml());
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.fragment_details_share_using)));
    }

    private void likePet() {
        mPetfinderServiceManager.getPetfinderPreference().savePreference(getActivity());
        mPetfinderServiceManager.getPetfinderPreference().addFavorite(mPet.mId.toString());
        getActivity().invalidateOptionsMenu();
    }

    private void dislikePet() {
        mPetfinderServiceManager.getPetfinderPreference().savePreference(getActivity());
        mPetfinderServiceManager.getPetfinderPreference().removeFavorite(mPet.mId.toString());
        getActivity().invalidateOptionsMenu();
    }

    private boolean isPetLiked() {
        return mPetfinderServiceManager.getPetfinderPreference().isFavorite(mPet.mId.toString());
    }

    @OnClick(R.id.fragment_details_phone_image)
    public void onClickPhone() {
        launchPhone();
    }

    @OnClick(R.id.fragment_details_email_image)
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
            mUpdatedLayout.setVisibility(View.VISIBLE);
            mUpdatedTextView.setText(mPet.mLastUpdate.toString());
        } else {
            mUpdatedLayout.setVisibility(View.GONE);
        }
    }

}
