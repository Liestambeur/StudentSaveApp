package be.kuleuven.chi.backend.categoryElements;

import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by joren on 4/6/14.
 */
public class CategoryElement {
    private final Category category;

    public CategoryElement(Category cat) {
        this.category = cat;
    }

    public String getTitle() { return category.getName(); }
}
