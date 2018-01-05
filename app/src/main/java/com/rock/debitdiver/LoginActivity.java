package com.rock.debitdiver;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rock.debitdiver.ApiUtils.ApiCallWrapper;
import com.rock.debitdiver.ApiUtils.AsyncTaskCallback;
import com.rock.debitdiver.ApiUtils.ServerURLs;
import com.rock.debitdiver.Utils.MPreferenceManager;
import com.rock.debitdiver.Utils.StringCheckUtil;

import org.json.JSONObject;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;

public class LoginActivity extends AppCompatActivity {

    TextView txtTitle;
    ImageView imgAvatar;
    TextInputLayout txtEmail, txtPassword;
    Button btLogin, btSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtTitle = findViewById(R.id.txtTitle);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btLogin = findViewById(R.id.btLogin);
        btSignup = findViewById(R.id.btSignup);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnLoginClick();
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSignupClick();
            }
        });

        YoYo.with(Techniques.FlipInX)
                .duration(200)
                .playOn(txtTitle);
    }

    private void OnSignupClick() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    private void OnLoginClick() {
        if(StringCheckUtil.isEmpty(this, txtEmail.getEditText()))
        {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(txtEmail);
            txtEmail.getEditText().setError(getString(R.string.required_field));
            return;
        }

        if(StringCheckUtil.isEmpty(this, txtPassword.getEditText()))
        {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(txtPassword);
            txtPassword.getEditText().setError(getString(R.string.required_field));
            return;
        }

        if(!StringCheckUtil.validEmail(txtEmail.getEditText()))
        {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(txtEmail);
            txtEmail.getEditText().setError("Invalid email address");
            return;
        }
        loginWithEmail();
    }

    private void loginWithEmail() {
        ContentValues headerValues = new ContentValues();
        ContentValues values = new ContentValues();
        values.put("EMAIL", txtEmail.getEditText().getText().toString());
        values.put("PASS", txtPassword.getEditText().getText().toString());
        ApiCallWrapper service = new ApiCallWrapper(this, POST, true, "Login", ServerURLs.LOGIN_URL, headerValues, values, new AsyncTaskCallback() {
            @Override
            public void onResultService(Object result) {
                try
                {
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        JSONObject msg = new JSONObject(returnValue.getString("msg"));
                        String token = msg.getString("TOKEN");
                        String email = msg.getString("EMAIL");
                        String name = msg.getString("NAME");
                        MPreferenceManager.saveStringInformation(LoginActivity.this, MPreferenceManager.TOKEN, token);
                        MPreferenceManager.saveStringInformation(LoginActivity.this, MPreferenceManager.EMAIL, email);
                        MPreferenceManager.saveStringInformation(LoginActivity.this, MPreferenceManager.NAME, name);
                        Intent main = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(main);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, returnValue.getString("msg"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(LoginActivity.this, getString(R.string.error_on_server), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        service.execute();
    }
}