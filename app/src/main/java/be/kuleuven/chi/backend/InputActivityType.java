package be.kuleuven.chi.backend;

import be.kuleuven.chi.app.R;

/**
 * Created by joren on 4/5/14.
 */
public enum InputActivityType {

    INCOME(R.array.income_categories),
    EXPENSE(R.array.expense_categories),
    SAVE(R.layout.activity_save);

    private final int categories;

    InputActivityType(int layoutId){
        this.categories=layoutId;
    }

    public int getCategories() {
        return categories;
    }
}
