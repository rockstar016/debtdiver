package com.rock.debitdiver;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rock.debitdiver.ApiUtils.ApiCallWrapper;
import com.rock.debitdiver.ApiUtils.AsyncTaskCallback;
import com.rock.debitdiver.ApiUtils.ServerURLs;
import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.Utils.DialogUtil;
import com.rock.debitdiver.Utils.MPreferenceManager;
import com.rock.debitdiver.Utils.StringCheckUtil;

import org.json.JSONObject;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;

public class AddDebitActivity extends BaseActivity {
    public static final String IS_EDIT = "IS_EDIT";
    public static final String EDIT_POSITION = "Edit_position";
    int position;
    TextInputLayout txtName, txtAmount, txtPaypalAddress;
    Button btAdd;
    boolean isEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        isEdit = getIntent().getBooleanExtra(IS_EDIT, false);

        txtName = findViewById(R.id.txtName);
        txtAmount = findViewById(R.id.txtAmount);
        txtPaypalAddress = findViewById(R.id.txtPaypal);
        btAdd = findViewById(R.id.btAdd);

        if(isEdit) {
            btAdd.setText("Save");
            getSupportActionBar().setTitle("Edit Debit");
            position = getIntent().getIntExtra(EDIT_POSITION, 0);
            DebtInfo selected_item = getMainApp().debtLists.get(position);
            txtAmount.getEditText().setText(selected_item.getAmount());
            txtPaypalAddress.getEditText().setText(selected_item.getPaypal_address());
            txtName.getEditText().setText(selected_item.getName());
        }
        else {
            btAdd.setText("Add");
            getSupportActionBar().setTitle("Add Debit");
            txtAmount.getEditText().setText("");
            txtPaypalAddress.getEditText().setText("");
            txtName.getEditText().setText("");
        }

        btAdd.setOnClickListener(v -> {
            OnClickAddorEdit();
        });
    }

    public void OnClickAddorEdit()
    {
        if(StringCheckUtil.isEmpty(this, txtName.getEditText()))
        {
            txtName.getEditText().setError("Fill out it, please");
            YoYo.with(Techniques.Shake)
                    .repeat(1)
                    .duration(500)
                    .playOn(txtName);
            return;
        }
        if(StringCheckUtil.isEmpty(this, txtPaypalAddress.getEditText()))
        {
            txtPaypalAddress.getEditText().setError("Fill out it, please");
            YoYo.with(Techniques.Shake)
                    .repeat(1)
                    .duration(500)
                    .playOn(txtPaypalAddress);
            return;
        }
        if(StringCheckUtil.isEmpty(this, txtAmount.getEditText()))
        {
            txtAmount.getEditText().setError("Fill out it, please");
            YoYo.with(Techniques.Shake)
                    .repeat(1)
                    .duration(500)
                    .playOn(txtAmount);
            return;
        }
        if (!StringCheckUtil.validEmail(txtPaypalAddress.getEditText())) {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(txtPaypalAddress);
            txtPaypalAddress.getEditText().setError("Invalid email address");
            return;
        }
        try
        {
            if(isEdit){
                EditDebt();
            }
            else{
                AddDebt();
            }
        }
        catch (Exception e)
        {
            YoYo.with(Techniques.Shake)
                    .repeat(1)
                    .duration(500)
                    .playOn(txtAmount);
            txtAmount.getEditText().setError("Invalid amount");
            return;
        }

    }

    private void EditDebt() {
        if(Double.parseDouble(txtAmount.getEditText().getText().toString()) < Double.parseDouble(getMainApp().debtLists.get(position).getCurrent_paid())){
            String msg = getMainApp().debtLists.get(position).getCurrent_paid() + " was already payed before. \n So input value should be more than " + getMainApp().debtLists.get(position).getCurrent_paid();
            DialogUtil.showWarningDialog(this, msg);
        }
        else{
            ContentValues headerValues = new ContentValues();
            ContentValues values = new ContentValues();
            values.put("TOKEN", MPreferenceManager.readStringInformation(this, MPreferenceManager.TOKEN));
            values.put("DEBT_ID", getMainApp().debtLists.get(position).getId());
            values.put("DEBT_NAME", txtName.getEditText().getText().toString());
            values.put("DEBT_EMAIL", txtPaypalAddress.getEditText().getText().toString());
            values.put("DEBT_AMOUNT", txtAmount.getEditText().getText().toString());
            ApiCallWrapper service = new ApiCallWrapper(this, POST, true, "EditDebt", ServerURLs.EDIT_DEBT_URL, headerValues, values, new AsyncTaskCallback() {
                @Override
                public void onResultService(Object result) {
                    try
                    {
                        JSONObject returnValue = new JSONObject(result.toString());
                        if(returnValue.getBoolean("result")){
                            finish();
                        }
                        else{
                            Toast.makeText(AddDebitActivity.this, returnValue.getString("msg"), Toast.LENGTH_LONG).show();
                            if(returnValue.getString("msg").equals(getString(R.string.invalid))){
                                startActivity(new Intent(AddDebitActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(AddDebitActivity.this, getString(R.string.error_on_server), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });
            service.execute();
        }
    }

    private void AddDebt() {
        ContentValues headerValues = new ContentValues();
        ContentValues values = new ContentValues();
        values.put("TOKEN", MPreferenceManager.readStringInformation(this, MPreferenceManager.TOKEN));
        values.put("DEBT_NAME", txtName.getEditText().getText().toString());
        values.put("DEBT_EMAIL", txtPaypalAddress.getEditText().getText().toString());
        values.put("DEBT_AMOUNT", txtAmount.getEditText().getText().toString());
        ApiCallWrapper service = new ApiCallWrapper(this, POST, true, "AddDebt", ServerURLs.ADD_DEBT_URL, headerValues, values, new AsyncTaskCallback() {
            @Override
            public void onResultService(Object result) {
                try
                {
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        finish();
                    }
                    else{
                        Toast.makeText(AddDebitActivity.this, returnValue.getString("msg"), Toast.LENGTH_LONG).show();
                        if(returnValue.getString("msg").equals(getString(R.string.invalid))){
                            startActivity(new Intent(AddDebitActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(AddDebitActivity.this, getString(R.string.error_on_server), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        service.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return true;
    }
}
