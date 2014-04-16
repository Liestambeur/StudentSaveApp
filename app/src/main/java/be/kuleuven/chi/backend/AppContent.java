package be.kuleuven.chi.backend;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chi.app.R;
import be.kuleuven.chi.backend.categories.ExpenseCategory;
import be.kuleuven.chi.backend.categories.Goal;
import be.kuleuven.chi.backend.categories.IncomeCategory;
import be.kuleuven.chi.backend.historyElements.HistoryElement;

/**
 * Created by Lies on 3/04/14.
 */
public class AppContent {

    private Goal currentGoal;
    private List<Goal> goals;
    private History history;
    private Currency currency;
    private List<IncomeCategory> incomeCategories;
    private List<ExpenseCategory> expenseCategories;

    private static AppContent singleton;

    /* A private Constructor prevents any other class from instantiating. */
    private AppContent(Context context) {
        this.goals = new ArrayList<Goal>();
        this.currency = Currency.EURO;
        this.history = new History();
        this.incomeCategories = new ArrayList<IncomeCategory>();
        this.expenseCategories = new ArrayList<ExpenseCategory>();
        Resources resources = context.getResources();
        String[] ics = resources.getStringArray(R.array.income_categories);
        String[] ecs = resources.getStringArray(R.array.expense_categories);
        for(int i=0;i<ics.length;i++){incomeCategories.add(new IncomeCategory(ics[i]));}
        for(int i=0;i<ecs.length;i++){expenseCategories.add(new ExpenseCategory(ecs[i]));}

    }

    /* Static 'instance' method */
    public static AppContent getInstance(Context context) {
        if(AppContent.singleton == null) {
            AppContent.singleton = new AppContent(context);
        }
        return singleton;
    }

    public Goal getCurrentGoal() {
        return currentGoal;
    }

    public void addGoal(Goal goal) {
        this.goals.add(goal);
        this.currentGoal = goal;
    }

    public boolean hasGoal() {
        return !this.goals.isEmpty();
    }

    public void deleteGoal(Goal goal) {
        this.goals.remove(goal);
        this.currentGoal = this.goals.get(this.goals.size() - 1);
    }

    public void addToHistory(HistoryElement element) {
       this.history.addToHistory(element);
    }

    public String getWalletTotal() {
        return (this.currency.getSymbol() + " " + this.history.getWalletTotal());
    }

    public double getWalletTotalAmount() {
        return this.history.getWalletTotal();
    }

    public int getNumberOfHistoryElements() {
        return this.history.getNumberOfHistoryElements();
    }

    public HistoryElement getHistoryElement(int index) {
        return this.history.getHistoryElement(index);
    }

    public boolean hasHistory(){
        return this.getNumberOfHistoryElements()!=0;
    }

    public List<IncomeCategory> getIncomeCategories(){ return this.incomeCategories; }
    public int getNumberOfIncomeCategories(){ return incomeCategories.size(); }
    public IncomeCategory getIncomeCategoryAt(int index){ return incomeCategories.get(index); }

    public List<ExpenseCategory> getExpenseCategories(){ return this.expenseCategories; }
    public int getNumberOfExpenseCategories(){ return expenseCategories.size(); }
    public ExpenseCategory getExpenseCategoryAt(int index){ return expenseCategories.get(index); }
}
