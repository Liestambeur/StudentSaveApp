package be.kuleuven.chi.backend.historyElements;

import android.graphics.drawable.Drawable;

import java.io.File;

import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by Lies on 3/04/14.
 */
public class ExpenseElement extends HistoryElement {

    public ExpenseElement(double amount, Category category, String title){
        super(-amount, category, title);
    }

    @Override
    public Drawable getTypePicture() {
        String filePath = new File("").getAbsolutePath();
       //filePath.concat("path to the property file");
        System.err.print(filePath);
        return null;
    }
}
