package com.prj666_183a06.xbudget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.pojo.PlanItem;

import java.util.ArrayList;

/**
 * Created by chanhwan on 2018-10-03.
 */

public class PlanAdapter extends BaseAdapter {

    ArrayList<Object> list;

    private static final int PLAN_ITEM = 0;
    private static final int HEADER = 1;
    private LayoutInflater inflater;

    public PlanAdapter(Context context, ArrayList<Object> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof PlanItem) {
            return PLAN_ITEM;
        }
        else {
            return HEADER;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            switch (getItemViewType(position)) {
                case PLAN_ITEM:
                    convertView = inflater.inflate(R.layout.plan_list_item, null);
                    break;
                case HEADER:
                    convertView = inflater.inflate(R.layout.plan_list_header, null);
                    break;
            }
        }

        switch (getItemViewType(position)) {
            case PLAN_ITEM:
//                ImageView image = convertView.findViewById(R.id.);
                TextView title = (TextView) convertView.findViewById(R.id.planItemTitle);
                TextView amount = (TextView) convertView.findViewById(R.id.planItemAmount);
                TextView period = (TextView) convertView.findViewById(R.id.planItemPeriod);

//                image.setImageResource(((PlanItem)list.get(position)).getImage());
                title.setText(((PlanItem)list.get(position)).getTitle());
                amount.setText(((PlanItem)list.get(position)).getAmount());
                period.setText(((PlanItem)list.get(position)).getPeriod());

                break;
            case HEADER:

                TextView header = (TextView) convertView.findViewById(R.id.planListHeader);
                header.setText(((String)list.get(position)));
                break;
        }
        return convertView;

    }
}
