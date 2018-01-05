package com.rock.debitdiver.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by michalejackson on 1/4/18.
 */

public class DialogUtil {
    public static void showWarningDialog(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setTitle("Attention")
                .setMessage(msg)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }
}
