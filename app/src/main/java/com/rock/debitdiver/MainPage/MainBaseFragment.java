package com.rock.debitdiver.MainPage;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.rock.debitdiver.MainActivity;

/**
 * Created by rock on 12/15/17.
 */

public class MainBaseFragment extends Fragment {
    protected MainActivity parentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parentActivity  = null;
    }
}
