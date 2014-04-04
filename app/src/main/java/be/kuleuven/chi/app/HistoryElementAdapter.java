package be.kuleuven.chi.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import be.kuleuven.chi.backend.AppContent;

/**
 * Created by NeleR on 4/04/2014.
 */
public class HistoryElementAdapter extends ArrayAdapter {

    public HistoryElementAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public int getCount() {
        return AppContent.getInstance().getNumberOfHistoryElements();
    }

    @Override
    public Object getItem(int i) {
        // fetches the history elements from new to old (they are stored from old to new)
        return AppContent.getInstance().getHistoryElement(this.getCount() - i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

}
