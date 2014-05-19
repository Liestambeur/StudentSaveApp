package be.kuleuven.chi.backend.historyElements;

import be.kuleuven.chi.app.R;
import be.kuleuven.chi.backend.categories.Goal;

/**
 * Created by NeleR on 18/05/2014.
 */
public class UndoSavingElement extends HistoryElement {

    public UndoSavingElement(Goal goal){
        // TODO naam juist formuleren -> zonder hard coded string!
        //super(goal.getAmountSaved(), goal, AppContent.getString(R.string.undo_saving_long, goal.getName()));
        super(goal.getAmountSaved(), goal, "Deleting goal \"" + goal.getName() + "\"");
    }

    @Override
    public int getTypePictureId() {
        return R.drawable.save;
    }

    @Override
    public String getCategoryName() {
        return "Undo savings " + getCategory().getName();
    }
}
