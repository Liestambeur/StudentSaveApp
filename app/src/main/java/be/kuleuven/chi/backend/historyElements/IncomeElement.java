package be.kuleuven.chi.backend.historyElements;

import be.kuleuven.chi.app.R;
import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by Lies on 3/04/14.
 */
public class IncomeElement extends HistoryElement {

    public IncomeElement(double amount, Category category, String title){
        super(amount, category, title);
    }

    @Override
    public int getTypePictureId() {
        return R.drawable.gitaar;
    }
}
