package com.rock.debitdiver.MainPage;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rock.debitdiver.CustomView.ContainerLayout;
import com.rock.debitdiver.Model.PayInfo;
import com.rock.debitdiver.R;
import com.rock.debitdiver.Utils.DateUtil;

import java.util.ArrayList;

/**
 * Created by rock on 12/15/17.
 */

public class DebtHistoryAdapter extends RecyclerView.Adapter<DebtHistoryAdapter.HistoryViewHolder> {

    Activity parentActivity;
    ArrayList<PayInfo> payHistory = new ArrayList<>();

    public DebtHistoryAdapter(ArrayList<PayInfo> payHistory, Activity parentActivity) {
        this.payHistory = payHistory;
        this.parentActivity = parentActivity;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parentActivity).inflate(R.layout.item_debit_check_history, parent, false);
        return new HistoryViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        PayInfo item = payHistory.get(position);
        holder.oncePayTotal.setText(item.getAmount());
        holder.date.setText(DateUtil.stringTP_to_stringDATE(item.getDate()));
        holder.setData(position);
    }



    @Override
    public int getItemCount() {
        return payHistory.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout container;
        TextView date, oncePayTotal;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.containerHistoryItem);
            date = itemView.findViewById(R.id.payDate);
            oncePayTotal = itemView.findViewById(R.id.oncePayTotal);
        }

        public void setData(int position)
        {
            if(container.getChildCount() > 0)
                container.removeAllViews();
            for(int i = 0 ; i < payHistory.get(position).getOncePayList().size() ; i++)
            {
                container.addView(new ContainerLayout(this.container.getContext(), payHistory.get(position).getOncePayList().get(i)));
            }
        }
    }
}
