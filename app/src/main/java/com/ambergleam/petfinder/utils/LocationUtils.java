package com.ambergleam.petfinder.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    private static final String TAG = LocationUtils.class.getSimpleName();

    public static Location getLocation(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location networkLocation = null;
        Location gpsLocation = null;
        if (gps_enabled) {
            gpsLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (network_enabled) {
            networkLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (gpsLocation != null && networkLocation != null) {
            if (gpsLocation.getAccuracy() >= networkLocation.getAccuracy()) {
                return gpsLocation;
            } else {
                return networkLocation;
            }
        } else {
            return ((gpsLocation != null) ? gpsLocation : networkLocation);
        }
    }

    public static String getZipCodeString(Context context, Location location) {
        String zip = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            zip = addresses.get(0).getPostalCode();
        } catch (NullPointerException e) {
            Log.e(TAG, "Could not retrieve location.", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not retrieve location.", e);
        }
        return zip;
    }

    public static String getStateString(Context context, Location location) {
        String state = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(1);
            String[] str = address.split(" ");
            state = str[str.length - 2];
        } catch (NullPointerException e) {
            Log.e(TAG, "Could not retrieve location.", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not retrieve location.", e);
        }
        return state;
    }

}
