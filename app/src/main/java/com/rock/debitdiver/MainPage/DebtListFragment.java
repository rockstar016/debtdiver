package com.rock.debitdiver.MainPage;


import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.rock.debitdiver.Utils.MPreferenceManager;
import com.rock.debitdiver.Utils.RecyclerUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.rock.debitdiver.ApiUtils.ApiCallWrapper.SERVICE_TYPE.POST;
import static com.rock.debitdiver.MainActivity.FRAGMENTS.CALCFRAGMENT;


public class DebtListFragment extends MainBaseFragment implements DebtListAdapter.IDebtItemClick {
    RecyclerView recyclerDebt;
    DebtListAdapter adapter;
    FloatingActionButton btAdd;
    public static final int REQUEST_ADD = 100;
    public static final int REQUEST_EDIT = 101;
    public DebtListFragment() {
        // Required empty public constructor
    }

    public static DebtListFragment newInstance() {
        DebtListFragment fragment = new DebtListFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_debt_list, container, false);

        btAdd = rootView.findViewById(R.id.btAdd);
        btAdd.setOnClickListener((v)->{
                OnClickAddButton();
        });
        recyclerDebt = rootView.findViewById(R.id.recyclerContent);
        adapter = new DebtListAdapter(parentActivity, parentActivity.getMainApp().debtLists, this);
        recyclerDebt.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(parentActivity);
        recyclerDebt.setAdapter(adapter);
        recyclerDebt.setLayoutManager(linearlayoutManager);

        updateDebtList();
        RecyclerUtil.HideButtonAtEnd(recyclerDebt, linearlayoutManager, btAdd);
        return rootView;
    }

    private void updateDebtList() {
        parentActivity.getMainApp().debtLists.clear();
        parentActivity.totalDebt = 0;
        parentActivity.totalpaid = 0;
        ContentValues headerValues = new ContentValues();
        ContentValues values = new ContentValues();
        values.put("TOKEN", MPreferenceManager.readStringInformation(parentActivity, MPreferenceManager.TOKEN));
        ApiCallWrapper service = new ApiCallWrapper(parentActivity, POST, true, "Get DebtList", ServerURLs.GET_DEBTLIST_URL, headerValues, values, new AsyncTaskCallback() {
            @Override
            public void onResultService(Object result) {
                try
                {
                    JSONObject returnValue = new JSONObject(result.toString());
                    if(returnValue.getBoolean("result")){
                        JSONArray array = returnValue.getJSONArray("msg");
                        for(int i = 0; i < array.length(); i ++){
                            DebtInfo item = new DebtInfo(array.getJSONObject(i));
                            parentActivity.getMainApp().debtLists.add(item);
                        }
                        for(DebtInfo item:parentActivity.getMainApp().debtLists){
                            parentActivity.totalDebt += Double.parseDouble(item.getAmount());
                            parentActivity.totalpaid += Double.parseDouble(item.getCurrent_paid());
                        }
                        adapter.paidTotal = parentActivity.totalpaid;
                        adapter.debtTotal = parentActivity.totalDebt;
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

    private void OnClickAddButton() {
        Intent i = new Intent(getContext(), AddDebitActivity.class);
        i.putExtra(AddDebitActivity.IS_EDIT, false);
        startActivityForResult(i, REQUEST_ADD);
    }



    @Override
    public void OnClickItem(int position) {
        Intent i = new Intent(getContext(), AddDebitActivity.class);
        i.putExtra(AddDebitActivity.IS_EDIT, true);
        i.putExtra(AddDebitActivity.EDIT_POSITION, position);
        startActivityForResult(i, REQUEST_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateDebtList();
    }
}
