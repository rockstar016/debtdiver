package com.rock.debitdiver.Register;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rock.debitdiver.R;
import com.rock.debitdiver.Utils.StringCheckUtil;


public class RegisterEmail extends RegisterBaseFragment {
    TextInputLayout txtEmail, txtPassword, confirmPassword;
    CheckBox chkApprove;
    Button btVerify;

    boolean hasVerify;
    public RegisterEmail() {
        // Required empty public constructor
    }

    public static RegisterEmail newInstance() {
        RegisterEmail fragment = new RegisterEmail();
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
        View rootView = inflater.inflate(R.layout.fragment_register_email, container, false);
        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtPassword = rootView.findViewById(R.id.txtPassword);
        confirmPassword = rootView.findViewById(R.id.confirmPassword);
//        chkApprove = rootView.findViewById(R.id.chkApprove);
        btVerify = rootView.findViewById(R.id.btVerifyEmail);

        btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickVerify();
            }
        });

//        chkApprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                OnCheckChange(isChecked);
//            }
//        });

        hasVerify = false;

        return rootView;
    }

    private void OnCheckChange(boolean isChecked) {
        hasVerify = isChecked;
        btVerify.setText(isChecked? "Next Page":"VerifyEmail");
    }

    private void OnClickVerify() {
        if(!hasVerify) {
            if (StringCheckUtil.isEmpty(getContext(), txtEmail.getEditText())) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(txtEmail);
                txtEmail.getEditText().setError(getString(R.string.required_field));
                return;
            }

            if (StringCheckUtil.isEmpty(getContext(), txtPassword.getEditText())) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(txtPassword);
                txtPassword.getEditText().setError(getString(R.string.required_field));
                return;
            }

            if (!StringCheckUtil.validEmail(txtEmail.getEditText())) {
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(txtEmail);
                txtEmail.getEditText().setError("Invalid email address");
                return;
            }
            if(!StringCheckUtil.isEqual(confirmPassword.getEditText(), txtPassword.getEditText())){
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(confirmPassword);
                confirmPassword.getEditText().setError("Password don't match");
                return;
            }
            ParentActivity.email = txtEmail.getEditText().getText().toString();
            ParentActivity.password = txtPassword.getEditText().getText().toString();
            ParentActivity.showProfilePage();
        }
        else
        {
            ParentActivity.showProfilePage();
        }
    }
}