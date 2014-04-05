package be.kuleuven.chi.backend;

import be.kuleuven.chi.backend.categories.Goal;
import be.kuleuven.chi.backend.historyElements.HistoryElement;

/**
 * Created by Lies on 3/04/14.
 */
public class AppContent {

    private Goal goal;
    private History history;
    private Currency currency;

    private static AppContent singleton;

    /* A private Constructor prevents any other class from instantiating. */
    private AppContent() {
        this.currency = Currency.EURO;
        this.history = new History();
    }

    /* Static 'instance' method */
    public static AppContent getInstance() {
        if(AppContent.singleton == null) {
            AppContent.singleton = new AppContent();
        }
        return singleton;
    }

    public Goal getGoal() {
        return goal;
    }

    public void addGoal(Goal goal) {
        this.goal = goal; // later a list of goals will be included
    }

    public boolean hasGoal() {
        return this.goal != null;
    }

    public void addToHistory(HistoryElement element) {
       this.history.addToHistory(element);
    }

    public String getWalletTotal() {
        return (this.currency.getSymbol() + " " + this.history.getWalletTotal());
    }

    public int getNumberOfHistoryElements() {
        return this.history.getNumberOfHistoryElements();
    }

    public HistoryElement getHistoryElement(int index) {
        return this.history.getHistoryElement(index);
    }
}
