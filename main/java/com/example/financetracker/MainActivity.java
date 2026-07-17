package com.example.financetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financetracker.model.Expense;
import com.example.financetracker.ui.AddExpenseActivity;
import com.example.financetracker.ui.InsightsActivity;
import com.example.financetracker.ui.adapter.ExpenseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ExpenseAdapter.OnExpenseActionListener {

    Button btnAddExpense, btnInsights;
    TextView txtTotal, tvEmptyState;
    RecyclerView recyclerExpenses;

    DatabaseHelper databaseHelper;
    ExpenseAdapter adapter;
    List<Expense> expenseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddExpense = findViewById(R.id.btnAddExpense);
        btnInsights = findViewById(R.id.btnInsights);
        txtTotal = findViewById(R.id.txtTotal);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        recyclerExpenses = findViewById(R.id.recyclerExpenses);

        databaseHelper = new DatabaseHelper(this);

        adapter = new ExpenseAdapter(expenseList, this);
        recyclerExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerExpenses.setAdapter(adapter);

        btnAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });

        btnInsights.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, InsightsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload every time the screen is shown again, so new/deleted expenses
        // (added from AddExpenseActivity or removed here) are always reflected.
        loadExpenses();
    }

    private void loadExpenses() {
        expenseList.clear();
        expenseList.addAll(databaseHelper.getAllExpensesList());
        adapter.notifyDataSetChanged();

        updateTotal();
        toggleEmptyState();
    }

    private void updateTotal() {
        double total = databaseHelper.getTotalExpense();
        txtTotal.setText(String.format(Locale.getDefault(), "₹%.2f", total));
    }

    private void toggleEmptyState() {
        boolean isEmpty = expenseList.isEmpty();
        tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerExpenses.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDeleteClicked(Expense expense, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Delete \"" + expense.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    databaseHelper.deleteExpense(expense.getId());
                    adapter.removeAt(position);
                    updateTotal();
                    toggleEmptyState();
                    Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
