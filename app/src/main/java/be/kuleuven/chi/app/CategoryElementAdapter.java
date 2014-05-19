package be.kuleuven.chi.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import be.kuleuven.chi.backend.AppContent;
import be.kuleuven.chi.backend.InputActivityType;
import be.kuleuven.chi.backend.categories.Category;
import be.kuleuven.chi.backend.categoryElements.CategoryElement;

/**
 * Created by joren on 4/6/14.
 */
class CategoryElementAdapter extends ArrayAdapter<CategoryElement> {
    private final String type;
    private Context mContext;
    private int layoutResourceId;

    public CategoryElementAdapter(String type, Context mContext, int layoutResourceId) {
        super(mContext, layoutResourceId);
//        String theType = "";
//        for(InputActivityType t : InputActivityType.values()) {
//            if(t.name().equals(type)) {
//                theType = type;
//            } else {
//                theType = "";
//            }
//        }
//        this.type = theType;
        this.type = type;
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        if(type.equals(InputActivityType.EXPENSE.name())){
            return AppContent.getInstance(mContext).getNumberOfExpenseCategories();
        } else if(type.equals(InputActivityType.INCOME.name())){
            return AppContent.getInstance(mContext).getNumberOfIncomeCategories();
        } else {
            return 1;
        }
    }

    @Override
    public CategoryElement getItem(int index) {
        final AppContent instance = AppContent.getInstance(mContext);
        CategoryElement categoryElement;
        // TODO Maak dit minder vuil
        if(type.equals(InputActivityType.EXPENSE.name())){
            categoryElement = new CategoryElement(instance.getExpenseCategoryAt(index));
        } else if (type.equals(InputActivityType.INCOME.name())) {
            categoryElement = new CategoryElement(instance.getIncomeCategoryAt(index));
        } else {
            categoryElement = new CategoryElement(new Category() {
                @Override
                public String getName() {
                    return "Your Goal";
                }
            });
        }
        return categoryElement;
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


        CategoryElement categoryElement = getItem(index);

        TextView categoryTitle = (TextView) convertView.findViewById(R.id.categoryTextView);
        categoryTitle.setText(categoryElement.getTitle());

        return convertView;
    }
}
