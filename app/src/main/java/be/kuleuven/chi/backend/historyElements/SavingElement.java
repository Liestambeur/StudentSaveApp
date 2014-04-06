package be.kuleuven.chi.backend.historyElements;

import android.graphics.drawable.Drawable;

import be.kuleuven.chi.app.HistoryElementAdapter;
import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by NeleR on 5/04/2014.
 */
public class SavingElement extends HistoryElement {

    public SavingElement(double amount, Category category){
        // TODO naam juist formuleren -> zonder hard coded string!
        super(-amount, category, "Savings for" + " " + category.getName());
    }

    @Override
    public Drawable getTypePicture() {
        return null;
    }
}
