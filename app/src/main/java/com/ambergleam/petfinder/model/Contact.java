package com.ambergleam.petfinder.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contact implements Serializable {

    @SerializedName("email")
    public Email mEmail;

    @SerializedName("zip")
    public Zip mZip;

    @SerializedName("city")
    public City mCity;

    @SerializedName("fax")
    public Fax mFax;

    @SerializedName("address1")
    public Address mAddress;

    @SerializedName("phone")
    public Phone mPhone;

    @SerializedName("state")
    public State mState;

    public String getAddressString() {
        return new StringBuilder()
                .append(mAddress).append("\n")
                .append(mCity).append(", ").append(mState).append(" ").append(mZip)
                .toString();
    }

    public boolean isAddressValid() {
        return mAddress.mString != null && mCity.mString != null && mState.mString != null && mZip.mString != null;
    }

}
