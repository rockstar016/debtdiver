package com.rock.debitdiver.Tutorials;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rock.debitdiver.R;

public class SinkingDebit extends TutorialBaseFragment {

    public SinkingDebit() {
        // Required empty public constructor
    }

    public static SinkingDebit newInstance() {
        SinkingDebit fragment = new SinkingDebit();
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
        View rootView = inflater.inflate(R.layout.fragment_sinking_debit, container, false);
        return rootView;
    }

}
