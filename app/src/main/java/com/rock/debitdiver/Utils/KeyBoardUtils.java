package com.rock.debitdiver.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by rock on 12/16/17.
 */

public class KeyBoardUtils {
    public static void HideKeyBoard(Context context, View view)
    {
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
