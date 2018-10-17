package com.prj666_183a06.xbudget.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prj666_183a06.xbudget.R;
import com.prj666_183a06.xbudget.database.entity.PlanEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chanhwan on 2018-10-03.
 */

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanHolder> {

    private static final String TAG = "PlanAdapter";

    private List<PlanEntity> plans = new ArrayList<>();
    private OnItemClickListener listener;

    // 이거 봐야함... PlanFragment is not MainActivity
    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_item, parent, false);
        Log.d(TAG, "onCreateViewHolder: itemView=" + itemView);
        return new PlanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanHolder holder, int position) {
        PlanEntity currentPlan = plans.get(position);
        holder.textview_planTitle.setText(currentPlan.getPlanTitle());
        holder.textview_planAmount.setText(String.format("%.2f", currentPlan.getPlanAmount()));
        holder.textview_planPeriod.setText(currentPlan.getPlanPeriod());

        if (currentPlan.getPlanType().matches("income")) {
            holder.textview_planAmount.setTextColor(Color.parseColor("#33cc33"));
        } else {
            holder.textview_planAmount.setTextColor(Color.parseColor("#ff1a1a"));
        }
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    // 이거 다시 바꿀꺼임...
    public void setPlans(List<PlanEntity> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }

    public PlanEntity getPlanPosition(int position) {
        return plans.get(position);
    }

    class PlanHolder extends RecyclerView.ViewHolder {
        private TextView textview_planTitle;
        private TextView textview_planAmount;
        private TextView textview_planPeriod;

        public PlanHolder(View itemView) {
            super(itemView);

            textview_planTitle = itemView.findViewById(R.id.textview_planTitle);
            textview_planAmount = itemView.findViewById(R.id.textview_planAmount);
            textview_planPeriod = itemView.findViewById(R.id.textview_planPeriod);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(plans.get(position));
                    }
                }
            });
        }
    }

    /**
     *  OnItemClickListener FOR PlanDetailView
     */
    public interface OnItemClickListener {
        void onItemClick(PlanEntity plan);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
