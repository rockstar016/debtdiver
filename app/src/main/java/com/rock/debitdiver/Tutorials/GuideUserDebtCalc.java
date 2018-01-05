package com.rock.debitdiver.Tutorials;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rock.debitdiver.R;


public class GuideUserDebtCalc extends Fragment {

    public GuideUserDebtCalc() {
        // Required empty public constructor
    }

    public static GuideUserDebtCalc newInstance() {
        GuideUserDebtCalc fragment = new GuideUserDebtCalc();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_guide_user_debt_calc, container, false);
        return rootView;
    }

}
