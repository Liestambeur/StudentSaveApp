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
        getViewIfFull(index, convertView);

        return convertView;
    }

    protected void getViewIfFull(int index, View convertView) {
        HistoryElement historyElement = getItem(index);

        getPicture( convertView, R.id.historyType, historyElement.getTypePictureId());
        getTitle(   convertView, R.id.historyTitle, historyElement.getCategoryName());
        getSubTitle(convertView, R.id.historyCategoryAndDate, historyElement.getTitle(), historyElement.getDateName());
        getAmount(  convertView, R.id.historyAmount, historyElement.getAmountName());
    }

    protected void getPicture(View convertView, int viewElement, int pictureID) {
        Drawable historyPicture = mContext.getResources().getDrawable(pictureID);
        ImageView historyType = (ImageView) convertView.findViewById(viewElement);
        historyType.setImageDrawable(historyPicture);
    }

    protected void getTitle(View convertView, int viewElement, String category) {
        TextView historyTitle = (TextView) convertView.findViewById(viewElement);
        historyTitle.setText(category);
    }

    protected void getSubTitle(View convertView, int viewElement, String title, String date) {
        TextView historyCategoryAndDate = (TextView) convertView.findViewById(viewElement);
        if(title == "") {
            historyCategoryAndDate.setText(date);
        }
        else{
            historyCategoryAndDate.setText(date + "  " + title);
        }
    }

    protected void getAmount(View convertView, int viewElement, String amount) {
        TextView historyAmount = (TextView) convertView.findViewById(viewElement);
        historyAmount.setText(amount);
    }

}
