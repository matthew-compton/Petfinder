package com.ambergleam.petfinder.utils;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class DialogUtils {

    private static final String TAG = DialogUtils.class.getSimpleName();

    public static void showLoadingDialog(FragmentManager fragmentManager, boolean cancelable) {
        if (fragmentManager == null) {
            return;
        }

        Fragment existingDialogFragment = fragmentManager.findFragmentByTag(TAG);
        boolean dialogExists = existingDialogFragment != null;

        DialogFragment updatedDialogFragment = ProgressDialogFragment.newInstance();
        updatedDialogFragment.setCancelable(cancelable);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(updatedDialogFragment, TAG);
        if (dialogExists) {
            transaction.remove(existingDialogFragment);
        }
        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static void hideLoadingDialog(FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return;
        }

        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment instanceof DialogFragment) {
            ((DialogFragment) fragment).dismissAllowingStateLoss();
        }
    }

}
