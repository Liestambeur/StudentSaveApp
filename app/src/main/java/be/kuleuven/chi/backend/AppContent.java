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
import be.kuleuven.chi.backend.historyElements.UndoSavingElement;

/**
 * Created by Lies on 3/04/14.
 */
public class AppContent implements Serializable {

    static final long serialVersionUID=0L;

    private Goal currentGoal;
    private Goal backupCurrentGoal; // used as backup when editing the current goal
    private List<Goal> goals;
    private History history;
    private Currency currency;
    private static Context context;
    private List<IncomeCategory> incomeCategories;
    private List<ExpenseCategory> expenseCategories;
    private static final String THISFILENAME = "appcontentstate";
    private static AppContent singleton;

    /* A private Constructor prevents any other class from instantiating. */
    private AppContent() {
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
            FileOutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = context.openFileOutput(THISFILENAME,Context.MODE_PRIVATE);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(this);
                oos.flush();
                fos.flush();
                oos.close();
                fos.close();
                System.out.println("State saved.");
            } catch(FileNotFoundException f) {
                System.out.println("WTF?! FileNotFound");
            } catch (NullPointerException n) {
                System.out.println("WTF?! NullPointer");
                System.out.println(
                        (fos==(null)?"fos!":"")
                        +(oos==(null)?"oos!":"")
                        +(context==(null)?"context?!":"")
                        +(this==null?"The sky is falling! Run!":"")
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* Static 'instance' method */
    public static AppContent getInstance(Context theContext) {
        context = theContext;
        if(AppContent.singleton == null) {
            //System.out.println("Singleton null");
            try{
                FileInputStream fis = context.openFileInput(THISFILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                AppContent.singleton = (AppContent) ois.readObject();
                //System.out.println("State loaded from file.");
            } catch(FileNotFoundException e){
                //System.out.println(e.getCause().toString());
                //System.out.println("Failed to load file!");
                AppContent.singleton = new AppContent();
            } catch(Exception e){
                System.out.println("Something is horribly wrong!");
                System.out.print(e.toString());
                System.out.println(e.getCause());
            }
        }
        return singleton;
    }

    /** CURRENCY **/
    public String getCurrencySymbol() {
        return this.currency.getSymbol();
    }

    /** GOALS **/
    private boolean hasGoal() {
        return !getGoals().isEmpty();
    }
    public void addGoal(Goal goal) {
        this.goals.add(goal);
        setCurrentGoal(goal);
        saveState();
    }
    public List<Goal> getGoals() {
        return goals;
    }
    private List<Goal> getGoalsDone(){
        List<Goal> result = new ArrayList<Goal>();
        for (Goal g : goals) {
            if (g.isDone()) {
                result.add(g);
            }
        }
        return result;
    }
    private List<Goal> getGoalsBusy(){
        List<Goal> result = new ArrayList<Goal>();
        for (Goal g : goals) {
            if (!g.isDone()) {
                result.add(g);
            }
        }
        return result;
    }
    private void deleteGoal(Goal goal) {
        addToHistory(new UndoSavingElement(goal));

        this.goals.remove(goal);
        if (!this.goals.isEmpty()) {
            setCurrentGoal(getGoals().get(this.goals.size() - 1));
        } else {
            setCurrentGoal(null);
        }
        saveState();
    }

    /** CURRENT GOAL **/
    public boolean hasCurrentGoal(){
        return hasGoal() && getCurrentGoal()!=null;
    }
    public Goal getCurrentGoal() {
        return this.currentGoal;
    }
    public void setCurrentGoal(Goal currentGoal) {
        this.currentGoal = currentGoal;
    }
    public void currentGoalDone(){
        setCurrentGoal(null);
        saveState();
    }
    public void deleteCurrentGoal() {
        this.deleteGoal(this.getCurrentGoal());
    }

    /** BACKUP OF CURRENT GOAL **/
    public void backupCurrentGoal() {
        this.backupCurrentGoal = this.currentGoal.getCopy();
    }
    public void resetCurrentGoalToBackup() {
        this.currentGoal.copyState(this.backupCurrentGoal);
    }
    public void deleteBackUpCurrentGoal(){
        this.backupCurrentGoal = null;
    }

    /** HISTORY **/
    public boolean hasHistory(){
        return this.getNumberOfHistoryElements()!=0;
    }
    public int getNumberOfHistoryElements() {
        return this.history.getNumberOfHistoryElements();
    }
    public HistoryElement getHistoryElement(int index) {
        return this.history.getHistoryElement(index);
    }
    public void addToHistory(HistoryElement element) {
        this.history.addToHistory(element);
        saveState();
    }

    /** WALLET **/
    public String getWalletTotal() {
        return (this.getCurrencySymbol() + " " + String.format("%.2f", this.history.getWalletTotal()));
    }
    public double getWalletTotalAmount() {
        return this.history.getWalletTotal();
    }

    /** INCOME CATEGORIES **/
    public List<IncomeCategory> getIncomeCategories(){ return this.incomeCategories; }
    public int getNumberOfIncomeCategories(){ return incomeCategories.size(); }
    public IncomeCategory getIncomeCategoryAt(int index){ return incomeCategories.get(index); }

    /** EXPENSE CATEGORIES **/
    public List<ExpenseCategory> getExpenseCategories(){ return this.expenseCategories; }
    public int getNumberOfExpenseCategories(){ return expenseCategories.size(); }
    public ExpenseCategory getExpenseCategoryAt(int index){ return expenseCategories.get(index); }

    public static String getString(int id, String... parameters) {
        if(context != null) {
            return context.getResources().getString(id, parameters);
        }
        else {
            // TODO wat dan?
            return "";
        }
    }
}
