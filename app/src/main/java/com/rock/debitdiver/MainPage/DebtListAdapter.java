package com.rock.debitdiver.MainPage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.R;

import java.util.ArrayList;

public class DebtListAdapter extends RecyclerView.Adapter<DebtListAdapter.DebtListItemViewHolder> {

    Activity parentActivity;
    public static final int CONTENT_ITEM = 0;
    public static final int FOOTER_ITEM = 1;
    IDebtItemClick itemClickListener;
    ArrayList<DebtInfo> debtLists;
    double debtTotal = 0;
    double paidTotal = 0;
    public DebtListAdapter(Activity context, ArrayList<DebtInfo>debtLists, IDebtItemClick parentListener)
    {
        this.parentActivity = context;
        this.debtLists = debtLists;
        this.itemClickListener = parentListener;
        for(DebtInfo item:debtLists){
            debtTotal += Double.parseDouble(item.getAmount());
            paidTotal += Double.parseDouble(item.getCurrent_paid());
        }
    }

    @Override
    public DebtListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == CONTENT_ITEM)
        {
            View itemView = LayoutInflater.from(parentActivity).inflate(R.layout.item_debt_charge, parent, false);
            DebtListContentItem holder = new DebtListContentItem(itemView);
            return holder;
        }
        else
        {
            View itemView = LayoutInflater.from(parentActivity).inflate(R.layout.item_debt_total_history, parent, false);
            DebtListFooterItem holder = new DebtListFooterItem(itemView);
            return holder;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position < debtLists.size())
            return CONTENT_ITEM;
        else
            return FOOTER_ITEM;
    }

    @Override
    public void onBindViewHolder(DebtListItemViewHolder holder, int position) {
        if(holder instanceof DebtListContentItem)
        {
            ((DebtListContentItem)holder).setListener(itemClickListener);
            DebtInfo item = debtLists.get(position);
            ((DebtListContentItem) holder).txtName.setText(item.getName());
            ((DebtListContentItem) holder).txtCount.setText(item.getAmount() + " / " + item.getCurrent_paid());
            ((DebtListContentItem) holder).pgStatus.setMax(Float.parseFloat(item.getAmount()));
            ((DebtListContentItem) holder).pgStatus.setProgress(Float.parseFloat(item.getCurrent_paid()));
        }
        else if(holder instanceof DebtListFooterItem){
            ((DebtListFooterItem) holder).debtTotal.setText(debtTotal + " / " + String.format("%.1f", paidTotal));
        }
    }

    @Override
    public int getItemCount() {
        return debtLists.size() + 1;
    }

    public class DebtListItemViewHolder extends RecyclerView.ViewHolder
    {
        public DebtListItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class DebtListContentItem extends DebtListItemViewHolder
    {
        IDebtItemClick parentListner;
        CardView cardContainer;
        TextView txtName, txtCount;
        IconRoundCornerProgressBar pgStatus;
        public DebtListContentItem(View itemView) {
            super(itemView);
            cardContainer = itemView.findViewById(R.id.cardContainer);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtCount = (TextView) itemView.findViewById(R.id.txtCount);
            pgStatus = itemView.findViewById(R.id.pgStatus);
            cardContainer.setOnClickListener((v) -> {
                OnClickItem(getAdapterPosition());
            });
        }

        public void OnClickItem(int position)
        {
            parentListner.OnClickItem(position);
        }

        public void setListener(IDebtItemClick parentListener)
        {
            this.parentListner = parentListener;
        }
    }

    public class DebtListFooterItem extends DebtListItemViewHolder
    {
        TextView debtTotal;
        public DebtListFooterItem(View itemView) {
            super(itemView);
            debtTotal = (TextView)itemView.findViewById(R.id.txtTotal);
        }

    }

    public interface IDebtItemClick
    {
        void OnClickItem(int position);
    }
}
