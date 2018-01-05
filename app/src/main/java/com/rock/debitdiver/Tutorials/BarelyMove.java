package com.rock.debitdiver.Tutorials;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rock.debitdiver.R;

public class BarelyMove extends TutorialBaseFragment {

    public BarelyMove() {
        // Required empty public constructor
    }

    public static BarelyMove newInstance() {
        BarelyMove fragment = new BarelyMove();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_barely_move, container, false);
        return rootView;
    }

}
