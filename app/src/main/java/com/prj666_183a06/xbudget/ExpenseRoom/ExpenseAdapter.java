package com.prj666_183a06.xbudget.ExpenseRoom;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prj666_183a06.xbudget.R;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {
    private List<Expense> expenses = new ArrayList<>();
    private onItemClickListener listener;

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ExpenseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        Expense currentExpense = expenses.get(position);
        holder.textViewStore.setText(currentExpense.getExpenseStore());
        holder.textViewItem.setText(currentExpense.getExpenseItem());
        holder.textViewCost.setText(String.valueOf(currentExpense.getExpenseCost()));
        holder.textViewDate.setText(currentExpense.getExpenseDate());
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    public Expense getExpenseAt(int position) {
        return expenses.get(position);
    }

    class ExpenseHolder extends RecyclerView.ViewHolder {
        private TextView textViewStore;
        private TextView textViewItem;
        private TextView textViewCost;
        private TextView textViewDate;

        public ExpenseHolder(View itemView) {
            super(itemView);
            textViewStore = itemView.findViewById(R.id.text_view_store);
            textViewItem = itemView.findViewById(R.id.text_view_item);
            textViewCost = itemView.findViewById(R.id.text_view_cost);
            textViewDate = itemView.findViewById(R.id.text_view_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(expenses.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(Expense expense);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }
}
