package be.kuleuven.chi.backend.categories;

import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by NeleR on 5/04/2014.
 */
public class ExpenseCategory implements Category {

    private String name;

    public ExpenseCategory(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
