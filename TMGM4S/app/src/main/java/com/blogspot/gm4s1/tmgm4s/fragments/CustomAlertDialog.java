package com.blogspot.gm4s1.tmgm4s.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.blogspot.gm4s1.tmgm4s.R;

/**
 * Created by GloryMaker on 12/18/2016.
 */

public class CustomAlertDialog extends DialogFragment {
    private String title = "";
    private String msg = "";
    private DialogInterface.OnClickListener pClickListener;
    private DialogInterface.OnClickListener nClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("Ok", pClickListener);
        if (nClickListener != null) {
            builder.setNegativeButton("Cancel", nClickListener);
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static CustomAlertDialog show(String title, String message, FragmentManager manager,
                                         DialogInterface.OnClickListener pClickListener) {
        CustomAlertDialog d = new CustomAlertDialog();
        d.title = title;
        d.msg = message;
        d.pClickListener = pClickListener;
        d.nClickListener = null;
        d.setCancelable(false);
        d.show(manager, "dialog");

        return d;
    }
    public static CustomAlertDialog show(String title, String message, FragmentManager manager,
                                         DialogInterface.OnClickListener pClickListener,
                                         DialogInterface.OnClickListener nClickListener) {
        CustomAlertDialog d = new CustomAlertDialog();
        d.title = title;
        d.msg = message;
        d.pClickListener = pClickListener;
        d.nClickListener = nClickListener;
        d.setCancelable(false);
        d.show(manager, "dialog");

        return d;
    }
}
