package com.rock.debitdiver.Tutorials;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rock.debitdiver.R;


public class IntroductionDiver extends TutorialBaseFragment {

    public IntroductionDiver() {
        // Required empty public constructor
    }

    public static IntroductionDiver newInstance() {
        IntroductionDiver fragment = new IntroductionDiver();
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
        View rootView = inflater.inflate(R.layout.fragment_introduction_diver, container, false);
        return rootView;
    }

}
