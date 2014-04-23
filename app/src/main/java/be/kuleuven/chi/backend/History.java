package be.kuleuven.chi.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import be.kuleuven.chi.backend.historyElements.HistoryElement;

/**
 * Created by Lies on 3/04/14.
 */
public class History implements Serializable{

    private List<HistoryElement> history;

    public History(){
        this.history=new ArrayList<HistoryElement>();
    }

    public void addToHistory(HistoryElement element){
        this.history.add(element);
    }

    public double getWalletTotal() {
        double value = 0;
        for(HistoryElement historyElement: this.history) {
            value += historyElement.getAmount();
        }
        return value;
    }

    public int getNumberOfHistoryElements() {
        return history.size();
    }

    public HistoryElement getHistoryElement(int index) {
        return history.get(index);
    }
}
