package be.kuleuven.chi.app;

import android.content.Context;
import android.view.View;
import be.kuleuven.chi.backend.historyElements.HistoryElement;

/**
 * Created by NeleR on 4/04/2014.
 */
public class HistoryElementAdapterPreview extends HistoryElementAdapter {


    public HistoryElementAdapterPreview(Context mContext, int layoutResourceId) {
        super(mContext, layoutResourceId);
    }

    @Override
    protected void getViewIfFull(int index, View convertView) {
        HistoryElement historyElement = getItem(index);

        getPicture( convertView, R.id.historyTypePrev, historyElement.getTypePictureId());
        getTitle(   convertView, R.id.historyTitlePrev, historyElement.getCategoryName());
        getAmount(  convertView, R.id.historyAmountPrev, historyElement.getAmountName());
    }

}
