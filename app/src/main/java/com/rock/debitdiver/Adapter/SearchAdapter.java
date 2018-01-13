package com.rock.debitdiver.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rock.debitdiver.R;

import java.util.ArrayList;

/**
 * Created by michalejackson on 1/9/18.
 */

public class SearchAdapter extends BaseAdapter {
    ArrayList<String> dataProvider;
    LayoutInflater inflater;
    Context context;

    public SearchAdapter(Context context, ArrayList<String> dataProvider) {
        this.dataProvider = dataProvider;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataProvider.size()+1;
    }

    @Override
    public Object getItem(int i) {
        if(i == 0)
            return null;
        else
            return dataProvider.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_spinner, null, false);
        TextView date = (TextView) view.findViewById(R.id.date);
        if(i == 0){
            date.setText("All");
        }
        else{
            date.setText(dataProvider.get(i-1));
        }
        return view;
    }
}
