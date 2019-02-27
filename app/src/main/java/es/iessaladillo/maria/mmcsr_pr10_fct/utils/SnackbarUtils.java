package es.iessaladillo.maria.mmcsr_pr10_fct.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {
    private SnackbarUtils(){ }

    public static void snackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}