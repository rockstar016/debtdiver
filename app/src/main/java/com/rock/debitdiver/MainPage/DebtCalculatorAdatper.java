package com.rock.debitdiver.MainPage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.rock.debitdiver.BaseActivity;
import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.R;
import com.rock.debitdiver.Utils.DialogUtil;

import java.util.ArrayList;

public class DebtCalculatorAdatper extends RecyclerView.Adapter<DebtCalculatorAdatper.DebtCalcItemViewHolder> {
    BaseActivity parentActivity;
    IItemChangeListener parentListener;
    ArrayList<DebtInfo> payLists;
    ArrayList<Boolean> editLists = new ArrayList<>();
    public double totalPay;
    public DebtCalculatorAdatper(BaseActivity activity, ArrayList<DebtInfo> payLists, IItemChangeListener parentListener) {
        this.parentActivity = activity;
        this.payLists = payLists;
        this.parentListener = parentListener;
        for(int i = 0; i < payLists.size(); i ++){
            editLists.add(false);
        }
        for(DebtInfo item:payLists){
            totalPay += Double.parseDouble(item.getAmount());
        }
    }

    @Override
    public DebtCalculatorAdatper.DebtCalcItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parentActivity).inflate(R.layout.item_debt_calc_content, parent, false);
        return new DebtCalcItemViewHolder(rootView, parentListener);
    }

    @Override
    public void onBindViewHolder(DebtCalculatorAdatper.DebtCalcItemViewHolder holder, int position) {
        holder.setData();
        DebtInfo item = payLists.get(position);
        holder.txtName.setText(item.getName());
        holder.txtAmount.setText(item.getAmount());
//        if(item.isFinished()){
//            holder.finished.setVisibility(View.VISIBLE);
//        }
//        else{
//            holder.finished.setVisibility(View.GONE);
//        }
    }
    public boolean isCalcable(){
        for(int i = 0; i < editLists.size(); i ++){
            if(editLists.get(i)){
                return false;
            }
        }
        return true;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(payLists.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return payLists.size();
    }

    public class DebtCalcItemViewHolder extends RecyclerView.ViewHolder
    {
        EditText txtAmount;
        ImageButton imgEditButton;
        CheckBox finished;
        TextView txtName;
        IItemChangeListener parentListener;
        public DebtCalcItemViewHolder(View itemView, IItemChangeListener listener) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            imgEditButton = itemView.findViewById(R.id.btEdit);
            finished = itemView.findViewById(R.id.finished);
            this.parentListener = listener;
        }

        public void setData()
        {
            imgEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickEditButton(getAdapterPosition());
                }
            });

        }


        public void OnClickEditButton(int position)
        {
            txtAmount.setEnabled(!txtAmount.isEnabled());
            editLists.set(position,txtAmount.isEnabled());
            imgEditButton.setImageResource(txtAmount.isEnabled()?R.drawable.ic_done_all_black_24dp:R.drawable.ic_edit_black_24dp);
            if(!txtAmount.isEnabled())
            {
                try
                {
                    for(DebtInfo item:parentActivity.getMainApp().debtLists) {
                        if (item.getId().equals(payLists.get(position).getId())) {
                            if (Double.parseDouble(item.getAmount()) - Double.parseDouble(item.getCurrent_paid()) < Double.parseDouble(txtAmount.getText().toString())) {
                                String msg = "Input value was overflow. \n left debit in this field is " + String.format("%.2f", Double.parseDouble(item.getAmount()) - Double.parseDouble(item.getCurrent_paid()));
                                DialogUtil.showWarningDialog(parentActivity, msg);
                                txtAmount.setEnabled(!txtAmount.isEnabled());
                                editLists.set(position, txtAmount.isEnabled());
                                imgEditButton.setImageResource(txtAmount.isEnabled() ? R.drawable.ic_done_all_black_24dp : R.drawable.ic_edit_black_24dp);
                            } else {
                                parentListener.OnChangeItemDetected(position, txtAmount.getText().toString());
                            }
                            break;
                        }
                    }

                }
                catch (Exception e)
                {
                    YoYo.with(Techniques.Shake)
                            .repeat(1)
                            .duration(500)
                            .playOn(txtAmount);
                    txtAmount.setError("Invalid amount");
                    txtAmount.setText("0");
                    return;
                }
            }
        }
    }


    public interface IItemChangeListener{
        void OnChangeItemDetected(int position, String value);
    }
}
