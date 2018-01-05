package com.rock.debitdiver.Register;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rock.debitdiver.ApiUtils.ApiCallWrapper;
import com.rock.debitdiver.ApiUtils.AsyncTaskCallback;
import com.rock.debitdiver.ApiUtils.ServerURLs;
import com.rock.debitdiver.R;
import com.rock.debitdiver.RegisterActivity;
import com.rock.debitdiver.TutorialActivity;
import com.rock.debitdiver.Utils.StringCheckUtil;

import org.json.JSONObject;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;

public class RegisterProfile extends RegisterBaseFragment {
    Button btSubmit;
    EditText userName;
    public RegisterProfile() {
        // Required empty public constructor
    }

    public static RegisterProfile newInstance() {
        RegisterProfile fragment = new RegisterProfile();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register_profile, container, false);
        userName = rootView.findViewById(R.id.txtUserName);
        btSubmit = rootView.findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSubmit();
            }
        });
        return rootView;
    }

    private void OnSubmit() {
        if (StringCheckUtil.isEmpty(getContext(), userName)) {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(userName);
            userName.setError(getString(R.string.required_field));
            return;
        }
        registerToServer();

    }

    private void registerToServer() {
        ContentValues headerValues = new ContentValues();
        ContentValues values = new ContentValues();
        values.put("EMAIL", ParentActivity.email);
        values.put("PASS", ParentActivity.password);
        values.put("NAME", userName.getText().toString());
        ApiCallWrapper service = new ApiCallWrapper(ParentActivity, POST, true, "Registering", ServerURLs.REGISTER_URL, headerValues, values, new AsyncTaskCallback() {
            @Override
            public void onResultService(Object result) {
                try
                {
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        Intent tutorial = new Intent(getContext(), TutorialActivity.class);
                        startActivity(tutorial);
                        ParentActivity.finish();
                    }
                    else{
                        Toast.makeText(ParentActivity, returnValue.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(ParentActivity, getString(R.string.error_on_server), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        service.execute();
    }
}
