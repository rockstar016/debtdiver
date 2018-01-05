package com.rock.debitdiver.MainPage;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rock.debitdiver.ApiUtils.ApiCallWrapper;
import com.rock.debitdiver.ApiUtils.AsyncTaskCallback;
import com.rock.debitdiver.ApiUtils.ServerURLs;
import com.rock.debitdiver.LoginActivity;
import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.Model.PayInfo;
import com.rock.debitdiver.R;
import com.rock.debitdiver.Utils.MPreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;


public class PaymentDetailFragment extends MainBaseFragment {
    RecyclerView recyclerHistory;
    DebtHistoryAdapter adapter;
    ArrayList<PayInfo> payHistory = new ArrayList<>();

    @Override
    public void registerForContextMenu(View view){
    }

    public PaymentDetailFragment() {
        // Required empty public constructor
    }

    public static PaymentDetailFragment newInstance() {
        PaymentDetailFragment fragment = new PaymentDetailFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_payment_detail, container, false);
        recyclerHistory = rootView.findViewById(R.id.recyclerContent);
        recyclerHistory.setHasFixedSize(true);
        adapter = new DebtHistoryAdapter(payHistory, parentActivity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
        recyclerHistory.setLayoutManager(layoutManager);
        recyclerHistory.setAdapter(adapter);
        updatePayList();
        return rootView;
    }

    private void updatePayList() {
        ContentValues headerValues = new ContentValues();
        ContentValues values = new ContentValues();
        values.put("TOKEN", MPreferenceManager.readStringInformation(parentActivity, MPreferenceManager.TOKEN));
        ApiCallWrapper service = new ApiCallWrapper(parentActivity, POST, true, "Get History", ServerURLs.GET_HISTORY, headerValues, values, new AsyncTaskCallback() {
            @Override
            public void onResultService(Object result) {
                try
                {
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        JSONArray array = returnValue.getJSONArray("msg");
                        for(int i = 0; i < array.length(); i ++){
                            PayInfo item = new PayInfo(array.getJSONObject(i));
                            payHistory.add(item);
                        }
                        adapter.notifyDataSetChanged();

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

}
