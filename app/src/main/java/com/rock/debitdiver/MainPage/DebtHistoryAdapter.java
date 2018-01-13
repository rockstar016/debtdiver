package com.rock.debitdiver.MainPage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import com.hendraanggrian.widget.ExpandableItem;
import com.hendraanggrian.widget.ExpandableRecyclerView;
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
    private SparseBooleanArray expandState = new SparseBooleanArray();
    ArrayList<Boolean> firstState = new ArrayList<>();

    public DebtHistoryAdapter(ArrayList<PayInfo> payHistory, Activity parentActivity) {

        this.payHistory = payHistory;
        this.parentActivity = parentActivity;
        for(int i = 0; i < payHistory.size(); i ++){
            expandState.append(i, true);
        }
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
        if(expandState.get(position)){
            holder.setData(position);
        }

        holder.setIsRecyclable(false);
        holder.expandable.setInRecyclerView(true);
        holder.expandable.setExpanded(expandState.get(position));

        holder.expandable.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {

                createRotateAnimator(holder.dropbox, 0f, 180f).start();
                expandState.put(position, true);
                notifyDataSetChanged();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.dropbox, 180f, 0f).start();
                expandState.put(position, false);
            }
        });

        holder.dropbox.setRotation(expandState.get(position) ? 180f : 0f);
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandable);
            }
        });
    }


    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return payHistory.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder
    {

        //Header
        LinearLayout header;
        TextView date, oncePayTotal;
        ImageView dropbox;

        //expand
        ExpandableLinearLayout expandable;
        LinearLayout container;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            //header
            header = itemView.findViewById(R.id.header);
            date = itemView.findViewById(R.id.payDate);
            oncePayTotal = itemView.findViewById(R.id.oncePayTotal);
            dropbox = itemView.findViewById(R.id.dropDown);
            //expand
            expandable = itemView.findViewById(R.id.expandableLayout);
            container = expandable.findViewById(R.id.containerHistoryItem);

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
    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
