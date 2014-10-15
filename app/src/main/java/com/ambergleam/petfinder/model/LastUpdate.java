package com.ambergleam.petfinder.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LastUpdate implements Serializable {

    private static final String TAG = LastUpdate.class.getSimpleName();

    /*
     * Example string:
     * 2014-09-25T22:06:36Z
     */
    @SerializedName("$t")
    public String mString;

    private String mDateString = null;

    public String toString() {
        if (mDateString == null) {
            String original = mString.split("T")[0];
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
            originalFormat.setLenient(false);
            SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");
            newFormat.setLenient(false);
            Date date = null;
            try {
                date = originalFormat.parse(original);
            } catch (ParseException e) {
                Log.e(TAG, "There was a problem parsing the date.", e);
            }
            if (date != null) {
                mDateString = newFormat.format(date);
            } else {
                mDateString = "";
            }
        }
        return mDateString;
    }

}