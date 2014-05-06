package be.kuleuven.chi.backend;

import android.content.Context;
import android.content.res.Resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
public class AppContent implements Serializable {

    static final long serialVersionUID=0L;

    private Goal currentGoal;
    private List<Goal> goals;
    private History history;
    private Currency currency;
    private Context context;
    private List<IncomeCategory> incomeCategories;
    private List<ExpenseCategory> expenseCategories;
    private static final String THISFILENAME = "anotherfile";
    private static AppContent singleton;

    /* A private Constructor prevents any other class from instantiating. */
    private AppContent(Context context) {
        this.context = context;
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
        //Persist this singleton
        saveState();
    }

    public void saveState() {
        if(context!=null){
            try {
                FileOutputStream fos = context.openFileOutput(THISFILENAME,Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(this);
                oos.close();
                fos.close();
                System.out.println("State saved.");
            } catch(FileNotFoundException f){
                System.out.println("WTF?!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* Static 'instance' method */
    public static AppContent getInstance(Context context) {
        if(AppContent.singleton == null) {
            System.out.println("Singleton null");
            try{
                FileInputStream fis = context.openFileInput(THISFILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                AppContent.singleton = (AppContent) ois.readObject();
                System.out.println("State loaded from file.");
            } catch(FileNotFoundException e){
                System.out.println(e.getCause().toString());
                System.out.println("Failed to load file!");
                AppContent.singleton = new AppContent(context);
            } catch(Exception e){
                System.out.print(e.toString());
                System.out.println(e.getCause());
                System.out.println("Something is horribly wrong!");
            }
        }
        return singleton;
    }

    public Goal getCurrentGoal() {
        return this.currentGoal;
    }

    public void addGoal(Goal goal) {
        this.goals.add(goal);
        setCurrentGoal(goal);
        saveState();
    }

    public boolean hasCurrentGoal(){
        return getCurrentGoal()!=null;
    }

    public boolean hasGoal() {
        return !getGoals().isEmpty();
    }

    public void deleteGoal(Goal goal) {
        this.goals.remove(goal);
        if (!this.goals.isEmpty()) {
            setCurrentGoal(getGoals().get(this.goals.size() - 1));
        } else {
            setCurrentGoal(null);
        }
        saveState();
    }

    public void addToHistory(HistoryElement element) {
        this.history.addToHistory(element);
        saveState();
    }

    /**
     * 2 decimals only!
     * @return
     */
    public String getWalletTotal() {
        return (this.currency.getSymbol() + " " + String.format("%.2f", this.history.getWalletTotal()));
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

    public List<Goal> getGoalsDone(){
        List<Goal> result = new ArrayList<Goal>();
        for (Goal g : goals) {
            if (g.isDone()) {
                result.add(g);
            }
        }
        return result;
    }

    public List<Goal> getGoalsBusy(){
        List<Goal> result = new ArrayList<Goal>();
        for (Goal g : goals) {
            if (!g.isDone()) {
                result.add(g);
            }
        }
        return result;
    }

    public void currentGoalDone(){
        setCurrentGoal(null);
        saveState();
    }


    public List<IncomeCategory> getIncomeCategories(){ return this.incomeCategories; }
    public int getNumberOfIncomeCategories(){ return incomeCategories.size(); }
    public IncomeCategory getIncomeCategoryAt(int index){ return incomeCategories.get(index); }

    public List<ExpenseCategory> getExpenseCategories(){ return this.expenseCategories; }
    public int getNumberOfExpenseCategories(){ return expenseCategories.size(); }
    public ExpenseCategory getExpenseCategoryAt(int index){ return expenseCategories.get(index); }

    public void setCurrentGoal(Goal currentGoal) {
        this.currentGoal = currentGoal;
    }

    public List<Goal> getGoals() {
        return goals;
    }
}
