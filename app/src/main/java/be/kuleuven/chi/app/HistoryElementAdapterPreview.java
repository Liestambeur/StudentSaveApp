package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.historyElements.HistoryElement;

/**
 * Created by NeleR on 4/04/2014.
 */
public class HistoryElementAdapterPreview extends HistoryElementAdapter {


    public HistoryElementAdapterPreview(Context mContext, int layoutResourceId) {
        super(mContext, layoutResourceId);
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {

        //TODO zou mooi moeten kunnen overerven van HistoryElementAdapter en niet met zo'n aparte row

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }


        HistoryElement historyElement = getItem(index);

        Drawable historyPicture = mContext.getResources().getDrawable(historyElement.getTypePictureId());
        ImageView historyType = (ImageView) convertView.findViewById(R.id.historyType1);
        historyType.setImageDrawable(historyPicture);

        TextView historyTitle = (TextView) convertView.findViewById(R.id.historyCategory1);
        historyTitle.setText(historyElement.getCategoryName());

        TextView historyAmount = (TextView) convertView.findViewById(R.id.historyAmount1);
        historyAmount.setText(Double.toString(historyElement.getAmount()));

        convertView.setClickable(true);
        return convertView;
    }

}
