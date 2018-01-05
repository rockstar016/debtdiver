package com.rock.debitdiver.CustomView;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rock.debitdiver.Model.DebtInfo;
import com.rock.debitdiver.R;

/**
 * Created by rock on 12/15/17.
 */

public class ContainerLayout extends LinearLayout {
    Context context;
    DebtInfo item;
    TextView txtName, txtAmount;
    public ContainerLayout(Context context, DebtInfo item) {
        super(context);
        this.context = context;
        this.item = item;
        InitView();
    }

    public ContainerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitView();
    }

    public ContainerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContainerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void InitView() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_item_debit_history, this);
        txtName = rootView.findViewById(R.id.txtName);
        txtAmount = rootView.findViewById(R.id.txtAmount);
        txtAmount.setText(item.getAmount());
        txtName.setText(item.getName());
    }
}
