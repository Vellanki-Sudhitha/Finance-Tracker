# Finance-Tracker
A simple Android app to track daily expenses, categorize spending, and get quick insights into where your money goes. It is all stored locally with SQLite.

# Features
1)Add Expenses — Log an expense with title, amount, category, and date

2)View Expenses — See all saved expenses in a clean, scrollable list

3)Delete Expenses — Remove any entry with a single tap and confirmation

4)Live Total — Running total of all expenses updates automatically

5)Insights — Category-wise spending breakdown with percentage bars and your top spending category

6)Offline — All data is stored locally on-device using SQLite


 # Tech Stack
Language: Java

Platform: Android Studio

Database: SQLite (SQLiteOpenHelper)

UI: RecyclerView, Spinner, DatePickerDialog

Architecture: Simple Activity-based structure




## Project Architecture

```

Finance-Tracker/
│
├── app/
│   └── src/
│       └── main/
│           │
│           ├── java/com/example/financetracker/
│           │   │
│           │   ├── model/
│           │   │   └── Expense.java
│           │   │       └── Represents expense data model containing details
│           │   │           such as title, amount, category, and date.
│           │   │
│           │   ├── ui/
│           │   │   ├── adapter/
│           │   │   │   └── ExpenseAdapter.java
│           │   │   │       └── Handles RecyclerView data binding and display.
│           │   │   │
│           │   │   ├── AddExpenseActivity.java
│           │   │   │   └── Allows users to add new expenses.
│           │   │   │
│           │   │   ├── MainActivity.java
│           │   │   │   └── Displays expense list and main dashboard.
│           │   │   │
│           │   │   └── InsightsActivity.java
│           │   │       └── Provides expense analysis and insights.
│           │   │
│           │   ├── DatabaseHelper.java
│           │   │   └── Manages SQLite database operations including
│           │   │       storing and retrieving expense records.
│           │   │
│           │   └── AndroidManifest.xml
│           │       └── Defines application components and permissions.
│           │
│           ├── res/
│           │   └── layout/
│           │       ├── activity_main.xml
│           │       ├── activity_add_expense.xml
│           │       ├── activity_insights.xml
│           │       └── item_expense.xml
│           │           └── Contains UI layouts for application screens.
│           │
│           └── README.md
│
└── Screenshots/
└── Application screenshots demonstrating features.

```
```


