package com.rock.debitdiver.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by michalejackson on 12/29/17.
 */

public class DebtInfo implements Serializable {
    String id;
    String name;
    String amount;
    String current_paid;
    String paypal_address;
    boolean edited = false;
    boolean calcCompleted;
    boolean finished = false;

    public DebtInfo() {
    }
    public DebtInfo(String id, String name, String amount){
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public DebtInfo(JSONObject item) {
        try {
            this.id = item.getString("ID");
            this.name = item.getString("DEBT_NAME");
            this.amount = item.getString("DEBT_AMOUNT");
            this.current_paid = item.getString("DEBT_CURRENT_PAID");
            this.paypal_address = item.getString("DEBT_EMAIL");
        } catch (JSONException e) {
            this.id = "";
            this.name = "";
            this.amount = "";
            this.current_paid = "";
            this.paypal_address = "";

            e.printStackTrace();
        }
    }

    public String getPaypal_address() {
        return paypal_address;
    }

    public void setPaypal_address(String paypal_address) {
        this.paypal_address = paypal_address;
    }

    public String getCurrent_paid() {
        return current_paid;
    }

    public void setCurrent_paid(String current_paid) {
        this.current_paid = current_paid;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }
    public boolean isCalcCompleted() {
        return calcCompleted;
    }

    public void setCalcCompleted(boolean calcCompleted) {
        this.calcCompleted = calcCompleted;
    }
}
