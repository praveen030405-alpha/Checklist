package com.boss.progresschecklist;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity {

    private SharedPreferences prefs;
    private TextView progress;
    private ProgressBar bar;
    private LinearLayout list;
    private int total;

    private static final int BG = Color.rgb(10, 11, 14);
    private static final int CARD = Color.rgb(22, 24, 29);
    private static final int CARD_2 = Color.rgb(27, 29, 35);
    private static final int TEXT = Color.rgb(244, 244, 245);
    private static final int MUTED = Color.rgb(161, 161, 170);
    private static final int RED = Color.rgb(239, 68, 68);
    private static final int GREEN = Color.rgb(34, 197, 94);
    private static final int BLUE = Color.rgb(59, 130, 246);
    private static final int PURPLE = Color.rgb(168, 85, 247);
    private static final int ORANGE = Color.rgb(245, 158, 11);

    static class Phase {
        String title;
        String subtitle;
        int accent;
        String[] items;

        Phase(String title, String subtitle, int accent, String... items) {
            this.title = title;
            this.subtitle = subtitle;
            this.accent = accent;
            this.items = items;
        }
    }

    private final List<Phase> phases = Arrays.asList(

        new Phase("Python Foundations", "Start here — build the core", RED,
            "Run Python in Termux",
            "print()",
            "Numbers & arithmetic",
            "Strings & quotes",
            "Variables",
            "Functions",
            "Parameters",
            "return",
            "f-strings"),

        new Phase("Python Logic", "Make programs think", RED,
            "if",
            "elif",
            "else",
            "Comparisons",
            "and / or / not",
            "for loops",
            "while loops",
            "range()",
            "break / continue"),

        new Phase("Data Structures", "Work with real data", BLUE,
            "Lists",
            "Tuples",
            "Sets",
            "Dictionaries",
            "Indexing & slicing",
            "Nested data",
            "Comprehensions"),

        new Phase("Professional Python", "Write useful programs", BLUE,
            "Exceptions",
            "Files",
            "JSON",
            "CSV",
            "Modules & imports",
            "Packages",
            "Virtual environments"),

        new Phase("Object-Oriented Python", "Build maintainable systems", BLUE,
            "Classes",
            "Objects",
            "Attributes",
            "Methods",
            "__init__",
            "Inheritance",
            "Composition",
            "Polymorphism",
            "Dataclasses"),

        new Phase("Advanced Python", "Move toward professional level", PURPLE,
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

        new Phase("Excel Foundations", "Master the workspace", GREEN,
            "Workbook & worksheet",
            "Rows & columns",
            "Cells & ranges",
            "Data entry",
            "Formatting",
            "Sort",
            "Filter",
            "Excel Tables"),

        new Phase("Excel Formulas", "Build calculation fluency", GREEN,
            "SUM",
            "AVERAGE",
            "MIN / MAX",
            "COUNT / COUNTA",
            "IF",
            "AND / OR",
            "IFERROR",
            "SUMIF / SUMIFS",
            "COUNTIF / COUNTIFS"),

        new Phase("Excel Analytics", "Turn data into insight", GREEN,
            "XLOOKUP",
            "INDEX / MATCH",
            "PivotTables",
            "PivotCharts",
            "Conditional formatting",
            "Charts",
            "KPI dashboard"),

        new Phase("Python + Excel", "Automate spreadsheet work", GREEN,
            "pandas",
            "openpyxl",
            "Read Excel",
            "Write Excel",
            "Clean data",
            "Transform data",
            "Automated reports"),

        new Phase("Data Analytics", "SQL + analytical thinking", BLUE,
            "SQL",
            "PostgreSQL",
            "NumPy",
            "pandas analysis",
            "Matplotlib",
            "Data cleaning",
            "Trend analysis",
            "Business KPIs"),

        new Phase("AI & Tool Building", "Build practical AI systems", ORANGE,
            "LLM APIs",
            "Structured outputs",
            "Function calling",
            "Tool schemas",
            "AI app architecture",
            "MCP concepts",
            "Build an MCP server",
            "ChatGPT tool integration",
            "Evaluation"),

        new Phase("UI/UX Design", "Design serious mobile interfaces", PURPLE,
            "Design principles",
            "Typography",
            "Color theory",
            "Spacing & layout",
            "Visual hierarchy",
            "Grid systems",
            "Accessibility",
            "Wireframing",
            "User flows",
            "Usability testing"),

        new Phase("Figma", "From idea to interactive prototype", PURPLE,
            "Frames",
            "Components",
            "Auto Layout",
            "Constraints",
            "Variants",
            "Styles",
            "Variables",
            "Prototyping",
            "Developer handoff"),

        new Phase("Mobile UI/UX", "Android + iOS interface design", PURPLE,
            "Android design patterns",
            "iOS design patterns",
            "Navigation",
            "Forms",
            "Lists",
            "Dialogs & sheets",
            "Empty states",
            "Loading states",
            "Error states",
            "Responsive layouts"),

        new Phase("Design Systems", "Create reusable visual systems", PURPLE,
            "Design tokens",
            "Component libraries",
            "Typography scale",
            "Color system",
            "Spacing system",
            "Icon system",
            "States & variants",
            "Documentation"),

        new Phase("Software Development", "Turn designs into products", BLUE,
            "Git fundamentals",
            "GitHub workflows",
            "Android fundamentals",
            "App architecture",
            "APIs & networking",
            "Local storage",
            "Testing",
            "Debugging",
            "Build & release"),
        
        new Phase("AI Product Engineering", "Connect software with AI", ORANGE,
            "Prompt engineering",
            "API authentication",
            "Streaming responses",
            "Tool execution",
            "State management",
            "RAG concepts",
            "Local AI",
            "AI evaluation",
            "Production safeguards"),

        new Phase("Real-World Projects", "Prove the skills by building", RED,
            "Python automation project",
            "Excel analytics dashboard",
            "UI/UX case study",
            "Android application",
            "AI-powered application",
            "MCP tool",
            "End-to-end capstone")
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
        v.setTextColor(TEXT);
        v.setTextSize(size);
        v.setGravity(Gravity.CENTER_VERTICAL);

        if (bold) {
            v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return v;
    }

    private GradientDrawable background(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(radius);
        return g;
    }

    private TextView chip(String value, int color) {
        TextView v = text(value, 13, true);
        v.setGravity(Gravity.CENTER);
        v.setPadding(24, 14, 24, 14);
        v.setTextColor(TEXT);
        v.setBackground(background(color, 40));
        return v;
    }

    private void addSpace(LinearLayout parent, int dp) {
        Space s = new Space(this);
        parent.addView(s, new LinearLayout.LayoutParams(1, dp));
    }

    private LinearLayout card() {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(28, 24, 28, 24);
        c.setBackground(background(CARD, 28));
        return c;
    }

    private void buildScreen() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        root.setPadding(16, 16, 16, 10);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setGravity(Gravity.CENTER_VERTICAL);

        ImageView logo = new ImageView(this);
        logo.setImageResource(com.boss.progresschecklist.R.drawable.ic_logo);
        logo.setPadding(3, 3, 3, 3);

        toolbar.addView(logo,
            new LinearLayout.LayoutParams(52, 52));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(14, 0, 0, 0);

        TextView title = text("BOSS", 21, true);
        TextView subtitle = text("PROGRESS CHECKLIST", 12, true);
        subtitle.setTextColor(RED);

        titles.addView(title);
        titles.addView(subtitle);

        toolbar.addView(titles,
            new LinearLayout.LayoutParams(0, 60, 1));

        root.addView(toolbar);

        addSpace(root, 14);

        LinearLayout hero = card();

        TextView welcome = text("4–5 HOURS / DAY", 13, true);
        welcome.setTextColor(RED);
        hero.addView(welcome);

        TextView goal = text("Build skills. Ship projects. Track the climb.", 21, true);
        goal.setPadding(0, 8, 0, 8);
        hero.addView(goal);

        TextView plan = text(
            "Default daily split  •  Python 2h  •  Excel 1h  •  UI/UX 1h  •  Project / Review 0.5h",
            13, false);
        plan.setTextColor(MUTED);
        hero.addView(plan);

        addSpace(hero, 14);

        LinearLayout chips = new LinearLayout(this);
        chips.setGravity(Gravity.CENTER_VERTICAL);
        TextView python = chip("Python · 2h", RED);
        TextView excel = chip("Excel · 1h", GREEN);
        TextView ux = chip("UI/UX · 1h", PURPLE);
        TextView project = chip("Project · 0.5h", BLUE);

        chips.addView(python);
        chips.addView(excel, new LinearLayout.LayoutParams(
            0, 48, 1));
        chips.addView(ux, new LinearLayout.LayoutParams(
            0, 48, 1));
        chips.addView(project, new LinearLayout.LayoutParams(
            0, 48, 1));

        hero.addView(chips);

        root.addView(hero);

        addSpace(root, 14);

        LinearLayout progressCard = card();

        TextView progressTitle = text("OVERALL PROGRESS", 13, true);
        progressTitle.setTextColor(MUTED);
        progressCard.addView(progressTitle);

        progress = text("0 / 0 completed • 0%", 18, true);
        progress.setPadding(0, 10, 0, 10);
        progressCard.addView(progress);

        bar = new ProgressBar(
            this,
            null,
            android.R.attr.progressBarStyleHorizontal);

        bar.setMax(100);
        progressCard.addView(bar,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 12));

        root.addView(progressCard);

        addSpace(root, 12);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setGravity(Gravity.CENTER_VERTICAL);

        Button reset = new Button(this);
        reset.setText("Reset Progress");
        reset.setTextColor(TEXT);
        reset.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            buildScreen();
        });

        buttons.addView(reset,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 52));

        TextView note = text(
            "  Existing progress is saved locally on this device.",
            12, false);
        note.setTextColor(MUTED);

        buttons.addView(note,
            new LinearLayout.LayoutParams(
                0, 52, 1));

        root.addView(buttons);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);

        scroll.addView(list);

        root.addView(scroll,
            new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0, 1));

        setContentView(root);

        buildChecklist();
        updateProgress();
    }

    private void buildChecklist() {

        total = 0;

        for (int p = 0; p < phases.size(); p++) {

            Phase phase = phases.get(p);

            addSpace(list, 10);

            LinearLayout header = card();

            TextView heading = text(
                phase.title.toUpperCase(),
                18, true);
            heading.setTextColor(phase.accent);
            header.addView(heading);

            TextView sub = text(
                phase.subtitle,
                12, false);
            sub.setTextColor(MUTED);
            sub.setPadding(0, 6, 0, 0);
            header.addView(sub);

            list.addView(header);

            for (int i = 0; i < phase.items.length; i++) {

                total++;

                final String key =
                    "phase_" + p + "_item_" + i;

                LinearLayout row = new LinearLayout(this);
                row.setGravity(Gravity.CENTER_VERTICAL);
                row.setPadding(8, 3, 8, 3);
                row.setBackground(background(CARD_2, 22));

                CheckBox checkbox = new CheckBox(this);
                checkbox.setText(phase.items[i]);
                checkbox.setTextColor(TEXT);
                checkbox.setTextSize(15);
                checkbox.setPadding(12, 7, 12, 7);

                checkbox.setChecked(
                    prefs.getBoolean(key, false));

                checkbox.setOnCheckedChangeListener(
                    (button, checked) -> {
                        prefs.edit()
                            .putBoolean(key, checked)
                            .apply();
                        updateProgress();
                    });

                row.addView(checkbox,
                    new LinearLayout.LayoutParams(
                        0, 54, 1));

                TextView number = text(
                    String.valueOf(i + 1), 12, true);
                number.setTextColor(phase.accent);
                number.setGravity(Gravity.CENTER);

                row.addView(number,
                    new LinearLayout.LayoutParams(36, 54));

                list.addView(row);

                addSpace(list, 5);
            }
        }
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
            total == 0 ? 0 : completed * 100 / total;

        progress.setText(
            completed + " / " + total +
            " completed  •  " + percentage + "%"
        );

        bar.setProgress(percentage);
    }
}
