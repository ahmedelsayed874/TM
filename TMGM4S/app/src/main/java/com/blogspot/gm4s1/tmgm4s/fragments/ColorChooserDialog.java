package com.blogspot.gm4s1.tmgm4s.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.blogspot.gm4s1.tmgm4s.R;

/**
 * Created by GloryMaker on 12/18/2016.
 */

public class ColorChooserDialog extends DialogFragment {
    private ItemChoosed _itemChoosed;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Color")
                .setItems(R.array.colors_names_preference, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if (_itemChoosed != null) _itemChoosed.onItemChoosed(which);
                    }
                });
        return builder.create();
    }

    public void setOnItemChoosedListener(ItemChoosed itemChoosed) {
        _itemChoosed = itemChoosed;
    }


    //------------------------------------------------------------//
    public interface ItemChoosed {
        void onItemChoosed(int idx);
    }
}
