package com.rock.debitdiver.Utils;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by rock on 2/8/2017.
 */

public class StringCheckUtil {
    public static boolean isEmpty(Context context, EditText txtEdit){
        return txtEdit.getText().toString().trim().isEmpty();
    }

    public static boolean isEqual(EditText edit1, EditText edit2){
        return (edit1.getText().toString().equals(edit2.getText().toString()));
    }

    public static boolean isLength(EditText edit, int length){
        return (edit.getText().toString().length() < length);
    }

    public static boolean validEmail(EditText edit){
        boolean result = android.util.Patterns.EMAIL_ADDRESS.matcher(edit.getText().toString()).matches();
        return result;
    }
}
