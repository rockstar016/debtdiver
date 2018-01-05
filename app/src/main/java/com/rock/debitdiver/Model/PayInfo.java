package com.rock.debitdiver.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by michalejackson on 1/4/18.
 */

public class PayInfo {
    String id;
    String date;
    String amount;
    ArrayList<DebtInfo> oncePayList = new ArrayList<>();

    public PayInfo() {
    }

    public PayInfo(JSONObject item) {
        try {
            this.id = item.getString("ID");
            this.date = item.getString("UPDATED_AT");
            this.amount = item.getString("AMOUNT");
            String arrayContent = item.getString("DEBT_CONTENT");
            JSONArray array = new JSONArray(arrayContent);
            for(int i = 0; i < array.length(); i ++){
                DebtInfo debtInfo = new DebtInfo(array.getJSONObject(i).getString("DEBT_ID"),array.getJSONObject(i).getString("DEBT_NAME"), array.getJSONObject(i).getString("AMOUNT"));
                oncePayList.add(debtInfo);
            }
        } catch (JSONException e) {
            this.id = "";
            this.date = "";
            this.amount = "";
            this.oncePayList = new ArrayList<>();
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public ArrayList<DebtInfo> getOncePayList() {
        return oncePayList;
    }

    public void setOncePayList(ArrayList<DebtInfo> oncePayList) {
        this.oncePayList = oncePayList;
    }
}
