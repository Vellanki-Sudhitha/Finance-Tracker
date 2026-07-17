package com.example.financetracker.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.R;
import com.example.financetracker.model.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private final List<Expense> expenseList;
    private final OnExpenseActionListener listener;

    public interface OnExpenseActionListener {
        void onDeleteClicked(Expense expense, int position);
    }

    public ExpenseAdapter(List<Expense> expenseList, OnExpenseActionListener listener) {
        this.expenseList = expenseList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, amount, category, date;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            amount = itemView.findViewById(R.id.tvAmount);
            category = itemView.findViewById(R.id.tvCategory);
            date = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.title.setText(expense.getTitle());
        holder.amount.setText(String.format("₹%.2f", expense.getAmount()));
        holder.category.setText(expense.getCategory());
        holder.date.setText(expense.getDate());

        holder.btnDelete.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                listener.onDeleteClicked(expenseList.get(adapterPosition), adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // Call after the backing list has been mutated (item removed/added) to keep
    // the RecyclerView positions and animations in sync.
    public void removeAt(int position) {
        if (position >= 0 && position < expenseList.size()) {
            expenseList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, expenseList.size());
        }
    }
}
