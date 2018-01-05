package com.rock.debitdiver.Tutorials;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rock.debitdiver.R;


public class FollowMe extends TutorialBaseFragment {

    public FollowMe() {
        // Required empty public constructor
    }

    public static FollowMe newInstance() {
        FollowMe fragment = new FollowMe();
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
        View rootView = inflater.inflate(R.layout.fragment_follow_me, container, false);
        return rootView;
    }

}
