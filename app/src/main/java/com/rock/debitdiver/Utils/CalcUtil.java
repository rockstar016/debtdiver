package com.rock.debitdiver.Utils;

import com.rock.debitdiver.Model.DebtInfo;

import java.util.ArrayList;

/**
 * Created by michalejackson on 12/30/17.
 */

public class CalcUtil {
    public static ArrayList<DebtInfo> updatedpayLists = new ArrayList<>();
    public static double current;
    public static ArrayList<DebtInfo>updateddebList = new ArrayList<>();
    public static ArrayList<DebtInfo> calc(ArrayList<DebtInfo> originalpayLists, ArrayList<DebtInfo> debtLists,double total){

        updateddebList.clear();
        updatedpayLists = originalpayLists;
        current = total;
        //initialization
        for(int i = 0; i < updatedpayLists.size(); i ++){
            for(int j = 0; j < debtLists.size(); j ++){
                if(updatedpayLists.get(i).getId().equals(debtLists.get(j).getId())){
                    updateddebList.add(debtLists.get(j));
                    break;
                }
            }
        }
        //process things that was just edited.
        for(int i = 0; i < updatedpayLists.size(); i ++){
            if(updatedpayLists.get(i).isEdited()){
                if(current >= Double.parseDouble(updatedpayLists.get(i).getAmount())){
                    current = current - Double.parseDouble(updatedpayLists.get(i).getAmount());
                    updatedpayLists.get(i).setCalcCompleted(true);
                }
                else{
                    updatedpayLists.get(i).setAmount(String.valueOf(current));
                    current = 0.00;
                }
            }
        }
        if(current <= 0.00){
            for(int i = 0; i < updatedpayLists.size(); i ++){
                if(!updatedpayLists.get(i).isEdited()){
                    updatedpayLists.get(i).setAmount("0.00");
                }
            }
        }
        else{
            int i = 0;
            while(i < updatedpayLists.size()){
                if(!updatedpayLists.get(i).isCalcCompleted() && (Double.parseDouble(updateddebList.get(i).getAmount()) - Double.parseDouble(updateddebList.get(i).getCurrent_paid())) < (current/CalcUtil.getUncompletedCount(updatedpayLists))){
                    updatedpayLists = smallThanAvgComplete(updatedpayLists, updateddebList);
                    i = 0;
                }
                else{
                    i ++;
                }
            }
            for(int j = 0; j < updatedpayLists.size(); j ++){
                int unCompletedCount = getUncompletedCount(updatedpayLists);
                if(!updatedpayLists.get(j).isCalcCompleted()){
                    updatedpayLists.get(j).setAmount(String.format("%.2f",current/unCompletedCount));
                }
            }
        }
        return updatedpayLists;
    }

    private static ArrayList<DebtInfo> smallThanAvgComplete(ArrayList<DebtInfo> payLists, ArrayList<DebtInfo> debLists) {
        for(int i = 0; i < payLists.size(); i ++){
            if(!payLists.get(i).isCalcCompleted() && (Double.parseDouble(debLists.get(i).getAmount()) - Double.parseDouble(debLists.get(i).getCurrent_paid())) < (current/CalcUtil.getUncompletedCount(payLists))){
                payLists.get(i).setAmount(String.format("%.2f", (Double.parseDouble(debLists.get(i).getAmount()) - Double.parseDouble(debLists.get(i).getCurrent_paid()))));
                payLists.get(i).setCalcCompleted(true);
                current = current - Double.parseDouble(payLists.get(i).getAmount());
            }
        }
        return payLists;
    }

    public static int getUncompletedCount(ArrayList<DebtInfo> payLists){
        int count = 0;
        for(DebtInfo item:payLists){
            if(!item.isCalcCompleted()){
                count++;
            }
        }
        return count;
    }
}
