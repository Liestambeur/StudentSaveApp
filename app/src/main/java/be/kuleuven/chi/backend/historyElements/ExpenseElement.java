package be.kuleuven.chi.backend.historyElements;

import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by Lies on 3/04/14.
 */
public class ExpenseElement extends HistoryElement {

    public ExpenseElement(double amount, Category category, String title){
        super(-amount, category, title);
    }
}
