package com.example.financetracker.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financetracker.DatabaseHelper;
import com.example.financetracker.R;

import java.util.Locale;
import java.util.Map;

public class InsightsActivity extends AppCompatActivity {

    private static final String[] PALETTE = {
            "#4CAF50", "#2196F3", "#FF9800", "#9C27B0",
            "#F44336", "#00BCD4", "#8BC34A", "#607D8B"
    };

    DatabaseHelper databaseHelper;
    TextView tvInsightsTotal, tvTopCategory, tvNoData;
    LinearLayout categoryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        databaseHelper = new DatabaseHelper(this);

        tvInsightsTotal = findViewById(R.id.tvInsightsTotal);
        tvTopCategory = findViewById(R.id.tvTopCategory);
        tvNoData = findViewById(R.id.tvNoData);
        categoryContainer = findViewById(R.id.categoryContainer);

        loadInsights();
    }

    private void loadInsights() {
        double total = databaseHelper.getTotalExpense();
        Map<String, Double> categoryTotals = databaseHelper.getCategoryTotals();

        tvInsightsTotal.setText(String.format(Locale.getDefault(), "₹%.2f", total));

        categoryContainer.removeAllViews();

        if (categoryTotals.isEmpty() || total <= 0) {
            tvNoData.setVisibility(View.VISIBLE);
            tvTopCategory.setText("");
            return;
        }

        tvNoData.setVisibility(View.GONE);

        String topCategory = categoryTotals.keySet().iterator().next();
        tvTopCategory.setText("Top category: " + topCategory);

        int colorIndex = 0;

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

            String category = entry.getKey();
            double amount = entry.getValue();
            int percent = (int) Math.round((amount / total) * 100);
            String color = PALETTE[colorIndex % PALETTE.length];
            colorIndex++;

            categoryContainer.addView(buildCategoryRow(category, amount, percent, color));
        }
    }

    private View buildCategoryRow(String category, double amount, int percent, String color) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.bottomMargin = dp(16);
        row.setLayoutParams(rowParams);

        LinearLayout headerRow = new LinearLayout(this);
        headerRow.setOrientation(LinearLayout.HORIZONTAL);
        headerRow.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView tvCategory = new TextView(this);
        tvCategory.setText(category);
        tvCategory.setTextColor(Color.parseColor("#1E1E1E"));
        tvCategory.setTextSize(15);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        tvCategory.setLayoutParams(labelParams);

        TextView tvAmount = new TextView(this);
        tvAmount.setText(String.format(Locale.getDefault(), "₹%.2f (%d%%)", amount, percent));
        tvAmount.setTextColor(Color.parseColor("#555555"));
        tvAmount.setTextSize(13);

        headerRow.addView(tvCategory);
        headerRow.addView(tvAmount);

        // Track (background) bar
        LinearLayout track = new LinearLayout(this);
        track.setOrientation(LinearLayout.HORIZONTAL);
        track.setBackgroundColor(Color.parseColor("#E0E0E0"));
        LinearLayout.LayoutParams trackParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(10));
        trackParams.topMargin = dp(6);
        track.setLayoutParams(trackParams);

        // Fill (foreground) bar, proportional width via weight
        View fill = new View(this);
        fill.setBackgroundColor(Color.parseColor(color));
        int safePercent = Math.max(percent, 2); // keep a sliver visible for tiny categories
        LinearLayout.LayoutParams fillParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, safePercent);
        fill.setLayoutParams(fillParams);

        View spacer = new View(this);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 100 - safePercent);
        spacer.setLayoutParams(spacerParams);

        track.addView(fill);
        track.addView(spacer);

        row.addView(headerRow);
        row.addView(track);

        return row;
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}
