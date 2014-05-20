package be.kuleuven.chi.backend.historyElements;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import be.kuleuven.chi.backend.categories.Category;

/**
 * Created by Lies on 3/04/14.
 */
public abstract class HistoryElement implements Serializable {

    static final long serialVersionUID = 0L;

    private double amount;
    private Category category;
    private String title;
    private Calendar date;


    HistoryElement(double amount, Category category, String title){
        this.date = new GregorianCalendar();
        this.amount = amount;
        this.category = category;
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    Category getCategory() {
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
        // note: the calendar counts months starting from 0 (e.g. January = 0, February = 1)
        // so decrease the user input with one as the user will start counting from 1.
        // days of month start from 1, as the user will expect so no change needed here.
        return this.date.get(Calendar.DAY_OF_MONTH) + "/"
                + (this.date.get(Calendar.MONTH) + 1) + "/"
                + this.date.get(Calendar.YEAR);
    }

    /**
     * Rounds the amount till two decimals and returns a string
     */
    public String getAmountName() {
        return String.format("%.2f", this.getAmount());
    }

}
