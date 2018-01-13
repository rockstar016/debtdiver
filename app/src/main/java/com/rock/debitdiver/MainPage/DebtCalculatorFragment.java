package com.rock.debitdiver.MainPage;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rock.debitdiver.AddDebitActivity;
import com.rock.debitdiver.ApiUtils.ApiCallWrapper;
import com.rock.debitdiver.ApiUtils.AsyncTaskCallback;
import com.rock.debitdiver.ApiUtils.ServerURLs;
import com.rock.debitdiver.LoginActivity;
import com.rock.debitdiver.MainActivity;
import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.R;
import com.rock.debitdiver.Utils.CalcUtil;
import com.rock.debitdiver.Utils.KeyBoardUtils;
import com.rock.debitdiver.Utils.MPreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;
import static com.rock.debitdiver.MainActivity.FRAGMENTS.DEBTFRAGMENT;


public class DebtCalculatorFragment extends MainBaseFragment implements DebtCalculatorAdatper.IItemChangeListener {
    Button btCalc, btPay;
    RecyclerView recyclerContent;
    DebtCalculatorAdatper adapter;
    EditText txtTotal;
    ArrayList<DebtInfo> payLists = new ArrayList<>();

    public DebtCalculatorFragment() {
        // Required empty public constructor
    }

    public static DebtCalculatorFragment newInstance() {
        DebtCalculatorFragment fragment = new DebtCalculatorFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_debt_calculator, container, false);

        btCalc = rootView.findViewById(R.id.btCalc);
        btPay = rootView.findViewById(R.id.btPay);
        btCalc.setOnClickListener(v -> OnCalClick());
        btPay.setOnClickListener(v -> OnPayClick());
        btPay.setClickable(false);
        txtTotal = rootView.findViewById(R.id.txtTotal);

        for(DebtInfo item:parentActivity.getMainApp().debtLists){
            if(Double.parseDouble(item.getCurrent_paid()) < Double.parseDouble(item.getAmount())){
                payLists.add(new DebtInfo(item.getId(), item.getName(),"0"));
            }
        }

        recyclerContent = rootView.findViewById(R.id.recyclerContent);
        recyclerContent.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        recyclerContent.setLayoutManager(layoutManager);
        recyclerContent.setItemAnimator(new DefaultItemAnimator());
        adapter = new DebtCalculatorAdatper(parentActivity, payLists, this);
        recyclerContent.setAdapter(adapter);
        KeyBoardUtils.HideKeyBoard(parentActivity, parentActivity.getCurrentFocus());

        return rootView;
    }

    private void OnPayClick() {
        Pay();
    }

    private void Pay() {
        ContentValues headerValues = new ContentValues();
        ContentValues values = new ContentValues();
        JSONArray debt_content = new JSONArray();
        debt_content = makeJson(payLists);
        values.put("TOKEN", MPreferenceManager.readStringInformation(parentActivity, MPreferenceManager.TOKEN));
        values.put("DEBT_CONTENT", debt_content.toString());
        double total = 0.0;
        for(DebtInfo item:payLists){
            total += Double.parseDouble(item.getAmount());
        }
        values.put("AMOUNT", String.format("%.1f", total));

        ApiCallWrapper service = new ApiCallWrapper(parentActivity, POST, true, "Paying", ServerURLs.INSERT_PAY_URL, headerValues, values, new AsyncTaskCallback() {
            @Override
            public void onResultService(Object result) {
                try
                {
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        parentActivity.navigationView.setCheckedItem(R.id.navDebit);
                        parentActivity.addFragment(DEBTFRAGMENT);
                    }
                    else{
                        Toast.makeText(parentActivity, returnValue.getString("msg"), Toast.LENGTH_LONG).show();
                        if(returnValue.getString("msg").equals(getString(R.string.invalid))){
                            startActivity(new Intent(parentActivity, LoginActivity.class));
                            parentActivity.finish();
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(parentActivity, getString(R.string.error_on_server), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        service.execute();
    }

    private JSONArray makeJson(ArrayList<DebtInfo> payLists) {
        JSONArray ret = new JSONArray();
        JSONObject obj = null;
        for(DebtInfo item:payLists){
            try {
                obj = new JSONObject();
                obj.put("DEBT_ID", item.getId());
                obj.put("DEBT_NAME", item.getName());
                obj.put("AMOUNT", item.getAmount());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ret.put(obj);
        }
        return ret;
    }

    private void OnCalClick() {
        try
        {
            if(adapter.isCalcable()){
                if(MainActivity.totalDebt - MainActivity.totalpaid < Double.parseDouble(txtTotal.getText().toString())){
                    showAlertDialog(Double.parseDouble(txtTotal.getText().toString()) - MainActivity.totalDebt + MainActivity.totalpaid);
                }
                else{
                    initPayLists();
                    DebtCalc(payLists, txtTotal.getText().toString());
                }
            }
            else{
                Toast.makeText(parentActivity, "First complete edit, please", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        catch (Exception e)
        {
            YoYo.with(Techniques.Shake)
                    .repeat(1)
                    .duration(500)
                    .playOn(txtTotal);
            txtTotal.setError("Invalid amount");
        }
    }

    private void initPayLists() {
        for(DebtInfo item:payLists){
            if(!item.isEdited()){
                item.setAmount("0");
            }
            item.setFinished(false);
        }
    }

    private void showAlertDialog(double rest) {
        new AlertDialog.Builder(parentActivity)
                .setTitle("Attention")
                .setMessage("Redundancy Debt is " + String.format("%.2f",rest) + " $"+ "\n" + "Continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initPayLists();
                        DebtCalc(payLists, txtTotal.getText().toString());
                    }
                })
                .setNegativeButton("No", null)
                .create()
                .show();

    }

    private void DebtCalc(ArrayList<DebtInfo> payLists, String s) {
        payLists = CalcUtil.calc(payLists, parentActivity.getMainApp().debtLists, Double.parseDouble(s));
        for(int i = 0; i < payLists.size(); i ++){
            payLists.get(i).setCalcCompleted(false);
        }
        btPay.setClickable(true);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void OnChangeItemDetected(int position, String value) {
        DebtInfo item = payLists.get(position);
        item.setAmount(value);
        item.setEdited(true);
        payLists.set(position, item);
        adapter.notifyDataSetChanged();
    }
}
