package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.historyElements.HistoryElement;

/**
 * Created by NeleR on 4/04/2014.
 */
public class HistoryElementAdapter extends ArrayAdapter {

    Context mContext;
    int layoutResourceId;

    public HistoryElementAdapter(Context mContext, int layoutResourceId) {
        super(mContext, layoutResourceId);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return AppContent.getInstance(mContext).getNumberOfHistoryElements();
    }

    @Override
    public HistoryElement getItem(int index) {
        // fetches the history elements from new to old (they are stored from old to new)
        int totalNumberOfHistoryElements = AppContent.getInstance(mContext).getNumberOfHistoryElements();
        return AppContent.getInstance(mContext).getHistoryElement(totalNumberOfHistoryElements - index - 1);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        HistoryElement historyElement = getItem(index);

        Drawable historyPicture = mContext.getResources().getDrawable(historyElement.getTypePictureId());
        ImageView historyType = (ImageView) convertView.findViewById(R.id.historyType);
        historyType.setImageDrawable(historyPicture);

        TextView historyTitle = (TextView) convertView.findViewById(R.id.historyTitle);
        historyTitle.setText(historyElement.getTitle());

        TextView historyCategoryAndDate = (TextView) convertView.findViewById(R.id.historyCategoryAndDate);
        historyCategoryAndDate.setText(historyElement.getCategoryName() + ", " + historyElement.getDateName());

        TextView historyAmount = (TextView) convertView.findViewById(R.id.historyAmount);
        historyAmount.setText(Double.toString(historyElement.getAmount()));

        return convertView;
    }

}
