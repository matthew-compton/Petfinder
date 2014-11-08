package com.ambergleam.petfinder.model;

import android.text.Html;
import android.text.Spanned;

import com.ambergleam.petfinder.controller.MainFragment;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Pet implements Serializable, Comparable {

    @SerializedName("id")
    public Id mId;

    @SerializedName("status")
    public Status mStatus;

    @SerializedName("description")
    public Description mDescription;

    @SerializedName("sex")
    public Gender mGender;

    @SerializedName("age")
    public Age mAge;

    @SerializedName("size")
    public Size mSize;

    @SerializedName("mix")
    public Mix mMix;

    @SerializedName("name")
    public Name mName;

    @SerializedName("animal")
    public Animal mAnimal;

    @SerializedName("media")
    public Media mMedia;

    @SerializedName("contact")
    public Contact mContact;

    @SerializedName("lastUpdate")
    public LastUpdate mLastUpdate;

    public String getCoverPhotoUrl() {
        if (mMedia != null && mMedia.mPhotos != null) {
            return mMedia.mPhotos.mPhotos[MainFragment.IMAGE_INDEX_INITIAL].mPhotoUrl;
        }
        return null;
    }

    public String toString() {
        return new StringBuilder()
                .append(mName.mString).append(", ")
                .append(mAnimal.mString)
                .toString();
    }

    public Spanned toHtml() {
        return Html.fromHtml(new StringBuilder()
                .append(mName.mString)
                .append(" (<a href=\"").append(this.getCoverPhotoUrl()).append("\">picture</a>) needs a home!")
                .append("<br/>")
                .append("<br/>")
                .append("For more information on how to adopt this cutie, try the following:")
                .append("<br/>")
                .append("Email - <a href=\"mailto:")
                .append(mContact.mEmail)
                .append("\">")
                .append(mContact.mEmail)
                .append("</a>")
                .append("<br/>")
                .append("Phone - <a href=\"tel:")
                .append(mContact.mPhone)
                .append("\">")
                .append(mContact.mPhone)
                .append("</a>")
                .toString());
    }

    @Override
    public int compareTo(Object another) {
        if (!(another instanceof Pet)) {
            return 1;
        }
        Pet pet = (Pet) another;
        return this.mName.toString().compareToIgnoreCase(pet.mName.toString());
    }

}