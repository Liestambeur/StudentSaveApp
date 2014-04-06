package be.kuleuven.chi.backend.historyElements;

import android.graphics.drawable.Drawable;

import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by Lies on 3/04/14.
 */
public class IncomeElement extends HistoryElement {

    public IncomeElement(double amount, Category category, String title){
        super(amount, category, title);
    }

    @Override
    public Drawable getTypePicture() {
        return null;
    }
}
