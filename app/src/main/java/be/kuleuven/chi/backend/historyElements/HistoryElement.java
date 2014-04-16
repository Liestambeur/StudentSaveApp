package be.kuleuven.chi.backend.historyElements;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by Lies on 3/04/14.
 */
public abstract class HistoryElement {

    private double amount;
    private Category category;
    private String title;
    private Calendar date;


    public HistoryElement(double amount, Category category, String title){
        this.date = new GregorianCalendar();
        this.amount=amount;
        this.category=category;
        this.title=title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getDate() {
        return date;
    }

    public abstract int getTypePictureId();

    public String getCategoryName() {
        return getCategory().getName();
    }

    public String getDateName() {
        return getDate().toString();
    }

}
