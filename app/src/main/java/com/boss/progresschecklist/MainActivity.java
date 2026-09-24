package com.boss.progresschecklist;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {

    private SharedPreferences prefs;
    private TextView progress;
    private ProgressBar bar;
    private int total;

    static class Phase {
        String title;
        String[] items;

        Phase(String title, String... items) {
            this.title = title;
            this.items = items;
        }
    }

    private final List<Phase> phases = Arrays.asList(

        new Phase("Python Foundations",
            "Run Python in Termux",
            "print()",
            "Numbers & arithmetic",
            "Strings & quotes",
            "Variables",
            "Functions",
            "Parameters",
            "return",
            "f-strings"),

        new Phase("Python Logic",
            "if",
            "elif",
            "else",
            "Comparisons",
            "and / or / not",
            "for loops",
            "while loops",
            "range()",
            "break / continue"),

        new Phase("Data Structures",
            "Lists",
            "Tuples",
            "Sets",
            "Dictionaries",
            "Indexing & slicing",
            "Nested data",
            "Comprehensions"),

        new Phase("Professional Python",
            "Exceptions",
            "Files",
            "JSON",
            "CSV",
            "Modules & imports",
            "Packages",
            "Virtual environments"),

        new Phase("Object-Oriented Python",
            "Classes",
            "Objects",
            "Attributes",
            "Methods",
            "__init__",
            "Inheritance",
            "Composition",
            "Polymorphism",
            "Dataclasses"),

        new Phase("Advanced Python",
            "Iterators",
            "Generators",
            "yield",
            "Decorators",
            "Context managers",
            "Advanced typing",
            "async / await",
            "Concurrency",
            "Testing",
            "Profiling",
            "Packaging"),

        new Phase("Excel Foundations",
            "Workbook & worksheet",
            "Rows & columns",
            "Cells & ranges",
            "Data entry",
            "Formatting",
            "Sort",
            "Filter",
            "Excel Tables"),

        new Phase("Excel Formulas",
            "SUM",
            "AVERAGE",
            "MIN / MAX",
            "COUNT / COUNTA",
            "IF",
            "AND / OR",
            "IFERROR",
            "SUMIF / SUMIFS",
            "COUNTIF / COUNTIFS"),

        new Phase("Excel Analytics",
            "XLOOKUP",
            "INDEX / MATCH",
            "PivotTables",
            "PivotCharts",
            "Conditional formatting",
            "Charts",
            "KPI dashboard"),

        new Phase("Python + Excel",
            "pandas",
            "openpyxl",
            "Read Excel",
            "Write Excel",
            "Clean data",
            "Transform data",
            "Automated reports"),

        new Phase("Data Analytics",
            "SQL",
            "PostgreSQL",
            "NumPy",
            "pandas analysis",
            "Matplotlib",
            "Data cleaning",
            "Trend analysis",
            "Business KPIs"),

        new Phase("AI & Tool Building",
            "LLM APIs",
            "Structured outputs",
            "Function calling",
            "Tool schemas",
            "AI app architecture",
            "MCP concepts",
            "Build an MCP server",
            "ChatGPT tool integration",
            "Evaluation")
    );

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        prefs = getSharedPreferences("progress", MODE_PRIVATE);

        buildScreen();
    }

    private TextView text(String value, float size, boolean bold) {
        TextView v = new TextView(this);

        v.setText(value);
        v.setTextColor(Color.WHITE);
        v.setTextSize(size);
        v.setPadding(24, 18, 24, 18);

        if (bold) {
            v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }

        return v;
    }

    private void buildScreen() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(16, 17, 20));

        LinearLayout header = new LinearLayout(this);

        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(20, 20, 20, 8);

        TextView title = text(
            "Boss Progress Checklist",
            26,
            true
        );

        header.addView(title);

        progress = text(
            "0% complete",
            15,
            false
        );

        progress.setTextColor(Color.LTGRAY);

        header.addView(progress);

        bar = new ProgressBar(
            this,
            null,
            android.R.attr.progressBarStyleHorizontal
        );

        bar.setMax(100);

        header.addView(
            bar,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                12
            )
        );

        Button reset = new Button(this);

        reset.setText("Reset Progress");

        reset.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            buildScreen();
        });

        header.addView(reset);

        root.addView(header);

        ScrollView scroll = new ScrollView(this);

        LinearLayout list = new LinearLayout(this);

        list.setOrientation(LinearLayout.VERTICAL);
        list.setPadding(12, 4, 12, 40);

        total = 0;

        for (int p = 0; p < phases.size(); p++) {

            Phase phase = phases.get(p);

            TextView heading = text(
                phase.title,
                19,
                true
            );

            heading.setTextColor(
                Color.rgb(239, 68, 68)
            );

            list.addView(heading);

            for (int i = 0; i < phase.items.length; i++) {

                total++;

                final String key =
                    "phase_" + p + "_item_" + i;

                CheckBox checkbox = new CheckBox(this);

                checkbox.setText(phase.items[i]);
                checkbox.setTextColor(Color.WHITE);
                checkbox.setTextSize(16);
                checkbox.setPadding(12, 8, 12, 8);

                checkbox.setChecked(
                    prefs.getBoolean(key, false)
                );

                checkbox.setOnCheckedChangeListener(
                    (button, checked) -> {

                        prefs.edit()
                            .putBoolean(key, checked)
                            .apply();

                        updateProgress();
                    }
                );

                list.addView(checkbox);
            }
        }

        scroll.addView(list);

        root.addView(
            scroll,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1
            )
        );

        setContentView(root);

        updateProgress();
    }

    private void updateProgress() {

        int completed = 0;

        for (int p = 0; p < phases.size(); p++) {

            for (int i = 0;
                 i < phases.get(p).items.length;
                 i++) {

                String key =
                    "phase_" + p + "_item_" + i;

                if (prefs.getBoolean(key, false)) {
                    completed++;
                }
            }
        }

        int percentage =
            total == 0
                ? 0
                : completed * 100 / total;

        progress.setText(
            completed +
            " / " +
            total +
            " completed  •  " +
            percentage +
            "%"
        );

        bar.setProgress(percentage);
    }
}
