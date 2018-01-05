package com.rock.debitdiver.Utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;


/**
 * Created by michalejackson on 1/3/18.
 */

public class RecyclerUtil {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void HideButtonAtEnd(RecyclerView recyclerDebt, LinearLayoutManager linearlayoutManager, FloatingActionButton btAdd) {
        recyclerDebt.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int visibleItemCount = recyclerDebt.getChildCount();
                int totalItemCount = linearlayoutManager.getItemCount();
                int firstVisibleItem = linearlayoutManager.findFirstCompletelyVisibleItemPosition();
                if(totalItemCount >= firstVisibleItem + visibleItemCount){
                    btAdd.show();
                }
                else{
                    btAdd.hide();
                }
            }
        });
    }
}
