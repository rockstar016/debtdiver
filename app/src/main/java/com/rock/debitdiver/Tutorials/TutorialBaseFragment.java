package com.rock.debitdiver.Tutorials;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.rock.debitdiver.TutorialActivity;

/**
 * Created by rock on 12/13/17.
 */

public class TutorialBaseFragment extends Fragment {
    TutorialActivity ParentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ParentActivity = (TutorialActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ParentActivity = null;
    }
}
