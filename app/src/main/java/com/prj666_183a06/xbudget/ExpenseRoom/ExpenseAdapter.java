package com.prj666_183a06.xbudget.ExpenseRoom;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.prj666_183a06.xbudget.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends ListAdapter<Expense, ExpenseAdapter.ExpenseHolder> {
    //public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseHolder> {
    private onItemClickListener listener;

    public ExpenseAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Expense> DIFF_CALLBACK = new DiffUtil.ItemCallback<Expense>() {
        @Override
        public boolean areItemsTheSame(Expense oldItem, Expense newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Expense oldItem, Expense newItem) {
            return
                    oldItem.getExpenseStore().equals(newItem.getExpenseStore()) &&
                            oldItem.getExpenseItem().equals(newItem.getExpenseItem()) &&
                            oldItem.getExpenseCost() == newItem.getExpenseCost() &&
                            oldItem.getExpenseDate().equals(newItem.getExpenseDate()) &&
                            oldItem.getExpenseCategory().equals(newItem.getExpenseCategory()
                            );
        }
    };

    @NonNull
    @Override
    public ExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ExpenseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseHolder holder, int position) {
        //Expense currentExpense = expenses.get(position);
        Expense currentExpense = getItem(position);

        holder.textViewStore.setText(currentExpense.getExpenseStore());
        holder.textViewItem.setText(currentExpense.getExpenseItem());
        //Format to Canadian currency format
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        String currency = format.format(currentExpense.getExpenseCost());
        holder.textViewCost.setText(currency);
        holder.textViewCategory.setText(currentExpense.getExpenseCategory());
        holder.textViewDate.setText(currentExpense.getExpenseDate());
    }

    /*@Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }*/

    public Expense getExpenseAt(int position) {
        return getItem(position);
    }

    class ExpenseHolder extends RecyclerView.ViewHolder {
        private TextView textViewStore;
        private TextView textViewItem;
        private TextView textViewCost;
        private TextView textViewDate;
        private TextView textViewCategory;

        public ExpenseHolder(View itemView) {
            super(itemView);
            textViewStore = itemView.findViewById(R.id.text_view_store);
            textViewItem = itemView.findViewById(R.id.text_view_item);
            textViewCost = itemView.findViewById(R.id.text_view_cost);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewCategory = itemView.findViewById(R.id.text_view_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
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
