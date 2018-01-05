package com.rock.debitdiver.Register;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.rock.debitdiver.RegisterActivity;


/**
 * Created by rock on 12/13/17.
 */

public class RegisterBaseFragment extends Fragment{
    RegisterActivity ParentActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ParentActivity = (RegisterActivity)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ParentActivity = null;
    }
}
