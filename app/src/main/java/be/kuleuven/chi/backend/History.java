package be.kuleuven.chi.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lies on 3/04/14.
 */
public class History {

    private List<HistoryElement> history;

    public History(){
        this.history=new ArrayList<HistoryElement>();
    }

    public void addToHistory(HistoryElement element){
        this.history.add(element);
    }
}
