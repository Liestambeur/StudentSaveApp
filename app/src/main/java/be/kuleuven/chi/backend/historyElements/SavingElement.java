package be.kuleuven.chi.backend.historyElements;

import be.kuleuven.chi.app.R;
import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.categories.Goal;

/**
 * Created by NeleR on 5/04/2014.
 */
public class SavingElement extends HistoryElement {

    public SavingElement(double amount, Goal goal){
        super(-amount, goal, AppContent.getString(R.string.saving_long, goal.getName()));
    }

    @Override
    public int getTypePictureId() {
        return R.drawable.save;
    }

}
