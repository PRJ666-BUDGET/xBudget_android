package com.prj666_183a06.xbudget.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prj666_183a06.xbudget.Model.Plan;
import com.prj666_183a06.xbudget.R;

import java.util.List;

/**
 * Created by chanhwan on 2018-10-03.
 */

public class PlanAdapter extends BaseAdapter {

    //ArrayList<Object> list;
    List<Plan> list;

    private static final int PLAN_ITEM = 0;
    private static final int HEADER = 1;
    private LayoutInflater inflater;

    public PlanAdapter(Context context, List<Plan> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof Plan) {
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

//                Old code with ArrayList<Object>
//                image.setImageResource(((PlanItem)list.get(position)).getImage());
//                title.setText(((PlanItem)list.get(position)).getTitle());
//                amount.setText(((PlanItem)list.get(position)).getAmount());
//                period.setText(((PlanItem)list.get(position)).getPeriod());

                title.setText(((Plan)list.get(position)).getPlanTitle());
                amount.setText(String.valueOf(((Plan)list.get(position)).getPlanAmount()));
                period.setText(((Plan)list.get(position)).getPlanPeriod());

                break;
            case HEADER:

                TextView header = (TextView) convertView.findViewById(R.id.planListHeader);
//                Old code with ArrayList<Object>
//                header.setText(((String)list.get(position)));
                header.setText(list.get(position).toString());
                break;
        }
        return convertView;

    }
}
