package com.rock.debitdiver.MainPage;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.github.aakira.expandablelayout.Utils;
import com.rock.debitdiver.Adapter.SearchAdapter;
import com.rock.debitdiver.ApiUtils.ApiCallWrapper;
import com.rock.debitdiver.ApiUtils.AsyncTaskCallback;
import com.rock.debitdiver.ApiUtils.ServerURLs;
import com.rock.debitdiver.LoginActivity;
import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.Model.PayInfo;
import com.rock.debitdiver.R;
import com.rock.debitdiver.Utils.DateUtil;
import com.rock.debitdiver.Utils.MPreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;


public class PaymentDetailFragment extends MainBaseFragment {
    RecyclerView recyclerHistory;
    DebtHistoryAdapter adapter;
    ArrayList<PayInfo> payHistory = new ArrayList<>();
    ArrayList<PayInfo> temp = new ArrayList<>();
    ArrayList<String> searchList = new ArrayList<>();
    int selected = 0;

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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_payment_detail, container, false);
        recyclerHistory = rootView.findViewById(R.id.recyclerContent);
        recyclerHistory.setHasFixedSize(true);
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
                    payHistory.clear();
                    searchList.clear();
                    temp.clear();
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        JSONArray array = returnValue.getJSONArray("msg");
                        for(int i = 0; i < array.length(); i ++){
                            PayInfo item = new PayInfo(array.getJSONObject(i));
                            payHistory.add(item);
                            temp.add(item);
                            if(i == 0){
                                searchList.add(DateUtil.stringTP_to_stringDATE(item.getDate()).substring(0, 7));
                            }
                            else{
                                if(!searchList.get(searchList.size() - 1).equals(DateUtil.stringTP_to_stringDATE(item.getDate()).substring(0, 7))){
                                    searchList.add(DateUtil.stringTP_to_stringDATE(item.getDate()).substring(0, 7));
                                }
                            }

                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity);
                        adapter = new DebtHistoryAdapter(payHistory, parentActivity);
                        recyclerHistory.setLayoutManager(layoutManager);
                        recyclerHistory.setAdapter(adapter);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search){
            CreateBottomSheetDialog();
        }
        return true;
    }

    private void CreateBottomSheetDialog() {
        final boolean[] dialogState = {false};
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(parentActivity);
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.spinner_search, null);
        Spinner searchSpinner = (Spinner)sheetView.findViewById(R.id.searchSpinner);
        SearchAdapter searchAdapter = new SearchAdapter(getActivity(), searchList);
        searchSpinner.setAdapter(searchAdapter);
        bottomSheetDialog.setContentView(sheetView);
        searchSpinner.setSelected(false);
        searchSpinner.setSelection(selected);
        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(dialogState[0]){
                    payHistory.clear();
                    for(int i = 0; i < temp.size(); i ++){
                        payHistory.add(temp.get(i));
                    }
                    if(position != 0){
                        PayListFilter(searchList.get(position - 1));
                        bottomSheetDialog.dismiss();
                    }
                    else {
                        adapter.notifyDataSetChanged();
                        bottomSheetDialog.dismiss();
                    }
                    selected = position;
                }
                dialogState[0] = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bottomSheetDialog.show();
    }

    private void PayListFilter(String s) {
        for(PayInfo item:payHistory){
            if(!DateUtil.stringTP_to_stringDATE(item.getDate()).contains(s)){
                payHistory.remove(item);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
